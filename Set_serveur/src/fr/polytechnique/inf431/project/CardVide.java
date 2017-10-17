package fr.polytechnique.inf431.project;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * A drawable graphical representation of a Set! card.
 */
class CardVide extends JButton {
    
    private static final int Width = 120;
    private static final int Height = 150;
    
    
    CardVide() {
    	this.setBounds(0, 0, Width+10, Height+10);
        //this.setVisible(true);
    }

    public void paint(Graphics g){
    	return;
    }

}

