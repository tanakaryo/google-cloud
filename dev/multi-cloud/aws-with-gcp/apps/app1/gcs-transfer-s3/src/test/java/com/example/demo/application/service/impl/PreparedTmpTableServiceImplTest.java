package com.example.demo.application.service.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.demo.adapter.out.invoker.BigQueryJobInvokeAdapter;
import com.example.demo.application.domain.service.impl.PreparedTmpTableServiceImpl;
import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.exceptions.BatchExecutionException;
import com.google.cloud.bigquery.BigQueryError;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobStatus;

public class PreparedTmpTableServiceImplTest {

    @Test
    @DisplayName("setJobParameter 正常系テスト1: setJobParameterメソッド呼び出し検証")
    public void test01() throws Exception {

        PreparedTmpTableServiceImpl target = new PreparedTmpTableServiceImpl();

        try {
            target.setJobParameter(new Properties());
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("bigQueryJobPort 異常系テスト2: bigQueryJobPortメソッド呼び出し検証(Adapter戻り値がNULL)")
    public void test02() throws Exception {

        PreparedTmpTableServiceImpl target = new PreparedTmpTableServiceImpl();

        BigQueryJobInvokeAdapter bigQueryJobInvokeAdapterMock = Mockito.mock(BigQueryJobInvokeAdapter.class);
        when(bigQueryJobInvokeAdapterMock.invoke(anyString())).thenReturn(null);

        Field f = target.getClass().getDeclaredField("bigQueryJobPort");
        f.setAccessible(true);
        f.set(target, bigQueryJobInvokeAdapterMock);

        Properties parameter = new Properties();
        parameter.setProperty(BatchConst.KEY_YEAR, "2025");
        parameter.setProperty(BatchConst.KEY_MONTH, "05");
        parameter.setProperty(BatchConst.KEY_DAY, "28");
        parameter.setProperty(BatchConst.KEY_CONTRACTOR, "PMJ");

        target.setJobParameter(parameter);
        Exception e = Assertions.assertThrows(BatchExecutionException.class, () -> target.execute());

        Assertions.assertEquals("【ERR】一時テーブル作成に失敗しました。(キー情報: null)", e.getMessage());
    }

    @Test
    @DisplayName("bigQueryJobPort 異常系テスト3: bigQueryJobPortメソッド呼び出し検証(Adapter戻り値がNotNULL)")
    public void test03() throws Exception {

        PreparedTmpTableServiceImpl target = new PreparedTmpTableServiceImpl();

        BigQueryError bigQueryErrorMock = new BigQueryError("dummy-reason", "dummy-location", "dummy-message");

        JobStatus jobStatusMock = Mockito.mock(JobStatus.class);
        when(jobStatusMock.getError()).thenReturn(bigQueryErrorMock);

        Job jobMock = Mockito.mock(Job.class);
        when(jobMock.getStatus()).thenReturn(jobStatusMock);

        BigQueryJobInvokeAdapter bigQueryJobInvokeAdapterMock = Mockito.mock(BigQueryJobInvokeAdapter.class);
        when(bigQueryJobInvokeAdapterMock.invoke(anyString())).thenReturn(jobMock);

        Field f = target.getClass().getDeclaredField("bigQueryJobPort");
        f.setAccessible(true);
        f.set(target, bigQueryJobInvokeAdapterMock);

        Properties parameter = new Properties();
        parameter.setProperty(BatchConst.KEY_YEAR, "2025");
        parameter.setProperty(BatchConst.KEY_MONTH, "05");
        parameter.setProperty(BatchConst.KEY_DAY, "28");
        parameter.setProperty(BatchConst.KEY_CONTRACTOR, "PMJ");

        target.setJobParameter(parameter);
        Exception e = Assertions.assertThrows(BatchExecutionException.class, () -> target.execute());

        Assertions.assertEquals("【ERR】一時テーブル作成に失敗しました。(キー情報: BigQueryError{reason=dummy-reason, location=dummy-location, message=dummy-message})", e.getMessage());
    }

    @Test
    @DisplayName("bigQueryJobPort 正常系テスト3: bigQueryJobPortメソッド呼び出し検証(Adapter戻り値がNotNULL)")
    public void test04() throws Exception {

        PreparedTmpTableServiceImpl target = new PreparedTmpTableServiceImpl();

        JobStatus jobStatusMock = Mockito.mock(JobStatus.class);
        when(jobStatusMock.getError()).thenReturn(null);

        Job jobMock = Mockito.mock(Job.class);
        when(jobMock.getStatus()).thenReturn(jobStatusMock);

        BigQueryJobInvokeAdapter bigQueryJobInvokeAdapterMock = Mockito.mock(BigQueryJobInvokeAdapter.class);
        when(bigQueryJobInvokeAdapterMock.invoke(anyString())).thenReturn(jobMock);

        Field f = target.getClass().getDeclaredField("bigQueryJobPort");
        f.setAccessible(true);
        f.set(target, bigQueryJobInvokeAdapterMock);

        Properties parameter = new Properties();
        parameter.setProperty(BatchConst.KEY_YEAR, "2025");
        parameter.setProperty(BatchConst.KEY_MONTH, "05");
        parameter.setProperty(BatchConst.KEY_DAY, "28");
        parameter.setProperty(BatchConst.KEY_CONTRACTOR, "PMJ");

        try {
            target.setJobParameter(parameter);
            target.execute();
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}
