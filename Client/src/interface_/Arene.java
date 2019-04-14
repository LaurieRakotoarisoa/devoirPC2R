package interface_;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import client.Client;
import clientB.ClientB;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Arene{
	
	private int height;
	private int width;
	private Stage arene;
	private Group nodes;
	private Canvas c;
	private Client client;
	
	public Arene(Client client,int h, int w, Stage s) {
		arene = s;
		this.client = client;
		
		height = h;
		width = w;
		arene.setResizable(false);
		
		nodes = new Group();
		c =new Canvas(w,h);
		
		
		nodes.getChildren().add(c);
		drawArene();
		drawObjectif();
		
		
		/*arene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch(event.getCode()) {
				case LEFT : client.clock(); break;
				case RIGHT : client.anticlock(); break;
				case UP : client.thrust(); break;
				default:
					break; 
				}
				
				
				
			}
			
		});*/
		
		arene.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				arene.close();
				try {
					client.deconnexion();
				} catch (IOException e) {
				}
				
			}
			
		});
		
		arene.setScene(new Scene(nodes,w,h));
		arene.show();
		
	}
	
	public void drawArene() {
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
	}
	
	/*public void refresh() throws IOException {
		client.changePos(width,height);
		drawArene();
		drawObjectif();
		c.getGraphicsContext2D().setFill(Color.YELLOW);
		c.getGraphicsContext2D().fillOval(client.getPosition().getX(), client.getPosition().getY(), 15.0, 15.0);
		c.getGraphicsContext2D().setFill(Color.RED);
		Map<String,Client.Coords> others = new HashMap<String,Client.Coords>(client.getPlayers());
		for(String j : others.keySet()) {
			if(!j.equals(client.nom)) {
				c.getGraphicsContext2D().fillOval(others.get(j).getX(), others.get(j).getY(), 15.0, 15.0);
			}
		}
		
		
	}*/
	
	public void drawObjectif() {
		c.getGraphicsContext2D().setFill(Color.BLACK);
		c.getGraphicsContext2D().fillOval(client.getObjectif().getX(), client.getObjectif().getY(), 15, 15);
		
	}


}
