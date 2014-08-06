package com.j7.sql;

import java.util.HashMap;
import java.util.Map;

import com.j7.server.J7CacheServer;

public class CreateTable {


	public static void main(String[] args) {
		

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String createTable(String SQL,J7CacheServer j7)
	{
		String rs = "";
		
		j7.wf = true;
		String table_name = SQL.substring(SQL.indexOf("CREATE TABLE") + 13);
		// //j7.logger.info("table_name=" + table_name);
		try {

			Map table = (Map) j7.database.get(table_name);
			if (table != null) {
				rs = table_name + " already exists.";
			} else {
				table = new HashMap();
				j7.database.put(table_name, table);
				rs = table_name + " creating successful";
				j7.lock.put(table_name, "0");
			}

		} catch (Exception ex) {

		}
		return rs;
	}

}
