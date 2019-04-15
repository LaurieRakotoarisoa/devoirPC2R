package clientB;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ClientB extends Thread{
	
	public String nom;
	private Socket service;
	private BufferedReader inchan;
	private DataOutputStream outchan;
	
	public double refresh_tickrate = 100;
	
	public String phase = "attente";
	
	private double objectifX;
	private double objectifY;
	
	private Map<String,Vehicule> players = new HashMap<String,Vehicule>();
	private List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private List<Obstacle> bombes = new ArrayList<Obstacle>();
	private List<Balle> tirs = new ArrayList<Balle>();
	private ObservableList<String> playersList = FXCollections.observableArrayList();
	
	public final Object sc_objectif = new Object();
	public final Object sc_players = new Object();
	public final Object sc_bombes = new Object();
	public final Object sc_tirs = new Object();
	
	public final Object declenche = new Object();
	
	private ThreadEnvoi te;
	
	private Chat chat;
	
		
	public ClientB(Socket s) {
		this.service = s;
		try {
			inchan = new BufferedReader(new InputStreamReader(service.getInputStream()));
			outchan = new DataOutputStream(s.getOutputStream());
		}catch (IOException e) {
			System.out.println("Erreur création canaux lecture/écriture");
		}
		
	}
	
	public ClientB() {
		
	}
	
	public void setClient(Socket s) throws IOException {
		service = s;
		inchan = new BufferedReader(new InputStreamReader(service.getInputStream()));
		outchan = new DataOutputStream(s.getOutputStream());
	}
	
	public void setChat(Chat c) {
		chat = c;
	}
	
	
	
	public boolean connexion(String name) throws IOException {
		outchan.writeBytes("CONNECT/"+name+"/\n");
		outchan.flush();
		
		String [] reponse = inchan.readLine().split("\\/");
		
		if(reponse[0].equals("WELCOME")) {
			this.nom = name;
			System.out.println("Scores actuels :");
			affiche_scores(reponse[2]);
			phase = "attente";
			String objectif = reponse[3];
			String x = objectif.split("Y")[0];
			String y = objectif.split("Y")[1];
			setObjectif(Double.parseDouble(x.substring(1)),Double.parseDouble(y));
			if(reponse.length >= 5  && !(reponse[4].equals(""))) {
				setObstacles(reponse[4].split("\\|"));
			}
			te = new ThreadEnvoi(this);
			te.start();
			return true;
		}
		
		else {
			return false;
		}
		
	}
	
	public void newPlayer(String name) {
		synchronized(sc_players) {
			players.put(name, new Vehicule());
		}
		playersList.add(name);
	}
	
	public void exit() throws IOException {
		outchan.writeBytes("EXIT/"+nom+"/\n");
		outchan.flush();
		
	}
	
	public void playerLeft(String name) {
		synchronized (sc_players) {
			players.remove(name);
			
		}
		playersList.remove(name);
	}
	
	public void session(String coords_joueurs, String coords_objectif) {
		String [] cj = coords_joueurs.split("\\|");
		synchronized(sc_players) {
			for(String s : cj) {
				String name = s.split("\\:")[0];
				String position = s.split("\\:")[1];
				String positionX = position.split("Y")[0].substring(1);
				String positionY = position.split("Y")[1];
				if(!players.containsKey(name)) {
					playersList.add(name);
					players.put(name, new Vehicule());
				}
				players.get(name).setPosition(Double.parseDouble(positionX), Double.parseDouble(positionY));
			}
		}
		String x = coords_objectif.split("Y")[0];
		String y = coords_objectif.split("Y")[1];
		setObjectif(Double.parseDouble(x.substring(1)),Double.parseDouble(y));
		System.out.println("objectif "+x+" "+y);
		
		
		synchronized (declenche) {
			phase = "jeu";
			declenche.notifyAll();
		}
	}
	
	public void winner(String scores) {
		System.out.println("Scores en fin de session :");
		affiche_scores(scores);
		synchronized (declenche) {
			phase = "attente";
		}
		
	}
	
	public void newComm() throws IOException {
		Vehicule v = players.get(nom);
		double a = v.getMyMove();
		int t = v.getNbPousse();
		//System.out.println("NEWCOM/A"+a+"T"+t+"/");
		outchan.writeBytes("NEWCOM/A"+a+"T"+t+"/\n");
		outchan.flush();
		

		getMyVehicule().reset();
	}
	
	public void tick(String [] vcoords_joueurs) {

		for(String s : vcoords_joueurs) {
			String name = s.split("\\:")[0];
			String data = s.split("\\:")[1];
			String position = data.split("VX")[0];
			try {
			double positionX = Double.parseDouble(position.split("Y")[0].substring(1));
			double positionY = Double.parseDouble(position.split("Y")[1]);
			String vitesse = data.split("VX")[1].split("T")[0];
			double vitesseX = Double.parseDouble(vitesse.split("VY")[0]);
			double vitesseY = Double.parseDouble(vitesse.split("VY")[1]);
			String direction = data.split("VX")[1].split("T")[1];
			
			/*System.out.println(name);
			System.out.println("pos "+positionX+" "+positionY);
			System.out.println(vitesseX+" "+vitesseY);
			System.out.println(direction);*/
			synchronized(sc_players) {
				if(!players.containsKey(name)) {
					players.put(name, new Vehicule());
				}
				
				players.get(name).setVitesse(vitesseX,vitesseY);
				players.get(name).setDirection(Double.parseDouble(direction));
				players.get(name).setPosition(positionX, positionY);
			}
			}catch(NumberFormatException e) {
				System.out.println("erreur lecture données");
				System.exit(-1);
			}
		}
		if(phase.equals("attente")) {
			phase = "jeu";
			synchronized (declenche) {
				declenche.notifyAll();
			}
		}
		
	}
	
	public void newObj(String coord,String scores) {
		double positionX = Double.parseDouble(coord.split("Y")[0].substring(1));
		double positionY = Double.parseDouble(coord.split("Y")[1]);
		setObjectif(positionX,positionY);
		System.out.println("Rappel des scores :");
		affiche_scores(scores);
	}
	
	public void affiche_scores(String scores) {
		String [] sc = scores.split("\\|");
		for(String s : sc) {
			String name = s.split("\\:")[0];
			String score = s.split("\\:")[1];
			System.out.println(name+"\t"+score);
		}
	}
	
	
	
	public void setObjectif(double x, double y) {
		synchronized(sc_objectif) {
			objectifX = x;
			objectifY = y;
		}
	}
	
	public void setObstacles(String [] obstacles) {
		for(String s : obstacles) {
			double ox = Double.parseDouble(s.split("Y")[0].substring(1));
			double oy = Double.parseDouble(s.split("Y")[1]);
			this.obstacles.add(new Obstacle(ox, oy));
		}
	}
	
	public double getObjectifX() {
		return objectifX;
	}
	
	public double getObjectifY() {
		return objectifY;
	}
	
	
	public Vehicule getMyVehicule() {
		synchronized (sc_players) {
			return players.get(nom);
		}
	}
	
	public Map<String,Vehicule> getVehicules(){
		Map <String,Vehicule> v;
		synchronized (sc_players) {

			v = new HashMap<String, Vehicule>(players);
			
		}
		v.remove(nom);
		return v;
	}
	
	
	public List<Obstacle> getObstacles(){
		return obstacles;
	}
	
	public void poserBombe() throws IOException {
		System.out.println("bombe");
		outchan.writeBytes("ENVOIBOMBE/\n");
		outchan.flush();
	}
	
	public void sendMessage(String txt) throws IOException {
		outchan.writeBytes("ENVOI/"+txt+"/\n");
		outchan.flush();
	}
	
	public void sendPMessage(String txt, String user) throws IOException {
		outchan.writeBytes("PENVOI/"+txt+"/"+user+"/\n");
		outchan.flush();
	}
	
	public void receiveMessage(String msg) {
		Platform.runLater(()->chat.receiveMessage(msg));
	}
	
	public void receivePMessage(String msg,String usr) {
		Platform.runLater(()->chat.receivePMessage(msg,usr));
	}
	
	public void setBombes(String [] bombes) {
		synchronized (sc_bombes) {
			this.bombes =  new ArrayList<Obstacle>();
			for(String s : bombes) {
				double ox = Double.parseDouble(s.split("Y")[0].substring(1));
				double oy = Double.parseDouble(s.split("Y")[1]);
				this.bombes.add(new Obstacle(ox, oy));
			}	
		}
	}
	
	public List<Obstacle> getBombes(){
		synchronized (sc_bombes) {
			return bombes;
		}
	}
	
	public void setTirs(String [] tirs) {
		synchronized (sc_tirs) {
			this.tirs =  new ArrayList<Balle>();
			for(String s : tirs) {
				double ox = Double.parseDouble(s.split("Y")[0].substring(1));
				double oy = Double.parseDouble(s.split("Y")[1]);
				this.tirs.add(new Balle(ox,oy));
			}	
		}
	}
	
	public List<Balle> getTirs(){
		synchronized (sc_bombes) {
			return tirs;
		}
	}
	
	public ObservableList<String> getPlayers(){
		return playersList;
	}
	
	public void tir() throws IOException {
		System.out.println("tir");
		outchan.writeBytes("TIR/\n");
		outchan.flush();
	}
	
	
	public void run() {
		System.out.println("Demmarage");
		String line;
		String [] commande;
		while(true) {
			try {
				line = inchan.readLine();
				commande = line.split("\\/");
				switch(commande[0]) {
					case "NEWPLAYER" :
						newPlayer(commande[1]); break;
					case "PLAYERLEFT" :
						playerLeft(commande[1]); break;
					case "SESSION" : 
						session(commande[1], commande[2]);
						if(commande.length >= 4 && !commande[3].equals("")) {
							setObstacles(commande[3].split("\\|"));
						}break;
					case "WINNER" : winner(commande[1]); break;
					
					case "TICK" : 
						tick(commande[1].split("\\|")); break;
					
					case "NEWOBJ" : newObj(commande[1], commande[2]); break;
					
					case "RECEPTION" : receiveMessage(commande[1]);break;
					
					case "PRECEPTION" : receivePMessage(commande[1],commande[2]);break;
					
					case "BOMBE" : setBombes(commande[1].split("\\|")); break;
					
					case "BALLES" : setTirs(commande[1].split("\\|")); System.out.println(commande[1]);
					break;
					
					
					default : break;
					
				}
			} catch (IOException e) {
				System.out.println("client B exception");
				break;
				
			}
			
		}
	}
	
	

}
