package client;

import java.io.IOException;

public class ThreadJeu extends Thread{
	
	private Client client;
	
	public ThreadJeu(Client c) {
		client = c;
	}
	
	@Override
	public void run() {
		System.out.println("thread jeu running");
		while(true) {
			try {
				client.jeu();
			} catch (IOException e) {
				System.out.println("pb thread de jeu");
				break;
			}
		}
		
		System.out.println("Fin thread jeu");
	}
	
	

}
