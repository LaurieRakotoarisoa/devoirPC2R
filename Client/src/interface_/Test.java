package interface_;


import java.io.IOException;
import java.net.Socket;

import client.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Test extends Application {

    public static void main(String[] args) {
        Application.launch(Test.class, args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	Client c;
    	try {
			c = new Client(new Socket("127.0.0.1",12345));
    	
		
        primaryStage.setTitle("Arene");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250, Color.LIGHTGREEN);      
        
        TextField f = new TextField();
        Button btn = new Button();
        btn.setText("Se connecter");
        btn.setLayoutY(150);
        btn.setLayoutX(100);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
               if(!f.getText().equals("")) {
            	   try {
					if(c.connect(f.getText())) {
						System.out.println("attente");
						   c.attenteDebut();
						   System.out.println("fin attente");
						   primaryStage.hide();
						   new GameWindow(c).debutSession();
						   
						  
					   }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               }
               
           
            }
        });
        f.setLayoutX(80);
        f.setLayoutY(120);
        f.resize(50, 20);
        root.getChildren().addAll(f,btn); 
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	
    }
}
