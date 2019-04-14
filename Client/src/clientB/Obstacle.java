package clientB;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Obstacle {
	
	private double x;
	private double y;
	
	public Obstacle(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(GraphicsContext gc, double centreX, double centreY) {
		gc.fillOval(centreX+x, centreY+y, 30.0,30.0);
		
	}

}
