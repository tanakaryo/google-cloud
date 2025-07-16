package com.example.demo.common.type;

import java.text.MessageFormat;

public enum MessageTypes {

    ERR001("【ERR】引数(バッチ処理日付)が不正な値です。(キー情報: {0})"),
    ERR002("【ERR】一時テーブル作成に失敗しました。(キー情報: {0})"),
    ERR003("【ERR】GCSバックアップへの連携ファイルPutに失敗しました。(キー情報: {0})"),
    ERR004("【ERR】3rdParty環境へのフラグファイル作成に失敗しました。(3rdParty: {0}, キー情報: {1})"),
    ERR005("【ERR】3rdParty環境への連携ファイルPutに失敗しました。(3rdParty: {0}, キー情報: {1})"),
    ERR006("【ERR】Missing Inject Target in Context (Type: {0})"),
    ERR007("【ERR】サービスアカウントのJWT取得に失敗しました。"),
    INF001("START Common File Transfer Batch (3rdParty: {0})."),
    INF002("END Common File Transfer Batch (3rdParty: {0})."),
    INF003("batch_exe_date is {0}."),
    INF004("START Step1 Create temp tables."),
    INF005("END Step1 Create temp tables."),
    INF006("START Step2 Create vehicle data (vehicleId: {0})."),
    INF007("END Step2 Create vehicle data (vehicleId: {0})."),
    INF008("Successful file put to Backup (vehicleId: {0})."),
    INF009("Successful file put to 3rdParty (vehicleId: {0})."),
    INF010("START Step3 Create flag file (Count: {0})"),
    INF011("END Step3 Create flag file (Count: {0})"),
    INF012("START Common File Transfer."),
    INF013("END Common File Transfer."),
    INF014("Transfer File Count is  {0} files.");


    private String message;

    private MessageTypes(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return MessageFormat.format(this.message, args);
    }

    public String getMessage() {
        return this.message;
    }
}
