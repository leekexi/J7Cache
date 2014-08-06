package com.j7.hd;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.j7.security.SecurityUtil;
import com.j7.server.J7CacheServer;
import com.j7.util.DateUtil;

public class HDDataSync extends Thread {

	J7CacheServer myj7 = null;
	String cl = "";

	public HDDataSync(J7CacheServer j7) {
		this.myj7 = j7;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {
		while (true) {
			try {
				Properties prop = new Properties();// 属性集合对象
				FileInputStream fis = new FileInputStream("server.properties");// 属性文件流
				prop.load(fis);// 将属性文件流装载到Properties对象中
				String HDSYNC_TIME = prop.getProperty("HDSYNC_TIME");
				int s_time = 0;
				if (HDSYNC_TIME.trim().length() > 1) {
					s_time = Integer.parseInt(HDSYNC_TIME);
				}
				if (s_time > 0) {
					Thread.sleep(s_time * 1000);
				}
				String HDSYNC = prop.getProperty("HDSYNC");

				if (HDSYNC.trim().equals("1")) {
					if (this.myj7.wf == true) {
						this.myj7.wf = false;

						Map database = (Map) this.clone(this.myj7);
						// database = this.myj7.database;
						String HDSYNC_DATA_DIR = prop.getProperty("HDSYNC_DATA_DIR");
						if (HDSYNC_DATA_DIR.trim().length() == 0) {
							HDSYNC_DATA_DIR = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
						}

						if (database != null) {

							Iterator<Map.Entry<?, ?>> it = database.entrySet().iterator();
							long begin = System.currentTimeMillis();
							while (it.hasNext()) {
								Map.Entry<?, ?> entry = it.next();
								Map table = (Map) entry.getValue();

								String filename = "";
								while(true)
								{
									if(this.myj7.lock.get(entry.getKey()).equals("0"))
									{
										this.myj7.lock.put(entry.getKey(), "1");
									
										filename = HDSYNC_DATA_DIR + "/" + entry.getKey();
	
										FileIOHelper file = new FileIOHelper(filename);
										Iterator<Map.Entry<?, ?>> it2 = table.entrySet().iterator();
										while (it2.hasNext()) {
											Map.Entry<?, ?> entry2 = it2.next();
											file.write_fw(SecurityUtil.encrypt(entry2.getValue().toString()) + "\r");
										}
										file.close_write();
										this.myj7.lock.put(entry.getKey(), "0");
										break;
									}
								}
								
							}
							long end = System.currentTimeMillis();

							System.out.println(DateUtil.getDateTime() + " SYNCED:" + (end - begin) + " ms");
						}
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object clone(J7CacheServer j7) {

		Map target = new HashMap();
		for (Iterator keyIt = j7.database.keySet().iterator(); keyIt.hasNext();) {
			Object key = keyIt.next();
			j7.lock.put(key, "1");
			target.put(key, j7.database.get(key));
			j7.lock.put(key, "0");
		}

		return target;
	}

}
