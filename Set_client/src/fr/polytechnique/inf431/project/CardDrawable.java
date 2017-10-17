package fr.polytechnique.inf431.project;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * A drawable graphical representation of a Set! card.
 */
public class CardDrawable extends JButton implements MouseListener{
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
    private int card;
    private String card_name;
    private Color c ;
    public boolean isChosen=false;

    /**
     * Whether the card is selected, or in the middle of a transition
     * (valid or invalid).
     */
    private int selected;

    /**
     * Creates a drawable that draws the specified card.
     * @param card the card to draw
     */
    public CardDrawable(String name){
    	this.card=0;
    	this.setPreferredSize(new Dimension(Width, Height));
    	this.card_name=name;
//    	this.addMouseListener(this);
    }
    
    public CardDrawable(int card,String name) {
    	this.card_name=name;
        this.card = card;
        this.c=Color.white;
        this.setPreferredSize(new Dimension(Width, Height));
       // this.addMouseListener(this);
    }

    /**
     * @returns the card drawn by this drawable
     */
    public int getCard() {
        return card;
    }
    
    public String getName(){
    	return card_name;
    }

    /**
     * Sets the card to draw. If any view is displaying the drawable,
     * it should be invalidated.
     * @param card the card to draw
     */
    void setCard(int card) {
        this.card = card;
        this.c = Color.WHITE;
        this.repaint();
    }
    
    void resetCard(){
    	this.card=0;
    	this.repaint();
    }

    public void paintComponent(Graphics g){
    	
    	Graphics2D g2d = (Graphics2D) g;

    	if (card == 0)
            return;
    	
    	//g2d.setBackground(Color.DARK_GRAY);  
    	Rectangle b = new Rectangle();
    	b.setBounds(0, 0, Width, Height);
    	int left = b.x + 5;
    	int top = b.y + 5;
    	int width = Width;
    	int height = Height;
    	//int width = b.width - 10;
    	//int height = b.height - 10;
    	if (isChosen){
    		//g2d.setPaint(Color.orange);
    		this.setBorderPainted(true);
    		this.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
    	}
    	else{
    		this.setBorderPainted(false);
    		//g2d.setPaint(Color.white);
    	}
    	
    	g2d.setColor(this.c);
    	g2d.fillRect(left, top, width, height);
    	
    	switch (Cards.colorOf(card)) {
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
    	
    	int n = Cards.numberOf(card);
        float hPadding = width * SHAPE_PADDING;
        float vPadding = height * SHAPE_PADDING;
        float h = (height - (n + 1) * vPadding) / 3.0F;
        float t = top + (height - n * h - (n - 1) * vPadding) / 2.0F;
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
                                 Cards.fillingOf(card), Cards.shapeOf(card));
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
    
    public void mouseClicked(MouseEvent event){
    }
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
    public void mousePressed(MouseEvent event){
    }
    public void mouseReleased(MouseEvent event){}

}

