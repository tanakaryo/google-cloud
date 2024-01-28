package dataflowtest.pipeline;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.DoFn.ProcessElement;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class FilteringPipeline2 {
    /**
     * Options
     */
    public interface FilteringPipelineOptions extends StreamingOptions {
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

    public static void main(String[] args) throws Exception {

        FilteringPipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
                .as(FilteringPipelineOptions.class);

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
                // 3) Filtering Data
                .apply(ParDo.of(
                    new DoFn<String,String>() {
                        @ProcessElement
                        public void processElement(ProcessContext c) {
                            String word = c.element();
                            try {
                                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder builder = factory.newDocumentBuilder();
                                InputSource is = new InputSource(new StringReader(word));
                                Document doc = builder.parse(is);
                            } catch (Exception e) {
                                c.output(StringUtils.EMPTY);
                            }
                            c.output(word);
                        }
                    }
                ))
                // 4) Write one file to GCS for every window of messages.
                .apply("Write Files to GCS", write);

        // Execute the pipeline and wait until it finishes running.
        pipeline.run();
    }
}
