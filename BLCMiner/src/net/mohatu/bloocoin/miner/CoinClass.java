package net.mohatu.bloocoin.miner;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;

public class CoinClass implements Runnable{
	
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
			String string = "{\"cmd\":\"my_coins\",\"addr\":\"" + MainView.getAddr() + "\",\"pwd\":\""
					+ MainView.getKey() + "\"}";
			DataInputStream is = new DataInputStream(sock.getInputStream());
			DataOutputStream os = new DataOutputStream(sock.getOutputStream());
			os.write(string.getBytes());
			os.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
				result += inputLine;
			}

			is.close();
			os.close();
			sock.close();
			String coins = result.split("t\": ")[1];
			coins = coins.split("}")[0];
			MainView.updateBLC(Integer.parseInt(coins));
			System.out.println(Integer.parseInt(coins));

		} catch (MalformedURLException murle) {
			murle.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
