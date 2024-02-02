package dataflowtest.policy;

import static com.google.common.base.MoreObjects.firstNonNull;

import java.time.LocalDate;

import org.apache.beam.repackaged.core.org.apache.commons.lang3.StringUtils;
import org.apache.beam.sdk.io.FileBasedSink;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.FileBasedSink.FilenamePolicy;
import org.apache.beam.sdk.io.FileBasedSink.OutputFileHints;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.io.fs.ResolveOptions.StandardResolveOptions;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.beam.sdk.transforms.windowing.IntervalWindow;
import org.apache.beam.sdk.transforms.windowing.PaneInfo;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class CustomFileNamePolicy extends FilenamePolicy {

    private static final DateTimeFormatter dateFormatter = ISODateTimeFormat.basicDate().withZone(DateTimeZone.getDefault());

    private static final DateTimeFormatter hourFormatter = ISODateTimeFormat.hourMinute();

    private final ResourceId baseFilename;

    public CustomFileNamePolicy(String baseFilename) {
        this.baseFilename = FileSystems.matchNewResource(baseFilename, true);
    }

    public String filenamePrefixForWindow(IntervalWindow window) {
        //String filePrefix = baseFilename.isDirectory() ? "" : firstNonNull(baseFilename.getFilename(), "");
        String directoryName = dateFormatter.print(window.start());
        return String.format("%s/HKK_RAW_DATA_%s",directoryName, dateFormatter.print(window.start()) + StringUtils.remove(hourFormatter.print(window.start()), ':') );
    }

    @Override
    public ResourceId windowedFilename(int shardNumber, int numShards, BoundedWindow window,PaneInfo paneInfo,
            OutputFileHints outputFileHints) {
                IntervalWindow intervalWindow = (IntervalWindow) window;
                String filename = String.format("%s_%s.jsonl",
                filenamePrefixForWindow(intervalWindow),
                shardNumber+1,
                numShards);
                
                return baseFilename.getCurrentDirectory().resolve(filename, StandardResolveOptions.RESOLVE_FILE);
    }

    @Override
    public ResourceId unwindowedFilename(int shardNumber, int numShards, OutputFileHints outputFileHints) {
        throw new UnsupportedOperationException("Unsupported.");
    }
    
}
