package com.ptit.workhours;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static final double STANDARD_HOURS_PER_DAY = 8.0; // hours beyond this per day are overtime
    public static final double OVERTIME_RATE = 1.5;          // overtime pays 1.5x hourly rate

    public static String today() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }
    public static int currentMonth() { return Calendar.getInstance().get(Calendar.MONTH) + 1; }
    public static int currentYear() { return Calendar.getInstance().get(Calendar.YEAR); }
    /** LIKE pattern for work dates in a month, e.g. 2026-10-% */
    public static String monthPrefix(int month, int year) {
        return String.format(Locale.US, "%04d-%02d-%%", year, month);
    }
    public static String fmtTime(Long t) {
        return t == null ? "--:--:--" : new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date(t));
    }
    public static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
    public static String money(double v) { return String.format(new Locale("vi", "VN"), "%,.0f VND", v); }

    public static String hash(String s) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
