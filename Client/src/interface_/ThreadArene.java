package interface_;

import java.io.IOException;

public class ThreadArene extends Thread{
	private Arene a;
	private int tickrate;
	
	public ThreadArene(Arene a,int t) {
		this.a = a;
	}
	public void run() {
		
		while(true) {
			try {
				a.refresh();
			} catch (IOException e) {
				break;
			}
			try {
				Thread.sleep((int)((1/100.0)*1000));
			} catch (InterruptedException e) {
				break;
			}
		}
		
		System.out.println("fin thread Arene");
		
	}

}
