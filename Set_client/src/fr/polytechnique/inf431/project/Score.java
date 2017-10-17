package fr.polytechnique.inf431.project;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * A drawable graphical representation of a Set! card.
 */
class Score extends JComponent {
    /**
     * Padding between shapes of a same card, as well as between
     * shapes and borders; expressed as a fraction of the bounding
     * dimension (width or height).
     */
    private static final float SHAPE_PADDING = 0.125F;

    /** Basic spacing step between concentric shapes. */
    private static final float CONCENTRIC_STEP = 6.0F;
    
    private static final int Width = 120;
    private static final int Height = 150;
   // private int x_pos;
   // private int y_pos;

    /** The card to draw. */
    private int card1;
    private int card2;
    private int card3;
    private int score;
    public Horloge horloge;
    private String time;

    /**
     * Whether the card is selected, or in the middle of a transition
     * (valid or invalid).
     */
    private int selected;
    
    public class Horloge extends Thread{
    	
    	public int count;
    	public int min;
    	public int sec;
    	
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
    		this.count = 0;
    		this.min = 0;
    		this.sec = 0;

    		while(mainFrame.restecard){
    		   time = this.toString();
     		   repaint();
     		   this.count++;
    		   this.sec++;
    		   if (this.sec == 60){
    			   this.min++;
    			   if (this.min == 60){
    				   this.min = 0;
    			   }
    			   this.sec = 0;
    		   }
    		   try {
    		    Thread.sleep(1000);
    		   } catch (InterruptedException e) {
    		    // TODO Auto-generated catch block
    			   e.printStackTrace();
    		   }
    		   }
    	}
    	
    	public String toString(){
    		String s = "";
    		if (this.min < 10){
    			s += "0" + this.min;
    		}
    		else {
    			s += this.min;
    		}
    		s += " : ";
    		if (this.sec < 10){
    			s += "0" + this.sec;
    		}
    		else {
    			s += this.sec;
    		}
    		return s;
    	}

    }

    /**
     * Creates a drawable that draws the specified card.
     * @param card the card to draw
     */
    Score(int card1, int card2, int card3, int score) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.score = score;
        this.horloge = new Horloge();
