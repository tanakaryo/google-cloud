package com.example.demo.common.utils;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.collections4.CollectionUtils;

import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.exceptions.BatchExecutionException;
import com.example.demo.common.type.MessageTypes;

public final class DateUtils {

    private static final int VALUE_PREVIOUS_DAY = -1;

    private static final String FMT_BATCH_DATE = "uuuu/MM/dd";

    private DateUtils() {
        super();
    }

    public static Date newDate() {
        return new Date();
    }

    public static Properties toPropertiesAndSetDefaultValue(List<String> argList) throws Exception {
        Properties properties = new Properties();

        if (CollectionUtils.isEmpty(argList)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newDate());
            calendar.add(Calendar.DAY_OF_MONTH, VALUE_PREVIOUS_DAY);
            SimpleDateFormat sdf = new SimpleDateFormat(BatchConst.FMT_DATE_YMD_WITH_SLASH);
            sdf.setTimeZone(TimeZone.getTimeZone(BatchConst.TIME_ZONE_TOKYO));
            String strYesterday = sdf.format(calendar.getTime());
            properties.put(BatchConst.KEY_JOB_PARAMETER_BATCH_EXE_DATE, strYesterday);
        } else {
            for (String value : argList) {
                properties.load(new StringReader(value));
            }
        }

        return properties;
    }

    public static void validBatchExeDate(String batchExeDate) throws Exception {
        try {
            LocalDate.parse(batchExeDate, DateTimeFormatter.ofPattern(FMT_BATCH_DATE).withResolverStyle(ResolverStyle.STRICT));
        } catch (Exception e) {
            throw new BatchExecutionException(MessageTypes.ERR001.getMessage(batchExeDate) , e);
        }
    }
}
