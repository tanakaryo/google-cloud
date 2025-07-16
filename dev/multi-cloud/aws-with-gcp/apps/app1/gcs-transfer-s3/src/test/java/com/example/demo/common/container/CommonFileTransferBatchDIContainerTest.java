package com.example.demo.common.container;

import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.adapter.in.batch.CommonFileTransferBatch;
import com.example.demo.adapter.out.invoker.BigQueryJobInvokeAdapter;
import com.example.demo.adapter.out.persistence.GcsObjectPersistenceAdapter;
import com.example.demo.adapter.out.persistence.HistoryDataRepository;
import com.example.demo.adapter.out.persistence.MspfdContractVehiclesListRepository;
import com.example.demo.adapter.out.persistence.S3ObjectPersistenceAdapter;
import com.example.demo.application.domain.service.impl.HistoryDataCreateServiceImpl;
import com.example.demo.application.domain.service.impl.PreparedTmpTableServiceImpl;
import com.example.demo.application.domain.service.impl.PutGcsObjectServiceImpl;
import com.example.demo.application.domain.service.impl.S3ObjectWriteServiceImpl;
import com.example.demo.application.domain.service.impl.VehicleIdListFindServiceImpl;

public class CommonFileTransferBatchDIContainerTest {

    @Test
    @DisplayName("injectDependencies 正常系テスト1: Batchインスタンスのフィールド変数(PreparedTmpTableService)にインスタンスがインジェクトされているを検証")
    public void test01() throws Exception {

        CommonFileTransferBatch actual = CommonFileTransferBatch.HistoryDataTransferBatchBuilder.getBuilder()
                .setParameter(new Properties()).build();

        // PreparedTmpTableServiceImpl検証
        Field f1 = actual.getClass().getDeclaredField("preparedTmpTableService");
        f1.setAccessible(true);
        Object instance1 = f1.get(actual);
        Assertions.assertNotNull(instance1);
        Assertions.assertEquals(instance1.getClass(), PreparedTmpTableServiceImpl.class);

        // BigQueryJobInvokeAdapter検証
        Field f1_1 = instance1.getClass().getDeclaredField("bigQueryJobPort");
        f1_1.setAccessible(true);
        Object instance1_1 = f1_1.get(instance1);
        Assertions.assertNotNull(instance1_1);
        Assertions.assertEquals(instance1_1.getClass(), BigQueryJobInvokeAdapter.class);
    }

    @Test
    @DisplayName("injectDependencies 正常系テスト2: Batchインスタンスのフィールド変数(VehicleIdListFindService)にインスタンスがインジェクトされているを検証")
    public void test02() throws Exception {

        CommonFileTransferBatch actual = CommonFileTransferBatch.HistoryDataTransferBatchBuilder.getBuilder()
                .setParameter(new Properties()).build();

        // VehicleIdListFindServiceImpl検証
        Field f1 = actual.getClass().getDeclaredField("vehicleIdListFindService");
        f1.setAccessible(true);
        Object instance1 = f1.get(actual);
        Assertions.assertNotNull(instance1);
        Assertions.assertEquals(instance1.getClass(), VehicleIdListFindServiceImpl.class);

        // MspfdContractVehiclesListRepository検証
        Field f1_1 = instance1.getClass().getDeclaredField("listVehiclesPort");
        f1_1.setAccessible(true);
        Object instance1_1 = f1_1.get(instance1);
        Assertions.assertNotNull(instance1_1);
        Assertions.assertEquals(instance1_1.getClass(), MspfdContractVehiclesListRepository.class);
    }

    @Test
    @DisplayName("injectDependencies 正常系テスト3: Batchインスタンスのフィールド変数(HistoryDataCreateService)にインスタンスがインジェクトされているを検証")
    public void test03() throws Exception {

        CommonFileTransferBatch actual = CommonFileTransferBatch.HistoryDataTransferBatchBuilder.getBuilder()
                .setParameter(new Properties()).build();

        // HistoryDataCreateServiceImpl検証
        Field f1 = actual.getClass().getDeclaredField("historyDataCreateService");
        f1.setAccessible(true);
        Object instance1 = f1.get(actual);
        Assertions.assertNotNull(instance1);
        Assertions.assertEquals(instance1.getClass(), HistoryDataCreateServiceImpl.class);

        // HistoryDataRepository検証
        Field f1_1 = instance1.getClass().getDeclaredField("historyDataReadPort");
        f1_1.setAccessible(true);
        Object instance1_1 = f1_1.get(instance1);
        Assertions.assertNotNull(instance1_1);
        Assertions.assertEquals(instance1_1.getClass(), HistoryDataRepository.class);
    }

    @Test
    @DisplayName("injectDependencies 正常系テスト4: Batchインスタンスのフィールド変数(PutGcsObjectService)にインスタンスがインジェクトされているを検証")
    public void test04() throws Exception {

        CommonFileTransferBatch actual = CommonFileTransferBatch.HistoryDataTransferBatchBuilder.getBuilder()
                .setParameter(new Properties()).build();

        // PutGcsObjectServiceImpl検証
        Field f1 = actual.getClass().getDeclaredField("gcsObjectWriteService");
        f1.setAccessible(true);
        Object instance1 = f1.get(actual);
        Assertions.assertNotNull(instance1);
        Assertions.assertEquals(instance1.getClass(), PutGcsObjectServiceImpl.class);

        // GcsObjectPersistenceAdapter検証
        Field f1_1 = instance1.getClass().getDeclaredField("createBackupObjectPort");
        f1_1.setAccessible(true);
        Object instance1_1 = f1_1.get(instance1);
        Assertions.assertNotNull(instance1_1);
        Assertions.assertEquals(instance1_1.getClass(), GcsObjectPersistenceAdapter.class);
    }

    @Test
    @DisplayName("injectDependencies 正常系テスト5: Batchインスタンスのフィールド変数(S3ObjectWriteService)にインスタンスがインジェクトされているを検証")
    public void test05() throws Exception {

        CommonFileTransferBatch actual = CommonFileTransferBatch.HistoryDataTransferBatchBuilder.getBuilder()
                .setParameter(new Properties()).build();

        // S3ObjectWriteServiceImpl検証
        Field f1 = actual.getClass().getDeclaredField("s3ObjectWriteService");
        f1.setAccessible(true);
        Object instance1 = f1.get(actual);
        Assertions.assertNotNull(instance1);
        Assertions.assertEquals(instance1.getClass(), S3ObjectWriteServiceImpl.class);

        // GcsObjectPersistenceAdapter検証
        Field f1_1 = instance1.getClass().getDeclaredField("createTransferObjectPort");
        f1_1.setAccessible(true);
        Object instance1_1 = f1_1.get(instance1);
        Assertions.assertNotNull(instance1_1);
        Assertions.assertEquals(instance1_1.getClass(), S3ObjectPersistenceAdapter.class);
    }
}
