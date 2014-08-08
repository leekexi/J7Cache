package com.j7.jdbc.driver;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "serial", "rawtypes" })
public class ResultSet extends HashMap {

	int rowid = -1;

	@SuppressWarnings({ "unused", "unchecked" })
	private void setTable(String table) {
		this.put("table", table);
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(Map map) {
		rowid += 1;
		this.put(rowid, map);
	}

	public String getString(String field) {
		Map m = (Map) this.get(rowid);
		return (String) m.get(field);
	}

	public int getInt(String field) {
		Map m = (Map) this.get(rowid);
		return Integer.parseInt(m.get(field) + "");
	}

	public double getDouble(String field) {
		Map m = (Map) this.get(rowid);
		return Double.parseDouble(m.get(field) + "");

	}

	public float getFloat(String field) {
		Map m = (Map) this.get(rowid);
		return Float.parseFloat(m.get(field) + "");
	}

	public Map next() {
		rowid += 1;
		return (Map) this.get(rowid);
	}

	public Map previous() {
		if (rowid > 0) {
			rowid -= 1;
			return (Map) this.get(rowid);
		} else {
			return null;
		}
	}

	public void close() {
		for (int k = rowid; k >= 0; k--) {
			this.remove(k);
		}
	}

}
