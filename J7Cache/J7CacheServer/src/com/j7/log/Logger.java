package com.j7.log;

import com.j7.util.DateUtil;

public class Logger {

	private String ClassName = "";

	public Logger(String className) {
		ClassName = className;
	}

	public void info(String msg) {
		System.out.println("[Info]" + DateUtil.getDateTime() + "- " + ClassName + " - " + msg);
	}

	public void warning(String msg) {
		System.out.println("[Warning]" + DateUtil.getDateTime() + "- " + ClassName + " - " + msg);
	}

	public void error(String msg) {
		System.out.println("[Error]" + DateUtil.getDateTime() + "- " + ClassName + " - " + msg);
	}

	public void debug(String msg) {
		System.out.println("[Debug]" + DateUtil.getDateTime() + "- " + ClassName + " - " + msg);
	}
}
