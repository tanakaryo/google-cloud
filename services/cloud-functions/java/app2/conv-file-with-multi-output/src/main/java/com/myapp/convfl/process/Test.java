package com.myapp.convfl.process;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.myapp.convfl.factory.FeedTypeFactory;
import com.myapp.convfl.type.FeedType;

public class Test {
    public static void main(String[] args) {
        String str = "/feed/personal_feed/PV_DATA.json";
        String[] spStr = StringUtils.split(str, "/");
        int i = 0;
        for (String s : spStr) {
            System.out.println("count:" + i);
            System.out.println("str:" + s);
            i++;
        }
        FeedType feedType = FeedTypeFactory.getFeedType(str);

        FeedType[] types = FeedType.class.getEnumConstants();
        System.out.println(types[0].toString());

        boolean b1 = Arrays.stream(FeedType.class.getEnumConstants()).anyMatch(e -> StringUtils.containsIgnoreCase("feed/personal_feed/FEED_20240219.json", e.toString()));
        System.out.println(b1);

        System.out.println(spStr[1]);
    }
}
