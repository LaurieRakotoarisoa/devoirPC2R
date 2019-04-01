package interface_;

import java.io.File;
import java.io.FileNotFoundException;

import client.Client;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameWindow {
	
	protected Stage game;
	protected GraphicsContext gc;
	protected Client c;
	
	public GameWindow(Client client) throws FileNotFoundException {
		c = client;
		game = new Stage();
		game.setTitle("Fenetre de jeu");
		
		Group root = new Group();
		Canvas c = new Canvas(450,450);
		gc = c.getGraphicsContext2D();
		root.getChildren().add(c);
		game.setScene(new Scene(root, 450, 450));
		
		
	}
	
	public void debutSession() {
		drawCars(gc);
		game.show();
	}
	
	public void drawCars(GraphicsContext gc) {
		gc.drawImage(new Image(new File("images/IMG_0094.JPG").toURI().toString(),50,50,false,false), 
				c.getPosition().getX(), c.getPosition().getY());
		for(String p : c.getPlayers().keySet()) {
			gc.drawImage(new Image(new File("images/IMG_0094.JPG").toURI().toString(),50,50,false,false), 
					c.getPlayers().get(p).getX(), c.getPlayers().get(p).getY());
		}
	}

}
