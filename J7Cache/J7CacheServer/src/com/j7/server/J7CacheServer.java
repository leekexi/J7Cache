package com.j7.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.j7.client.J7ClusterTransmission;
import com.j7.hd.FileIOHelper;
import com.j7.hd.HDDataSync;
import com.j7.log.Logger;
import com.j7.sql.CreateTable;
import com.j7.sql.Delete;
import com.j7.sql.DropTable;
import com.j7.sql.InsertInto;
import com.j7.sql.Select;
import com.j7.sql.Update;
import com.j7.util.MD5;

public class J7CacheServer extends Thread {

	// 集群参数
	String[] cluster = null;
	boolean is_cluster = false;
	// SQL命令行参数
	ArrayList<String> comList = new ArrayList<String>();
	// 集群命令
	@SuppressWarnings("rawtypes")
	public Map cluster_cmd = new HashMap();

	// 表级锁标记
	@SuppressWarnings("rawtypes")
	public Map lock = new HashMap();

	// 数据库
	@SuppressWarnings("rawtypes")
	public Map database = new HashMap();

	// 日志工具
	public Logger logger = new Logger(J7CacheServer.class.toString());

	// 本地通讯地址和端口
	public String ip = "";
	public int port = 7777;

	// 写标记
	public boolean wf = false;

	public boolean stop_app = false;

	public ServerSocket server = null;

	public String user = "";
	public String pwd = "";

	public String HDSYNC_DATA_DIR = "";

