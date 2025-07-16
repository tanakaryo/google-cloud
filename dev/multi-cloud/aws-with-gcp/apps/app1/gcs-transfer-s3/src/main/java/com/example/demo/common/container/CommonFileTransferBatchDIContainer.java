package com.example.demo.common.container;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.demo.adapter.out.invoker.BigQueryJobInvokeAdapter;
import com.example.demo.adapter.out.persistence.GcsObjectPersistenceAdapter;
import com.example.demo.adapter.out.persistence.HistoryDataRepository;
import com.example.demo.adapter.out.persistence.MspfdContractVehiclesListRepository;
import com.example.demo.adapter.out.persistence.S3ObjectPersistenceAdapter;
import com.example.demo.application.domain.service.HistoryDataCreateService;
import com.example.demo.application.domain.service.PreparedTmpTableService;
import com.example.demo.application.domain.service.PutGcsObjectService;
import com.example.demo.application.domain.service.S3ObjectWriteService;
import com.example.demo.application.domain.service.VehicleIdListFindService;
import com.example.demo.application.domain.service.impl.HistoryDataCreateServiceImpl;
import com.example.demo.application.domain.service.impl.PreparedTmpTableServiceImpl;
import com.example.demo.application.domain.service.impl.PutGcsObjectServiceImpl;
import com.example.demo.application.domain.service.impl.S3ObjectWriteServiceImpl;
import com.example.demo.application.domain.service.impl.VehicleIdListFindServiceImpl;
import com.example.demo.application.port.out.BigQueryJobPort;
import com.example.demo.application.port.out.CreateBackupObjectPort;
import com.example.demo.application.port.out.CreateTransferObjectPort;
import com.example.demo.application.port.out.HistoryDataReadPort;
import com.example.demo.application.port.out.ListVehiclesPort;
import com.example.demo.common.annotation.Inject;
import com.example.demo.common.exceptions.BatchExecutionException;
import com.example.demo.common.type.MessageTypes;

public final class CommonFileTransferBatchDIContainer {

    private static Map<Class<?>, Object> context;

    static {
        context = new HashMap<>();
        context.put(PreparedTmpTableService.class, new PreparedTmpTableServiceImpl());
        context.put(VehicleIdListFindService.class, new VehicleIdListFindServiceImpl());
        context.put(HistoryDataCreateService.class, new HistoryDataCreateServiceImpl());
        context.put(PutGcsObjectService.class, new PutGcsObjectServiceImpl());
        context.put(S3ObjectWriteService.class, new S3ObjectWriteServiceImpl());
        context.put(BigQueryJobPort.class, new BigQueryJobInvokeAdapter());
        context.put(HistoryDataReadPort.class, new HistoryDataRepository());
        context.put(CreateBackupObjectPort.class, new GcsObjectPersistenceAdapter());
        context.put(CreateTransferObjectPort.class, new S3ObjectPersistenceAdapter());
        context.put(ListVehiclesPort.class, new MspfdContractVehiclesListRepository());
    }

    public static void injectDependencies(Object target) throws Exception {
        for(Field f : target.getClass().getDeclaredFields()) {
            if (f.getDeclaredAnnotation(Inject.class) != null) {
                f.setAccessible(true);
                Object child = context.get(f.getType());
                if (Objects.isNull(child)) {
                    throw new BatchExecutionException(MessageTypes.ERR006.getMessage(f.getType()));
                }
                injectDependencies(child);
                f.set(target, child);
                f.setAccessible(false);
            }
        }
    }
}
