import java.io.*;
import java.util.*;
import java.net.*;

import fr.polytechnique.inf431.project.CardDrawable;
import fr.polytechnique.inf431.project.total_Cards;

public class Cardtransport implements Runnable {
	public ArrayList<ObjectOutputStream> loos;
	public ArrayList<ObjectInputStream> lois;
	private static total_Cards unSet = new total_Cards();
	public static int index=-1;
	public Thread[] t;

	public Cardtransport(ArrayList<ObjectInputStream> lois, ArrayList<ObjectOutputStream> loos) {
		this.lois = lois;
		this.loos = loos;
		this.t=new Thread[lois.size()];
	}

	private boolean finish(boolean[] b) {
		boolean result = true;
		for (boolean pb : b) {
			result = result && pb;
		}
		return result;
	}

	public void run() {
		try {

			boolean[] initialisation = new boolean[lois.size()];
			for (int i = 0; i < lois.size(); i++) {
				initialisation[i] = false;
			}

			while (true) {
				if (unSet.No_Card())
					break;
				boolean utilise = false;

				int card = unSet.tire_Card();
				for (int i = 0; i < lois.size(); i++) {
					String s = (String) lois.get(i).readObject();
					if (s.equals("morecard")) {

						loos.get(i).writeObject(card);
						loos.get(i).flush();
						utilise = true;
					}
					if (s.equals("initialisation_finish"))
						initialisation[i] = true;
				}
				if (finish(initialisation)) {
					break;
				}
			}

			for (int i = 0; i < lois.size(); i++) {
				loos.get(i).writeBoolean(true);
				loos.get(i).flush();
			}
			
			for(int i=0;i<lois.size();i++){
				t[i]=new Thread(new FindSet(lois.get(i),""+i));
				t[i].start();
			}
			
			while (true) {
				if (unSet.No_Card())
					break;
				
//				for(int i=0;i<lois.size();i++){
//					t[i].join();
//				}
				while (index == -1) {
					Thread.sleep(1000);
				}

				System.out.println("out of attent active");
				while (true) {
					int card = unSet.tire_Card();
					String s = (String) lois.get(index).readObject();
					if (s.equals("morecard")) {
						loos.get(index).writeObject(card);
						loos.get(index).flush();
					}
					if (s.equals("nocard")) {
						boolean nocard = unSet.No_Card();
						loos.get(index).writeBoolean(nocard);
						loos.get(index).flush();
					}
					if (s.equals("updated")){
						t[index]=new Thread(new FindSet(lois.get(index),""+index));
						t[index].start();
						break;
						}
				}
				index=-1;
			}		
		} catch (IOException e) {
			System.err.println("Erreur serveur");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class FindSet implements Runnable{
		ObjectInputStream ois;
		String name;
		FindSet(ObjectInputStream i,String name){
			this.ois=i;
			this.name=name;
		}
		
		public void run() {
			try {
				while (true) {
					System.out.println("inside thread "+name);
					String s = (String) ois.readObject();
					if (s.equals("findset")) {
						index = lois.indexOf(ois);
						System.out.println("findset"+index);
						loos.get(index).writeBoolean(true);
						System.out.println("order sent");
						loos.get(index).flush();
						break;
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}