	@Override
	public void run() {
		try {
			start_socket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void start_socket() throws IOException {

		J7CacheServer j7 = new J7CacheServer();

		Properties prop = new Properties();// 属性集合对象
		FileInputStream fis = new FileInputStream("server.properties");// 属性文件流
		prop.load(fis);// 将属性文件流装载到Properties对象中

		j7.user = prop.getProperty("USERNAME");
		j7.pwd = prop.getProperty("PASSWORD");

		j7.logger.info("Cache restore start......");
		long begin = System.currentTimeMillis();
		j7.HDSYNC_DATA_DIR = prop.getProperty("HDSYNC_DATA_DIR");
		FileIOHelper fio = new FileIOHelper();
		String[] files = fio.getCacheFiles(j7.HDSYNC_DATA_DIR);
		if (files != null) {
			if (files.length > 0) {
				for (String file : files) {
					j7.logger.info(file + " restoreing......");
					fio.readFileByLines(j7.HDSYNC_DATA_DIR, file, j7);
					j7.logger.info(file + " restore ok.");
				}
			}
		}
		long end = System.currentTimeMillis();
		j7.logger.info("Synchronous data into memory :" + (end - begin) + " ms");
		j7.logger.info("Cache restore end......");

		j7.ip = prop.getProperty("IP");
		j7.port = Integer.parseInt(prop.getProperty("LOCALPORT"));
		String CLUSTER = prop.getProperty("CLUSTER");
		if (CLUSTER.length() > 0) {
			j7.cluster = CLUSTER.split(",");
			j7.is_cluster = true;
			ScanClusterConfig scc = new ScanClusterConfig(j7);
			scc.start();
			j7.logger.info("Cluster started。");
		}

		HDDataSync hd = new HDDataSync(j7);
		hd.start();

		System.gc();

		j7.logger.info("J7Cache Server [" + new Date() + "] Start in " + j7.port);

		j7.server = new ServerSocket(j7.port);

		while (true) {
			Socket socket = j7.server.accept();
			invoke(socket, j7);
		}

	}

	public boolean validate_user(String sql) {

		if (sql.indexOf("USER_PWD") == 0) {
			int k = sql.indexOf(";");
			String[] u = sql.substring(8, k).split("=");
			if (u[0].trim().equals(user) && MD5.GetMD5Code(u[1]).equals(pwd)) {
				return true;
			} else {
				return false;
			}

		} else {
			return true;
		}
	}

	private static void invoke(final Socket client, final J7CacheServer j7) throws IOException {

		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {

				if (j7.stop_app) {
					return;
				} else {
					BufferedReader in = null;
					PrintWriter out = null;
					String rs = "";
					try {
						in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						out = new PrintWriter(client.getOutputStream());
						boolean cluster_already = false;
						while (true) {
							String SQL = "";
							try {
								SQL = in.readLine().trim();
								// SQL = Encode.decode(SQL);
							} catch (Exception ee) {
								break;
							}
							if (!j7.validate_user(SQL)) {
								rs = "User or password error.";
							} else {
								if (SQL.length() > 0) {

									int k = SQL.indexOf(";");
									SQL = SQL.substring(k + 1);// 过滤调用户和密码

									if (SQL.indexOf("CLUSTER_FLAG") >= 0) {

										int cl = SQL.lastIndexOf(":");
										String tsp = SQL.substring(cl + 1);
										if (j7.cluster_cmd.get(tsp) == null) {
											if (j7.cluster != null) {

												j7.cluster_cmd.put(tsp, SQL);
												for (String element : j7.cluster) {
													if (SQL.indexOf(element) == -1) {
														j7.logger.info("Synchronoused " + SQL);
														String host = element.split(":")[0];
														int port = Integer.parseInt(element.split(":")[1]);
														J7ClusterTransmission j7c = new J7ClusterTransmission(host,
																port, SQL);
														j7c.start();
													}
												}

											}
											k = SQL.indexOf("CLUSTER_FLAG");
											SQL = SQL.substring(0, k);
										} else {
											cluster_already = true;
										}

									} else {

										if (SQL.indexOf("SELECT") != 0) {
											if (j7.cluster != null) {
												String tsp = timeSystem() + "";
												String ipport = "";
												for (String element : j7.cluster) {
													ipport += element + ",";
												}
												String tempSql = SQL + "CLUSTER_FLAG" + ":" + ipport + ":" + tsp;
												j7.cluster_cmd.put(tsp, SQL);
												for (String element : j7.cluster) {
													if (!element.equals(j7.ip + ":" + j7.port)) {
														j7.logger.info("Synchronous from " + element + " " + SQL);
														String host = element.split(":")[0];
														int port = Integer.parseInt(element.split(":")[1]);
														J7ClusterTransmission j7c = new J7ClusterTransmission(host,
																port, tempSql);
														j7c.start();
													}
												}
											}
										}

									}

									if (!cluster_already) {
										j7.comList.add(SQL);
										rs = executeSQL(SQL, j7);
									}
								}

							}

							while (rs.length() > 2048) {
								out.println(rs.substring(0, 2048));
								out.flush();
								rs = rs.substring(2048);
							}
							rs = rs + "[EOF]";
							out.println(rs);
							out.flush();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						try {
							in.close();
						} catch (Exception e) {

						}
						try {
							out.close();
						} catch (Exception e) {
						}
						try {
							client.close();
						} catch (Exception e) {
						}
					}

				}
			}
		}).start();
	}

	public synchronized static long timeSystem() {// use 188

		long currentTime = System.currentTimeMillis();
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return currentTime;

	}

	private static String executeSQL(String SQL, final J7CacheServer j7) throws Exception {

		SQL = SQL.trim();

		String rs = "";
		if (SQL.indexOf("CREATE TABLE") == 0)// CREATE TABLE
		{
			CreateTable cc = new CreateTable();
			rs = cc.createTable(SQL, j7);

		} else if (SQL.indexOf("INSERT INTO") == 0) {
			InsertInto ii = new InsertInto();
			rs = ii.insertInto(SQL, j7);

		} else if (SQL.indexOf("SELECT") == 0) {
			Select sl = new Select();
			rs = sl.select(SQL, j7);

		} else if (SQL.indexOf("UPDATE") == 0) {

			Update up = new Update();
			rs = up.update(SQL, j7);

		} else if (SQL.indexOf("DELETE") == 0) {

			Delete dl = new Delete();
			rs = dl.delete(SQL, j7);

		} else if (SQL.indexOf("DROP TABLE") == 0) {

			DropTable dt = new DropTable();
			rs = dt.dropTable(SQL, j7);

		}
		return rs;
	}
}
