package com.j7.jdbc.driver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {

	String ip = "";
	int port = 7777;
	String user = "";
	String pwd = "";
	Socket socket = null;

	// jdbc:j7cache://117.25.144.148:3307:user=XXXX:password=*********
	public void setURL(String url) {
		String[] u = url.split(":");
		setIp(u[2].substring(2));
		setPort(Integer.parseInt(u[3]));
		setUser(u[4].split("=")[1]);
		setPwd(u[5].split("=")[1]);
	}

	public Connection getConnection() {
		return this;
	}

	public Statement createStatement() {
		return new Statement(this);
	}

	public Socket connect() throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		return socket;
	}

	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getIp() {
		return ip;
	}

	private void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	private void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	private void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	private void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
