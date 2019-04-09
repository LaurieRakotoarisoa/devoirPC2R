package interface_;



import java.io.IOException;
import java.net.Socket;

import client.Client;
import client.ThreadJeu;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InterfaceClient extends Application{
	
	protected Arene arene;
	
	public static void main (String [] args) {
		Application.launch(InterfaceClient.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Client c =new Client(new Socket("127.0.0.1",12345));
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

            public void handle(ActionEvent event) {
            	Alert a;
               if(!userText.getText().equals("")) {
            	   try {
					if(c.connect(userText.getText())) {
						a = new Alert(AlertType.INFORMATION);
						 a.setHeaderText("attente");
		            	   a.setContentText("Attente début de session");
		            	   a.show();
						   c.attenteDebut();
						   primaryStage.hide();
						   arene = new Arene(c,400,900);
						   
						   
						  
					   }
					else {
						a = new Alert(AlertType.WARNING);
		            	   a.setHeaderText("nom déjà existant");
		            	   a.setContentText("Ce nom est déjà utilisé");
		            	   a.show();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               }
               else {
            	   a = new Alert(AlertType.WARNING);
            	   a.setHeaderText("nom incorrect");
            	   a.setContentText("Au moins 1 caractère requis");
            	   a.show();
               }
               
           
            }
        });
		
		
		
		
		grid.add(connexion, 4, 5);
		
		primaryStage.show();
		
		
	}

}
