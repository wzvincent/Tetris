package graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Shape {
	
	boolean gameOver = false;
	Tetris1 tc;
	int x,y;
	public boolean paused = false;
	
	int type;
	public boolean stopped=false;
	private Color color = null;
	
	public static Color[] colorArr = {
		Color.green,
		Color.red,
		Color.blue,
		Color.yellow,
		Color.orange,
		Color.pink,
		Color.cyan
		};
	int colorIndex = Tetris1.r.nextInt(colorArr.length);
	
	int[][][] data= {
			{//0 I
				{1,1,1,1},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0}
			},
			{//1 J
				{0,1,0},
				{0,1,0},
				{1,1,0}
			},	
			{//2 L
				{0,1,0},
				{0,1,0},
				{1,1,0}
			},	
			{//3 T
				{0,1,0},
				{1,1,0},
				{0,1,0},
			},	
			{//4 O
				{1,1},
				{1,1}
			},	
			{//5 Z
				{1,1,0},
				{0,1,1},
				{0,0,0}
			},	
			{//6 S
				{0,1,1},
				{1,1,0},
				{0,0,0}
			}
	};
	
	List<square> squares = new ArrayList<square>();
	
	public Shape(int x, int y, int type, Tetris1 tc){
		this.x = x;
		this.y = y;
		this.type = type;
		this.color = colorArr[colorIndex];
		this.tc = tc;
		
		for(int i = 0; i<4; i++){
			squares.add(new square(color,tc));
		}
		createByType();
	}
	
	public Shape(int x, int y, int type, int nextTypeChangeStep,
			Tetris1 tc) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.color = colorArr[colorIndex];

		
		for(int i = 0; i<4; i++){
			squares.add(new square(Color.gray));
		}
		createByType();
		// TODO Auto-generated constructor stub
	}

	public void redraw(int x, int y, int type){
		this.x = x;
		this.y = y;
		this.type = type;
		this.color = colorArr[colorIndex];
		createByType();
	}
	
	public void createByType() {
		// TODO Auto-generated method stub
		int count = 0;
		square a = new square();
		for(int i = 0; i<data[type].length;i++){
			for(int j = 0;j<data[type][i].length;j++){
				if(data[type][i][j]==1){
					squares.get(count).setX(x+j*a.squareSize);
					squares.get(count).setY(y+i*a.squareSize);
					count++;
				}
			}
		}
	}
	
	
	public void draw(Graphics g){
		g.setColor(color);
		for(int i=0; i<squares.size(); i++){
			squares.get(i).drawSquare(g);
		}
	}
	
	public void drop(){
		square a = new square();
		y += a.fallSpeed; 
		for(int i=0; i<squares.size(); i++){
			squares.get(i).drop();
		}
	}
	
	public void changeStatus(){
		for(int i = 0; i < squares.size(); i++){
			square s = squares.get(i);
			s.changeStatus();
			if(s.stopped){
				stopped = true;
				for(int t=0; t<data[type].length; t++){
					int temp_height = y + t*s.squareSize;
					if(tc.isFull(temp_height)){
						tc.disappear(temp_height);
						tc.reloadSquares(temp_height);
					}
				}
				break;
			}
		}
		if(stopped){
			for(int i = 0; i<squares.size(); i++){
				squares.get(i).stopped = true;
			}
		}
		if(stopped && y < 50){
			gameOver = true;
			tc.gameOver = true;
		}
		
	}
	
	public void mousePressed(MouseEvent e){
		int mouse = e.getButton();
		if(mouse==MouseEvent.BUTTON1)
			if(!hitLeft())
				if(!paused)
					moveLeft();
		if(mouse==MouseEvent.BUTTON3)
			if(!hitRight())	
				if(!paused)	
					moveRight();
	}
	
	public void mouseWheelMoved(MouseWheelEvent e){
		if(e.getWheelRotation()==1)
			//System.out.println("ok");
			rotateClockwise();
		if(e.getWheelRotation()==-1)
			//System.out.println("ko");
			rotateCounterClockwise();
	}

	public void rotateCounterClockwise() {
		// TODO Auto-generated method stub
		square f = new square();
		//System.out.println("222");
		for(int i = 0; i<squares.size(); i++){
			square s = squares.get(i);
			int old_x = s.getX();
			int old_y = s.getY();
			int times_x = (old_x-x)/f.squareSize;
			int times_y = (old_y-y)/f.squareSize;
			s.setY((data[type].length-1 - times_x)*f.squareSize+y);
			s.setX(times_y*f.squareSize+x);
		}
		tc.repaint();
	}

	public void rotateClockwise() {
		// TODO Auto-generated method stub
		square f = new square();
		//System.out.println("111");
		for(int i = 0; i<squares.size(); i++){
			square s = squares.get(i);
			int old_x = s.getX();
			int old_y = s.getY();
			int times_x = (old_x-x)/f.squareSize;
			int times_y = (old_y-y)/f.squareSize;
			s.setX((data[type].length-1 - times_y)*f.squareSize+x);
			s.setY(times_x*f.squareSize+y);
		}
		
	}

	private void moveRight() {
		// TODO Auto-generated method stub
		square b = new square();
		x += b.squareSize;
		for(int i = 0; i<squares.size();i++){
			squares.get(i).moveRight();
		}
	}

	private void moveLeft() {
		// TODO Auto-generated method stub
		square b = new square();
		x -= b.squareSize;
		for(int i = 0; i<squares.size();i++){
			squares.get(i).moveLeft();
		}
	}
	
	public boolean hitLeft(){
		boolean b = false;
		for(int i=0; i<squares.size();i++){
			if(squares.get(i).hitLeft()){
				b = true;
				break;
			}
		}
		return b;
	}
	
	public boolean hitRight(){
		boolean b = false;
		for(int i=0; i<squares.size();i++){
			if(squares.get(i).hitRight()){
				b = true;
				break;
			}
		}
		return b;
	}
}
