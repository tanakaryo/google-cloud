package dataflowtest.pipeline;

import java.io.IOException;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

public class PubSubToGcsMultiPipeLine {
    public interface PubSubToGcsMPOptions extends StreamingOptions {
        @Description("The Cloud Pub/Sub topic to read from.")
        @Required
        String getInputTopic();

        void setInputTopic(String value);

        @Description("The Cloud Storage bucket to write to")
        @Required
        String getBucketName();

        void setBucketName(String value);

        @Description("Output file's window size in number of minutes.")
        @Default.Integer(1)
        Integer getWindowSize();

        void setWindowSize(Integer value);

        @Description("Path of the output file including its filename prefix.")
        @Required
        String getOutput();

        void setOutput(String value);
    }

    public static void main(String[] args) throws IOException {

        PubSubToGcsMPOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
                .as(PubSubToGcsMPOptions.class);

        options.setStreaming(true);

        Pipeline pipeline = Pipeline.create(options);
        PubsubIO.Read<String> read = PubsubIO.readStrings().fromTopic(options.getInputTopic());
        Window<String> window = Window.<String>into(FixedWindows.of(Duration.standardMinutes(options.getWindowSize())));
        TextIO.Write write = TextIO.write().withWindowedWrites().to(options.getBucketName())
                .withCompression(Compression.GZIP).withNumShards(5);

        pipeline
                // 1) Read string messages from a Pub/Sub topic.
                .apply("Read PubSub Messages", read)
                // 2) Group the messages into fixed-sized minute intervals.
                .apply(window)
                // 3) Write one file to GCS for every window of messages.
                .apply("Write Files to GCS", write);

        // Execute the pipeline and wait until it finishes running.
        pipeline.run();
    }
}
