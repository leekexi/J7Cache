package com.j7.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.j7.log.Logger;

public class J7ClusterTransmission extends Thread {

	Logger logger = new Logger(J7ClusterTransmission.class.toString());

	String host;
	int port;
	String SQL;

	public J7ClusterTransmission(String host, int port, String SQL) {
		this.host = host;
		this.port = port;
		this.SQL = SQL;
	}

	@Override
	public void run() {
		client_send(this.host, this.port, this.SQL);
	}

	public void client_send(String host, int port, String SQL) {

		Socket socket = null;
		try {
			socket = new Socket(host, port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);

			out.println(SQL);
			in.readLine();

		} catch (Exception ex) {
			logger.error("Server " + host + ":" + port + " out of service or network failure");
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
			}
		}

	}
}
