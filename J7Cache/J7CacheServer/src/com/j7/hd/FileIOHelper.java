package com.j7.hd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.j7.security.SecurityUtil;
import com.j7.server.J7CacheServer;

public class FileIOHelper {

	FileOutputStream out = null;

	FileWriter fw = null;

	public FileIOHelper(String file) {
		try {

			out = new FileOutputStream(new File(file));
			fw = new FileWriter(file);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FileIOHelper() {

	}

	public void write(String context) {
		try {
			out.write(context.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write_fw(String context) {
		try {
			fw.write(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close_write() {
		try {
			out.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void readFileByLines(String path, String fileName, J7CacheServer j7) {
		File file = new File(path + "/" + fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			Map table = new HashMap();
			while ((tempString = reader.readLine()) != null) {
				Map row = new HashMap();

				tempString = SecurityUtil.decrypt(tempString);

				String[] tempStrings = tempString.substring(1, tempString.length() - 1).split(",");
				for (String tempString2 : tempStrings) {
					row.put(tempString2.split("=")[0].trim(), tempString2.split("=")[1].trim());
				}
				table.put(tempStrings[0].split("=")[1].trim(), row);

			}
			j7.database.put(fileName, table);
			j7.lock.put(fileName, "0");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public String[] getCacheFiles(String path) {
		if (path.trim().length() == 0) {
			path = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
		}
		File rootDir = new File(path);
		String[] fileList = rootDir.list();
		return fileList;
	}

	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

}
