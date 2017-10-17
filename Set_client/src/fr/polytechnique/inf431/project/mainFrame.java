package fr.polytechnique.inf431.project;

import java.awt.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import java.io.*;
import java.net.*;

public class mainFrame extends JFrame implements Runnable {
	
	private Socket socket;
	private static ObjectInputStream ois=null;
	private static ObjectOutputStream oos=null;
	private static boolean clock_start=false;
	int WIDTH = 120, HEIGHT = 150;
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	CardDrawable[] cardDrawable = new CardDrawable[15];
	volatile LinkedList<CardDrawable> set_Candidate=new LinkedList<>();
	Score s = new Score(0, 0, 0, 0);
    LimpidLabel[] limpidlabel = new LimpidLabel[3];
	Container con = this.getContentPane();
	static boolean restecard = true;
	static int findset = 0;
	int[] place = new int[3];
	CardDrawable c1, c2, c3;
	static volatile boolean pret = false;
	
	public mainFrame(Socket s){
		this.socket=s;
	}
	
	public boolean contain_a_Set(int m){
		for(int i=0;i<m-2;i++)
			for(int j=i+1;j<m-1;j++)
				for(int k=j+1;k<m;k++){
					if(Cards.isSet(cardDrawable[i].getCard(), cardDrawable[j].getCard(), cardDrawable[k].getCard()))
						{System.out.println("i="+i+" j="+j+" k="+k);
						return true;}
				}
		return false;
	}
	
