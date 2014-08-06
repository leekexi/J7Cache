package com.j7.security;

public class SecurityUtil {

	static String[] ekey = { "#", "&", "2", "@", "W", "!", "~", "*", ")", "_", "V", "Y", "^", "+", "%", "(" };
	private static String hexString = "0123456789ABCDEF";

	public static void main(String[] args) {

		String src = "S=2002:MAC=00-F1-F3-16-96-C: LOGIN=1323823787428:DIP=192.168.0.1:DPORT=2305:SIP=192.172.23.300:SPORT=5930:KEY=技术测试\n\n\n";

		String ek = encrypt(src);
		System.out.println(ek);

		String endsrc = decrypt(ek);
		System.out.println(endsrc);
		System.out.println(src);
	}

	/**
	 * decrypt
	 * 
	 * @param src
	 * @return
	 */
	public static String decrypt(String src) {
		String ecrt = "";
		for (int i = 0; i < src.length(); i++) {
			char a = src.charAt(i);
			if (a == '#') {
				ecrt += "0";
			} else if (a == '&') {
				ecrt += "1";
			} else if (a == '2') {
				ecrt += "2";
			} else if (a == '@') {
				ecrt += "3";
			} else if (a == 'W') {
				ecrt += "4";
			} else if (a == '!') {
				ecrt += "5";
			} else if (a == '~') {
				ecrt += "6";
			} else if (a == '*') {
				ecrt += "7";
			} else if (a == ')') {
				ecrt += "8";
			} else if (a == '_') {
				ecrt += "9";
			} else if (a == 'V') {
				ecrt += "A";
			} else if (a == 'Y') {
				ecrt += "B";
			} else if (a == '^') {
				ecrt += "C";
			} else if (a == '+') {
				ecrt += "D";
			} else if (a == '%') {
				ecrt += "E";
			} else if (a == '(') {
				ecrt += "F";
			}
		}
		String dcode = decode(ecrt);
		return dcode;
	}

	/**
	 * encrypt
	 * 
	 * @param src
	 * @return
	 */
	public static String encrypt(String src) {
		String ecode = encode(src);// 第一个条件 先全部转成16进制
		String ecrt = "";
		for (int i = 0; i < ecode.length(); i++) {
			char a = ecode.charAt(i);
			if (a == '0') {
				ecrt += ekey[0];
			} else if (a == '1') {
				ecrt += ekey[1];
			} else if (a == '2') {
				ecrt += ekey[2];
			} else if (a == '3') {
				ecrt += ekey[3];
			} else if (a == '4') {
				ecrt += ekey[4];
			} else if (a == '5') {
				ecrt += ekey[5];
			} else if (a == '6') {
				ecrt += ekey[6];
			} else if (a == '7') {
				ecrt += ekey[7];
			} else if (a == '8') {
				ecrt += ekey[8];
			} else if (a == '9') {
				ecrt += ekey[9];
			} else if (a == 'A') {
				ecrt += ekey[10];
			} else if (a == 'B') {
				ecrt += ekey[11];
			} else if (a == 'C') {
				ecrt += ekey[12];
			} else if (a == 'D') {
				ecrt += ekey[13];
			} else if (a == 'E') {
				ecrt += ekey[14];
			} else if (a == 'F') {
				ecrt += ekey[15];
			}
		}
		return ecrt;
	}

	/**
	 * 转化字符串为十六进制编码
	 * 
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		byte[] bytes;
		try {
			bytes = str.getBytes("gb2312");
		} catch (Exception e) {
			bytes = str.getBytes();
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(hexString.charAt((b & 0xF0) >> 4));
			sb.append(hexString.charAt((b & 0x0F) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 转化十六进制编码为字符串
	 */
	public static String decode(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			s = new String(baKeyword, "GB2312");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	public boolean validate_user(String user, String pwd) {

		return false;
	}

}
