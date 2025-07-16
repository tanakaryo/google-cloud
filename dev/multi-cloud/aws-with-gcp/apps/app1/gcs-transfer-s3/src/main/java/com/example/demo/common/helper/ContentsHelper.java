package com.example.demo.common.helper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public final class ContentsHelper {

    private ContentsHelper() {
        super();
    }

    public static String toFlagFileContent(List<String> fileNameList) {

        StringBuilder sb = new StringBuilder();
        sb.append("{\"totalNumberOfFiles\":" + fileNameList.size() + ",");
        sb.append("\"fileList\":[");
        fileNameList.forEach((s) -> {
            sb.append("\"" + s + "\",");
        });
        String content = sb.toString();
        content = StringUtils.removeEnd(content, ",");

        return content + "]}";
    }

    public static String toVehicleDataContent(String vehicleId, List<String> vehicleData) {
        StringBuilder sb = new StringBuilder();

        sb.append("{ \"resultItems\":  [{\"vehicleId\": \"" + vehicleId + "\", \"vehicleData\":");
        sb.append(vehicleData.toString() + "}]}");

        return sb.toString();
    }
}
