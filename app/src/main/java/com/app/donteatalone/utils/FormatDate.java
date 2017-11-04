package com.app.donteatalone.utils;

import com.app.donteatalone.R;

/**
 * Created by ChomChom on 12-Oct-17
 */

public class FormatDate {
    public static boolean checkLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 != 0) {
                return true;
            }
        }
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            }
        }
        return false;
    }

    public static int checkDateInMonth(int month, int year) {
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return R.array.Day_31;
            case 3:
            case 5:
            case 8:
            case 10:
                return R.array.Day_30;
            case 1:
                if (checkLeapYear(year)) {
                    return R.array.Day_29;
                } else {
                    return R.array.Day_28;
                }
            default:
                return R.array.Day_31;
        }
    }
}
