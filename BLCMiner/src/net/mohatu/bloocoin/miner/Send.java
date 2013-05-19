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

public class Send implements Runnable{
	
	String url = Main.getURL();
	int port = Main.getPort();
	String destAddr = "";
	int amount = 0;
	
	public Send(String dest, int amount){
		this.destAddr = dest;
		this.amount = amount;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		getCoins();
	}
	
	public void getCoins() {
		try {
			String result = new String();
			Socket sock = new Socket(this.url, this.port);
			String command = "{\"cmd\":\"send_coin"
					+ "\",\"to\":\"" +destAddr + "\",\"addr\":\""
					+ Main.getAddr() + "\",\"pwd\":\"" + Main.getKey() + "\",\"amount\":" + amount + "}";
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
			System.out.println(result);
			if(result.contains("true")){
				Main.updateStatusText(amount + " BLC sent to " + destAddr, Color.blue);
				Thread cs = new Thread(new Coins());
				Thread tc = new Thread(new Transactions());
				cs.start();
				tc.start();
			}else if(result.contains("false")){
				Main.updateStatusText("Transaction failed!", Color.red);
			}

		} catch (MalformedURLException murle) {
			murle.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
