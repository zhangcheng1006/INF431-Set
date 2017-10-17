import java.io.*;
import java.net.*;
import java.util.*;

public class Connection implements Runnable {
	private static ServerSocket ss=null;
	private static Socket socket=null;
	private static Thread t=null;
	private static int nombre_joueur=0;
	private int nombre_total;
	private ArrayList<ObjectInputStream> lois;
	private ArrayList<ObjectOutputStream> loos;
	
	public Connection(ServerSocket ss,int nombre_total,ArrayList<ObjectInputStream> lois,ArrayList<ObjectOutputStream> loos){
		this.ss=ss;
		this.nombre_total=nombre_total;
		this.lois=lois;
		this.loos=loos;
	}
	
	public void run(){
		try {
			while (nombre_joueur<this.nombre_total) {
				socket = ss.accept();
				nombre_joueur++;
				System.out.println("Connection" + nombre_joueur);
				lois.add(new ObjectInputStream(socket.getInputStream()));
				loos.add(new ObjectOutputStream(socket.getOutputStream()));
			}
			t=new Thread(new Cardtransport(lois, loos));
			t.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
