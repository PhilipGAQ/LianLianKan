package LLK;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.Timer;

import javax.swing.*;

public class GameClient extends JFrame{
	GamePanel panel2;
	JButton button1 = new JButton("重来一局");
	JButton button2 = new JButton("退出");
	static JTextField textField = new JTextField(10);
	static JTextField timeField = new JTextField(5);
	String username;
	int gridSize;
	long startTime;
	Timer timer;

	public GameClient(){
		login();

		panel2 = new GamePanel(gridSize);

		JLabel label1 = new JLabel("已消去方块数量：");
		JLabel timeLabel= new JLabel("耗时：");
		JPanel panel = new JPanel(new BorderLayout());
		textField.setEditable(false);
		timeField.setEditable(false);
		
		panel2.setLayout(new BorderLayout());
		panel.setLayout(new FlowLayout());
		panel.add(timeLabel);
		panel.add(timeField);
		panel.add(label1);
		panel.add(textField);
		panel.add(button1);
		panel.add(button2);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel,BorderLayout.SOUTH);
		this.getContentPane().add(panel2,BorderLayout.CENTER);
//		this.getContentPane().add(panel3,BorderLayout.SOUTH);
		this.setSize(600,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("连连看");
		this.setVisible(true);
		button1.setEnabled(true);
		button2.setEnabled(true);
		
		button1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				startNewGame();
			}
		});
		
		button2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				saveUserData();
				System.exit(0);
			}
		});
		startNewGame();
	}
	
	private void login(){
		username = JOptionPane.showInputDialog(this, "请输入用户名:");
        if (username == null || username.isEmpty()) {
            System.exit(0);
        }

        String[] options = {"8x8", "16x16"};
        int choice = JOptionPane.showOptionDialog(this, "请选择规格:", "规格选择",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if (choice == 0) {
            gridSize = 8;
        } else if (choice == 1) {
            gridSize = 16;
        } else {
            System.exit(0);
        }
	}

	private void startNewGame(){
		textField.setText("0");
		startTime=System.currentTimeMillis();
		timer=new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                timeField.setText(elapsed + " 秒");
			}
		});
		timer.start();
		panel2.startNewGame();
	}

	private void saveUserData(){
		timer.stop();
		long elapsed=(System.currentTimeMillis()-startTime)/1000;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("userData.txt", true))) {
            writer.write(username + "," + elapsed + "," + textField.getText());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	
	public static void main(String[] args) {
		new GameClient();
	}

}
