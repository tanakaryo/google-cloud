package com.example.demo.application.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.demo.adapter.out.persistence.S3ObjectPersistenceAdapter;
import com.example.demo.application.domain.service.impl.S3ObjectWriteServiceImpl;
import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.exceptions.BatchExecutionException;

public class S3ObjectWriteServiceImplTest {

    @Test
    @DisplayName("initialize 正常系テスト1: initializeメソッド呼び出し検証")
    public void test01() throws Exception {

        S3ObjectWriteServiceImpl target = new S3ObjectWriteServiceImpl();

        S3ObjectPersistenceAdapter s3ObjectPersistenceAdapterMock = Mockito.mock(S3ObjectPersistenceAdapter.class);
        doNothing().when(s3ObjectPersistenceAdapterMock).initialize(any());

        Field f = target.getClass().getDeclaredField("createTransferObjectPort");
        f.setAccessible(true);
        f.set(target, s3ObjectPersistenceAdapterMock);

        try {
            target.initialize(new Properties());
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
        
    }

    @Test
    @DisplayName("initialize 異常系テスト2: initializeメソッド呼び出し検証")
    public void test02() throws Exception {

        S3ObjectWriteServiceImpl target = new S3ObjectWriteServiceImpl();

        S3ObjectPersistenceAdapter s3ObjectPersistenceAdapterMock = Mockito.mock(S3ObjectPersistenceAdapter.class);
        doThrow(new Exception()).when(s3ObjectPersistenceAdapterMock).initialize(any());

        Field f = target.getClass().getDeclaredField("createTransferObjectPort");
        f.setAccessible(true);
        f.set(target, s3ObjectPersistenceAdapterMock);

        Exception actual = Assertions.assertThrows(BatchExecutionException.class, () -> target.initialize(new Properties()));
        Assertions.assertEquals("【ERR】サービスアカウントのJWT取得に失敗しました。", actual.getMessage());
    } 

    @Test
    @DisplayName("close 正常系テスト3: closeメソッド呼び出し検証")
    public void test03() throws Exception {

        S3ObjectWriteServiceImpl target = new S3ObjectWriteServiceImpl();

        S3ObjectPersistenceAdapter s3ObjectPersistenceAdapterMock = Mockito.mock(S3ObjectPersistenceAdapter.class);
        doNothing().when(s3ObjectPersistenceAdapterMock).close();

        Field f = target.getClass().getDeclaredField("createTransferObjectPort");
        f.setAccessible(true);
        f.set(target, s3ObjectPersistenceAdapterMock);

        try {
            target.close();
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
        
    } 

    @Test
    @DisplayName("execute 正常系テスト4: executeメソッド呼び出し検証(車両情報がNULL)")
    public void test04() throws Exception {

        S3ObjectWriteServiceImpl target = new S3ObjectWriteServiceImpl();

        S3ObjectPersistenceAdapter s3ObjectPersistenceAdapterMock = Mockito.mock(S3ObjectPersistenceAdapter.class);


        Field f = target.getClass().getDeclaredField("createTransferObjectPort");
        f.setAccessible(true);
        f.set(target, s3ObjectPersistenceAdapterMock);

        try {
            target.execute(null, "dummy-id");
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
        
    }

    @Test
    @DisplayName("execute 正常系テスト5: executeメソッド呼び出し検証(車両情報がNotNULL)")
    public void test05() throws Exception {

        S3ObjectWriteServiceImpl target = new S3ObjectWriteServiceImpl();

        S3ObjectPersistenceAdapter s3ObjectPersistenceAdapterMock = Mockito.mock(S3ObjectPersistenceAdapter.class);
        doNothing().when(s3ObjectPersistenceAdapterMock).putObject(anyString(), anyString());

        Field f = target.getClass().getDeclaredField("createTransferObjectPort");
        f.setAccessible(true);
        f.set(target, s3ObjectPersistenceAdapterMock);

        String jsonData = "{ \\\"resultItems\\\":  [{\\\"vehicleId\\\": \\\"8192c452-a0e8-4263-9241-39d3e118646d\\\", \\\"vehicleData\\\":[[{\\\"dataTimestamp\\\": \\\"2025-05-28T16:50:19Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1},, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:20:20Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1}]]}]}";

        try {
            target.execute(jsonData, "8192c452-a0e8-4263-9241-39d3e118646d");
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
        
    } 

    @Test
    @DisplayName("execute 異常系テスト6: executeメソッド呼び出し検証(Adapterで例外発生)")
    public void test06() throws Exception {

        S3ObjectWriteServiceImpl target = new S3ObjectWriteServiceImpl();

        S3ObjectPersistenceAdapter s3ObjectPersistenceAdapterMock = Mockito.mock(S3ObjectPersistenceAdapter.class);
        doNothing().when(s3ObjectPersistenceAdapterMock).initialize(any());
        doThrow(new Exception()).when(s3ObjectPersistenceAdapterMock).putObject(anyString(), anyString());

        Field f = target.getClass().getDeclaredField("createTransferObjectPort");
        f.setAccessible(true);
        f.set(target, s3ObjectPersistenceAdapterMock);

        String jsonData = "{ \\\"resultItems\\\":  [{\\\"vehicleId\\\": \\\"8192c452-a0e8-4263-9241-39d3e118646d\\\", \\\"vehicleData\\\":[[{\\\"dataTimestamp\\\": \\\"2025-05-28T16:50:19Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1},, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:20:20Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1}]]}]}";
        Properties properties = new Properties();
        properties.setProperty(BatchConst.KEY_CONTRACTOR, "DMY");
        properties.setProperty(BatchConst.KEY_AWS_S3_BUCKET_NAME, "dummy-bucket");

        target.initialize(properties);
        Exception actual = Assertions.assertThrows(BatchExecutionException.class, () -> {
            target.execute(jsonData, "8192c452-a0e8-4263-9241-39d3e118646d");
        });
            
        Assertions.assertEquals("【ERR】3rdParty環境への連携ファイルPutに失敗しました。(3rdParty: DMY, キー情報: dummy-bucket)", actual.getMessage());
        
    } 

    @Test
    @DisplayName("putFlag 正常系テスト7: putFlagメソッド呼び出し検証")
    public void test07() throws Exception {

        S3ObjectWriteServiceImpl target = new S3ObjectWriteServiceImpl();

        S3ObjectPersistenceAdapter s3ObjectPersistenceAdapterMock = Mockito.mock(S3ObjectPersistenceAdapter.class);
        doNothing().when(s3ObjectPersistenceAdapterMock).initialize(any());
        when(s3ObjectPersistenceAdapterMock.putFlagObject()).thenReturn(1);

        Field f = target.getClass().getDeclaredField("createTransferObjectPort");
        f.setAccessible(true);
        f.set(target, s3ObjectPersistenceAdapterMock);

        Properties properties = new Properties();
        properties.setProperty(BatchConst.KEY_CONTRACTOR, "DMY");

        try {
            target.initialize(properties);
            target.putFlag();
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
    } 
}
