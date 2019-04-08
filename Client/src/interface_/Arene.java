package interface_;

import java.io.File;
import java.io.IOException;

import client.Client;
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

public class Arene{
	
	private int height;
	private int width;
	private Stage arene;
	private Group nodes;
	private Canvas c;
	private Client client;
	
	private Image joueur = new Image(new File("images/IMG_0094.GIF").toURI().toString(),50,50,false,false);
	
	public Arene(Client client,int h, int w) {
		this.client = client;
		
		height = h;
		width = w;
		arene = new Stage();
		arene.setResizable(false);
		
		nodes = new Group();
		c =new Canvas(w,h);
		
		
		nodes.getChildren().add(c);
		drawArene();
		
		
		arene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

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
			
		});
		
		arene.setScene(new Scene(nodes,w,h));
		arene.show();
		new ThreadArene(this).start();
		
	}
	
	public void drawArene() {
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		gc.setFill(Color.BURLYWOOD);
		gc.fillOval(0, 0, width, height);
	}
	
	public void changePos() {
		try {
			client.changePos();
		} catch (IOException e) {
			System.out.println("erreur envoi serveur coords");
		}
	}
	
	public void refresh() throws IOException {
		client.changePos();
		drawArene();
		drawObjectif();
		c.getGraphicsContext2D().drawImage(joueur,client.getPosition().getX()%width, client.getPosition().getY()%height);
		
	}
	
	public void drawObjectif() {
		c.getGraphicsContext2D().setFill(Color.BLACK);
		c.getGraphicsContext2D().fillOval(client.getObjectif().getX(), client.getObjectif().getY(), 15.0, 15.0);
	}


}
