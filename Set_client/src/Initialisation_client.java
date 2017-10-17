import java.io.*;
import java.net.*;

import fr.polytechnique.inf431.project.mainFrame;

public class Initialisation_client implements Runnable {
	private static Socket socket=null;
	private static Thread t=null;
	
	public Initialisation_client(Socket socket) {
		this.socket=socket;
	}
	
	public void run(){
//		t=new Thread(new Card_demander(socket));
//		t=new Thread(new Cardtest(socket));
		t=new Thread(new mainFrame(socket));
		t.start();
	}
}
