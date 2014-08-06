package com.j7.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	/**
	 * 如将时间200911120000转换为2009-11-12 00:00:00
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String changeFormat(String date) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		Date dDate = format.parse(date);
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String reTime = format2.format(dDate);
		return reTime;
	}

	/**
	 * 获得系统当前时间:2009-11-12 00:00:00
	 * 
	 * @return
	 */
	public static String getDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		return date;
	}
	
	public static String getDateTime_k() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		return date;
	}

	public static int addSomeHours(String times) throws ParseException {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date a1 = format.parse(times);
		Date a2 = format.parse(getDateTime());

		long l = a2.getTime() - a1.getTime();

		int h = (int) (l / 1000.0 / 60 / 60);

		return h;
	}

	public static int addSomeSeconds(String times) throws ParseException {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date a1 = format.parse(times);
		Date a2 = format.parse(getDateTime());

		long l = a2.getTime() - a1.getTime();

		int s = (int) (l / 1000.0);

		return s;
	}

	public static void main(String[] args) {
		int hours;
		try {
			hours = DateUtil.addSomeHours("2014-1-24 11:45:23");
			System.out.println(hours);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
