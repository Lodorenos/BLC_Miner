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
 * @author Mohatu
 * @version 2.9
 */

package net.mohatu.bloocoin.miner;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Submitter implements Runnable {

	String hash = "";
	String solution = "";
	String url = Main.getURL();
	int port = Main.getPort();
	String addr = "";
	String key = "";
	boolean submitted = false;

	public Submitter(String hash, String solution) {
		this.hash = hash;
		this.solution = solution;
	}

	@Override
	public void run() {

		while (!submitted) {
			boolean sawException = false;
			try {
				Main.updateStatusText("Submitting " + solution, Color.blue);
				submit();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				sawException = true;
			}
			if (sawException) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void submit() {
		try {
			String result = new String();
			Socket sock = new Socket(this.url, this.port);
			String command = "{\"cmd\":\"check" + "\",\"winning_string\":\""
					+ solution + "\",\"winning_hash\":\"" + hash
					+ "\",\"addr\":\"" + Main.getAddr() + "\"}";
			DataInputStream is = new DataInputStream(sock.getInputStream());
			DataOutputStream os = new DataOutputStream(sock.getOutputStream());
			os.write(command.getBytes());
			os.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result += inputLine;
			}

			is.close();
			os.close();
			sock.close();
			if (result.contains("\"success\": true")) {
				System.out.println("Result: Submitted");
				submitted = true;
				Main.updateStatusText(solution + " submitted", Color.blue);
			} else if (result.contains("\"success\": false")) {
				System.out.println("Result: Failed");
				submitted = true;
				Main.updateStatusText("Submission of " + solution
						+ " failed, already exists!", Color.red);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Main.updateStatusText("Submission of " + solution
					+ " failed, connection failed!", Color.red);
		} catch (IOException e) {
			e.printStackTrace();
			Main.updateStatusText("Submission of " + solution
					+ " failed, connection failed!", Color.red);
		}
		Thread gc = new Thread(new Coins());
		gc.start();
	}
}
