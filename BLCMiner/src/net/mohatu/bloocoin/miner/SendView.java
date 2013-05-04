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

public class SendView implements Runnable {

	private JFrame frmSendCoins;
	private JTextField tfAddr;
	private JTextField tfAmount;

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
		frmSendCoins.setBounds(100, 100, 450, 87);
		frmSendCoins.setLocationRelativeTo(MainView.scrollPane);
		frmSendCoins.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		frmSendCoins.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblRecipientAddress = new JLabel("Recipient address: ");
		lblRecipientAddress.setBounds(10, 11, 111, 14);
		panel.add(lblRecipientAddress);

		tfAddr = new JTextField();
		tfAddr.setBounds(131, 8, 303, 20);
		panel.add(tfAddr);
		tfAddr.setColumns(10);

		JLabel lblAmount = new JLabel("Amount: ");
		lblAmount.setBounds(10, 36, 87, 14);
		panel.add(lblAmount);

		tfAmount = new JTextField();
		tfAmount.setBounds(131, 33, 86, 20);
		panel.add(tfAmount);
		tfAmount.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] options = { "Yes", "No" };
				int answer = JOptionPane.showOptionDialog(MainView.scrollPane,
						"Are you sure you want to send " + tfAmount.getText()
								+ " BLC to\n" + tfAddr.getText() + "?",
						"Confirmation", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, options,
						options[1]);
				if(answer == JOptionPane.YES_OPTION){
					Thread sc = new Thread(new SendClass(tfAddr.getText(), Integer.parseInt(tfAmount.getText())));
					sc.start();
					frmSendCoins.dispose();
				}else{
					//cancelled
				}

			}
		});
		btnSend.setBounds(227, 32, 98, 23);
		panel.add(btnSend);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSendCoins.dispose();
			}
		});
		btnCancel.setBounds(335, 32, 99, 23);
		panel.add(btnCancel);
	}
}
