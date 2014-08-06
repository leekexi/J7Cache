package com.j7.sql;

import com.j7.hd.FileIOHelper;
import com.j7.server.J7CacheServer;

public class DropTable {

	public static void main(String[] args) {

	}

	public String dropTable(String SQL, J7CacheServer j7) {
		String rs = "";
		int k1 = SQL.indexOf("DROP TABLE");
		String table_name = SQL.substring(k1 + 11).trim();
		if (j7.database.get(table_name) != null) {
			j7.database.remove(table_name);
			rs = table_name + " droped.";
			FileIOHelper ff = new FileIOHelper();
			boolean rr = ff.deleteFile(j7.HDSYNC_DATA_DIR + "/" + table_name);
			if (rr) {
				j7.logger.info(table_name + " deleted.");
			}
		} else {
			rs = table_name + " does not exist.";
		}
		j7.lock.remove(table_name);
		return rs;
	}

}
