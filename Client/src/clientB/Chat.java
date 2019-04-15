package clientB;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Chat {
	private Stage chat = new Stage();
	private GridPane gp = new GridPane();
	private GridPane container = new GridPane();
	private ScrollPane sc = new ScrollPane(container);
	private TextField tf= new TextField();
	ListView<String> list;
	private int index=0;
	private ClientB client;
	
	public Chat(ClientB c) {
		client = c;
		chat.initModality(Modality.NONE);
		list = new ListView<String>(c.getPlayers());
		gp.setHgap(20);
		gp.setVgap(20);
		gp.setPadding(new Insets(25,25,25,25));
		gp.add(sc,2,0);
		gp.add(tf, 2, 1);
		Button b = new Button("envoyer");
		b.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(!tf.getText().equals("")) {
				try {
					sendMessage(tf.getText());
					tf.setText("");
				} catch (IOException e) {
					System.out.println("Erreur IO sendMessage");
				}
				}
				
			}
		});
		gp.add(b,2,2);
		
		Button b2 = new Button("envoyer priver");
		b2.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(!tf.getText().equals("")) {
					try {
						sendPMessage(tf.getText(),list.getSelectionModel().getSelectedItem());
					} catch (IOException e) {
						System.out.println("erreur PMESSAGE");
					}
				}
				
			}
		});
		gp.add(b2,2,3);
		gp.add(list, 2, 5);
		Scene scene = new Scene(gp,200,200);
		chat.setScene(scene);
		chat.show();
	}
	
	public void sendMessage(String txt) throws IOException {
		client.sendMessage(txt);
		Label msg = new Label(txt);
		msg.setTextFill(Color.RED);
		container.add(msg, 3, index++);
	}
	
	public void sendPMessage(String txt, String user) throws IOException {
		if(!user.equals("null")) {
			client.sendPMessage(txt,user);
			Label msg = new Label(txt);
			msg.setTextFill(Color.GREEN);
			container.add(msg, 3, index++);
		}
	}
	
	public void receiveMessage(String txt) {
		Label msg = new Label(txt);
		msg.setTextFill(Color.BLUE);
		container.add(msg, 0, index++);
	}

}
