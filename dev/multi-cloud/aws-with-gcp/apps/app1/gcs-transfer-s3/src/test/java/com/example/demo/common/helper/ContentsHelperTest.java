package com.example.demo.common.helper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContentsHelperTest {

    @Test
    @DisplayName("toFlagFileContent 正常系テスト1: ファイル1件以上の場合")
    public void test01() {

        List<String> fileNameList = new ArrayList<>();
        fileNameList.add("20250528_PMJ_ev_charging_data_history_8192c452-a0e8-4263-9241-39d3e118646d.json");
        fileNameList.add("20250528_PMJ_ev_charging_data_history_4192c452-a0e8-4263-9241-39d3e118646d.json");

        String actual = ContentsHelper.toFlagFileContent(fileNameList);

        Assertions.assertEquals("{\"totalNumberOfFiles\":2,\"fileList\":[\"20250528_PMJ_ev_charging_data_history_8192c452-a0e8-4263-9241-39d3e118646d.json\",\"20250528_PMJ_ev_charging_data_history_4192c452-a0e8-4263-9241-39d3e118646d.json\"]}", actual);
    }

    @Test
    @DisplayName("toFlagFileContent 正常系テスト2: ファイル0件の場合")
    public void test02() {

        List<String> fileNameList = new ArrayList<>();

        String actual = ContentsHelper.toFlagFileContent(fileNameList);

        Assertions.assertEquals("{\"totalNumberOfFiles\":0,\"fileList\":[]}", actual);
    }

    @Test
    @DisplayName("toVehicleDataContent 正常系テスト3: ファイル1件以上の場合")
    public void test03() {

        List<String> records = new ArrayList<>();
        records.add("[{\"dataTimestamp\": \"2025-05-28T16:50:19Z\", \"stateOfCharge\": 91.2, \"plugConnectionStatus\": 2, \"latitude\": 43.071049, \"longitude\": 141.370823, \"odometer\": 10416.0, \"batteryCapacity\": 12.1},");
        records.add("{\"dataTimestamp\": \"2025-05-28T16:20:20Z\", \"stateOfCharge\": 91.2, \"plugConnectionStatus\": 2, \"latitude\": 43.071049, \"longitude\": 141.370823, \"odometer\": 10416.0, \"batteryCapacity\": 12.1}]");
        String actual = ContentsHelper.toVehicleDataContent("8192c452-a0e8-4263-9241-39d3e118646d", records);

        Assertions.assertEquals("{ \"resultItems\":  [{\"vehicleId\": \"8192c452-a0e8-4263-9241-39d3e118646d\", \"vehicleData\":[[{\"dataTimestamp\": \"2025-05-28T16:50:19Z\", \"stateOfCharge\": 91.2, \"plugConnectionStatus\": 2, \"latitude\": 43.071049, \"longitude\": 141.370823, \"odometer\": 10416.0, \"batteryCapacity\": 12.1},, {\"dataTimestamp\": \"2025-05-28T16:20:20Z\", \"stateOfCharge\": 91.2, \"plugConnectionStatus\": 2, \"latitude\": 43.071049, \"longitude\": 141.370823, \"odometer\": 10416.0, \"batteryCapacity\": 12.1}]]}]}", actual);
    }

    @Test
    @DisplayName("toVehicleDataContent 正常系テスト4: ファイル0件の場合")
    public void test04() {

        List<String> records = new ArrayList<>();
        String actual = ContentsHelper.toVehicleDataContent("8192c452-a0e8-4263-9241-39d3e118646d", records);

        Assertions.assertEquals("{ \"resultItems\":  [{\"vehicleId\": \"8192c452-a0e8-4263-9241-39d3e118646d\", \"vehicleData\":[]}]}", actual);
    }
}
