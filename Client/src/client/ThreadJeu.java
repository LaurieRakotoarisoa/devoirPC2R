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
				break;
			} catch (RestartSessionException e) {
				
				try {
					System.out.println("Nouvelle session");
					client.attenteDebut();
					
				} catch (IOException e1) {
					
					System.out.println("Erreur redemarrage client");
				}
			}
		}
		
		System.out.println("Fin thread jeu");
	}
	
	

}