	public void setPremiersCards(int[] place){
		if (place[0] < 12 && place[1] < 12){
			if (place[2] == 12){
				cardDrawable[place[0]].setCard(cardDrawable[13].getCard());
				cardDrawable[place[1]].setCard(cardDrawable[14].getCard());
			}
			if (place[2] == 13){
				cardDrawable[place[0]].setCard(cardDrawable[12].getCard());
				cardDrawable[place[1]].setCard(cardDrawable[14].getCard());
			}
			if (place[2] == 14){
				cardDrawable[place[0]].setCard(cardDrawable[12].getCard());
				cardDrawable[place[1]].setCard(cardDrawable[13].getCard());
			}
		}
		if (place[0] < 12 && place[2] < 12){
			if (place[1] == 12){
				cardDrawable[place[0]].setCard(cardDrawable[13].getCard());
				cardDrawable[place[2]].setCard(cardDrawable[14].getCard());
			}
			if (place[1] == 13){
				cardDrawable[place[0]].setCard(cardDrawable[12].getCard());
				cardDrawable[place[2]].setCard(cardDrawable[14].getCard());
			}
			if (place[1] == 14){
				cardDrawable[place[0]].setCard(cardDrawable[12].getCard());
				cardDrawable[place[2]].setCard(cardDrawable[13].getCard());
			}
		}
		if (place[1] < 12 && place[2] < 12){
			if (place[0] == 12){
				cardDrawable[place[1]].setCard(cardDrawable[13].getCard());
				cardDrawable[place[2]].setCard(cardDrawable[14].getCard());
			}
			if (place[0] == 13){
				cardDrawable[place[1]].setCard(cardDrawable[12].getCard());
				cardDrawable[place[2]].setCard(cardDrawable[14].getCard());
			}
			if (place[0] == 14){
				cardDrawable[place[1]].setCard(cardDrawable[12].getCard());
				cardDrawable[place[2]].setCard(cardDrawable[13].getCard());
			}
		}
		if (place[0] < 12 && place[1] >= 12 && place[2] >= 12){
			if (place[1] != 12 && place[2] != 12)
				cardDrawable[place[0]].setCard(cardDrawable[12].getCard());
			if (place[1] != 13 && place[2] != 13)
				cardDrawable[place[0]].setCard(cardDrawable[13].getCard());
			if (place[1] != 14 && place[2] != 14)
				cardDrawable[place[0]].setCard(cardDrawable[14].getCard());
		}
		if (place[1] < 12 && place[0] >= 12 && place[2] >= 12){
			if (place[0] != 12 && place[2] != 12)
				cardDrawable[place[1]].setCard(cardDrawable[12].getCard());
			if (place[0] != 13 && place[2] != 13)
				cardDrawable[place[1]].setCard(cardDrawable[13].getCard());
			if (place[0] != 14 && place[2] != 14)
				cardDrawable[place[1]].setCard(cardDrawable[14].getCard());
		}
		if (place[2] < 12 && place[0] >= 12 && place[1] >= 12){
			if (place[0] != 12 && place[1] != 12)
				cardDrawable[place[2]].setCard(cardDrawable[12].getCard());
			if (place[0] != 13 && place[1] != 13)
				cardDrawable[place[2]].setCard(cardDrawable[13].getCard());
			if (place[0] != 14 && place[1] != 14)
				cardDrawable[place[2]].setCard(cardDrawable[14].getCard());
		}
		return;
	}
	
	
	public void run(){
		
		this.setTitle("Jeu de carte Set!");
		this.setSize(520, 660);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(gbl);
		con.setBackground(Color.DARK_GRAY);
	    for (int i = 0; i < 3; i++){
	    	limpidlabel[i] = new LimpidLabel();
	    }
	    
	    SetDicider sd = new SetDicider();
	    sd.start();
	    
	    AfterDicider ad = new AfterDicider();
	    ad.start();
	    
		int cards = 0;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("outputstream prepared");
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("InputStream prepared");
			for (int i = 0; i < 12; i++) {
				final int k = i;
				// cards = this.unSet.tire_Card();
				oos.writeObject("morecard");
				oos.flush();
				cards = (int) ois.readObject();
				cardDrawable[i] = new CardDrawable(cards, ("card" + i));
				cardDrawable[i].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (!cardDrawable[k].isChosen) {
							cardDrawable[k].isChosen = true;
						} else {
							cardDrawable[k].isChosen = false;
						}
						if (!set_Candidate.contains(cardDrawable[k])) {
							set_Candidate.add(cardDrawable[k]);
						} else {
							set_Candidate.remove(cardDrawable[k]);
						}
					}
				});
			}
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridwidth = 1;
			gbc.weightx = 25.0;
			gbc.weighty = 25.0;
			for (int i = 0; i < 3; i++) {
				gbc.gridy = i;
				for (int j = 0; j < 4; j++) {
					gbc.gridx = j;
					con.add(cardDrawable[i * 4 + j], gbc);
				}
			}
		
			 
		for (int i = 12; i < 15; i++){
			final int k = i;
			this.cardDrawable[k]=new CardDrawable("card"+k);		
			cardDrawable[k].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!cardDrawable[k].isChosen)
			    	{
			    	 cardDrawable[k].isChosen=true;
			    	}
			    	else{
			    		cardDrawable[k].isChosen=false;
			    	}
					if (!set_Candidate.contains(cardDrawable[k])){
						set_Candidate.add(cardDrawable[k]);
					}
					else {
						set_Candidate.remove(cardDrawable[k]);
					}
				}
			});
		}

		if(!contain_a_Set(12)){
			oos.writeObject("morecard");
			oos.flush();
			cards = (int) ois.readObject();
			System.out.println(cards);
			this.cardDrawable[12].setCard(cards);
			oos.writeObject("morecard");
			oos.flush();
			cards = (int) ois.readObject();
			System.out.println(cards);
			this.cardDrawable[13].setCard(cards);
			oos.writeObject("morecard");
			oos.flush();
			cards = (int) ois.readObject();
			System.out.println(cards);
			this.cardDrawable[14].setCard(cards);
			while(!contain_a_Set(15)){
				oos.writeObject("morecard");
				oos.flush();
				cards = (int) ois.readObject();
				this.cardDrawable[14].setCard(cards);
			}
		}			
		gbc.gridy = 3;
		for (int i = 0; i < 3; i++){
			gbc.gridx = i;
			con.add(this.cardDrawable[i+12],gbc);
		}
		gbc.gridx = 3;
		con.add(s, gbc);
		oos.writeObject("initialisation_finish");		
		oos.flush();
		clock_start=true;
		Thread hs=new Thread(new Horloge_start());
		hs.start();
		hs.join();
	    this.setLocationRelativeTo(null);
	    this.setVisible(true);} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class Horloge_start extends Thread {
		public void run() {
			try {
				System.out.println("horloge_start");
				while (true) {
					if (clock_start == true) {
						if (ois.readBoolean()) {
							s.horloge.start();
							break;
						}
					}
				}
				System.out.println("start");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class AfterDicider extends Thread{

		public void run() {
			// TODO Auto-generated method stub
			System.out.println("afterdicider");
			while (true) {
				if (!pret)
					continue;
				if (findset != 0) {
					long t1 = System.currentTimeMillis();
					while (System.currentTimeMillis() - t1 < 2000) {
						for (int i = 0; i < 3; i++) {
							limpidlabel[i].setVisible(true);
						}
					}
					for (int i = 0; i < 3; i++) {
						limpidlabel[i].setVisible(false);
					}
				}
				if (findset == 1) {
					s.win_ScoreCard(c1.getCard(), c2.getCard(), c3.getCard());

					boolean complement = false;
					for (int i = 0; i < 3; i++) {
						if (place[i] >= 12) {
							complement = true;
							break;
						}
					}
					System.out.println(complement);
					if (!complement) {
						try{
						oos.writeObject("nocard");
						oos.flush();
						boolean nocard=ois.readBoolean();
						System.out.println("nocard="+nocard);
						if (!nocard) {
							try {
								oos.writeObject("morecard");
								oos.flush();
								int cards = (int) ois.readObject();
								c1.setCard(cards);
								oos.writeObject("morecard");
								oos.flush();
								cards = (int) ois.readObject();
								c2.setCard(cards);
								oos.writeObject("morecard");
								oos.flush();
								cards = (int) ois.readObject();
								c3.setCard(cards);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (!contain_a_Set(12)) {
								oos.writeObject("nocard");
								oos.flush();
								nocard=ois.readBoolean();
								if (!nocard) {
									try {
										oos.writeObject("morecard");
										oos.flush();
										int cards = (int) ois.readObject();
										cardDrawable[12].setCard(cards);
										oos.writeObject("morecard");
										oos.flush();
										cards = (int) ois.readObject();
										cardDrawable[13].setCard(cards);
										oos.writeObject("morecard");
										oos.flush();
										cards = (int) ois.readObject();
										cardDrawable[14].setCard(cards);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									while (!contain_a_Set(15)) {
										oos.writeObject("nocard");
										oos.flush();
										nocard=ois.readBoolean();
										if (nocard) {
											System.out.println("il n'y a plus de card!");
											restecard = false;
											for (int n = 0; n < 15; n++) {
												cardDrawable[n].setEnabled(false);
											}
											break;
										}
										int c = cardDrawable[14].getCard();
//										int[] temp = Cards.Card_elements(c);
										try {
											oos.writeObject("morecard");
											oos.flush();
											int cards = (int) ois.readObject();

											cardDrawable[14] = new CardDrawable(cards, ("card" + 14));
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (ClassNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
//										unSet.insert_Card(temp);
									}
								} else {
									System.out.println("il n'y a plus de card!");
									restecard = false;
									for (int n = 0; n < 15; n++) {
										cardDrawable[n].setEnabled(false);
									}
								}
							} else {
								cardDrawable[12].resetCard();
								cardDrawable[13].resetCard();
								cardDrawable[14].resetCard();
							}
						} else {
							System.out.println("il n'y a plus de card!");
							restecard = false;
							for (int n = 0; n < 15; n++) {
								cardDrawable[n].setEnabled(false);
							}
						}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					} else {
						setPremiersCards(place);
						if (!contain_a_Set(12)) {
							try{
							oos.writeObject("nocard");
							oos.flush();
							boolean nocard=ois.readBoolean(); 
							if (!nocard) {
								try {
									oos.writeObject("morecard");
									oos.flush();
									int cards = (int) ois.readObject();
									cardDrawable[12].setCard(cards);
									oos.writeObject("morecard");
									oos.flush();
									cards = (int) ois.readObject();
									cardDrawable[13].setCard(cards);
									oos.writeObject("morecard");
									oos.flush();
									cards = (int) ois.readObject();
									cardDrawable[14].setCard(cards);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								while (!contain_a_Set(15)) {
									oos.writeObject("nocard");
									oos.flush();
									nocard=ois.readBoolean(); 
									if (nocard) {
										System.out.println("il n'y a plus de card!");
										restecard = false;
										for (int n = 0; n < 15; n++) {
											cardDrawable[n].setEnabled(false);
										}
										break;
									}
									// int c = cardDrawable[14].getCard();
									// int[] temp = Cards.Card_elements(c);
									try {
										oos.writeObject("morecard");
										oos.flush();
										int cards = (int) ois.readObject();

										cardDrawable[14] = new CardDrawable(cards, ("card" + 14));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// unSet.insert_Card(temp);
								}
							} else {
								System.out.println("il n'y a plus de card!");
								restecard = false;
								for (int n = 0; n < 15; n++) {
									cardDrawable[n].setEnabled(false);
								}
							}
							}catch(IOException e){
								e.printStackTrace();
							}
						} else {
							cardDrawable[12].resetCard();
							cardDrawable[13].resetCard();
							cardDrawable[14].resetCard();
						}
					}
				}
				if (findset == 2) {
					s.loss_ScoreCard();
					c1.setCard(c1.getCard());
					c2.setCard(c2.getCard());
					c3.setCard(c3.getCard());
				}
				try {
					oos.writeObject("updated");
					oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				findset = 0;
				pret = false;
			}
		}
	}
		

	class SetDicider extends Thread{
		private boolean right=false;
		
		public void run(){
			System.out.println("setdicider");
			while (true) {
				if (set_Candidate.size() == 3) {
					try {
						oos.writeObject("findset");
						oos.flush();
						right = ois.readBoolean();
						System.out.println("right=" + right);
						if (right == false)
							continue;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					c1 = set_Candidate.poll();
					c2 = set_Candidate.poll();
					c3 = set_Candidate.poll();
					c1.isChosen = false;
					c2.isChosen = false;
					c3.isChosen = false;

					for (int i = 0; i < 15; i++) {
						if (c1 == cardDrawable[i]) {
							place[0] = i;
							System.out.println("place1 = " + i);
						}
						if (c2 == cardDrawable[i]) {
							place[1] = i;
							System.out.println("place2 = " + i);
						}
						if (c3 == cardDrawable[i]) {
							place[2] = i;
							System.out.println("place3 = " + i);
						}
					}

					if (Cards.isSet(c1.getCard(), c2.getCard(), c3.getCard())) {
						findset = 1;
						for (int i = 0; i < 3; i++) {
							limpidlabel[i].setColor(Color.green);
							gbc.gridy = place[i] / 4;
							gbc.gridx = place[i] % 4;
							con.add(limpidlabel[i], gbc);
							con.add(cardDrawable[place[i]], gbc);
						}
					} else {
						findset = 2;
						for (int i = 0; i < 3; i++) {
							limpidlabel[i].setColor(Color.red);
							gbc.gridy = place[i] / 4;
							gbc.gridx = place[i] % 4;
							con.add(limpidlabel[i], gbc);
							con.add(cardDrawable[place[i]], gbc);
						}
					}
					pret = true;
				}
			}
		}
	}
}

