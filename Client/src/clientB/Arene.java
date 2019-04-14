package clientB;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import client.Client;
import clientB.ClientB;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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
	public boolean canRefresh = false;
	
	private double centreX;
	private double centreY;
		
	public Arene(ClientB client,int h, int w) {
		arene =new Stage();
		arene.initModality(Modality.NONE);
		this.client = client;
		
		height = h;
		width = w;
		centreX = w/2.0;
		centreY = h/2.0;
		arene.setResizable(false);
		
		nodes = new Group();
		c =new Canvas(w,h);
		
		
		nodes.getChildren().add(c);
		drawArene();
		
		
		arene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(client.getMyVehicule() !=null) {
					switch(event.getCode()) {
					case LEFT : client.getMyVehicule().clock(); break;
					case RIGHT : client.getMyVehicule().anticlock(); break;
					case UP : client.getMyVehicule().pousse(); break;
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
		for(Obstacle o : client.getObstacles()) {
			o.drawObstacle(c.getGraphicsContext2D(), centreX, centreY);
		}
	}
	
	public void refresh() throws IOException, InterruptedException {
		drawArene();
		drawObjectif();
		drawObstacles();
		c.getGraphicsContext2D().setFill(Color.YELLOW);
		Vehicule myV = client.getMyVehicule();
		if(!client.phase.equals("attente") && myV != null) {
			c.getGraphicsContext2D().fillOval(centreX+myV.getPositionX(), centreY+myV.getPositionY(), 15.0, 15.0);
			c.getGraphicsContext2D().setFill(Color.RED);
			Map<String,Vehicule> others = client.getVehicules();
			for(String j : others.keySet()) {
				if(!j.equals(client.nom)) {
					c.getGraphicsContext2D().fillOval(others.get(j).getPositionX(), others.get(j).getPositionY(), 15.0, 15.0);
				}
			}
		}
		
		
	}
	
	public void drawObjectif() {
		c.getGraphicsContext2D().setFill(Color.BLACK);
		synchronized(client.sc_objectif) {
			c.getGraphicsContext2D().fillOval(centreX+client.getObjectifX(), centreY +client.getObjectifY(), 15.0, 15.0);
		}
		
	}


}
