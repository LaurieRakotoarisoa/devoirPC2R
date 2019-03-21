package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	
	private String nom;
	private Socket service;
	private BufferedReader inchan;
	private DataOutputStream outchan;
	
	
	public Client(Socket s) throws IOException {
		this.service = s;
		inchan = new BufferedReader(new InputStreamReader(service.getInputStream()));
		outchan = new DataOutputStream(s.getOutputStream());
	}
	
	public boolean connect(String nom) throws IOException {
		outchan.writeBytes("CONNECT/"+nom+"/"+"\n");
		outchan.flush();
		String s = inchan.readLine();
		if(s.split("\\/")[0].equals("DENIED")) {
			return false;
		}
		this.nom = nom;
		return true;
	}
	
	public void deconnexion() throws IOException {
		outchan.writeBytes("EXIT/"+nom+"/\n");
		outchan.flush();
	}
	
	public void ecoute() throws IOException {
		while(true) {
			System.out.println(inchan.readLine());
		}
	}

}
