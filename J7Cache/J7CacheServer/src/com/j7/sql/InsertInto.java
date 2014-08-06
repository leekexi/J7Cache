package com.j7.sql;

import java.util.HashMap;
import java.util.Map;

import com.j7.server.J7CacheServer;

public class InsertInto {

	public static void main(String[] args) {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String insertInto(String SQL, J7CacheServer j7) {
		String rs = "";

		int k1 = SQL.indexOf("(");
		int k2 = SQL.indexOf(")");
		String table_name = SQL.substring(SQL.indexOf("INSERT INTO") + 12, k1).trim();
		// //j7.logger.info("table_name=" + table_name);
		while (true) {

			if (j7.lock.get(table_name) != null) {
				j7.wf = true;
				if (j7.lock.get(table_name).equals("0")) {
					j7.lock.put(table_name, "1");
					String[] fields = SQL.substring(k1 + 1, k2).split(",");
					SQL = SQL.substring(k2 + 1, SQL.length());
					k1 = SQL.indexOf("(");
					k2 = SQL.indexOf(")");

					String[] values = SQL.substring(k1 + 1, k2).split(",");
					if (fields.length != values.length) {
						rs = "Syntax error, key-value pair does not match.";
					} else {
						Map table = (Map) j7.database.get(table_name);
						if (table != null) {

							Map row = (Map) table.get(values[0]);
							if (row == null) {
								row = new HashMap();
								for (int i = 0; i < fields.length; i++) {
									row.put(fields[i], values[i]);
								}
								table.put(values[0], row);
								rs = "Data inserted successfully.";
							} else {
								rs = "Primary key violation.";
							}

						} else {
							rs = table_name + " does not exist.";
						}
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
