package com.j7.server;

import java.io.FileInputStream;
import java.util.Properties;

public class ScanClusterConfig extends Thread {

	J7CacheServer myj7 = null;
	String cl = "";

	public ScanClusterConfig(J7CacheServer j7) {
		this.myj7 = j7;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Properties prop = new Properties();// 属性集合对象
				FileInputStream fis = new FileInputStream("server.properties");// 属性文件流
				prop.load(fis);// 将属性文件流装载到Properties对象中
				String CLUSTER = prop.getProperty("CLUSTER");
				if (!this.cl.equals(CLUSTER)) {
					this.myj7.logger.info("Cluster service has been modified, the new configuration:" + CLUSTER);
					if (CLUSTER.length() > 0) {
						this.myj7.cluster = CLUSTER.split(",");
						this.myj7.is_cluster = true;
					}
					this.cl = CLUSTER;
				}

				Thread.sleep(2000000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

}
