package com.j7.server;

import com.j7.util.ShutDownHook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import com.j7.client.J7Client;
import com.j7.util.MD5;

public class J7CacheStart {

	public static void main(String[] args) {
		J7CacheServer j7 = new J7CacheServer();

		j7.start();
		
		Runtime rt = Runtime.getRuntime();
	    rt.addShutdownHook(new Thread(new ShutDownHook(j7)));//register to the jvm

		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (true) {
				String st = buf.readLine();
				String temp = st;
				st = st.toUpperCase();

				if (st.equals("BYE!")) {
					System.exit(0);
					break;
				} else if (st.indexOf("SET USER") >= 0) {
					int k = st.indexOf("SET USER");
					st = st.substring(k + 9);
					String[] u = st.trim().split("=");

					Properties prop = new Properties();// 属性集合对象
					FileInputStream fis = new FileInputStream("server.properties");// 属性文件流
					prop.load(fis);// 将属性文件流装载到Properties对象中
					prop.setProperty("USERNAME", u[0].trim());
					prop.setProperty("PASSWORD", MD5.GetMD5Code(u[1].trim()));

					OutputStream outputStream = new FileOutputStream("server.properties");
					prop.store(outputStream, "author: 7haowei@sina.com");
					outputStream.close();
					fis.close();
					System.out.println("set data base users successful");
				} else if ((st.indexOf("SELECT") >= 0) || (st.indexOf("INSERT INTO") >= 0)
						|| (st.indexOf("UPDATE") >= 0) || (st.indexOf("DELETE FROM") >= 0)
						|| (st.indexOf("CREATE TABLE") >= 0) || (st.indexOf("DROP TABLE") >= 0)) {
					J7Client j7c = new J7Client();
					Properties prop = new Properties();// 属性集合对象
					FileInputStream fis = new FileInputStream("server.properties");// 属性文件流
					prop.load(fis);// 将属性文件流装载到Properties对象中
					String host = prop.getProperty("IP");
					int port = Integer.parseInt(prop.getProperty("LOCALPORT"));
					String rs = j7c.client_send(host, port, temp);
					System.out.println(rs);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
