package clientB;

public class Vehicule {
	private	double positionX;
	private double positionY;
	private double vitesseX;
	private double vitesseY;
	private double direction = 0;
	private int pousse = 0;
	private double rayon;
	
	private final double turnit = 0.174533;
	private final double thrustit = 5;
	
	public Vehicule(double rayon) {
		this.rayon = rayon;
		vitesseX = 0;
		vitesseY = 0;
	}
	
	
	public synchronized void clock() {
		direction = direction - turnit;	
		System.out.println("clock "+direction);

	}
	
	public synchronized void anticlock() {
		direction = direction + turnit;
		System.out.println("anticlock "+direction);
	}
	
	public void thrust() {
		vitesseX = vitesseX +thrustit*Math.cos(direction);
		vitesseY = vitesseY + thrustit * Math.sin(direction);
	}
	
	public synchronized void setPosition(double x, double y) {
		positionX =x;
		positionY = y;
		
	}
	
	public synchronized double getPositionX() {
		return positionX;
	}
	
	public synchronized double getPositionY() {
		return positionY;
	}
	
	public void setVitesse(double vx, double vy) {
		vitesseX = vx;
		vitesseY = vy;
	}
	
	public void setDirection(double d) {
		direction = d;
	}
	
	public synchronized void pousse() {
		System.out.println("pousse "+pousse);
		pousse++;
		
	}
	
	public synchronized int getNbPousse() {
		return pousse;
	}
	public synchronized void reset() {
		pousse = 0;
		direction = 0;
	}
	
	public double getDirection() {
		return direction;
	}
	
	
	

}
