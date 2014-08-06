package com.j7.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class J7Client {

	String str = "";

	public String client_send(String host, int port, String SQL) throws UnknownHostException, IOException {
		Socket socket = new Socket(host, port);
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);
			out.println(SQL);
			str = in.readLine();
		} finally {
			socket.close();
		}
		return str;
	}

}
