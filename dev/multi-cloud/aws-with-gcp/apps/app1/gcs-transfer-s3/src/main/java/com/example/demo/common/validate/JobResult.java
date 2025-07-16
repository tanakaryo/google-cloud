package com.example.demo.common.validate;

import java.util.Objects;

import com.example.demo.common.exceptions.BatchExecutionException;
import com.example.demo.common.type.MessageTypes;
import com.google.cloud.bigquery.Job;

public final class JobResult {

    public static void verify(Job result) throws Exception {
        if (Objects.isNull(result)) {
            throw new BatchExecutionException(MessageTypes.ERR002.getMessage(result));
        }
        if (Objects.nonNull(result.getStatus().getError())) {
            throw new BatchExecutionException(MessageTypes.ERR002.getMessage(result.getStatus().getError()));
        }
    }
}
