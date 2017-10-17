import java.net.*;

import fr.polytechnique.inf431.project.CardDrawable;
import fr.polytechnique.inf431.project.mainFrame;

import java.io.*;

public class Cardtest implements Runnable {
	private static Socket socket=null;
	private static ObjectInputStream ois=null;
	private static ObjectOutputStream oos=null;
	
	public Cardtest(Socket socket){
		this.socket=socket;
	}
	
	public void run(){
		try {
			oos= new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			for(int i=0;i<12;i++){

			oos.writeObject("morecard");
			oos.flush();

			int card=(int) ois.readObject();
			System.out.println(card);

			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
