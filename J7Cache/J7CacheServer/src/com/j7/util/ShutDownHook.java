package com.j7.util;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.j7.hd.FileIOHelper;
import com.j7.security.SecurityUtil;
import com.j7.server.J7CacheServer;

public class ShutDownHook implements Runnable {

	J7CacheServer j7 = null;

	public ShutDownHook(J7CacheServer j7) {
		this.j7 = j7;
	}

	@Override
	public void run() {

		j7.stop_app = true;

		System.out.println("J7Cache will soon stop, please wait, synchronous data to the hard disk.");

		Map database = (Map) this.clone(this.j7);
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream("server.properties");
			prop.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}

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

				filename = HDSYNC_DATA_DIR + "/" + entry.getKey();

				FileIOHelper file = new FileIOHelper(filename);
				Iterator<Map.Entry<?, ?>> it2 = table.entrySet().iterator();
				while (it2.hasNext()) {
					Map.Entry<?, ?> entry2 = it2.next();
					file.write_fw(SecurityUtil.encrypt(entry2.getValue().toString()) + "\r");
				}
				file.close_write();
			}
			long end = System.currentTimeMillis();

			System.out.println(DateUtil.getDateTime() + " SYNCED:" + (end - begin) + " ms");
		}

		System.out.println("J7Cache stoped");
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
