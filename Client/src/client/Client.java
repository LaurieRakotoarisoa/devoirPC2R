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
	private double _x;
	private double _y;
	
	
	public Client(Socket s) throws IOException {
		this.service = s;
		inchan = new BufferedReader(new InputStreamReader(service.getInputStream()));
		outchan = new DataOutputStream(s.getOutputStream());
	}
	
	public boolean connect(String nom) throws IOException {
		outchan.writeBytes("CONNECT/"+nom+"/"+"\n");
		outchan.flush();
		String s = inchan.readLine();
		String [] splitted = s.split("\\/");
		if(splitted[0].equals("DENIED")) {
			return false;
		}
		String [] coords = splitted[3].split("Y");
		_x = Integer.parseInt(coords[0].substring(1));
		_y = Integer.parseInt(coords[1]);
		this.nom = nom;
		return true;
	}
	
	public void changePos(double x, double y) throws IOException {
		String s = "NEWPOS/X"+x+"Y"+y;
		outchan.writeBytes(s);
		outchan.flush();
	}
	
	public void deconnexion() throws IOException {
		outchan.writeBytes("EXIT/"+nom+"/\n");
		outchan.flush();
		service.close();
	}
	
	public void envoiComm (double angle, int pousse) {
		String s = "";
	}
	
	public void ecoute() throws IOException {
		while(true) {
			System.out.println(inchan.readLine());
		}
	}

}
