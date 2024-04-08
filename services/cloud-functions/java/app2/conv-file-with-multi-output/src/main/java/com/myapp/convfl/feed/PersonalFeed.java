package com.myapp.convfl.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersonalFeed フィードオブジェクト
 * 
 * 出力対象項目のみ定義する。
 * 出力対象外であればignoreされる。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalFeed {

    /** 氏名 */
    @JsonProperty("name")
    private String name;
    /** 住所 */
    @JsonProperty("address")
    private String address;
    



}
