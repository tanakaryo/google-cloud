package com.myapp.convfl.factory;

import org.apache.commons.lang3.StringUtils;

import com.myapp.convfl.constants.CommonConstants;
import com.myapp.convfl.type.FeedType;

/**
 * FeedTypeFactory
 * 
 * FeedType生成クラス
 */
public final class FeedTypeFactory {

    /** FeedNameに該当する文字配列の番号 */
    private static final int NUMBER_OF_FEED_NAME = 1;

    /**
     * FeedTypeオブジェクトを生成・提供する
     * 
     * @param gcsFileName CloudStorageファイル名
     * @return FeedTypeオブジェクト
     */
    public static FeedType getFeedType(String gcsFileName) {

        // CloudStorageファイル名分割
        String[] splitedDataName = StringUtils.split(gcsFileName, CommonConstants.DIR_SEPARATOR);

        // フィードタイプ取得
        return FeedType.valueOf(new String(splitedDataName[NUMBER_OF_FEED_NAME]).toUpperCase());
    }
}
