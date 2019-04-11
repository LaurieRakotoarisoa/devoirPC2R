package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import interface_.InterfaceClient;
import javafx.application.Application;

public class ClientMain {
	public static void main(String [] args) throws UnknownHostException, IOException {
		Client c = new Client(new Socket("127.0.0.1",12345));
		c.connect("laurie");
		c.attenteDebut();
		while(true) {
			try {
				c.jeu();
			} catch (RestartSessionException e) {
				System.out.println("restart");
			}
	
		}
	}

}
