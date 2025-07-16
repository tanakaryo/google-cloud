package com.example.demo.common.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.exceptions.BatchExecutionException;

public class DateUtilsTest {

    @Test
    @DisplayName("toPropertiesAndSetDefaultValue 正常系テスト1: バッチ実行日付がある場合")
    public void test01() throws Exception {
        String[] args = {"batch_exe_date=2025/05/28"};
        Properties actual = DateUtils.toPropertiesAndSetDefaultValue(Arrays.asList(args));

        Assertions.assertEquals("2025/05/28", actual.getProperty(BatchConst.KEY_JOB_PARAMETER_BATCH_EXE_DATE));
    }

    @Test
    @DisplayName("toPropertiesAndSetDefaultValue 正常系テスト2: バッチ実行日付がない場合")
    public void test02() throws Exception {
        //try (MockedStatic<DateUtils> dateUtils = Mockito.mockStatic(DateUtils.class)) {
        //    Date dummyDate = new SimpleDateFormat("yyyy/MM/dd").parse("2025/05/28");
        //    dateUtils.when(() -> DateUtils.newDate()).thenReturn(dummyDate);

        String[] args = {};
            Properties actual = DateUtils.toPropertiesAndSetDefaultValue(Arrays.asList(args));

            // MockedStaticを使用すると全メソッドが動かなくなるのでCalendarから前日日を生成して検証する
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.newDate());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            SimpleDateFormat sdf = new SimpleDateFormat(BatchConst.FMT_DATE_YMD_WITH_SLASH);
            sdf.setTimeZone(TimeZone.getTimeZone(BatchConst.TIME_ZONE_TOKYO));
            String strYesterday = sdf.format(calendar.getTime());
            
            Assertions.assertEquals(strYesterday, actual.getProperty(BatchConst.KEY_JOB_PARAMETER_BATCH_EXE_DATE));
        //}
    }

    @Test
    @DisplayName("validBatchExeDate 正常系テスト3: 正しい日付フォーマットの場合(0パディング形式)")
    public void test03() throws Exception {
        DateUtils.validBatchExeDate("2025/05/28");
    }

    @Test
    @DisplayName("validBatchExeDate 異常系テスト4: 誤った日付フォーマットの場合(パディングなし)")
    public void test04() throws Exception {
        Exception actual = Assertions.assertThrows(BatchExecutionException.class, 
        () -> DateUtils.validBatchExeDate("2025/5/28"));
        Assertions.assertEquals("【ERR】引数(バッチ処理日付)が不正な値です。(キー情報: 2025/5/28)", actual.getMessage());
        ;
    }

    @Test
    @DisplayName("validBatchExeDate 異常系テスト5: 誤った日付フォーマットの場合(yyyy-MM-dd)")
    public void test05() throws Exception {
        Exception actual = Assertions.assertThrows(BatchExecutionException.class, 
        () -> DateUtils.validBatchExeDate("2025-05-28"));
        Assertions.assertEquals("【ERR】引数(バッチ処理日付)が不正な値です。(キー情報: 2025-05-28)", actual.getMessage());
        ;
    }

    @Test
    @DisplayName("validBatchExeDate 異常系テスト6: 存在しない日付の場合(2025/02/31)")
    public void test06() throws Exception {
        Exception actual = Assertions.assertThrows(BatchExecutionException.class, 
        () -> DateUtils.validBatchExeDate("2025/02/31"));
        Assertions.assertEquals("【ERR】引数(バッチ処理日付)が不正な値です。(キー情報: 2025/02/31)", actual.getMessage());
        ;
    }

}
