package interface_;

import java.io.IOException;

public class ThreadArene extends Thread{
	private Arene a;
	private double tickrate;
	
	public ThreadArene(Arene a,double t) {
		this.a = a;
		tickrate = t;
	}
	public void run() {
		
		/*while(true) {
			try {
				a.refresh();
			} catch (IOException e) {
				break;
			}
			try {
				Thread.sleep((int)((1/tickrate)*1000));
			} catch (InterruptedException e) {
				break;
			}
		}
		
		System.out.println("fin thread Arene");
		*/
		while(true) {
		}
	}

}
