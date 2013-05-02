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

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class MainView {
	private static boolean mining = true;
	private static int counter = 0;
	private JFrame frmBlcMiner;
	private static JLabel lblTriedAmount;
	private static JLabel lblSolvedAmount;
	private static JLabel lblKHsAmount;
	private static JTable table;
	public static DefaultTableModel solved = new DefaultTableModel(
			new Object[] { "Solved" }, 0);


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frmBlcMiner.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlcMiner = new JFrame();
		frmBlcMiner.setTitle("BLC Miner");
		frmBlcMiner.setBounds(100, 100, 450, 191);
		frmBlcMiner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frmBlcMiner.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton btnStartMining = new JButton("Start Mining");
		btnStartMining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread miner = new Thread(new MinerHandler());
				Thread khs = new Thread(new KhsClass());
				miner.start();
				khs.start();
				mining = true;
			}
		});
		btnStartMining.setBounds(10, 84, 179, 23);
		panel.add(btnStartMining);

		JButton btnStopMining = new JButton("Stop Mining");
		btnStopMining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mining = false;
			}
		});
		btnStopMining.setBounds(10, 118, 179, 23);
		panel.add(btnStopMining);

		JLabel lblTried = new JLabel("Tried:");
		lblTried.setBounds(10, 11, 46, 14);
		panel.add(lblTried);

		JLabel lblSolved = new JLabel("Solved:");
		lblSolved.setBounds(10, 34, 46, 14);
		panel.add(lblSolved);

		JLabel lblKhs = new JLabel("Kh/s:");
		lblKhs.setBounds(10, 59, 46, 14);
		panel.add(lblKhs);

		lblTriedAmount = new JLabel("0");
		lblTriedAmount.setBounds(66, 11, 132, 14);
		panel.add(lblTriedAmount);

		lblSolvedAmount = new JLabel("0");
		lblSolvedAmount.setBounds(66, 34, 46, 14);
		panel.add(lblSolvedAmount);

		lblKHsAmount = new JLabel("0.0");
		lblKHsAmount.setBounds(66, 59, 46, 14);
		panel.add(lblKHsAmount);

		table = new JTable(1, 1);
		table.setModel(solved);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setToolTipText("Solved hashes");
		scrollPane.setBounds(199, 11, 225, 131);
		panel.add(scrollPane);
	}

	public static void updateCounter() {
		counter++;
		// lblTriedAmount.setText(Integer.toString(Integer.parseInt(lblTriedAmount.getText())+1));
	}

	public static void updateKhs(double khs) {
		lblKHsAmount.setText(Double.toString(khs));
		lblTriedAmount.setText(Integer.toString(counter));
	}

	public static void updateSolved(String solution) {
		lblSolvedAmount.setText(Integer.toString(Integer
				.parseInt(lblSolvedAmount.getText()) + 1));
		solved.addRow(new Object[] { solution });
	}

	public static int getCounter() {
		// return Integer.parseInt(lblTriedAmount.getText());
		return counter;
	}

	public static boolean getStatus() {
		return mining;
	}
}
