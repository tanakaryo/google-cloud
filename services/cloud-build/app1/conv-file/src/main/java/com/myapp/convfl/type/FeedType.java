package com.myapp.convfl.type;

import com.myapp.convfl.constants.CommonConstants;
import com.myapp.convfl.exif.ExternalFeedIfDefinition;
import com.myapp.convfl.feed.PersonalFeed;

public enum FeedType {

    /** PersonalFeedタイプ */
    PERSONAL_FEED(new ExternalFeedIfDefinition(
        new String[]{"name","address"}
        , CommonConstants.LINE_SEPARATOR_CRLF
        , CommonConstants.COLUMN_SEPARATOR_COMMA
        , CommonConstants.COLUMN_QUOTE_DOUBLE
    ), PersonalFeed.class);

    private ExternalFeedIfDefinition exifDef;

    private Class<?> feedObjectClass;

    private FeedType(ExternalFeedIfDefinition exifDef, Class<?> feedObjectClass) {
        this.exifDef = exifDef;
        this.feedObjectClass = feedObjectClass;
    }

    public ExternalFeedIfDefinition getExifDef() {
        return this.exifDef;
    }

    public Class<?> getFeedObjectClass() {
        return this.feedObjectClass;
    }
}
