package interface_;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import client.Client;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameWindow {
	
	protected Stage game;
	protected GraphicsContext gc;
	protected Client c;
	
	
	public GameWindow(Client client) throws FileNotFoundException {
		c = client;
		game = new Stage();
		game.setTitle("Fenetre de jeu");
		game.setResizable(false);
		game.setWidth(430);
		game.setHeight(430);
		Group root = new Group();
		Canvas c = new Canvas(450,450);
		gc = c.getGraphicsContext2D();
		root.getChildren().add(c);
		game.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				try {
				if(event.getCode() == KeyCode.LEFT) client.changePos(Math.abs((client.getPosition().getX()-10+game.getWidth())%game.getWidth()), client.getPosition().getY());
				if(event.getCode() == KeyCode.RIGHT) client.changePos(Math.abs((client.getPosition().getX()+10)%(game.getWidth()-10)), client.getPosition().getY());
				if(event.getCode() == KeyCode.UP) client.changePos(client.getPosition().getX(), Math.abs((client.getPosition().getY()-10+game.getHeight())%game.getHeight()));
				if(event.getCode() == KeyCode.DOWN) client.changePos(client.getPosition().getX(), Math.abs((client.getPosition().getY()+10)%(game.getHeight()-10)));
				System.out.println("X"+client.getPosition().getX()+"Y"+client.getPosition().getY());
				}catch(IOException e) {
					System.out.println("erreur");
				}
				drawCars(gc);
				drawObjectif();
			}
		});
		game.setScene(new Scene(root, 450, 450));
		
		
	}
	
	public void debutSession() {
		drawCars(gc);
		drawObjectif();
		game.show();
	}
	
	public void drawCars(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0,game.getWidth()-1, game.getHeight()-1);
		gc.drawImage(new Image(new File("images/IMG_0094.JPG").toURI().toString(),50,50,false,false), 
				c.getPosition().getX(), c.getPosition().getY());
		for(String p : c.getPlayers().keySet()) {
			gc.drawImage(new Image(new File("images/IMG_0094.JPG").toURI().toString(),50,50,false,false), 
					c.getPlayers().get(p).getX(), c.getPlayers().get(p).getY());
		}
	}
	
	public void drawObjectif() {
		gc.setFill(Color.BLACK);
		gc.fillOval(c.getObjectif().getX(), c.getObjectif().getY(), 15.0, 15.0);
	}

	

	
}
