package com.example.demo;

import java.util.Properties;
import java.util.logging.Logger;

import com.example.demo.adapter.in.batch.CommonFileTransferBatch;
import com.example.demo.common.parse.ArgumentParser;
import com.example.demo.common.type.MessageTypes;

public class AppMain {

    private static final Logger LOGGER = Logger.getLogger(AppMain.class.getName());

    public static void main(String[] args) throws Exception {

        LOGGER.info(MessageTypes.INF012.getMessage());

        // Job Parameter Build
        Properties jobParameter = ArgumentParser.parseArgumentAndBuildJobParameter(args);

        CommonFileTransferBatch batch = CommonFileTransferBatch.HistoryDataTransferBatchBuilder.getBuilder()
                .setParameter(jobParameter).build();
        batch.run();

        LOGGER.info(MessageTypes.INF013.getMessage());
    }
}
