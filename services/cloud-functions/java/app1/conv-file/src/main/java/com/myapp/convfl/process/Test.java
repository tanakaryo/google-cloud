package com.myapp.convfl.process;

import org.apache.commons.lang3.StringUtils;

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

        System.out.println(spStr[1]);
    }
}
