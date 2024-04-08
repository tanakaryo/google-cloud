package com.myapp.convfl.constants;

/**
 * 共通定数クラス
 * 
 * 特定ビジネス,ドメインに依存しない定数を定義
 */
public class CommonConstants {

    /**
     * コンストラクタ
     */
    private CommonConstants() {
    }

    // Directory-Separator
    public static final String DIR_SEPARATOR = "/";

    // Line-Separator
    public static final String LINE_SEPARATOR_CRLF = "\r\n";

    // Column-Separator
    public static final String COLUMN_SEPARATOR_COMMA = ",";

    // Colum-Quotation
    public static final char COLUMN_QUOTE_DOUBLE = '"';

    // File-Extention
    public static final String FILE_EXT_CSV = ".csv";
    public static final String FILE_EXT_JSON = ".json";
}
