package net.mohatu.bloocoin.miner;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;

public class Transactions implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		loadTransactions();
	}
	
	public void loadTransactions() {
		try {
			String result = new String();
			Socket socket = new Socket(Main.getURL(), Main.getPort());
			String string = "{\"cmd\":\"" + "transactions"
					+ "\",\"addr\":\"" + Main.getAddr() + "\",\"pwd\":\""
					+ Main.getKey() + "\"}";
			DataInputStream is = new DataInputStream(socket.getInputStream());
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			os.write(string.getBytes());
			os.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result += "\n"+ inputLine;
			}
			
			String[] transactions = result.split("\\{\\\"to\\\"\\: ");
			Main.clearDFM();
			for(int i=1;i<transactions.length;i++){
				transactions[i] = transactions[i].replace("\"", "");
				System.out.println(transactions[i]);
				Main.addTransaction(transactions[i]);
			}

			is.close();
			os.close();
			socket.close();
			Main.updateStatusText("Transactions updated",Color.blue);
			//System.out.println(result);

		} catch (MalformedURLException murle) {
			murle.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
