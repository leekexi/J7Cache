package com.j7.test;

import com.j7.jdbc.driver.Connection;
import com.j7.jdbc.driver.ResultSet;
import com.j7.jdbc.driver.Statement;

public class TestJDBC {

	public static void main(String[] args) {
		Connection conn = new Connection();
		String url = "jdbc:j7cache://127.0.0.1:5555:user=root:password=123456";
		conn.setURL(url);

		Statement stmt = conn.createStatement();

		long startTime = System.currentTimeMillis(); // 获取开始时间

		// String sql = "CREATE TABLE test?";
		// for (int i = 0; i < 1; i++) {
		// String sql_temp = sql.replace("?", i + "");
		// System.out.println(sql_temp);
		// String rs = stmt.executeUpdate(sql_temp);
		// System.out.println(rs);
		// }
		//
		// stmt.close();
		// conn.close();

		// String sql = "INSERT INTO TEST(A,B,C) VALUES(?,技术测试,看看再说)";
		// for (int i = 5000; i < 6000; i++) {
		// String sql_temp = sql.replace("?", i + "");
		// String rs = stmt.executeUpdate(sql_temp);
		// // System.out.println(rs);
		// }
		// stmt.close();
		// conn.close();

		String sql = "SELECT A,B,C FROM TEST";
		ResultSet rsset = stmt.executeQuery(sql);

		while (rsset.next() != null) {
			System.out.print(rsset.getString("A") + " | ");
			System.out.print(rsset.getString("B") + " | ");
			System.out.println(rsset.getString("C"));
		}

		System.out.println(rsset.size());
		rsset.close();
		stmt.close();
		conn.close();

		long endTime = System.currentTimeMillis(); // 获取结束时间

		System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

	}
}
