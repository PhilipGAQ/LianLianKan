package LLK;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.io.*;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameJPanel extends JPanel implements ActionListener ,MouseListener,KeyListener{

	private Image[] pics;
	private int n;
	private int[][] map;
	private boolean isClick = false;
	private int clickId,clickX,clickY;
	private int linkMethod;
	private Node z1,z2;
	private picmap mapUtil;
	private int cellHeight;
	private int cellWidth;
	public static int count = 0;
	public long startTime;
	private Timer timer;
	private Image backgroundImage;
	
	


	
	
	public static final int LINKBYHORIZONTAL = 1,LINKBYVERTICAL = 2,LINKBYONECORNER = 3,LINKBYTWOCORNER = 4;
	public static final int BLANK_STATE = -1;
	
	public GameJPanel(int num){
		setSize(700, 800);
		count = 10;
		n=num;
		mapUtil = new picmap(count, num);
		map = mapUtil.getMap();
		this.setVisible(true); 
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setFocusable(true);
		getPics();
		cellHeight=(int)(getWidth()/n);
		cellWidth=(int)(getWidth()/n);
		repaint();
	}
	
	private void getPics() {
		pics = new Image[10];
		for(int i=0;i<=9;i++){
			pics[i] = Toolkit.getDefaultToolkit().getImage(Client.picPath+(i+1)+".png");
		}
		
        ImageIcon icon = new ImageIcon(Client.backgroundPath); // Path to your image file
        backgroundImage = icon.getImage();
    }		
	

	public void paint(Graphics g){
		int gridSize=n;

		drawBackGround(g);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int imageIndex = map[i][j];

                if (imageIndex!=BLANK_STATE) {
                    g.drawImage(pics[imageIndex], j * cellWidth, i * cellHeight, cellWidth, cellHeight, this);
                }

            }
        }
	}		

	public void drawBackGround(Graphics g){
		if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
	}
	

	private boolean horizonLink(int clickX1, int clickY1, int clickX2, int clickY2) {
		
		if(clickY1>clickY2){
			int temp1 = clickX1;
			int temp2 = clickY1;
			clickX1 = clickX2;
			clickY1 = clickY2;
			clickX2 = temp1;
			clickY2 = temp2;
		}
		
		if(clickX1==clickX2){ 
			
			for(int i=clickY1+1;i<clickY2;i++){
				if(map[clickX1][i]!=BLANK_STATE){
					return false;
				}
			}
			
			linkMethod = LINKBYHORIZONTAL;
			return true;
		}
		
		
		return false;
	}


	private boolean verticalLink(int clickX1, int clickY1, int clickX2, int clickY2) {
		
		if(clickX1>clickX2){
			int temp1 = clickX1;
			int temp2 = clickY1;
			clickX1 = clickX2;
			clickY1 = clickY2;
			clickX2 = temp1;
			clickY2 = temp2;
		}
		
		if(clickY1==clickY2){
			
			for(int i=clickX1+1;i<clickX2;i++){
				if(map[i][clickY1]!=BLANK_STATE){
					return false;
				}
			}

			linkMethod = LINKBYVERTICAL;

			return true;
		}
		
		
		
		return false;
	}
	
	private boolean cornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {
		
		if(clickY1>clickY2){
			int temp1 = clickX1;
			int temp2 = clickY1;
			clickX1 = clickX2;
			clickY1 = clickY2;
			clickX2 = temp1;
			clickY2 = temp2;
		}
		
		if(clickX1<clickX2){
			
			if(map[clickX1][clickY2]==BLANK_STATE&&horizonLink(clickX1, clickY1, clickX1, clickY2)&&verticalLink(clickX2,clickY2,clickX1,clickY2)){
				linkMethod = LINKBYONECORNER;
				z1 = new Node(clickX1,clickY2);
				return true;
			}
			
			if(map[clickX2][clickY1]==BLANK_STATE&&horizonLink(clickX2, clickY2, clickX2, clickY1)&&verticalLink(clickX1,clickY1,clickX2, clickY1)){
				linkMethod = LINKBYONECORNER;
				z1 = new Node(clickX2,clickY1);
				return true;
			}
			
		}else{
			
			if(map[clickX2][clickY1]==BLANK_STATE&&horizonLink(clickX2, clickY2, clickX2, clickY1)&&verticalLink(clickX1,clickY1,clickX2, clickY1)){
				linkMethod = LINKBYONECORNER;
				z1 = new Node(clickX2,clickY1);
				return true;				
			}
			
			if(map[clickX1][clickY2]==BLANK_STATE&&horizonLink(clickX1, clickY1, clickX1, clickY2)&&verticalLink(clickX2,clickY2,clickX1, clickY2)){
				linkMethod = LINKBYONECORNER;
				z1 = new Node(clickX1,clickY2);
				return true;				
			}
				
		}
			
		return false;
	}
	
	
	
	private boolean twoLink(int clickX1, int clickY1, int clickX2, int clickY2) {
		for(int i=clickX1-1;i>=-1;i--){
			
			if(i==-1&&throughVerticalLink(clickX2, clickY2, true)){
				z1 = new Node(-1,clickY1);
				z2 = new Node(-1,clickY2);
				linkMethod = LINKBYTWOCORNER;
				return true;
			}
			
			if(i>=0&&map[i][clickY1]==BLANK_STATE){
				
				if(cornerLink(i, clickY1, clickX2, clickY2)){
					linkMethod = LINKBYTWOCORNER;
					z1 = new Node(i,clickY1);
					z2 = new Node(i,clickY2);
					return true;
				}
				
			
			}else{
				break;
			}
			
		}
		
		for(int i=clickX1+1;i<=n;i++){
			
			if(i==n&&throughVerticalLink(clickX2, clickY2, false)){
				z1 = new Node(n,clickY1);
				z2 = new Node(n,clickY2);
				linkMethod = LINKBYTWOCORNER;
				return true;
			}
			
			if(i!=n&&map[i][clickY1]==BLANK_STATE){
				
				if(cornerLink(i, clickY1, clickX2, clickY2)){
					linkMethod = LINKBYTWOCORNER;
					z1 = new Node(i,clickY1);
					z2 = new Node(i,clickY2);
					return true;
				}
			
			}else{
				break;
			}
			
		}
		
		for(int i=clickY1-1;i>=-1;i--){
			if(i==-1&&throughhorizonLink(clickX2, clickY2, true)){
				linkMethod = LINKBYTWOCORNER;
				z1 = new Node(clickX1,-1);
				z2 = new Node(clickX2,-1);
				return true;
			} 
			
			
			if(i!=-1&&map[clickX1][i]==BLANK_STATE){
				
				if(cornerLink(clickX1, i, clickX2, clickY2)){
					linkMethod = LINKBYTWOCORNER;
					z1 = new Node(clickX1,i);
					z2 = new Node(clickX2,i);
					return true;
				}
			
			}else{
				break;
			}
			
		}
		
		for(int i=clickY1+1;i<=n;i++){

			if(i==n&&throughhorizonLink(clickX2, clickY2, false)){
				z1 = new Node(clickX1,n);
				z2 = new Node(clickX2,n);
				linkMethod = LINKBYTWOCORNER;
				return true;
			}
			
			if(i!=n&&map[clickX1][i]==BLANK_STATE){
				
				if(cornerLink(clickX1, i, clickX2, clickY2)){
					linkMethod = LINKBYTWOCORNER;
					z1 = new Node(clickX1,i);
					z2 = new Node(clickX2,i);
					return true;
				}
				
			}else{
				break;
			}			
			
		}
		
		
		return false;
	}


	private boolean throughhorizonLink(int clickX, int clickY,boolean flag){

		if(flag){
			
			for(int i=clickY-1;i>=0;i--){
				if(map[clickX][i]!=BLANK_STATE){
					return false;
				}
			}			
			
		}else{
			
			for(int i=clickY+1;i<n;i++){
				if(map[clickX][i]!=BLANK_STATE){
					return false;
				}
			}
			
		}
		
		return true;
	}

	
	private boolean throughVerticalLink(int clickX,int clickY,boolean flag){
		
		if(flag){
			
			for(int i=clickX-1;i>=0;i--){
				if(map[i][clickY]!=BLANK_STATE){
					return false;
				}
			}
			
		}else{
			
			for(int i=clickX+1;i<n;i++){
				if(map[i][clickY]!=BLANK_STATE){
					return false;
				}
			}
			
		}
	
		
		return true;
	}

	private void drawSelect(int x, int y, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		BasicStroke s = new BasicStroke(1);
		g2.setStroke(s);
		g2.setColor(Color.BLUE);
		g.drawRect(x+1, y+1, cellWidth-2, cellHeight-2);
	}

	@SuppressWarnings("static-access")
	private void drawLink(int x1, int y1, int x2, int y2) {

		Graphics g = this.getGraphics();
		g.setColor(Color.BLUE);
		Point p1 = new Point(y1*cellWidth+cellWidth/2,x1*cellHeight+cellHeight/2);
		Point p2 = new Point(y2*cellWidth+cellWidth/2,x2*cellHeight+cellHeight/2);
		if(linkMethod == LINKBYHORIZONTAL || linkMethod == LINKBYVERTICAL){
			g.drawLine(p1.x, p1.y,p2.x, p2.y);
		}else if(linkMethod ==LINKBYONECORNER){
			Point point_z1 = new Point(z1.y*cellWidth+cellWidth/2,z1.x*cellHeight+cellHeight/2);
			g.drawLine(p1.x, p1.y,point_z1.x, point_z1.y);
			g.drawLine(p2.x, p2.y,point_z1.x, point_z1.y);
		}else{
			Point point_z1 = new Point(z1.y*cellWidth+cellWidth/2,z1.x*cellHeight+cellHeight/2);
			Point point_z2 = new Point(z2.y*cellWidth+cellWidth/2,z2.x*cellHeight+cellHeight/2);
			
			if(p1.x!=point_z1.x&&p1.y!=point_z1.y){
				Point temp;
				temp = point_z1;
				point_z1 = point_z2;
				point_z2 = temp;
			}

			g.drawLine(p1.x, p1.y, point_z1.x, point_z1.y);
			g.drawLine(p2.x, p2.y, point_z2.x, point_z2.y);
			g.drawLine(point_z1.x,point_z1.y, point_z2.x, point_z2.y);
			
		}
		
		count+=2;
		if (count>Client.maxscore){
			Client.maxscore=count;
		}
		Client.textField.setText(count+"");
		Client.textField2.setText(Client.maxscore+"");
		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		repaint();
		map[x1][y1] = BLANK_STATE;
		map[x2][y2] = BLANK_STATE;
		isWin();
	}

	
	public void clearSelectBlock(int x,int y,Graphics g){
		g.clearRect(y*cellWidth, x*cellHeight, cellWidth, cellHeight);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		// g.drawImage(pics[map[i][j]],j*cellWidth,i*cellHeight,cellWidth,cellHeight,this);
		for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int imageIndex = map[i][j];

                if (imageIndex!=BLANK_STATE) {
                    g.drawImage(pics[imageIndex], j * cellWidth, i * cellHeight, cellWidth, cellHeight, this);
                }

            }
        }
	}

	private boolean find2Block() {
		
		
		if(isClick){
			clearSelectBlock(clickX, clickY, this.getGraphics());
		isClick = false;
		}
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				
				if(map[i][j]==BLANK_STATE){
					continue;
				}				
				
				for(int p=i;p<n;p++){
					for(int q=0;q<n;q++){
						if(map[p][q]!=map[i][j]||(p==i&&q==j)){
							continue;
						}						  
						
						if(verticalLink(p,q,i,j)||horizonLink(p,q,i,j)
								||cornerLink(p,q,i,j)||twoLink(p,q,i,j)){
							drawSelect(j*cellWidth-1, i*cellHeight-1, this.getGraphics());
							drawSelect(q*cellWidth-1, p*cellHeight-1, this.getGraphics());
							drawLink(p, q, i, j);
							repaint();
							return true;
						}
				
					}
				}				
			}
		}
		
		isWin();
		
		return false;
	}


	private void isWin() {
		
		if(count==n*n){
			String msg = "You Win! Restart?";
			saveUserData();
			int type = JOptionPane.YES_NO_OPTION;
			String title = "过关";
			int choice = 0;
			choice = JOptionPane.showConfirmDialog(null, msg,title,type);
			if(choice==1){
				System.exit(0);
			}else if(choice == 0){
				startNewGame();
			}
		}
		
	}
	
	

	public void startNewGame() {
		// TODO Auto-generated method stub
		count = 0;
		mapUtil = new picmap(10,n);		
		map = mapUtil.getMap();
		isClick = false;
		clickId = -1;
		clickX = -1;
		clickY = -1;
		linkMethod = -1;
		Client.textField.setText(count+"");
		Client.textField2.setText(Client.maxscore+"");
		startTime=System.currentTimeMillis();
		timer=new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                Client.timeField.setText(elapsed + " s");
			}
		});
		timer.start();
		repaint();
	}

	
	
	private void saveUserData() {
        timer.stop();
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
		File file;
		if (n==8){
			file = new File("userData.txt");
		} else{
			file = new File("UserData2.txt");
		}
        Map<String, Long> userData = new HashMap<>();

        // Read existing data
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

        // Update with the new data
        if (userData.containsKey(Client.username)) {
            long existingTime = userData.get(Client.username);
            if (elapsed < existingTime) {
                userData.put(Client.username, elapsed);
            }
        } else {
            userData.put(Client.username, elapsed);
        }

        // Write updated data back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Long> entry : userData.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



	public class Node{
		int x;
		int y;
		
		public Node(int x,int y){
			this.x = x;
			this.y = y;
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getKeyCode() == KeyEvent.VK_Q){	
			map = mapUtil.getResetMap();
			repaint();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_H){
			if(!find2Block()){
				JOptionPane.showMessageDialog(this, "No available");
			}
			
			isWin();
			
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		Graphics g = this.getGraphics();
		
		int x = e.getX();
		int y = e.getY();
		int i = y/cellHeight;
		int j = x/cellWidth;
		if(x<0||y<0)
			return ;
		
	
		
		if(isClick){
		
			if(map[i][j]!=BLANK_STATE){
				if(map[i][j]==clickId){
					if(i==clickX&&j==clickY)
						return ;
					
					if(verticalLink(clickX,clickY,i,j)||horizonLink(clickX,clickY,i,j)||cornerLink(clickX,clickY,i,j)||twoLink(clickX,clickY,i,j)){				
						drawSelect(j*cellWidth,i*cellHeight,g);
						drawLink(clickX,clickY,i,j);
						isClick = false;	

					}else{
						clickId = map[i][j];
						clearSelectBlock(clickX,clickY,g);
						clickX = i;
						clickY = j;
						drawSelect(j*cellWidth,i*cellHeight,g);
					}
					
				}else{
					clickId = map[i][j];
					clearSelectBlock(clickX,clickY,g);
					clickX = i;
					clickY = j;
					drawSelect(j*cellWidth,i*cellHeight,g);
				}
				
			}
			
		}else{
			if(map[i][j]!=BLANK_STATE){
				clickId = map[i][j];
				isClick = true;
				clickX = i;
				clickY = j;
				drawSelect(j*cellWidth,i*cellHeight,g);
			}
		}
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	
	@Override 
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	
	}
	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
