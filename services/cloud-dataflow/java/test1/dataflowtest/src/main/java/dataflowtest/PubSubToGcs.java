package dataflowtest;

import org.apache.beam.examples.common.WriteOneFilePerWindow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

public class PubSubToGcs {

    public interface PubSubToGcsOptions extends StreamingOptions {
        @Required
        String getInputTopic();

        void setInputTopic(String value);

        @Default.Integer(1)
        Integer getWindowSize();

        void setWindowSize(Integer value);

        @Required
        String getOutput();

        void setOutput(String value);
    }

    public static void main(String[] args) {
        int numShards = 1;

        PubSubToGcsOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(PubSubToGcsOptions.class);

        options.setStreaming(true);

        Pipeline pipeline = Pipeline.create(options);

        pipeline
        .apply("Read PubSub Messages", PubsubIO.readStrings().fromTopic(options.getInputTopic()))
        .apply(Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSize()))))
        .apply("Write Files to GCS", new WriteOneFilePerWindow(options.getOutput(), numShards));

        pipeline.run().waitUntilFinish();
    }
}
