package clientB;

import java.io.IOException;

public class ThreadEnvoi extends Thread {
	private ClientB c;
	
	public ThreadEnvoi(ClientB client) {
		c = client;
	}
	
	@Override
	public void run() {
		while(true) {
			synchronized (c.declenche) {
				while(c.phase.equals("attente")) {
					try {
						System.out.println("attente envoi");
						c.declenche.wait();
					} catch (InterruptedException e) {
						break;
					}
				}
				try {
					c.newComm();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						break;
					}
				} catch (IOException e) {
					break;
				}
		
			
		}
		}
	}
	
	

}
