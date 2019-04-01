package interface_;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Arene {
	
	private Stage stage;
	private Scene scene;
	private Group group;
	private GraphicsContext gc;
	
	public Arene(int height, int width) {
		stage = new Stage();
		group = new Group();
		scene = new Scene(group,width,height,Color.WHITE);
		Canvas canvas = new Canvas(height,width);
		gc = canvas.getGraphicsContext2D();
		group.getChildren().add(canvas);
		canvas.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				System.out.println("released");		
				}
		});
		
		stage.setScene(scene);
		stage.setTitle("Arene");
		stage.show();
	}
	
	public void add() {
		gc.drawImage(new Image("images/img.jpg"), 50, 50);
	}

}
