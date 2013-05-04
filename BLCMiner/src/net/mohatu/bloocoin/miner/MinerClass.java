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

package net.mohatu.bloocoin.miner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MinerClass implements Runnable {
	private String difficulty = "0000000";

	public MinerClass(int diff) {

		if (diff == 7) {
			this.difficulty = "0000000";
		}
		if (diff == 8) {
			this.difficulty = "00000000";
		}
		if (diff == 9) {
			this.difficulty = "000000000";
		}
		if (diff == 10) {
			this.difficulty = "0000000000";
		}
	}

	@Override
	public void run() {
		try {
			mine();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mine() throws NoSuchAlgorithmException {
		String startString = randomString();
		String currentString;
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		StringBuffer sb;
		//String testString = "dx3NAa"; //dx3NAa257363
		while (MainView.getStatus()) {
		for (int counter = 0; counter < 1000000000; counter++) {
				MainView.updateCounter();
				sb = new StringBuffer();
				currentString = startString + counter;
				md.update(currentString.getBytes());
				byte byteData[] = md.digest();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer
							.toString((byteData[i] & 0xff) + 0x100, 16)
							.substring(1));
				}
				if(!MainView.getStatus()){
					counter = 1000000000;
					System.out.println("STOPPING");
				}
				if (sb.toString().startsWith(difficulty)) {
					Thread sub = new Thread(new SubmitterClass(sb.toString(), currentString));
					sub.start();
					MainView.updateSolved(currentString);
					System.out.println("Success: " + currentString);
					try {
					    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("BLC_Solved.txt", true)));
					    out.println(currentString);
					    out.close();
					} catch (IOException e) {
					    //Error
						e.printStackTrace();
					}
				}
			}
		}

	}

	public String randomString() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random r = new Random();
		int limit = 5;
		StringBuffer buf = new StringBuffer();

		buf.append(chars.charAt(r.nextInt(26)));
		for (int i = 0; i < limit; i++) {
			buf.append(chars.charAt(r.nextInt(chars.length())));
		}
		System.out.println(buf);
		return buf.toString();
	}

	public void submit() {

	}

}
