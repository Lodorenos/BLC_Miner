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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;

public class CoinClass implements Runnable {

	String url = MainView.getURL();
	int port = MainView.getPort();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		getCoins();
	}

	public void getCoins() {
		try {
			String result = new String();
			Socket sock = new Socket(this.url, this.port);
			String string = "{\"cmd\":\"my_coins\",\"addr\":\""
					+ MainView.getAddr() + "\",\"pwd\":\"" + MainView.getKey()
					+ "\"}";
			DataInputStream is = new DataInputStream(sock.getInputStream());
			DataOutputStream os = new DataOutputStream(sock.getOutputStream());
			os.write(string.getBytes());
			os.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result += inputLine;
			}

			is.close();
			os.close();
			sock.close();
			System.out.println(result);
			String coins = result.split("t\": ")[1];
			coins = coins.split("}")[0];
			System.out.println(coins);
			MainView.updateBLC(Integer.parseInt(coins));

		} catch (MalformedURLException murle) {
			murle.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
