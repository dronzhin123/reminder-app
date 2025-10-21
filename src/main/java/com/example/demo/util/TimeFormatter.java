package com.example.demo.util;

public class TimeFormatter {

    private static final String HMS_FORMAT = "%02d:%02d:%02d";

    public static String sToHMS(long seconds) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return String.format(HMS_FORMAT, h, m, s);
    }

}
