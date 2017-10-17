package fr.polytechnique.inf431.project;

import java.util.Collections;
import java.util.LinkedList;

public class total_Cards {
	
//	on définit ici l'ensemble de carte;
	private LinkedList<int[]> total_Cards;

	public total_Cards() {
		this.total_Cards = new LinkedList<>();
		for (int i = 1; i <= 3; i++)
			for (int j = 1; j <= 3; j++)
				for (int k = 1; k <= 3; k++)
					for (int l = 1; l <= 3; l++) {
						int[] temp = new int[] { i, j, k, l };
						this.total_Cards.add(temp);
					}
		Collections.shuffle(this.total_Cards);
	}

//	tirer une carte dans l'ensemble de carte
	public int tire_Card(){
		int[] temp=this.total_Cards.pop();
		int value = Cards.valueOf(temp[0], temp[1], temp[2], temp[3]);
		return value;
	}

//	tester s'il y a encore de cartes
	public boolean No_Card(){
		return (this.total_Cards.size()<3);
	}
	
	public void insert_Card(int card){
		int[] a=Cards.Card_elements(card);
		this.total_Cards.add(a);
		Collections.shuffle(total_Cards);
	}
}
