package cloudfn;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.protobuf.util.JsonFormat;
import com.google.cloud.functions.CloudEventsFunction;

import io.cloudevents.CloudEvent;

public class GCSEventFunction implements CloudEventsFunction {

    private static final Logger logger = Logger.getLogger(GCSEventFunction.class.getName());

    @Override
    public void accept(CloudEvent event) throws Exception {
        logger.info("Event" + event.getId());
        logger.info("Event Type:" + event.getType());

        if (event.getData() == null) {
            logger.warning("No data found in cloud event payload.");
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
    }

}
