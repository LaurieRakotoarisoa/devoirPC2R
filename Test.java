

import java.net.Socket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Test extends Application {

    public static void main(String[] args) {
        Application.launch(Test.class, args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250, Color.LIGHTGREEN);
        TextField f = new TextField();
        Button btn = new Button();
        btn.setText("Se connecter");
        btn.setLayoutY(150);
        btn.setLayoutX(100);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
               System.out.println("Demande de connexion");
               if(!f.getText().equals("")) {
            	   Stage s = new Stage();
                   s.setTitle("Fenetre de jeu");
                   s.setScene(new Scene(new Group(), 450, 450));
                   s.show();
                   primaryStage.hide();
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
    }
}
