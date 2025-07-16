package com.example.demo.application.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.demo.adapter.out.persistence.GcsObjectPersistenceAdapter;
import com.example.demo.application.domain.service.impl.PutGcsObjectServiceImpl;
import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.exceptions.BatchExecutionException;

public class PutGcsObjectServiceImplTest {
    @Test
    @DisplayName("initialize 正常系テスト1: initializeメソッド呼び出し検証")
    public void test01() throws Exception {

        PutGcsObjectServiceImpl target = new PutGcsObjectServiceImpl();

        GcsObjectPersistenceAdapter gcsObjectPersistenceAdapterMock = Mockito
                .mock(GcsObjectPersistenceAdapter.class);
        doNothing().when(gcsObjectPersistenceAdapterMock).initialize(any());

        Field f = target.getClass().getDeclaredField("createBackupObjectPort");
        f.setAccessible(true);
        f.set(target, gcsObjectPersistenceAdapterMock);

        try {
            target.initialize(new Properties());
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
        
    }

    @Test
    @DisplayName("close 正常系テスト2: closeメソッド呼び出し検証")
    public void test02() throws Exception {

        PutGcsObjectServiceImpl target = new PutGcsObjectServiceImpl();

        GcsObjectPersistenceAdapter gcsObjectPersistenceAdapterMock = Mockito
                .mock(GcsObjectPersistenceAdapter.class);
        doNothing().when(gcsObjectPersistenceAdapterMock).close();

        Field f = target.getClass().getDeclaredField("createBackupObjectPort");
        f.setAccessible(true);
        f.set(target, gcsObjectPersistenceAdapterMock);

        try {
            target.close();
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("execute 正常系テスト3: executeメソッド呼び出し検証(車両情報がNULL)")
    public void test03() throws Exception {

        PutGcsObjectServiceImpl target = new PutGcsObjectServiceImpl();

        GcsObjectPersistenceAdapter gcsObjectPersistenceAdapterMock = Mockito
                .mock(GcsObjectPersistenceAdapter.class);

        Field f = target.getClass().getDeclaredField("createBackupObjectPort");
        f.setAccessible(true);
        f.set(target, gcsObjectPersistenceAdapterMock);

        try {
            target.execute(null, "dummy");
            Assertions.assertTrue(true);
        } catch ( Exception e) {
            Assertions.fail();
        }
        
    }

    @Test
    @DisplayName("execute 正常系テスト4: executeメソッド呼び出し検証(車両情報がNotNULL)")
    public void test04() throws Exception {

        PutGcsObjectServiceImpl target = new PutGcsObjectServiceImpl();

        GcsObjectPersistenceAdapter gcsObjectPersistenceAdapterMock = Mockito
                .mock(GcsObjectPersistenceAdapter.class);
        doNothing().when(gcsObjectPersistenceAdapterMock).putObject(anyString(), anyString());

        Field f = target.getClass().getDeclaredField("createBackupObjectPort");
        f.setAccessible(true);
        f.set(target, gcsObjectPersistenceAdapterMock);

        String jsonData = "{ \\\"resultItems\\\":  [{\\\"vehicleId\\\": \\\"8192c452-a0e8-4263-9241-39d3e118646d\\\", \\\"vehicleData\\\":[[{\\\"dataTimestamp\\\": \\\"2025-05-28T16:50:19Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1},, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:20:20Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1}]]}]}";

        try {
            target.execute(jsonData, "8192c452-a0e8-4263-9241-39d3e118646d");
            Assertions.assertTrue(true);
        } catch ( Exception e) {
            Assertions.fail();
        }
        
    }

    @Test
    @DisplayName("execute 異常系テスト4: executeメソッド呼び出し検証(Adapter側で例外発生)")
    public void test05() throws Exception {

        PutGcsObjectServiceImpl target = new PutGcsObjectServiceImpl();

        GcsObjectPersistenceAdapter gcsObjectPersistenceAdapterMock = Mockito
                .mock(GcsObjectPersistenceAdapter.class);
        doNothing().when(gcsObjectPersistenceAdapterMock).initialize(any());
        doThrow(new Exception()).when(gcsObjectPersistenceAdapterMock).putObject(anyString(), anyString());

        Field f = target.getClass().getDeclaredField("createBackupObjectPort");
        f.setAccessible(true);
        f.set(target, gcsObjectPersistenceAdapterMock);

        String jsonData = "{ \\\"resultItems\\\":  [{\\\"vehicleId\\\": \\\"8192c452-a0e8-4263-9241-39d3e118646d\\\", \\\"vehicleData\\\":[[{\\\"dataTimestamp\\\": \\\"2025-05-28T16:50:19Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1},, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:20:20Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1}]]}]}";

        Properties properties = new Properties();
        properties.setProperty(BatchConst.KEY_GCS_BUCKET_NAME, "dummy-bucket");

        target.initialize(properties);
        Exception actual = Assertions.assertThrows(BatchExecutionException.class, () -> target.execute(jsonData, "8192c452-a0e8-4263-9241-39d3e118646d"));

        Assertions.assertEquals("【ERR】GCSバックアップへの連携ファイルPutに失敗しました。(キー情報: dummy-bucket)", actual.getMessage());
        
    }
}
