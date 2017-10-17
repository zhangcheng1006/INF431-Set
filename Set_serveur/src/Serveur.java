import java.io.*;
import java.net.*;
import java.util.*;


public class Serveur {
	public static ServerSocket ss=null;
	public static Thread t1=null;
	private static int nombre_joueur=2;
	private static ArrayList<ObjectInputStream> lois=new ArrayList<>(nombre_joueur);
	private static ArrayList<ObjectOutputStream> loos=new ArrayList<>(nombre_joueur);
	
	public static void main(String[] args) throws IOException {
		try{
			System.out.println("please input the nember of players");
			Scanner sc=new Scanner(System.in);
			nombre_joueur=sc.nextInt();
			sc.close();
			ss=new ServerSocket(2013);
			t1=new Thread(new Connection(ss,nombre_joueur,lois,loos));
//			t1=new Thread(new Cardtransport(ss));
			t1.start();
			t1.join();
			
		}catch (IOException e) {
            System.err.println("Le port "+ss.getLocalPort()+" est déjà utilisé !");
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			ss.close();
		}
	}
}
