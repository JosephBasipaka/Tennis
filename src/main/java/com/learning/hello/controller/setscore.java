package com.learning.hello.controller;

public class setscore {
	public match match;
	static int player1_setScore=0;
	static int player2_setScore=0;
	static int setNumber =1; 
	public setscore() {
		match = new match();
		this.player1_setScore =player1_setScore;
		this.player2_setScore = player2_setScore;
		this.setNumber = setNumber;
		
	}
	public boolean setscore1(int player1_gamescore,int player2_gamescore,int player) {
		// TODO Auto-generated method stub
		match m = new match();
		boolean checker = false;
		if((player1_setScore == 5 || player2_setScore == 5) ) {
			player1_setScore=0;
			player2_setScore = 0;
		}

		
		while(setNumber < 6) {
			System.out.println("SetCount " + setNumber);
			if(player1_gamescore > player2_gamescore) {
				player1_setScore++;
				setNumber++;	
			}
			else {
				player2_setScore++;
				setNumber++;	
			}
//		System.out.println("SetScoreplayer1 " + player1_setScore);
//		System.out.println("SetScoreplayer2 " + player2_setScore);
		return false;
		}
		checker = m.matchscore(player1_setScore,player2_setScore);
		if(checker == true) return true;
		else {
		 setNumber = 0;
		 return false;
		}
	}
	
	public int getSetScoreOfPlayer1() {
		return player1_setScore;
	}
	public int getSetScoreOfPlayer2() {
		return player2_setScore;
	}
	
}
