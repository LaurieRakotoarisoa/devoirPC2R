import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {
	
	public static void main(String [] args) {
		int port = Integer.parseInt(args[0]);
		try {
			Socket sock = new Socket("127.0.0.1",port);
			System.out.println("La machine autorise les connexions sur le port : " + port);
			BufferedOutputStream bos = new BufferedOutputStream(sock.getOutputStream());
			String request = "CONNECT/laurie/";
			bos.write(request.getBytes());
			bos.flush();
			 BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
			 
		}catch(UnknownHostException e) {
			
		}
		catch(IOException e) {
			
		}
	}

}
