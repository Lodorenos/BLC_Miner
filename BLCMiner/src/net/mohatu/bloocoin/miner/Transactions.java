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
import java.net.MalformedURLException;
import java.net.Socket;

public class Transactions implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		loadTransactions();
	}

	public void loadTransactions() {
		try {
			String result = new String();
			Socket socket = new Socket(Main.getURL(), Main.getPort());
			String string = "{\"cmd\":\"" + "transactions" + "\",\"addr\":\""
					+ Main.getAddr() + "\",\"pwd\":\"" + Main.getKey() + "\"}";
			DataInputStream is = new DataInputStream(socket.getInputStream());
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			os.write(string.getBytes());
			os.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result += "\n" + inputLine;
			}

			String[] transactions = result.split("\\{\\\"to\\\"\\: ");
			Main.clearDFM();
			for (int i = 1; i < transactions.length; i++) {
				transactions[i] = transactions[i].replace("\"", "");
				System.out.println(transactions[i]);
				Main.addTransaction(transactions[i]);
			}

			is.close();
			os.close();
			socket.close();
			Main.updateStatusText("Transactions updated", Color.blue);
			// System.out.println(result);

		} catch (MalformedURLException murle) {
			murle.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
