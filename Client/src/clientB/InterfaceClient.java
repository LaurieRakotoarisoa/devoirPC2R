package clientB;



import java.io.IOException;
import java.net.Socket;

//import client.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InterfaceClient extends Application{
	
	protected Arene arene;
	protected String name;
	protected Chat chat;
	
	public static void main(String [] args) {
		Application.launch(InterfaceClient.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final ClientB c = new ClientB();
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(25,25,25,25));
		
		Scene scene = new Scene(grid, 600,600);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		
		
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font(30));
		grid.add(scenetitle, 4, 1);
		
		
		
		Label AdresseIP = new Label("AdresseIP");
		AdresseIP.setFont(Font.font(10));
		grid.add(AdresseIP,0,3);
		
		TextField AdressText = new TextField();
		AdressText.setText("127.0.0.1");
		grid.add(AdressText,4,3);
		
		Label user = new Label("User");
		user.setFont(Font.font(10));
		grid.add(user,0,5);
		
		TextField userText = new TextField();
		grid.add(userText, 4, 5);
		
		Label port = new Label("Port");
		port.setFont(Font.font(10));
		grid.add(port,0,4);
		
		TextField portText = new TextField();
		portText.setText("1999");
		grid.add(portText, 4, 4);
		
		
		Button connexion = new Button("connexion");
		connexion.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
            	Alert a;
               if(!userText.getText().equals("")) {
            	   try {
            		   c.setClient(new Socket(AdressText.getText(),Integer.parseInt(portText.getText())));
					if(c.connexion(userText.getText())) {
						   arene = new Arene(c,400,900);
 						   c.start();
						  
					}
					else {
						a = new Alert(AlertType.ERROR);
		            	   a.setHeaderText("Erreur");
		            	   a.setContentText("Erreur lors de la connexion, veuillez réessayer");
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
		
		
		
		
		grid.add(connexion, 5, 5);
		
		primaryStage.show();
		
		
	}

}
