package com.shine.apps.utils;

import android.util.Log;

import com.shine.apps.BuildConfig;


/**
 * 日志工具类
 *
 * @author wangyang
 */

public class L {

    private static boolean DEBUG = BuildConfig.DEBUG;

    public static void v(String tag, String msg, Object... args) {

        if (DEBUG) {
            Log.v(tag, String.format(msg, args));
        }

    }

    public static void d(String tag, String msg, Object... args) {

        if (DEBUG) {
            Log.d(tag, String.format(msg, args));
        }

    }

    public static void i(String tag, String msg) {

        if (DEBUG) {
            Log.i(tag, msg);
        }

    }

    public static void i(String tag, String type, String msg) {

        if (DEBUG) {
            Log.i(tag, type + " " + msg);
        }

    }

    public static void i(String tag, String type, String msg, Object... args) {

        if (DEBUG) {
            Log.i(tag, String.format(type + " " +msg, args));
        }

    }

    public static void w(String tag, String msg, Object... args) {

        if (DEBUG) {
            Log.w(tag, String.format(msg, args));
        }

    }

    public static void e(String tag, String msg, Object... args) {

        if (DEBUG) {
            Log.e(tag, String.format(msg, args));
        }

    }

    public static void e(String tag, String msg, Throwable ex) {
        if (DEBUG) {
            Log.e(tag, msg, ex);
        }

    }

    public static void e(String tag, Throwable ex) {
        if (DEBUG) {
            Log.e(tag, ex.getMessage(), ex);
        }
    }
}
