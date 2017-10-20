package graphic;

import java.awt.Color;
import java.awt.Graphics;


public class square {
	int x,y;
	public boolean stopped = false;
	private Color color = Color.black;
	private Tetris1 tc;

	public int fallSpeed = 30;
	public int squareSize = 30;
	
	public square(){}
	
	public square(Color color, Tetris1 tc){
		this.color = color;
		this.tc = tc;
		tc.ss.add(this);
	}
	
	public square(Color color){
		this.color = color;
	}
	
	public square(int x, int y,Color color){
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public void drawSquare(Graphics g){
		Color c = g.getColor();
		g.setColor(color);
		g.fillRect(x,y,squareSize,squareSize);
		//Color c = g.getColor();
		g.setColor(Color.black);
		g.drawRect(x,y,squareSize,squareSize);
		g.setColor(c);
	}
	
	public void drop(){
		y+=fallSpeed;
	}
	
	public void changeStatus(){
		//condition1
		if(y+squareSize>=581){
			stopped = true;
		}
		//condition2
		if(isHit()){
			stopped = true;
		}
	}
	
	public boolean isHit() {
		// TODO Auto-generated method stub
		for(int i=0; i<tc.ss.size();i++){
			square s = tc.ss.get(i);
			if(x == s.getX() && y + s.squareSize == s.getY() && s.stopped){
				return true;
			}
		}
		return false;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public void moveRight() {
		// TODO Auto-generated method stub
		x+=squareSize;
	}

	public void moveLeft() {
		// TODO Auto-generated method stub
		x-=squareSize;
	}

	public boolean hitLeft() {
		// TODO Auto-generated method stub
		for(int i=0; i<tc.ss.size(); i++){
			square s = tc.ss.get(i);
			//if(s.getX()==x-s.squareSize && s.getY() == y && s.stopped){
			if(x<=0){
				return true;
			}
		}
		return false;
	}
	
	public boolean hitRight() {
		// TODO Auto-generated method stub
		for(int i=0; i<tc.ss.size(); i++){
			square s = tc.ss.get(i);
			//if(s.getX()==x+s.squareSize && s.getY() == y && s.stopped){
			if(x>=270){
				return true;
			}
		}
		return false;
	}
}
