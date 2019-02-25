package devoir;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Arene extends JPanel {
	
	
	
	public void paintComponent(Graphics g) {
		
		int x1 = this.getWidth()/4;
	    int y1 = this.getHeight()/4;                      
	    g.fillOval(0, 0, this.getWidth(), this.getHeight());
		
	}

}
