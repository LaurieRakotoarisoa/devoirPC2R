package interface_;

import java.io.IOException;

public class ThreadArene extends Thread{
	private Arene a;
	
	public ThreadArene(Arene a) {
		this.a = a;
	}
	public void run() {
		
		while(true) {
			try {
				a.refresh();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep((int)((1/100.0)*1000));
			} catch (InterruptedException e) {
				break;
			}
		}
		
	}

}
