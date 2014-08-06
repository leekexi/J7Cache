package com.j7.sql;

import java.util.Map;

import com.j7.server.J7CacheServer;

public class Update {

	public static void main(String[] args) {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String update(String SQL, J7CacheServer j7) {
		String rs = "";

		int k1 = SQL.indexOf("UPDATE");
		int k2 = SQL.indexOf("SET");
		int k3 = SQL.indexOf("WHERE");
		String table_name = SQL.substring(k1 + 6, k2).trim();
		// String condi = SQL.substring(k3 + 5).trim().split("=")[0];
		String condi_values = SQL.substring(k3 + 5).trim().split("=")[1];
		String[] uv = SQL.substring(k2 + 3, k3).trim().split(",");
		while (true) {
			if (j7.lock.get(table_name) != null) {
				j7.wf = true;
				if (j7.lock.get(table_name).equals("0")) {
					j7.lock.put(table_name, "1");
					Map table = (Map) j7.database.get(table_name);
					if (table != null) {
						Map row = (Map) table.get(condi_values);
						if (row != null) {
							for (String element : uv) {
								String key = element.split("=")[0].trim();
								String value = element.split("=")[1].trim();
								row.put(key, value);
							}
							rs = "Updated successfully.";
						} else {
							rs = "Possible error conditions, invalid update.";
						}
					} else {
						rs = table_name + " does not exist.";
					}
					j7.lock.put(table_name, "0");
					break;
				} else {
					j7.logger.info(SQL + " waitting to process.");
				}
			} else {
				rs = table_name + " does not exist.";
				break;
			}

		}
		return rs;
	}

}
