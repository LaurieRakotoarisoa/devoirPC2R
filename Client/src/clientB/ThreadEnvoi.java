package clientB;

import java.io.IOException;

public class ThreadEnvoi extends Thread{
	private ClientB client;

	public ThreadEnvoi(ClientB client) {
		this.client = client;
		
	}
	
	public void run() {
		while(true) {
			try {
				client.sendCommands();
			} catch (IOException e) {
				System.out.println("Erreur envoi commandes");
			}
			
			try {
				Thread.sleep((int)((1.0/client.getServerTickRate()*1000)));
			} catch (InterruptedException e) {
				System.out.println("Interruption thread envoi");
			}
		}
	}
}
