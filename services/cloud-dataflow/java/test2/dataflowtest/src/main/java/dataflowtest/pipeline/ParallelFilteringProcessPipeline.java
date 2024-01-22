package dataflowtest.pipeline;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.beam.sdk.Pipeline;
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
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParallelFilteringProcessPipeline {
    /**
     * Options
     */
    public interface ParallelFilteringProcessPipelineOptions extends StreamingOptions {
        @Description("The Cloud Pub/Sub topic to read from.")
        @Required
        String getInputTopic();

        void setInputTopic(String value);

        @Description("The Cloud Storage bucket of Main to write to")
        @Required
        String getBucketMain();

        void setBucketMain(String value);

        @Description("The Cloud Storage bucket of Sub to write to")
        @Required
        String getBucketSub();

        void setBucketSub(String value);

        @Description("Output file's window size in number of minutes.")
        @Default.Integer(1)
        Integer getWindowSizeMain();

        void setWindowSizeMain(Integer value);

        @Description("Output file's window size in num or min.")
        @Default.Integer(1)
        Integer getWindowSizeSub();

        void setWindowSizeSub(Integer value);

        @Description("Output files's num of sharding.")
        @Default.Integer(1)
        Integer getShardNumMain();

        void setShardNumMain(Integer value);

        @Description("Output files's num of sharding.")
        @Default.Integer(1)
        Integer getShardNumSub();

        void setShardNumSub(Integer value);

        @Description("Path of the output file including its filename prefix.")
        @Required
        String getOutput();

        void setOutput(String value);
    }

    public static void main(String[] args) throws Exception {

        ParallelFilteringProcessPipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
                .as(ParallelFilteringProcessPipelineOptions.class);

        options.setStreaming(true);

        Pipeline pipeline = Pipeline.create(options);
        PubsubIO.Read<String> read = PubsubIO.readStrings().fromTopic(options.getInputTopic());
        PCollection<String> readItem = pipeline.apply("Read from Topic.", read);

        // Main Process(JSON処理用)
        readItem.apply("Filtering JSON",
                ParDo.of(new DoFn<String, String>() {
                    @ProcessElement
                    public void filteringJson(ProcessContext c) {
                        String word = c.element();
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode root = mapper.readTree(word);
                            c.output(word);
                        } catch (Exception e) {
                        }
                    }
                }))
                .apply("Window Main",
                        Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSizeMain()))))
                .apply("Write to main", TextIO.write().withWindowedWrites().withNumShards(options.getShardNumMain())
                        .to(options.getBucketMain()));

        // Sub Process(XML処理用)
        readItem.apply("Filtering XML",
                ParDo.of(new DoFn<String, String>() {
                    @ProcessElement
                    public void filteringXML(ProcessContext c) {
                        String word = c.element();
                        try {
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            InputSource is = new InputSource(new StringReader(word));
                            Document doc = builder.parse(is);
                            c.output(word);
                        } catch (Exception e) {
                        }
                    }
                }))
                .apply("Window Sub",
                        Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSizeSub()))))
                .apply("Write to sub", TextIO.write().withWindowedWrites().withNumShards(options.getShardNumSub())
                        .to(options.getBucketSub()));

        // Execute the pipeline and wait until it finishes running.
        pipeline.run();
    }
}
