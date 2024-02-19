package com.myapp.convfl.exif;

import org.apache.commons.lang3.StringUtils;

import com.myapp.convfl.constants.CommonConstants;

/**
 * External Feed Interface difinition class.
 * フィードデータ 外部インターフェース情報定義クラス
 */
public class ExternalFeedIfDefinition implements IfDefinition {

    /** 出力対象項目 */
    private String[] outputColumns;

    /** 改行文字 */
    private String lineSeparator;

    /** 項目区切り文字 */
    private String columnSeparator;

    /** 項目囲い文字 */
    private char columnQuote;

    /** ヘッダーレコード */
    private String headerRecord;

    /**
     * コンストラクタ
     * 
     * @param outputColumns 出力対象項目
     * @param lineSeparator 改行文字
     * @param columnSeparator 項目区切り文字
     * @param columnQuote ヘッダーレコード
     */
    public ExternalFeedIfDefinition(String[] outputColumns, String lineSeparator,
            String columnSeparator, char columnQuote) {
        this.outputColumns = outputColumns;
        this.lineSeparator = lineSeparator;
        this.columnSeparator = columnSeparator;
        this.columnQuote = columnQuote;
    }

    /**
     * ヘッダーレコード取得
     * 
     * @return　ヘッダーレコード
     */
    public String getHeaderRecord() {
        // 作成済みの場合はメンバ変数を返却
        if (StringUtils.isNotEmpty(this.headerRecord)) {
            return this.headerRecord;
        }

        // データがない場合生成する
        StringBuilder sb = new StringBuilder();
        for (String column : this.outputColumns) {
            sb.append(this.columnQuote)
                    .append(column)
                    .append(this.columnQuote)
                    .append(this.columnSeparator);
        }
        this.headerRecord = StringUtils.removeEnd(sb.toString(), this.columnSeparator) + this.lineSeparator;
        return headerRecord;
    }

    /**
     * 項目並び順を取得
     * 
     * @return 項目並び順(String配列)
     */
    public String[] getColumnsOrder() {
        // 基本的にヘッダーレコードと同じ並び順となる想定
        return this.outputColumns;
    }

    /**
     * 出力ファイル拡張子を取得
     * 
     * @return 出力ファイル拡張子
     */
    public String getOutputFileExt() {
        // Feedは全てCSVとなる想定
        return CommonConstants.FILE_EXT_CSV;
    }

    @Override
    public String getLineSeparator() {
        return this.lineSeparator;
    }

    @Override
    public String getColumnSeparator() {
        return this.columnSeparator;
    }

    @Override
    public char getColumnQuote() {
        return this.columnQuote;
    }
}
