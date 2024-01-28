package dataflowtest.pipeline;

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
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.joda.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParallelFilteringTagProcessPipeline {

    /** Process Tags */
    static final TupleTag<String> processJsonTag = new TupleTag<String>() {
    };
    static final TupleTag<String> processXmlTag = new TupleTag<String>() {
    };

    /**
     * Options
     */
    public interface ParallelFilteringTagProcessPipelineOptions extends StreamingOptions {
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

        ParallelFilteringTagProcessPipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
                .as(ParallelFilteringTagProcessPipelineOptions.class);

        options.setStreaming(true);

        Pipeline pipeline = Pipeline.create(options);
        PubsubIO.Read<String> read = PubsubIO.readStrings().fromTopic(options.getInputTopic());
        PCollection<String> readItem = pipeline.apply("Read from Topic.", read);

        PCollectionTuple tagCollection = readItem.apply(ParDo.of(new DoFn<String, String>() {
            @ProcessElement
            public void processElement(ProcessContext c) {
                String element = c.element();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(element);
                    c.output(processJsonTag, element);
                } catch (Exception e) {
                    c.output(processXmlTag, element);
                }
            }
        }).withOutputTags(processJsonTag, TupleTagList.of(processXmlTag)));

        // Main Process(JSON処理用)
        tagCollection.get(processJsonTag)
                .apply("Window for JSON Output",
                        Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSizeMain()))))
                .apply("Write to JSON Bucket", TextIO.write().withWindowedWrites().withNumShards(options.getShardNumMain())
                        .to(options.getBucketMain()));

        // Sub Process(XML処理用)
        tagCollection.get(processXmlTag)
                .apply("Window for XML Output",
                        Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSizeSub()))))
                .apply("Write to XML Bucket", TextIO.write().withWindowedWrites().withNumShards(options.getShardNumSub())
                        .to(options.getBucketSub()));

        // Execute the pipeline and wait until it finishes running.
        pipeline.run();
    }
}
