package com.j7.jdbc.driver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Encode {

	/**
	 * UTF编码
	 * 
	 * @param strInput
	 * @return
	 */
	public static String encode(String strInput) {
		StringBuffer output = new StringBuffer();
		// System.out.println("\""+strInput+ "\" 的utf8编码：");
		for (int i = 0; i < strInput.length(); i++) {
			// System.out.println(strInput.charAt(i));
			String r = strInput.charAt(i) + "";
			Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
			Matcher m = p_str.matcher(r);
			if (m.find() && m.group(0).equals(r)) {
				output.append("\\u" + Integer.toString(strInput.charAt(i), 16));
			} else {
				output.append(r);
			}
		}
		System.out.println(output);
		return output.toString();
	}

	public static String decode(String s) {
		byte[] baKeyword = new byte[s.trim().length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			s = new String(baKeyword, "UTF-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
}
