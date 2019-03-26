package interface_;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InterfaceClient extends Application{
	
	public static void main (String [] args) {
		Application.launch(InterfaceClient.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(25,25,25,25));
		
		Scene scene = new Scene(grid, 500,500);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		
		
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font(40));
		grid.add(scenetitle, 3, 1);
		
		Label user = new Label("User");
		user.setFont(Font.font(20));
		grid.add(user,0,5);
		
		TextField userText = new TextField();
		grid.add(userText, 3, 5);
		
		Button connexion = new Button("Connexion");
		connexion.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Arene a = new Arene(400,400);
				
			}
		});
		grid.add(connexion, 4, 5);
		
		primaryStage.show();
		
	}

}
