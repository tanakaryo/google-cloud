package com.myapp.convfl.util;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;

import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.protobuf.util.JsonFormat;

import io.cloudevents.CloudEvent;

public final class CloudEventUtility {

    public static boolean hasPayload(CloudEvent event, Logger logger) throws Exception {
        logger.info("Event" + event.getId());
        logger.info("Event Type:" + event.getType());

        if (event.getData() == null) {
            logger.error("No data found in cloud event payload.");
            return false;
        }

        String cloudEventData = new String(event.getData().toBytes(), StandardCharsets.UTF_8);
        StorageObjectData.Builder builder = StorageObjectData.newBuilder();
        JsonFormat.parser().merge(cloudEventData, builder);
        StorageObjectData data = builder.build();

        logger.info("Bucket: " + data.getBucket());
        logger.info("File: " + data.getName());
        logger.info("Metageneration: " + data.getMetageneration());
        logger.info("Created: " + data.getTimeCreated());
        logger.info("Updated: " + data.getUpdated());

        return true;
    }
}
