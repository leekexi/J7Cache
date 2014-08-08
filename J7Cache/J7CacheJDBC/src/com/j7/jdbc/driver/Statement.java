package com.j7.jdbc.driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

@SuppressWarnings({ "rawtypes", "serial" })
public class Statement extends HashMap {

	Connection conn = null;
	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;

	public Statement(Connection conn) {
		this.conn = conn;
		try {
			socket = this.conn.connect();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String executeUpdate(String sql) {

		String rs = "";
		if (conn != null) {
			try {
				sql = Encode.encode(sql);
				out.println(sql);
				rs = in.readLine();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	@SuppressWarnings({ "unchecked" })
	public ResultSet executeQuery(String sql) {

		ResultSet rs = null;
		if (conn != null) {
			try {
				long startTime = System.currentTimeMillis();
				out.println(sql);
				String drs = "";
				String kdrs = in.readLine();
				int ke = kdrs.indexOf("[EOF]");
				while (ke == -1) {
					drs = drs + kdrs;
					kdrs = in.readLine();
					ke = kdrs.indexOf("[EOF]");
				}

				drs += kdrs.substring(0, ke);
				drs = drs.replaceAll("\\\\u", "\\u");
				long endTime = System.currentTimeMillis();
				System.out.println(sql + "\r\nThe client receives the data of the time： " + (endTime - startTime)
						+ "ms");
				if (drs.indexOf("{") == 0) {
					rs = new ResultSet();

					JSONObject jsonObject = JSONObject.fromObject(drs);

					Iterator it = jsonObject.keys();
					while (it.hasNext()) {
						String key = (String) it.next();
						JSONObject jsonObject2 = (JSONObject) jsonObject.get(key);
						Iterator it2 = jsonObject2.keys();
						while (it2.hasNext()) {
							String key2 = (String) it2.next();
							JSONObject jsonObject3 = (JSONObject) jsonObject2.get(key2);
							Iterator it3 = jsonObject3.keys();
							Map map = new HashMap();
							while (it3.hasNext()) {
								String key3 = (String) it3.next();
								map.put(key3, jsonObject3.get(key3));
							}
							rs.addRow(map);
						}
					}
					rs.rowid = -1;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

}
