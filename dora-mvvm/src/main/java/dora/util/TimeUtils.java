/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date and Time Related Tools.
 * 简体中文：日期时间相关工具。
 */
public final class TimeUtils {

    public final static String FORMAT_TYPE_DATE = "yyyy.MM.dd"; //yyyy.MM.dd
    public final static String FORMAT_TYPE_DATE_2 = "yyyy-MM-dd";   //yyyy-MM-dd
    public final static String FORMAT_TYPE_DATE_3 = "yyyyMMdd"; //yyyyMMdd
    public final static String FORMAT_TYPE_TIME = "HH:mm:ss";   //HH:mm:ss
    public final static String FORMAT_TYPE_TIME_2 = "HHmmss";   //HHmmss

    private TimeUtils() {
    }

    // <editor-folder desc="Date and time conversion.">

    public static String getTimeString(String formatType) {
        return getTimeString(formatType, Locale.getDefault());
    }

    public static String getTimeString(String formatType, Locale locale) {
        return getTimeString(getTimeLong(), locale, formatType);
    }

    public static String getTimeString(Date data, String formatType) {
        return getTimeString(data, Locale.getDefault(), formatType);
    }
    public static String getTimeString(Date data, Locale locale, String formatType) {
        return new SimpleDateFormat(formatType, locale).format(data);
    }

    public static String getTimeString(long currentTime, String formatType) {
       return getTimeString(currentTime, Locale.getDefault(), formatType);
    }

    public static String getTimeString(long currentTime, Locale locale, String formatType) {
        Date date = getTimeDate(currentTime, locale, formatType);
        return getTimeString(date, formatType);
    }

    public static Date getTimeDate(String formatType) {
        return getTimeDate(getTimeLong(), Locale.getDefault(), formatType);
    }

    public static Date getTimeDate(String strTime, String formatType) {
        return getTimeDate(strTime, Locale.getDefault(), formatType);
    }

    public static Date getTimeDate(String strTime, Locale locale, String formatType) {
        Date date = null;
        try {
            date = new SimpleDateFormat(formatType, locale).parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getTimeDate(long currentTime, String formatType) {
        return getTimeDate(currentTime, Locale.getDefault(), formatType);
    }

    public static Date getTimeDate(long currentTime, Locale locale, String formatType) {
        Date dateOld = new Date(currentTime);
        String sDateTime = getTimeString(dateOld, formatType);
        return getTimeDate(sDateTime, locale, formatType);
    }

    public static long getTimeLong() {
        return System.currentTimeMillis();
    }

    public static long getTimeLong(Date date) {
        return date.getTime();
    }

    public static long getTimeLong(String strTime, String formatType) {
        return getTimeLong(strTime, Locale.getDefault(), formatType);
    }

    public static long getTimeLong(String strTime, Locale locale, String formatType) {
        Date date = getTimeDate(strTime, locale, formatType);
        if (date == null) {
            return 0;
        } else {
            return getTimeLong(date);
        }
    }

    // </editor-folder>

    public static int getDaysOfMonth(int year, int month) {
        int result;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                result = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                result = 30;
                break;
            default:
                if (isLeapYear(year)) {
                    result = 29;
                } else {
                    result = 28;
                }
                break;
        }
        return result;
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static int getMonthsDifference(Date date1, Date date2) {
        int m1 = date1.getYear() * 12 + date1.getMonth();
        int m2 = date2.getYear() * 12 + date2.getMonth();
        return m2 - m1 + 1;
    }

    public static String formatTime(long milliSecs) {
        StringBuilder sb = new StringBuilder();
        int h = (int) (milliSecs / (60 * 60 * 1000));
        if (h < 10) {
            sb.append("0");
        }
        sb.append(h);
        sb.append(":");
        int m = (int) (milliSecs - h * (60 * 60 * 1000)) / (60 * 1000);
        if (m < 10) {
            sb.append("0");
        }
        sb.append(m);
        sb.append(":");
        int s = (int) (milliSecs % (60 * 1000) / 1000);
        if (s < 10) {
            sb.append("0");
        }
        sb.append(s);
        return sb.toString();
    }
}
