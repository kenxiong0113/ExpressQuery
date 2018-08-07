package com.ken.base.utils;

import android.util.Log;

public class LogUtil {
	private static final int VERBOSE = 1;
	private static final int DEBUG = 2;
	private static final int INFO = 3;
	private static final int WARN = 4;
	private static final int ERROR = 5;
	private static final int NOTHING = 6;
	private static int leave = VERBOSE;

	public static void v(String tag, String msg) {
		if (leave <= VERBOSE) {
			Log.v(tag, msg);
		}

	}

	public static void d(String tag, String msg) {
		if (leave <= DEBUG) {
			Log.d(tag, msg);
		}

	}

	public static void i(String tag, String msg) {
		if (leave <= INFO) {
			Log.i(tag, msg);
		}

	}

	public static void w(String tag, String msg) {
		if (leave <= WARN) {
			Log.w(tag, msg);
		}

	}

	public static void e(String tag, String msg) {
		if (leave <= ERROR) {
			Log.e(tag, msg);
		}

	}
}
