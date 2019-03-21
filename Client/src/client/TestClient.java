package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {
	public static void main(String [] args) throws NumberFormatException, UnknownHostException, IOException {
		Socket s = new Socket(args[0], Integer.parseInt(args[1]));
		
		Client c = new Client(s);
		System.out.println(c.connect("laurie"));
		c.deconnexion();
	}

}
