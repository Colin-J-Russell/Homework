package JavaSwing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Tetris extends JFrame{

	private static final long serialVersionUID = -8410572359888197083L;
	
	private final int WIDTH = 400;
	private final int HEIGHT = 540;
		
	private GridPanel gridPanel;
	private ControlPanel controlPanel;
	private Timer animationTimer = null;
	private int speed = 400;
	private block a = new block();
	private int LocX=5;
	private int LocY=2;
	private int currentPiece = (int)(Math.random()*7);
	private int nextPiece = (int)(Math.random()*7);
	
	
	public Tetris() {
		super("Tetris");
		 
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(5, 5));
		
		gridPanel = new GridPanel();
		this.add(gridPanel, BorderLayout.CENTER);
			
		controlPanel = new ControlPanel();
		this.add(controlPanel, BorderLayout.WEST);
		this.setResizable(false);
		
        gridPanel.repaint();
        
		this.setVisible(true);
		
		gridPanel.setFocusable(true);
        gridPanel.requestFocus();
	 }
	
	public class GridPanel extends JPanel{
		private static final long serialVersionUID = 4427529661161840363L;
		private int verts = 20;
		private int horzs = 10;
		private int[][] playfield;
		private int rotation=0;
		
		public GridPanel() {
			super();
			this.setBackground(Color.BLUE);
			this.prepareActionHandlers();
			playfield=new int[verts][horzs];
			
		}
		public void setHV(int h, int v) {
			this.horzs=h;
			this.verts=v;
			
		}
		@Override
		public void paint(Graphics g){
			super.paintComponent(g);
			Graphics2D graphicsContext = null;
			if (g instanceof Graphics2D) {
				graphicsContext = (Graphics2D)g;
			}
			else {
				System.out.println("you have an old JVM");
				return;
			}
			
        	int height = this.getHeight();
        	int width = this.getWidth();
        	
        	double horzspacing = width / (double)horzs;
        	
        	double vertspacing = height / (double)verts;
        	
        	int x=0;
        	int y=0;
        	graphicsContext.setColor(Color.RED);
        	
        	for(int i=0;i<horzs;i++) {
        		for(int j=0;j<verts;j++) {
        			if(playfield[j][i]==1) {
        				x=(int)(i*horzspacing);
                		y=(int)(j*vertspacing);
        				graphicsContext.fillRect(x, y, width/horzs, height/verts);
        			}
        		}
        	}
        	
        	double x0 = 0.0;
        	graphicsContext.setColor(Color.WHITE);
        	for (int i = 0; i < horzs; ++i) {
        		graphicsContext.drawLine((int)x0, 0, (int)x0, height);
        		x0 += horzspacing;
        	}
        	
        	double y0 = 0.0;
        	graphicsContext.setColor(Color.WHITE);
        	for (int i = 0; i < verts; ++i) {
        		graphicsContext.drawLine(0, (int)y0, width, (int)y0);
        		y0 += vertspacing;
        	}
		}
		private void dropPiece() {
			if(this.canDrop()) {
				clearPiece();
				LocY++;
				for(int i=0;i<4;i++) {
					try {
						playfield[LocY+a.piece[currentPiece][rotation][i].y][LocX+a.piece[currentPiece][rotation][i].x]=1;
					}
					catch(NullPointerException e) {
						continue;
						
					}
				}
				
			}
			else {
				this.newPiece();
			}
			
		}
		private boolean canDrop() {
			int pSize = 0;
			for(int i=0;i<4;i++) {
				if(a.piece[currentPiece][rotation][i].y>pSize) {
					pSize=a.piece[currentPiece][rotation][i].y;
				}
			}
			if(LocY>=19-pSize) {
				return false;
			}
			for(int i=0;i<4;i++) {
				boolean isBottom = true;
				for(int j=1;j<4;j++) {
					if(a.piece[currentPiece][rotation][(i+j)%4].x==a.piece[currentPiece][rotation][i].x&&a.piece[currentPiece][rotation][(i+j)%4].y>a.piece[currentPiece][rotation][i].y) {
						isBottom = false;
					}
				}
				if(isBottom && playfield[LocY+a.piece[currentPiece][rotation][i].y+1][LocX+a.piece[currentPiece][rotation][i].x]==1) {
					return false;
				}
			}
			return true;
			
		}
		private boolean canMoveR() {
			for(int i=0;i<4;i++) {
				boolean isSide = true;
				for(int j=1;j<4;j++) {
					if(a.piece[currentPiece][rotation][(i+j)%4].y==a.piece[currentPiece][rotation][i].y&&a.piece[currentPiece][rotation][(i+j)%4].x>a.piece[currentPiece][rotation][i].x) {
						isSide = false;
					}
				}
				if(isSide && playfield[LocY+a.piece[currentPiece][rotation][i].y][LocX+a.piece[currentPiece][rotation][i].x+1]==1) {
					return false;
				}
			}
			return true;
		}
		private boolean canMoveL() {
			for(int i=0;i<4;i++) {
				boolean isSide = true;
				for(int j=1;j<4;j++) {
					if(a.piece[currentPiece][rotation][(i+j)%4].y==a.piece[currentPiece][rotation][i].y&&a.piece[currentPiece][rotation][(i+j)%4].x<a.piece[currentPiece][rotation][i].x) {
						isSide = false;
					}
				}
				if(isSide && playfield[LocY+a.piece[currentPiece][rotation][i].y][LocX+a.piece[currentPiece][rotation][i].x-1]==1) {
					return false;
				}
			}
			return true;
		}
		private void clearPiece() {
			for(int i=0;i<4;i++) {
				try {
					playfield[LocY+a.piece[currentPiece][rotation][i].y][LocX+a.piece[currentPiece][rotation][i].x]=0;
				}
				catch(NullPointerException e) {
					continue;
				}
				catch(ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		private int fullRow() {
			for(int i=0;i<20;i++) {
				int rowCount = 0;
				for(int j=0;j<10;j++) {
					if(playfield[i][j]==1) {
						rowCount++;
					}
				}
				if(rowCount==10) {
					return i;
				}
				rowCount=0;
			}
			return -1;
		}
		private void clearRow(int rowNum) {
			for(int i=rowNum;i>0;i--) {
				for(int j=0;j<10;j++) {
					playfield[i][j]=playfield[i-1][j];
				}
			}
			for(int i=0;i<10;i++) {
				playfield[0][i]=0;
			}
			controlPanel.updateScore(1);
		}
		private void newPiece() {
			if(playfield[3][5]==1) {
				animationTimer.stop();
			}
			LocY=2;
			LocX=5;
			currentPiece=nextPiece;
			nextPiece=(int)(Math.random()*7);
			controlPanel.showNext();
			while(fullRow()!=-1) {
				clearRow(fullRow());
			}
		}
		private void prepareActionHandlers(){
			this.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_UP) {
						boolean canRotate=true;
						try {
							for(int i=0;i<4;i++) {
								boolean isSide = true;
								for(int j=1;j<4;j++) {
									if(a.piece[currentPiece][(rotation+1)%4][(i+j)%4].y==a.piece[currentPiece][(rotation+1)%4][i].y&&a.piece[currentPiece][(rotation+1)%4][(i+j)%4].x>a.piece[currentPiece][(rotation+1)%4][i].x) {
										isSide = false;
									}
									if(isSide&&LocX+a.piece[currentPiece][(rotation+1)%4][i].x>=10) {
										canRotate=false;
									}
								}
							}
							boolean can=true;
							if(canRotate) {
								for(int i=0;i<4;i++) {
									if(canDrop()) {
										clearPiece();
									}
									if(playfield[LocY + a.piece[currentPiece][(rotation+1)%4][i].y][LocX + a.piece[currentPiece][(rotation+1)%4][i].x]==1) {
										can=false;
									}
								}
							}
							if(can&&canRotate) {
								rotation++;
								rotation%=4;
							}
						}
						catch(ArrayIndexOutOfBoundsException error) {
							System.out.println(error);
						}
					}
				else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
						boolean canRotate=true;
						try {
							int newR;
							if(rotation!=0) {
								newR=rotation-1;
							}
							else {
								newR=3;
							}
							for(int i=0;i<4;i++) {
								boolean isSide = true;
								for(int j=1;j<4;j++) {
									if(a.piece[currentPiece][newR][(i+j)%4].y==a.piece[currentPiece][newR][i].y&&a.piece[currentPiece][newR][(i+j)%4].x>a.piece[currentPiece][newR][i].x) {
										isSide = false;
									}
									if(isSide&&LocX+a.piece[currentPiece][newR][i].x>=10) {
										canRotate=false;
									}
								}
							}
							boolean can=true;
							if(canRotate) {
								for(int i=0;i<4;i++) {
									if(canDrop()) {
										clearPiece();
									}
									
									if(playfield[LocY + a.piece[currentPiece][newR][i].y][LocX + a.piece[currentPiece][newR][i].x]==1) {
										can=false;
		 							}
								}
							}if(can&canRotate){
								rotation=newR;
							}
						}
						catch(ArrayIndexOutOfBoundsException error) {
						}
					}
					else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
						if(canDrop()) {
							clearPiece();
						}
						int furthestX = 0;
						for(int i=0;i<4;i++) {
							if(a.piece[currentPiece][rotation][i].x<furthestX) {
								furthestX=a.piece[currentPiece][rotation][i].x;
							}
						}
						if(LocX+furthestX!=0&&canMoveL()) {
							LocX--;
						}
						
					}
					else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
						if(canDrop()) {
							clearPiece();
						}
						int furthestX = 0;
						for(int i=0;i<4;i++) {
							if(a.piece[currentPiece][rotation][i].x>furthestX) {
								furthestX=a.piece[currentPiece][rotation][i].x;
							}
						}
						if(LocX+furthestX<9&&canMoveR()) {
							LocX++;
						}
						
					}
					else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
						while(canDrop()) {
							dropPiece();
						}
					}
					
				}
				@Override
				public void keyReleased(KeyEvent e) {
				}
			});
		}

		
	 }
	public class ControlPanel extends JPanel{
		private static final long serialVersionUID = 440020653220885023L;
		private JTextField Score;
		private JButton stopGo;
		private graphicsPanel gPanel;
		private int stopGoCounter = 0;
		private int scoreCounter = 0;
		public ControlPanel() {
			prepareButtonHandlers();
			setLayout(new FlowLayout());
			Score = new JTextField("0", 5);  
			Score.setEditable(false);
			gPanel=new graphicsPanel(50, 50);
			this.add(stopGo);
			this.add(gPanel);
			this.add(Score);
			
		}
		public void showNext() {
			gPanel.repaint();
		}
		private void prepareButtonHandlers() {
			stopGo = new JButton("Go");
			stopGo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					stopGoCounter++;
					if(stopGoCounter%2==0) {
						stopGo.setText("Go");
						animationTimer.stop();
					}
					else {
						stopGo.setText("Stop");
						animationTimer = new Timer(speed, 
								new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										gridPanel.dropPiece();
										gridPanel.repaint();
										
									}
								}
						);
						animationTimer.start();
					}
					gridPanel.requestFocus();
				}
				
			});
			
		}
		public void updateScore(int i) {
			scoreCounter+=i;
			Score.setText(scoreCounter + "");
		}
		public Dimension getPreferredSize() 
		{
			return new Dimension(100, 500);
		}
		
	 }
	 public class graphicsPanel extends JPanel{
		private static final long serialVersionUID = -63141418591219159L;
		private int height;
		private int width;
		public graphicsPanel (int height, int width){
				this.height = height;
				this.width = width;
				
				this.setLayout(new FlowLayout());
				this.setBackground(Color.BLUE);
		}
		public Dimension getPreferredSize(){
				return new Dimension (this.width, this.height);
		}
		@Override
		public void paint (Graphics g){
			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.RED);
			
			int pSize = width/4;
			switch(nextPiece) {
			case 0:
				g2d.fillRect((width-pSize)/2, (height-pSize)/2, pSize, pSize);
				g2d.setColor(Color.BLACK);
				g2d.drawRect((width-pSize)/2, (height-pSize)/2, pSize, pSize);
				break;
			case 1:
				g2d.fillRect((width-pSize)/2, height/4+pSize, pSize, pSize);
				g2d.fillRect((width-pSize)/2, height/4, pSize, pSize);
				g2d.setColor(Color.BLACK);
				g2d.drawRect((width-pSize)/2, height/4+pSize, pSize, pSize);
				g2d.drawRect((width-pSize)/2, height/4, pSize, pSize);
				break;
			case 2:
				g2d.fillRect((width-pSize)/2, pSize, pSize, pSize);
				g2d.fillRect((width-pSize)/2, 0, pSize, pSize);
				g2d.fillRect((width-pSize)/2, (3*pSize), pSize, pSize);
				g2d.fillRect((width-pSize)/2, (2*pSize), pSize, pSize);
				g2d.setColor(Color.BLACK);
				g2d.drawRect((width-pSize)/2, pSize, pSize, pSize);
				g2d.drawRect((width-pSize)/2, 0, pSize, pSize);
				g2d.drawRect((width-pSize)/2, (3*pSize), pSize, pSize);
				g2d.drawRect((width-pSize)/2, (2*pSize), pSize, pSize);
				break;
			case 3:
				g2d.fillRect((width-pSize)/3, (height-pSize)/3+pSize, pSize, pSize);
				g2d.fillRect((width-pSize)/3, (height-pSize)/3, pSize, pSize);
				g2d.fillRect((width-pSize)/3+pSize, (height-pSize)/3, pSize, pSize);
				g2d.fillRect((width-pSize)/3+pSize, (height-pSize)/3+pSize, pSize, pSize);
				g2d.setColor(Color.BLACK);
				g2d.drawRect((width-pSize)/3, (height-pSize)/3+pSize, pSize, pSize);
				g2d.drawRect((width-pSize)/3, (height-pSize)/3, pSize, pSize);
				g2d.drawRect((width-pSize)/3+pSize, (height-pSize)/3, pSize, pSize);
				g2d.drawRect((width-pSize)/3+pSize, (height-pSize)/3+pSize, pSize, pSize);
				break;
			case 4:
				g2d.fillRect((width-pSize)/3, (height-pSize)/3, pSize, pSize);
				g2d.fillRect((width-pSize)/3+pSize, (height-pSize)/3, pSize, pSize);
				g2d.fillRect((width-pSize)/3-pSize, (height-pSize)/3+pSize, pSize, pSize);
				g2d.fillRect((width-pSize)/3+(2*pSize), (height-pSize)/3+pSize, pSize, pSize);
				g2d.setColor(Color.BLACK);
				g2d.drawRect((width-pSize)/3, (height-pSize)/3, pSize, pSize);
				g2d.drawRect((width-pSize)/3+pSize, (height-pSize)/3, pSize, pSize);
				g2d.drawRect((width-pSize)/3-pSize, (height-pSize)/3+pSize, pSize, pSize);
				g2d.drawRect((width-pSize)/3+(2*pSize), (height-pSize)/3+pSize, pSize, pSize);
				break;
			case 5:
				g2d.fillRect((width-pSize)/3, (height-pSize)/3-pSize, pSize, pSize);
				g2d.fillRect((width-pSize)/3+pSize, (height-pSize)/3, pSize, pSize);
				g2d.fillRect((width-pSize)/3+(2*pSize), (height-pSize)/3+pSize, pSize, pSize);
				g2d.fillRect((width-pSize)/3+pSize, (height-pSize)/3+(2*pSize), pSize, pSize);
				g2d.setColor(Color.BLACK);
				g2d.drawRect((width-pSize)/3, (height-pSize)/3-pSize, pSize, pSize);
				g2d.drawRect((width-pSize)/3+pSize, (height-pSize)/3, pSize, pSize);
				g2d.drawRect((width-pSize)/3+(2*pSize), (height-pSize)/3+pSize, pSize, pSize);
				g2d.drawRect((width-pSize)/3+pSize, (height-pSize)/3+(2*pSize), pSize, pSize);
				break;
			case 6:
				g2d.fillRect((width-pSize)/3, (height-pSize)/3, pSize, pSize);
				g2d.fillRect((width-pSize)/3+pSize, (height-pSize)/3+pSize, pSize, pSize);
				g2d.fillRect((width-pSize)/3+pSize, (height-pSize)/3-pSize, pSize, pSize);
				g2d.fillRect((width-pSize)/3+(2*pSize), (height-pSize)/3+(2*pSize), pSize, pSize);
				g2d.setColor(Color.BLACK);
				g2d.drawRect((width-pSize)/3, (height-pSize)/3, pSize, pSize);
				g2d.drawRect((width-pSize)/3+pSize, (height-pSize)/3+pSize, pSize, pSize);
				g2d.drawRect((width-pSize)/3+pSize, (height-pSize)/3-pSize, pSize, pSize);
				g2d.drawRect((width-pSize)/3+(2*pSize), (height-pSize)/3+(2*pSize), pSize, pSize);
				break;
			}
		}
	 }
	 public class block{
		 Point[][][] piece = new Point[7][4][4];
		 block(){
			 for(int i=0;i<piece.length;i++) {
				 for(int j=0;j<piece[0].length;j++) {
					 for(int h=0;h<piece[0][0].length;h++) {
						 piece[i][j][h]= new Point();
					 }
				 }
			 }
			 //first piece
			 for(int i=0;i<4;i++) {
				 piece[0][i][0].x=0;
				 piece[0][i][0].y=0;
			 }
			 
			 //second piece
			 for(int i=0;i<4;i+=2) {
				 piece[1][i][0].x=0;
				 piece[1][i][0].y=0;
				 piece[1][i][1].x=0;
				 piece[1][i][1].y=1;
			 }
			 for(int i=1;i<4;i+=2) {
				 piece[1][i][0].x=0;
				 piece[1][i][0].y=0;
				 piece[1][i][1].x=1;
				 piece[1][i][1].y=0;
			 }
			 //third piece
			 for(int i=0;i<4;i+=2) {
				 piece[2][i][0].x=0;
				 piece[2][i][0].y=0;
				 piece[2][i][1].x=0;
				 piece[2][i][1].y=1;
				 piece[2][i][2].x=0;
				 piece[2][i][2].y=2;
				 piece[2][i][3].x=0;
				 piece[2][i][3].y=3;
			 }
			 for(int i=1;i<4;i+=2) {
				 piece[2][i][0].x=0;
				 piece[2][i][0].y=0;
				 piece[2][i][1].x=1;
				 piece[2][i][1].y=0;
				 piece[2][i][2].x=2;
				 piece[2][i][2].y=0;
				 piece[2][i][3].x=3;
				 piece[2][i][3].y=0;
			 }
			 //fourth piece
			 for(int i=0;i<4;i++) {
				 piece[3][i][0].x=0;
				 piece[3][i][0].y=0;
				 piece[3][i][1].x=0;
				 piece[3][i][1].y=1;
				 piece[3][i][2].x=1;
				 piece[3][i][2].y=1;
				 piece[3][i][3].x=1;
				 piece[3][i][3].y=0;
			 }
			 //fifth piece
			 	piece[4][0][0].x=0;
			 	piece[4][0][0].y=0;
			 	piece[4][0][1].x=1;
			 	piece[4][0][1].y=-1;
			 	piece[4][0][2].x=0;
			 	piece[4][0][2].y=1;
			 	piece[4][0][3].x=1;
			 	piece[4][0][3].y=2;
			 	
			 	piece[4][1][0].x=0;
			 	piece[4][1][0].y=0;
			 	piece[4][1][1].x=1;
			 	piece[4][1][1].y=1;
			 	piece[4][1][2].x=-1;
			 	piece[4][1][2].y=0;
			 	piece[4][1][3].x=-2;
			 	piece[4][1][3].y=1;
			 	
			 	piece[4][2][0].x=0;
			 	piece[4][2][0].y=0;
			 	piece[4][2][1].x=0;
			 	piece[4][2][1].y=-1;
			 	piece[4][2][2].x=-1;
			 	piece[4][2][2].y=1;
			 	piece[4][2][3].x=-1;
			 	piece[4][2][3].y=-2;
			 	
			 	piece[4][3][0].x=0;
			 	piece[4][3][0].y=0;
			 	piece[4][3][1].x=1;
			 	piece[4][3][1].y=-1;
			 	piece[4][3][2].x=-1;
			 	piece[4][3][2].y=0;
			 	piece[4][3][3].x=-2;
			 	piece[4][3][3].y=-1;
			 	
			 	//sixth piece
			 	piece[5][0][0].x=0;
			 	piece[5][0][0].y=0;
			 	piece[5][0][1].x=1;
			 	piece[5][0][1].y=-1;
			 	piece[5][0][2].x=2;
			 	piece[5][0][2].y=-2;
			 	piece[5][0][3].x=-1;
			 	piece[5][0][3].y=-1;
			 	
			 	piece[5][1][0].x=0;
			 	piece[5][1][0].y=0;
			 	piece[5][1][1].x=1;
			 	piece[5][1][1].y=1;
			 	piece[5][1][2].x=2;
			 	piece[5][1][2].y=2;
			 	piece[5][1][3].x=1;
			 	piece[5][1][3].y=-1;
			 	
			 	piece[5][2][0].x=0;
			 	piece[5][2][0].y=0;
			 	piece[5][2][1].x=1;
			 	piece[5][2][1].y=1;
			 	piece[5][2][2].x=-1;
			 	piece[5][2][2].y=1;
			 	piece[5][2][3].x=-2;
			 	piece[5][2][3].y=2;
			 	
			 	piece[5][3][0].x=0;
			 	piece[5][3][0].y=0;
			 	piece[5][3][1].x=-1;
			 	piece[5][3][1].y=-1;
			 	piece[5][3][2].x=-2;
			 	piece[5][3][2].y=-2;
			 	piece[5][3][3].x=-1;
			 	piece[5][3][3].y=1;
			 	
			 	//seventh piece
			 	piece[6][0][0].x=0;
			 	piece[6][0][0].y=0;
			 	piece[6][0][1].x=1;
			 	piece[6][0][1].y=-1;
			 	piece[6][0][2].x=-1;
			 	piece[6][0][2].y=-1;
			 	piece[6][0][3].x=-2;
			 	piece[6][0][3].y=-2;
			 	
			 	piece[6][1][0].x=0;
			 	piece[6][1][0].y=0;
			 	piece[6][1][1].x=1;
			 	piece[6][1][1].y=1;
			 	piece[6][1][2].x=1;
			 	piece[6][1][2].y=-1;
			 	piece[6][1][3].x=2;
			 	piece[6][1][3].y=-2;
			 	
			 	piece[6][2][0].x=0;
			 	piece[6][2][0].y=0;
			 	piece[6][2][1].x=1;
			 	piece[6][2][1].y=1;
			 	piece[6][2][2].x=-1;
			 	piece[6][2][2].y=1;
			 	piece[6][2][3].x=2;
			 	piece[6][2][3].y=2;
			 	
			 	piece[6][3][0].x=0;
			 	piece[6][3][0].y=0;
			 	piece[6][3][1].x=-1;
			 	piece[6][3][1].y=-1;
			 	piece[6][3][2].x=-1;
			 	piece[6][3][2].y=1;
			 	piece[6][3][3].x=-2;
			 	piece[6][3][3].y=2;
		 }
	 }
	public static void main(String[] args) {
		new Tetris();
	}
}