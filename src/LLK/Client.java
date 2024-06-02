package LLK;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.Timer;

import javax.swing.*;

public class Client extends JFrame{
	GameJPanel panel2;
	JButton button1 = new JButton("Restart");
	JButton button2 = new JButton("Exit");
	JButton button3 = new JButton("Show LeaderBoard");
	static JTextField textField = new JTextField(5);
	static JTextField timeField = new JTextField(5);
	static JTextField textField2 = new JTextField(5);
	static String username;
	int gridSize;
	long startTime;
	Timer timer;
	static int maxscore=0;
	private SystemParameters systemParameters;
	static String backgroundPath="pics\\background.jpg";
	static String picPath="pics\\pic";

	public Client(){
		login();

		panel2 = new GameJPanel(gridSize);

		JLabel label1 = new JLabel("Current Score:");
		JLabel timeLabel= new JLabel("Time:");
		JLabel label2 = new JLabel("Max Score:");
		JPanel panel = new JPanel(new BorderLayout());
		textField.setEditable(false);
		timeField.setEditable(false);
		textField2.setEditable(false);
		panel2.setLayout(new BorderLayout());
		panel.setLayout(new FlowLayout());
		panel.add(timeLabel);
		panel.add(timeField);
		panel.add(label1);
		panel.add(textField);
		panel.add(label2);
		panel.add(textField2);
		panel.add(button1);
		panel.add(button2);
		panel.add(button3);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel,BorderLayout.SOUTH);
		this.getContentPane().add(panel2,BorderLayout.CENTER);
		this.setSize(700,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("University LianLianKan");
		this.setVisible(true);
		button1.setEnabled(true);
		button2.setEnabled(true);
		button3.setEnabled(true);
		systemParameters = new SystemParameters("system.properties");
		
		button1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				startNewGame();
			}
		});
		
		button2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				System.exit(0);
			}
		});
		button3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				showLeaderboard();
			}
		});
		startNewGame();
	}
	
	private void login(){
		username = JOptionPane.showInputDialog(this, "Enter UserName:");
        if (username == null || username.isEmpty()) {
            System.exit(0);
        }

        String[] options = {"8x8", "16x16"};
        int choice = JOptionPane.showOptionDialog(this, "Choose mode:", "mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if (choice == 0) {
            gridSize = 8;
        } else if (choice == 1) {
            gridSize = 16;
        } else {
            System.exit(0);
        }
		JOptionPane.showMessageDialog(this,"Link two blocks to get 2 scores.\nPress Q to repaint the graph.\nPress H to help to discover 2 linkable blocks.\nTry to LINK and CLEAR all univs' logo in the shortest time!" ,"How to play",JOptionPane.INFORMATION_MESSAGE);
	}

	private void showLeaderboard() {
		File file = new File("userData.txt");
		File file2 = new File("userData2.txt");
		Map<String, Long> userData = new HashMap<>();
		Map<String, Long> userData2 = new HashMap<>();
		

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) {
					String existingUsername = parts[0];
					long existingTime = Long.parseLong(parts[1]);
					userData.put(existingUsername, existingTime);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) {
					String existingUsername = parts[0];
					long existingTime = Long.parseLong(parts[1]);
					userData2.put(existingUsername, existingTime);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(userData.entrySet());
		List<Map.Entry<String, Long>> sortedEntries2 = new ArrayList<>(userData2.entrySet());
		Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});
		Collections.sort(sortedEntries2, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});

		StringBuilder leaderboard = new StringBuilder("LeaderBoard:\n");
		int count = 0;
		for (Map.Entry<String, Long> entry : sortedEntries) {
			if (count++ >= 5) break;
			leaderboard.append(count).append(". ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" s\n");
		}


		StringBuilder leaderboard2 = new StringBuilder("LeaderBoard:\n");
		int count2 = 0;
		for (Map.Entry<String, Long> entry2 : sortedEntries2) {
			if (count2++ >= 5) break;
			leaderboard2.append(count2).append(". ").append(entry2.getKey()).append(": ").append(entry2.getValue()).append(" s\n");
		}

		JOptionPane.showMessageDialog(this, "8x8\n"+leaderboard.toString()+"\n16x16\n"+leaderboard2.toString(), "LeaderBoard", JOptionPane.INFORMATION_MESSAGE);
	}

	

	private void startNewGame(){
		panel2.startNewGame();
	}

	
	public static void main(String[] args) {
		new Client();
	}

}
