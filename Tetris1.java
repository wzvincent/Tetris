package graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Tetris1 extends Frame{
	//boolean changeM = false;
	//boolean changeN = false;
	//boolean changeS = false;
	boolean cursor = true;
	boolean gameOver = false;
	public int N = 20;
	public double S = 0.1;
	public int M = 1;
	public int level = 1;
	public int lines, score = 0;
	public double speed = 1;
	public int xP, xQ, yP, yQ, xS, yS;
	
	
	Point mouse = new Point();
	public static Random r = new Random();
	int nextType;
	int nextTypeChangeStep;
	Shape nextShape = null;
	Shape changeShape = null;
	//mouseMonitor monitor=new mouseMonitor();
	//Shape s = new Shape(150,28,4,null,this);
	List<square>ss = new ArrayList<square>();
	
	Shape s = null;
	Parameter pa = new Parameter();

	public void lancher(){
		//this.setLocation(x, y);
		this.setSize(600,600);
		this.setTitle("Tetris Game");
		this.setVisible(true);
		this.addMouseListener(new mouseMonitor());
		this.addMouseMotionListener(new mouseMonitor());
		this.addMouseWheelListener(new MouseWheelEventDemo());
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				//System.exit(0);
			}
			
		});
		
		s = new Shape(150,-2,1,Tetris1.this);
		nextType = r.nextInt(7);
		nextTypeChangeStep = r.nextInt(4);
		nextShape = new Shape(390,80, nextType, nextTypeChangeStep, this);
		Parameter i = new Parameter();
		
		S = 0.1*(i.jcb3.getSelectedIndex()+1);
        M = i.jcb1.getSelectedIndex()+1;
        N = i.jcb2.getSelectedIndex()*5+20;
		
		
		new Thread(new paintThread()).start();
		
		
		
	}
	
	public void paint(Graphics g){
		//Parameter j = new Parameter();
		
        if(xP<=300){
			s.paused = true;
			//System.out.println(xP);
		}
        square h = new square();
		if(cursor && s.x<=xP && xP<= s.x + h.squareSize*3 && s.y<=yP && yP<=s.y + h.squareSize*3){
			s.redraw(s.x, s.y, r.nextInt(7));
			score -= level*M; 
			cursor = !cursor;
			//break;
		}
        if(s.x>xP || xP> s.x + h.squareSize*3 || s.y>yP || yP>s.y + h.squareSize*3)
        	cursor = !cursor;
        
		if(xP>300){
			s.paused = false;
		}
		if(s.paused){
			Color q = g.getColor();
			g.setColor(Color.BLUE);
			g.drawString("PAUSE", 150, 300);
			g.setColor(q);
		}
		if(xQ>400 && xQ<500 && yQ>550 && yQ<590){
			System.exit(0);
			}
		g.drawRect(0, 1, 300, 598);
		g.drawRect(390, 50, 120, 120);
		g.drawRect(400, 550, 100, 40);
		g.setFont(new Font("黑体",Font.BOLD,20));
		g.drawString("Level:       "+level, 370, 250);
		g.drawString("Lines:       "+lines, 370, 280);
		g.drawString("Score:       "+score, 370, 310);
		g.drawString("QUIT", 430, 575);
		
		s.draw(g);
		
		s.changeStatus();
		for(int i = 0; i<ss.size();i++){
			ss.get(i).drawSquare(g);
		}
		if(nextShape!=null)
			nextShape.draw(g);
		//g.drawString(nextType+"", 330, 130);
		if(gameOver){
			Font f = g.getFont();
			g.setFont(new Font("黑体",Font.BOLD,40));
			g.drawString("Game Over!!", 230, 290);
			g.setFont(f);
		}
	}

	
	public static void main(String[] args){
		new Tetris1().lancher();
	}
	
	private class paintThread implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			while(true){
				repaint();
				if(!gameOver){
				if(s.stopped){
					s = null;
					
					s = new Shape(150,-2,nextType,Tetris1.this);
					nextType = r.nextInt(7);
					for(int i = 0;i<nextTypeChangeStep; i++){
						s.rotateClockwise();
					}
					nextTypeChangeStep = r.nextInt(4);
					if(nextShape == null){
						nextShape = new Shape(390,80,nextType, nextTypeChangeStep,Tetris1.this);
					}else{
						nextShape = null;
						nextShape = new Shape(390,80,nextType, nextTypeChangeStep,Tetris1.this);
					}
				}
				if(!s.stopped&&!s.paused)
					s.drop();
				}
				try {
					Thread.sleep((long) (400/speed));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public class mouseMonitor extends MouseAdapter{
		//Point mouse;
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			xP = e.getX();
			yP = e.getY();
			mouse = new Point((float)xP, (float)yP);
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			//System.out.println("ok");
			s.mousePressed(e);
			xQ = e.getX(); yQ = e.getY();
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}
	
	public class MouseWheelEventDemo implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e){
			s.mouseWheelMoved(e);
		}
	}
	
	public boolean isFull(int height){
		int count = 0;
		for(int i=0; i<ss.size(); i++){
			square s = ss.get(i);
			if(s.getY()==height){
				count++;
			}
		}
		if(count==10){
			return true;
		}else{
			return false;
		}
	}
	
	public void disappear(int height){
		Iterator it = ss.iterator();
		while(it.hasNext()){
			square s = (square)it.next();
			if(s.getY()==height){
				it.remove();
			}
		}
		lines++;
		score = score + level*M;
		if(lines>=N){
			level++;
			speed = speed*(1+level*S);
			lines = 0;
		}
	}
	
	public void reloadSquares(int height){
		Iterator it = ss.iterator();
		while(it.hasNext()){
			square s = (square)it.next();
			if(s.getY()<height){
				s.setY(s.getY() + s.squareSize);
			}
		}
	}
	
	
	
	public boolean insidePolygon(Point p, Point[] pol) {
	   int n=pol.length,j=n-1;
	   boolean b = false;
	   //double x = p.getX();
	   double y = p.y;
	   for (int i=0; i<n; i++){
		   if (pol[j].y <= y && y < pol[i].y && Tools2D.area2(pol[j], pol[i], p) > 0 || 
			   pol[i].y <= y && y < pol[j].y && Tools2D.area2(pol[i], pol[j], p) > 0) b = !b;
		   j = i; 
	   }
	   return b; 
	}
	
	public class Parameter extends JFrame{
		JList jlist;    //列表框  
	    JComboBox jcb1,jcb2,jcb3;  //下拉框  
	    JPanel jp1, jp2, jp3;    //面板  
	    JLabel jlb1, jlb2, jlb3;  
	    JScrollPane jsp;    //滚动控件  
	      
	    //构造函数  
	    public Parameter(){  
	        jp1 = new JPanel();  
	        jp2 = new JPanel(); 
	        jp3 = new JPanel();
	          
	        jlb1 = new JLabel("scoring factor：");  
	        String str1[] = {"1","2","3", "4","5","6","7","8","9","10"};  
	        jcb1 = new JComboBox(str1);  
	          
	        jlb2 = new JLabel("difficulty：");  
	        String str2[] = {"20", "25", "30", "35", "40", "45", "50"};  
	        jcb2 = new JComboBox(str2);  
	        
	        jlb3 = new JLabel("speed factor: ");
	        String str3[] = {"0.1","0.2","0.3","0.4","0.5","0.6","0.7","0.8","0.9","1.0"};
	        jcb3 = new JComboBox(str3);
	         
	        //jsp = new JScrollPane(jlist);  
	          
	        jp1.add(jlb1);  
	        jp1.add(jcb1);  
	          
	        jp2.add(jlb2);  
	        jp2.add(jcb2);
	        
	        jp3.add(jlb3);
	        jp3.add(jcb3);
	        
	        //int  = jcb1.getSelectedIndex();
	           
	        this.setLayout(new GridLayout(3, 1));  
	          
	        this.add(jp1);  
	        this.add(jp2);
	        this.add(jp3);
	          
	        this.setSize(200,200);  
	        this.setBounds(650, 350, 200, 200);
	        this.setTitle("Parameter Setting");  
	        this.setVisible(true);  
	        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    }  
	}
}

