package fr.polytechnique.inf431.project;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;

import javax.swing.JButton;
import javax.swing.JLabel;

public class LimpidLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private static final int Width = 120;
    private static final int Height = 150;
    
    private Color c ;
    
    public LimpidLabel(){
    	this.c = Color.green;
    	this.setPreferredSize(new Dimension(Width, Height));
    }
    
    public void setColor(Color c){
    	this.c = c;
    }
	
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g.create();
        
        g2d.setComposite(AlphaComposite.SrcOver.derive(.3f)); 
        g2d.setColor(this.c);
        g2d.fillRect(0, 0, Width+10, Height+10); 
        g2d.dispose();
    }
	

}
