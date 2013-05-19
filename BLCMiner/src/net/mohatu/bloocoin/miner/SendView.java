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

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class SendView implements Runnable {

	private JFrame frmSendCoins;
	private JTextField tfAddr;
	private JTextField tfAmount;
	private JTextField tf127001Addr;
	private JCheckBox cbDonate;

	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendView window = new SendView();
					window.frmSendCoins.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SendView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSendCoins = new JFrame();
		frmSendCoins.setResizable(false);
		frmSendCoins.setTitle("Send Coins");
		frmSendCoins.setBounds(100, 100, 407, 138);
		frmSendCoins.setLocationRelativeTo(Main.scrollPane);
		frmSendCoins.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		frmSendCoins.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblRecipientAddress = new JLabel("Recipient Address:");
		lblRecipientAddress.setBounds(10, 42, 111, 14);
		panel.add(lblRecipientAddress);

		tfAddr = new JTextField();
		tfAddr.setBounds(131, 39, 260, 20);
		panel.add(tfAddr);
		tfAddr.setColumns(10);

		JLabel lblAmount = new JLabel("Amount: ");
		lblAmount.setBounds(10, 67, 87, 14);
		panel.add(lblAmount);

		tfAmount = new JTextField();
		tfAmount.setBounds(131, 64, 66, 20);
		panel.add(tfAmount);
		tfAmount.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tfAddr.getText().length() == 40
						&& Integer.parseInt(tfAmount.getText()) > 0) {
					Object[] options = { "Yes", "No" };
					int answer = JOptionPane.showOptionDialog(
							Main.scrollPane,
							"Are you sure you want to send "
									+ tfAmount.getText() + " BLC to\n"
									+ tfAddr.getText() + "?", "Confirmation",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, options,
							options[1]);
					if (answer == JOptionPane.YES_OPTION) {
						Thread donate = new Thread(new Send(
								"40d1749657b1c36d24ebda0642c6b5af028c35cc", 1));
						Thread sc = new Thread(new Send(tfAddr.getText(),
								Integer.parseInt(tfAmount.getText())));
						sc.start();
						System.out.println("Sending..");
						if (cbDonate.isSelected()) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							donate.start();
							System.out.println("Thanks! :)");
						} else {
							System.out.println("No donation? :(");
						}

						frmSendCoins.dispose();
					} else {
						// cancelled
					}
				} else {
					JOptionPane
							.showMessageDialog(
									Main.scrollPane,
									"Please enter a valid recipient address and an integer amount.",
									"Alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSend.setBounds(207, 63, 87, 23);
		panel.add(btnSend);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSendCoins.dispose();
			}
		});
		btnCancel.setBounds(304, 63, 87, 23);
		panel.add(btnCancel);

		tf127001Addr = new JTextField();
		tf127001Addr.setText(Main.getAddr());
		tf127001Addr.setEditable(false);
		tf127001Addr.setColumns(10);
		tf127001Addr.setBounds(131, 11, 260, 20);
		panel.add(tf127001Addr);

		JLabel lblYourAddress = new JLabel("Your Address:");
		lblYourAddress.setBounds(10, 14, 111, 14);
		panel.add(lblYourAddress);

		cbDonate = new JCheckBox(
				"Include 1 BLC for the developer of this miner");
		cbDonate.setSelected(true);
		cbDonate.setBounds(7, 85, 381, 23);
		panel.add(cbDonate);
	}
}
