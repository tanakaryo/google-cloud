package com.example.demo.adapter.in.batch;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.example.demo.application.domain.service.HistoryDataCreateService;
import com.example.demo.application.domain.service.PreparedTmpTableService;
import com.example.demo.application.domain.service.PutGcsObjectService;
import com.example.demo.application.domain.service.S3ObjectWriteService;
import com.example.demo.application.domain.service.VehicleIdListFindService;
import com.example.demo.common.annotation.Inject;
import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.container.CommonFileTransferBatchDIContainer;
import com.example.demo.common.type.MessageTypes;

import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;

public class CommonFileTransferBatch {

    private static final Logger LOGGER = Logger.getLogger(CommonFileTransferBatch.class.getName());

    @Inject
    private PreparedTmpTableService preparedTmpTableService;

    @Inject
    private VehicleIdListFindService<String> vehicleIdListFindService;

    @Inject
    private HistoryDataCreateService historyDataCreateService;

    @Inject
    private PutGcsObjectService gcsObjectWriteService;

    @Inject
    private S3ObjectWriteService s3ObjectWriteService;

    private Properties batchParameter;

    private CommonFileTransferBatch(Properties batchParameter) {
        this.batchParameter = batchParameter;
    }

    public void run() throws Exception {

        LOGGER.info(MessageTypes.INF001.getMessage(batchParameter.getProperty(BatchConst.KEY_CONTRACTOR)));
        LOGGER.info(MessageTypes.INF003.getMessage(batchParameter.getProperty(BatchConst.KEY_JOB_PARAMETER_BATCH_EXE_DATE)));

        // Create RetryPolicy
        RetryPolicy<Object> retryPolicy = RetryPolicy.builder().handle(Exception.class).withDelay(Duration.ofSeconds(3)).withMaxRetries(3).build();

        // BigQuery Job Execute.
        preparedTmpTableService.setJobParameter(batchParameter);
        Failsafe.with(retryPolicy).run(() -> preparedTmpTableService.execute());

        // Get VehicleId List from BigQuery
        vehicleIdListFindService.setParameter(batchParameter);
        List<String> list = Failsafe.with(retryPolicy).get(() -> vehicleIdListFindService.execute());

        historyDataCreateService.initialize(batchParameter);
        Failsafe.with(retryPolicy).run(() -> gcsObjectWriteService.initialize(batchParameter));
        Failsafe.with(retryPolicy).run(() -> s3ObjectWriteService.initialize(batchParameter));

        for (String vehicleId : list) {
            String jsonData = Failsafe.with(retryPolicy).get(() -> historyDataCreateService.execute(vehicleId));

            Failsafe.with(retryPolicy).run(() -> gcsObjectWriteService.execute(jsonData, vehicleId));
            Failsafe.with(retryPolicy).run(() -> s3ObjectWriteService.execute(jsonData, vehicleId));
        }

        Failsafe.with(retryPolicy).run(() -> s3ObjectWriteService.putFlag());

        gcsObjectWriteService.close();
        s3ObjectWriteService.close();

        LOGGER.info(MessageTypes.INF002.getMessage(batchParameter.getProperty(BatchConst.KEY_CONTRACTOR)));
    }

    public static class HistoryDataTransferBatchBuilder {

        private Properties parameter;

        public static HistoryDataTransferBatchBuilder getBuilder() {
            return new HistoryDataTransferBatchBuilder();
        }

        public HistoryDataTransferBatchBuilder setParameter(Properties parameter) {
            this.parameter = parameter;
            return this;
        }

        public CommonFileTransferBatch build() throws Exception {
            CommonFileTransferBatch batch = new CommonFileTransferBatch(parameter);
            CommonFileTransferBatchDIContainer.injectDependencies(batch);
            return batch;
        }
    }
}
