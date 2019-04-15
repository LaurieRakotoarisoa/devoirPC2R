package clientB;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import clientB.ClientB;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Arene {
	
	private int height;
	private int width;
	private Stage arene;
	private Group nodes;
	private Canvas c;
	private ClientB client;
	private ThreadArene ta;
	
	private double centreX;
	private double centreY;
	
	private ImageView iv = new ImageView(new Image(new File("images/VEHICULE.GIF").toURI().toString(),50,50,false,false));
	private Chat chat;
		
	public Arene(ClientB client,int h, int w) {
		chat = new Chat(client);
		arene =new Stage();
		arene.initModality(Modality.NONE);
		this.client = client;
		client.setChat(chat);
		height = h;
		width = w;
		centreX = w/2.0;
		centreY = h/2.0;
		arene.setResizable(false);
		
		nodes = new Group();
		nodes.getChildren().add(new ToolBar(new Button("hi")));
		c =new Canvas(w,h);
		
		
		nodes.getChildren().addAll(c,iv);
		drawArene();
		
		
		arene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(client.getMyVehicule() !=null) {
					switch(event.getCode()) {
					case LEFT : client.getMyVehicule().clock(); break;
					case RIGHT : client.getMyVehicule().anticlock(); break;
					case UP : client.getMyVehicule().pousse(); break;
					case SPACE : try {
							client.poserBombe();
						} catch (IOException e) {
							System.out.println("Erreur IO bombe");
						}break;
					case  B: try {
							client.tir();
						} catch (IOException e) {
							System.out.println("Erreur IO tir");
						}
					default:
						break; 
					}
				}
				
				
				
			}
			
		});
		
		arene.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				
				
				try {
					client.exit();

					System.exit(0);
				} catch (IOException e) {
					System.out.println("erreur deconnexion");
				}				
			}
			
		});
		
		arene.setScene(new Scene(nodes,w,h));
		ta = new ThreadArene(this, 100);
		ta.start();
		arene.show();
		
	}
	
	public void drawArene() {
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
	}
	
	public void drawObstacles() {
		c.getGraphicsContext2D().setFill(Color.BROWN);
		for(Obstacle o : client.getObstacles()) {
			o.draw(c.getGraphicsContext2D(), centreX, centreY);
		}
	}
	
	public void drawBombes() {
		c.getGraphicsContext2D().setFill(Color.BLACK);
		for(Obstacle b : client.getBombes()) {
			b.draw(c.getGraphicsContext2D(), centreX, centreY);
		}
	}
	
	public void drawBalles() {
		for(Balle b : client.getTirs()) {
			b.drawBalle(c.getGraphicsContext2D(), centreX, centreY);
		}
	}
	
	public void refresh() throws IOException, InterruptedException {
		drawArene();
		drawObjectif();
		drawObstacles();
		drawBombes();
		drawBalles();
		Vehicule myV = client.getMyVehicule();
		if(!client.phase.equals("attente") && myV != null) {
			iv.setRotate(Math.toDegrees(myV.direction()));
			iv.setX(centreX+myV.getPositionX()-25);
			iv.setY(centreY+myV.getPositionY()-25);
			c.getGraphicsContext2D().setFill(Color.RED);
			Map<String,Vehicule> others = client.getVehicules();
			for(String j : others.keySet()) {
				if(!j.equals(client.nom)) {
					c.getGraphicsContext2D().fillOval(others.get(j).getPositionX(), others.get(j).getPositionY(), 30.0, 30.0);
				}
			}
		}
		
		
	}
	
	public void drawObjectif() {
		c.getGraphicsContext2D().setFill(Color.YELLOW);
		synchronized(client.sc_objectif) {
			c.getGraphicsContext2D().fillOval(centreX+client.getObjectifX()-10, centreY +client.getObjectifY()-10, 20.0, 20.0);
		}
		
	}
	
	public void canRefresh() throws InterruptedException {
		synchronized (client.declenche) {
			while(!client.phase.equals("jeu")){
				client.declenche.wait();
			}
		}
	}
	
	public ClientB getClient() {
		return client;
	}


}
