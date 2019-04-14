package clientB;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Balle {
	
	private double x;
	private double y;
	
	public Balle(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void drawBalle(GraphicsContext gc, double centreX, double centreY) {
		gc.setFill(Color.GREY);
		gc.fillOval(centreX+x, centreY+y, 10.0,10.0);
	}

}
