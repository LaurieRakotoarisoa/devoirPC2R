package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {
	
	private String nom;
	private Socket service;
	private BufferedReader inchan;
	private DataOutputStream outchan;
	
	private Coords obj_coords = new Coords();
	private Coords my_coords = new Coords();
	private Coords my_speed = new Coords();
	private double angle = 0;
	private final double turnit=1;
	private final double thrustit=1;
	
	private String phase;
	private Map<String,Coords> players = new HashMap<String, Client.Coords>();
	private Map <String,Integer> scores = new HashMap<String, Integer>();
	
	public class Coords{
		private double _x;
		private double _y;
		
		public double getX() {
			return _x;
		}
		
		public double getY() {
			return _y;
		}
		
		public void setX(double n) {
			_x = n;
		}
		public void setY(double n) {
			_y = n;
		}
		
		protected void setCoords(String s) {
			String [] splitted = s.split("Y");
			_x = Double.parseDouble(splitted[0].substring(1));
			_y = Double.parseDouble(splitted[1]);
		}
		
		@Override
		public String toString() {
			return "X"+_x+"Y"+_y;
		}
	}
	
	
	public Client(Socket s) throws IOException {
		this.service = s;
		inchan = new BufferedReader(new InputStreamReader(service.getInputStream()));
		outchan = new DataOutputStream(s.getOutputStream());
		my_speed.setX(0);
		my_speed.setY(0);
	}
	
	public boolean connect(String nom) throws IOException {
		outchan.writeBytes("CONNECT/"+nom+"/"+"\n");
		outchan.flush();
		String s = inchan.readLine();
		String [] splitted = s.split("\\/");
		if(splitted[0].equals("DENIED")) {
			return false;
		}
		phase = splitted[1];
		if(!splitted[3].equals("")) {
			obj_coords.setCoords(splitted[3]);
		}
		this.nom = nom;
		return true;
	}
	
	public void attenteDebut() throws IOException {
		String line;
		while(phase.equals("attente")) {
			System.out.println("boucle");
			line = inchan.readLine();
			System.out.println(line);
			String [] splitted = line.split("\\/");
			switch(splitted[0]) {
			case "NEWPLAYER" : ajoutJoueur(splitted[1]); break;
			case "PLAYERLEFT" : retraitJoueur(splitted[1]); break;
			case "SESSION" : String [] players = splitted[1].split("\\|");
			for(String p : players) {
				String user = p.split("\\:")[0];
				String coords = p.split("\\:")[1];
				if(user.equals(nom)) {
					my_coords.setCoords(coords);
					continue;
				}
				this.players.put(user, new Coords());
				this.players.get(user).setCoords(coords);
			}
			obj_coords.setCoords(splitted[2]); //vérification coordonnées
			phase = "jeu";
			break;
			default : break;
			}
		}
		for(String p : players.keySet()){
			System.out.println("joueur "+p+" "+players.get(p));
		}
	}
	
	public synchronized void changePos() throws IOException {
		my_coords.setX(my_coords._x+my_speed._x);
		my_coords.setY(my_coords._y+my_speed._y);
		String s = "NEWPOS/X"+my_coords._x+"Y"+my_coords._y;
		outchan.writeBytes(s);
		outchan.flush();
	}
	
	public void ajoutJoueur(String name) {
		players.put(name, new Coords());
		scores.put(name, 0);
	}
	
	public void retraitJoueur(String name) {
		players.remove(name);
		scores.remove(name);
	}
	
	public void deconnexion() throws IOException {
		outchan.writeBytes("EXIT/"+nom+"/\n");
		outchan.flush();
		service.close();
	}
	
	public synchronized void clock() {
		angle = angle - turnit;
		my_speed.setX(thrustit*Math.cos(angle));
		my_speed.setY(thrustit*Math.sin(angle));
	}
	
	public synchronized void anticlock() {
		angle = angle +turnit;
		my_speed.setX(thrustit*Math.cos(angle));
		my_speed.setY(thrustit*Math.sin(angle));
	}
	
	public synchronized void thrust() {
		my_speed.setX(my_speed._x + thrustit*Math.cos(angle));
		my_speed.setY(my_speed._y + thrustit*Math.sin(angle));
	}
	
	
	public Map<String, Coords> getPlayers(){
		return players;
	}
	
	public Coords getPosition() {
		return my_coords;
	}
	
	public Coords getObjectif() {
		return obj_coords;
	}

}
