import java.io.*;
import java.net.*;

public class Client {
	public static Socket socket=null;
	public static Thread t=null;
	
	public static void main(String[] args){
		try {
			socket=new Socket(InetAddress.getLocalHost(), 2013);
			System.out.println("client connected");
			t=new Thread(new Initialisation_client(socket));
			t.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
