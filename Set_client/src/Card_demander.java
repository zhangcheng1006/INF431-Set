import java.io.*;
import java.net.*;

public class Card_demander implements Runnable {
	private Socket socket;
	private static ObjectInputStream ois=null;
	private static ObjectOutputStream oos=null;
	
	public Card_demander(Socket socket){
		this.socket=socket;
	}
	
	public void run(){
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("outputstream prepared");
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("inputstream prepared");
			for (int i = 0; i < 12; i++) {
				oos.writeObject("morecard");				
				oos.flush();
				System.out.println("request sent");
				int card = (int) ois.readObject();
				System.out.println(card);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
	}
}
