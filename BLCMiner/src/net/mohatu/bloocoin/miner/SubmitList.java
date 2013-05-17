/*
 * Copyright (C) 2013 Mohatu.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/* 
 * SubmitterClass.java
 * Submits solution to the server
 */

package net.mohatu.bloocoin.miner;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

public class SubmitList implements Runnable {
	String hash = "";
	String solution = "";
	String url = Main.getURL();
	int port = Main.getPort();
	ArrayList<String> solved = new ArrayList<String>();

	@Override
	public void run() {
		InputStream is;
		BufferedReader br;
		String line;
		try {
			is = new FileInputStream(
					System.getProperty("user.home")
					+ "/.bloocoin/solved.dat");
			br = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				solved.add(line);
			}
			is.close();
			br.close();
			System.out.println("Array Built: " + solved.size());
			submit();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		br = null;
		is = null;
	}

	private void submit() {
		for (int i = 0; i < solved.size(); i++) {
			try {
				Socket sock = new Socket(this.url, this.port);
				String result = new String();
				DataInputStream is = new DataInputStream(sock.getInputStream());
				DataOutputStream os = null;
				BufferedReader in = new BufferedReader(
						new InputStreamReader(is));
				solution = solved.get(i);
				hash = DigestUtils.sha512Hex(solution);
				
				String command = "{\"cmd\":\"check"
						+ "\",\"winning_string\":\"" + solution
						+ "\",\"winning_hash\":\"" + hash + "\",\"addr\":\""
						+ Main.getAddr() + "\"}";
				os = new DataOutputStream(sock.getOutputStream());
				os.write(command.getBytes());

				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}

				if (result.contains("\"success\": true")) {
					System.out.println("Result: Submitted");
					Main.updateStatusText(solution + " submitted",
							Color.blue);
					Thread gc = new Thread(new Coins());
					gc.start();
				} else if (result.contains("\"success\": false")) {
					System.out.println("Result: Failed");
					Main.updateStatusText("Submission of " + solution
							+ " failed, already exists!", Color.red);
				}
				is.close();
				os.close();
				os.flush();
				sock.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				Main.updateStatusText("Submission of " + solution
						+ " failed, connection failed!", Color.red);
			} catch (IOException e) {
				e.printStackTrace();
				Main.updateStatusText("Submission of " + solution
						+ " failed, connection failed!", Color.red);
			}
		}
		Thread gc = new Thread(new Coins());
		gc.start();
	}
}
