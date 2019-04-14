package clientB;

import java.io.IOException;

public class ThreadArene extends Thread{
	private Arene a;
	private double refresh_tickrate;
	
	public ThreadArene(Arene a,double t) {
		this.a = a;
		refresh_tickrate = t;
	}
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			try {
				a.refresh();
				Thread.sleep((int)((1.0/refresh_tickrate)*1000));

			} catch (InterruptedException e) {
				System.out.println("Interruption thread Arene");
				break;
			} catch (IOException e) {
				System.out.println("erreur I/O thread Arene");
				break;
				
			}
		}
	}

}
