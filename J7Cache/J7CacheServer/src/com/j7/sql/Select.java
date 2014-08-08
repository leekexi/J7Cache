package com.j7.sql;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.j7.server.J7CacheServer;

public class Select {

	public static void main(String[] args) {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String select(String SQL, J7CacheServer j7) {
		String rs = "";

		int k1 = SQL.indexOf(" ");
		int k2 = SQL.indexOf("FROM");
		int k3 = SQL.indexOf("WHERE");
		String[] fields = SQL.substring(k1 + 1, k2).trim().split(",");
		String table_name = "";

		String condi = "";
		String condi_values = "";

		if (k3 > 0) {
			table_name = SQL.substring(k2 + 4, k3).trim();
			SQL = SQL.substring(k3 + 5).trim();
			condi = SQL.split("=")[0];
			condi_values = SQL.split("=")[1];
		} else {
			table_name = SQL.substring(k2 + 4).trim();
		}
		// //j7.logger.info("table_name=" + table_name);

		Map table = (Map) j7.database.get(table_name);
		if (table != null) {
			JSONObject jsonRs = new JSONObject();

			if ((condi.length() > 0) && (condi_values.length() > 0)) {// 有条件
				String[] condi_values_s = condi_values.split(",");

				JSONObject jsonRs2 = new JSONObject();

				for (String condi_values_ : condi_values_s) {
					Map row = (Map) table.get(condi_values_);
					Map row2 = new HashMap();
					if (row != null) {
						for (String field : fields) {
							row2.put(field, row.get(field));
						}
					}
					jsonRs2.put(condi_values_, row2);
				}
				jsonRs.put(table_name, jsonRs2);
			} else {// 无条件
				jsonRs.put(table_name, table);
			}

			rs = jsonRs.toString();
		} else {
			rs = table_name + " does not exist.";
		}
		return rs;
	}
}
