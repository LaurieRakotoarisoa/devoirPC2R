package clientB;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import client.RestartSessionException;
import client.ThreadJeu;

public class ClientB{
	
	public String nom;
	private Socket service;
	private BufferedReader inchan;
	private DataOutputStream outchan;
	
	private Coords obj_coords = new Coords(); //Coordonnées de l'objectif
	private Coords my_coords = new Coords(); //Coordonnées du client 
	private Coords my_speed = new Coords(); // Vecteur vitesse
	private final double turnit=1;
	private final double thrustit=1;
	private double rotation = 0;
	private int poussees = 0;
	private double server_tickrate;
	
	private String phase; //phase de la session courante
	
	private double tickrate = 100.0;
	private Map<String,Coords> players = new HashMap<String, ClientB.Coords>(); //Nom des adversaires et leur position
	private Map <String,Integer> scores = new HashMap<String, Integer>(); // scores des adversaires
	
	public class Coords{ //Classe interne pour la gestion des coordonnées de joueurs
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
	
	
	public ClientB(Socket s) throws IOException {
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
		
		String [] scores = splitted[2].split("\\|");
		String score;
		String name;
		for(String sc : scores) {
			name = sc.split("\\:")[0];
			score = sc.split("\\:")[1];
			this.scores.put(name, Integer.parseInt(score));
		}
		
		return true;
	}
	
	public void attenteDebut() throws IOException {
		String line;
		while(phase.equals("attente")) {
			line = inchan.readLine();
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
	}
	
	public synchronized void changePos(int width,int height) throws IOException {
		my_coords.setX((my_coords._x+my_speed._x+width)%width);
		my_coords.setY((my_coords._y+my_speed._y+height)%height);
		
	}
	
	public void sendCommands() throws IOException {
		String s = "NEWCOM/A"+rotation+"T"+poussees+"/\n";
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
		rotation = rotation - turnit;
	}
	
	public synchronized void anticlock() {		
		rotation = rotation+ turnit;
	}
	
	public synchronized void thrust() {
		poussees+= thrustit;
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
	
	public double getTickRate() {
		return tickrate;
	}
	
	public double getServerTickRate() {
		return server_tickrate;
	}
	
	
	
	public void jeu() throws IOException, RestartSessionException {
		String line;
		line = inchan.readLine();
		String [] splitted = line.split("\\/");
		
		String command = splitted[0];
		
		String [] scores;
		
		switch(command) {
		
		
			case "TICK" : 
				String [] vcoords = splitted[1].split("\\|");
				for(String vc : vcoords) {
					String name = vc.split("\\:")[0];
					String data = vc.split("\\:")[1];
					if(!players.containsKey(name)) {
						players.put(name, new Coords());
					}
					Coords c = players.get(name);
					
					String vx ="";
					String vy = "";
					String theta = "";
					
				}
				
			case "NEWOBJ" : obj_coords.setCoords(splitted[1]); 
							scores = splitted[2].split("\\|");
							for(String name_score : scores) {
								String name = name_score.split("\\:")[0];
								String score = name_score.split("\\:")[1];
								this.scores.put(name, Integer.parseInt(score));
							}
							break;
							
			case "WINNER" : scores = splitted[1].split("\\|");
							System.out.println("Scores finaux session");
							for(String s : scores) {
								System.out.println(s.split("\\:")[0]+" "+s.split("\\:")[1]);
							}
							this.scores.clear();
							phase ="attente";
							
							throw new RestartSessionException("Fin de session  -> relancement");
			
			case "NEWPLAYER" : ajoutJoueur(splitted[1]); break;
			
			case "PLAYERLEFT" : retraitJoueur(splitted[1]); break;
			
			default : break;
							
		}
		
		
	}

}