//        horloge.start();
        this.setPreferredSize(new Dimension(Width, Height));
    }
    
    public void win_ScoreCard(int card1,int card2,int card3){
    	this.card1=card1;
    	this.card2=card2;
    	this.card3=card3;
    	this.score++;
    	this.repaint();
    }

    public void loss_ScoreCard(){
    	this.score--;
    	this.repaint();
    }


    public void paintComponent(Graphics g){
    	
    	Graphics2D g2d = (Graphics2D) g.create();
    	super.paintComponent(g2d); 
    	
    	Rectangle b = new Rectangle();
    	b.setBounds(0, 0, Width, Height);
    	int left = b.x + 5;
    	int top = b.y + 5;
    	int width = Width;
    	int height = Height;
    	//int width = b.width - 10;
    	//int height = b.height - 10;
    	g2d.setColor(Color.white);
    	g2d.fillRect(left, top, width, height);
    	
    	g2d.setColor(Color.BLACK);
    	g2d.setFont(new Font("Arial",Font.BOLD,15));
    	g2d.drawString(("Time: " + this.time), 20, 30);
    	g2d.drawString(("Score: "+this.score), 35, 50);

    	
    	if (card1 != 0 && card2 != 0 && card3 != 0){
	    	width = Width / 3;
	    	height = Height - 50;
	    	
	    	switch (Cards.colorOf(card1)) {
	        case 1:
	            g2d.setColor(Color.red);
	            break;
	        case 2:
	            g2d.setColor(Color.blue);
	            break;
	        case 3:
	            g2d.setColor(Color.green);
	            break;
	        default:
	            throw new IllegalStateException("Illegal color characteristic.");
	        }
	    	
	    	int n = Cards.numberOf(card1);
	        float hPadding = width * SHAPE_PADDING;
	        float vPadding = height * SHAPE_PADDING;
	        float h = (height - (n + 1) * vPadding) / 3.0F;
	        float t = top +50+ (height - n * h - (n - 1) * vPadding) / 2.0F;
	        for (int i = 0; i < n; ++i) {
	            /*
	             * Reset the Rect r as it may have been overriden in
	             * drawShape(). We also keep the variable t local for the
	             * same reason.
	             */
	            float newleft = left + hPadding;
	            float newright = left+width - hPadding;
	            float newtop = t;
	            float newbottom = t + h;
	            Rectangle r = new Rectangle();
	            r.setBounds((int) newleft, (int) newtop, (int) (newright-newleft), (int) (newbottom-newtop));
	            t = r.y+r.height + vPadding;
	            drawShapeWithFilling(g2d, r, 
	                                 Cards.fillingOf(card1), Cards.shapeOf(card1));
	        }
	        
	        switch (Cards.colorOf(card2)) {
	        case 1:
	            g2d.setColor(Color.red);
	            break;
	        case 2:
	            g2d.setColor(Color.blue);
	            break;
	        case 3:
	            g2d.setColor(Color.green);
	            break;
	        default:
	            throw new IllegalStateException("Illegal color characteristic.");
	        }
	    	
	    	n = Cards.numberOf(card2);
	    	h = (height - (n + 1) * vPadding) / 3.0F;
	        t = top +50+ (height - n * h - (n - 1) * vPadding) / 2.0F;
	        for (int i = 0; i < n; ++i) {
	            /*
	             * Reset the Rect r as it may have been overriden in
	             * drawShape(). We also keep the variable t local for the
	             * same reason.
	             */
	            float newleft = left + width + hPadding;
	            float newright = left+ 2*width - hPadding;
	            float newtop = t;
	            float newbottom = t + h;
	            Rectangle r = new Rectangle();
	            r.setBounds((int) newleft, (int) newtop, (int) (newright-newleft), (int) (newbottom-newtop));
	            t = r.y+r.height + vPadding;
	            drawShapeWithFilling(g2d, r, 
	                                 Cards.fillingOf(card2), Cards.shapeOf(card2));
	        }
	        
	        switch (Cards.colorOf(card3)) {
	        case 1:
	            g2d.setColor(Color.red);
	            break;
	        case 2:
	            g2d.setColor(Color.blue);
	            break;
	        case 3:
	            g2d.setColor(Color.green);
	            break;
	        default:
	            throw new IllegalStateException("Illegal color characteristic.");
	        }
	    	
	    	n = Cards.numberOf(card3);
	    	h = (height - (n + 1) * vPadding) / 3.0F;
	        t = top +50+ (height - n * h - (n - 1) * vPadding) / 2.0F;
	        for (int i = 0; i < n; ++i) {
	            /*
	             * Reset the Rect r as it may have been overriden in
	             * drawShape(). We also keep the variable t local for the
	             * same reason.
	             */
	            float newleft = left + 2*width + hPadding;
	            float newright = left+ 3*width - hPadding;
	            float newtop = t;
	            float newbottom = t + h;
	            Rectangle r = new Rectangle();
	            r.setBounds((int) newleft, (int) newtop, (int) (newright-newleft), (int) (newbottom-newtop));
	            t = r.y+r.height + vPadding;
	            drawShapeWithFilling(g2d, r, 
	                                 Cards.fillingOf(card3), Cards.shapeOf(card3));
	        }
    	}
    }
    	
    	
    /**
     * Draws a single shape with the specified filling.
     * @param canvas the canvas on which to draw
     * @param filling the filling characteristic to draw
     * @param shape the shape characteristic to draw
     */
    private void drawShapeWithFilling(Graphics2D g2d, Rectangle r, int filling, int shape) {
    	//int left = r.x;
		int width = r.width;
		//int top = r.y;
		int height = r.height;
    	switch (filling) {
        case 1:
        	drawShape(g2d, r, shape);
            break;
        case 2:
            /*
             * For intermediate filling, we draw concentric copies of
             * the same shape.
             */
            float w = width / 2.0F;
            float u = CONCENTRIC_STEP * (height / width);
            Rectangle rc = new Rectangle(r);
            for (float i = 0; i < w; i += CONCENTRIC_STEP) {
                drawShape(g2d, rc, shape);
                rc.x += CONCENTRIC_STEP;
                rc.width -= 2*CONCENTRIC_STEP;
                rc.y += u;
                rc.height -= 2*u;
            }
            break;
        case 3:
            fillShape(g2d, r, shape);
            break;
        default:
            throw new IllegalArgumentException(
                "Illegal filling characteristic.");
        }
    }

    /**
     * Draws a single shape.
     * @param canvas the canvas on which to draw
     * @param shape the shape characteristic to draw
     */
    private void drawShape(Graphics2D g2d, Rectangle r, int shape) {
    	int left = r.x;
		int width = r.width;
		int top = r.y;
		int height = r.height;
    	switch (shape){
    	case 1:
    		g2d.drawOval(left, top, width, height);
    		break;
    	case 2:
    		g2d.drawRect(left, top, width, height);
    		break;
    	case 3:
    		drawDiamond(g2d, left, top, width, height);
    		break;
        default:
            throw new IllegalArgumentException(
                "Illegal shape characteristic.");
        }
    }
    
    private void fillShape(Graphics2D g2d, Rectangle r, int shape) {
    	int left = r.x;
		int width = r.width;
		int top = r.y;
		int height = r.height;
    	switch (shape){
    	case 1:
    		g2d.fillOval(left, top, width, height);
    		break;
    	case 2:
    		g2d.fillRect(left, top, width, height);
    		break;
    	case 3:
    		fillDiamond(g2d, left, top, width, height);
    		break;
        default:
            throw new IllegalArgumentException(
                "Illegal shape characteristic.");
        }
    }

    /**
     * Draws a diamond shape within the specified rectangle.
     * @param canvas the canvas on which to draw
     */
    private void drawDiamond(Graphics2D g2d, int left, int top, int width, int height) {
        int[] xpoints = new int[]{left,left+width/2,left+width,left+width/2};
        int[] ypoints = new int[]{top+height/2,top,top+height/2,top+height};
    	g2d.drawPolygon(xpoints, ypoints, 4);
    }
    
    private void fillDiamond(Graphics2D g2d, int left, int top, int width, int height) {
        int[] xpoints = new int[]{left,left+width/2,left+width,left+width/2};
        int[] ypoints = new int[]{top+height/2,top,top+height/2,top+height};
    	g2d.fillPolygon(xpoints, ypoints, 4);
    }

}
