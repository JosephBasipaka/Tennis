package com.learning.hello.controller;

public class match {
	int match_id = 1;
	int player1_matchesWon=0;
	int player2_matchesWon=0;
	public boolean matchscore(int player1_setScore, int player2_setScore) {
		// TODO Auto-generated method stub		
		match_id++;
		if(player1_setScore > player2_setScore) {
			player1_matchesWon++;
			System.out.println("Player1 Won the match!");
			return true;
		}
		else if(player1_setScore < player2_setScore ) {
			player2_matchesWon++;
			System.out.println("Player2 Won the match!");
			return true;
		}
		else {
			return false;
		}
	}
	
	public int getMatchId() {
		return match_id;
	}
	public int getPlayer1WonMatches() {
		return player1_matchesWon;
	}
	public int getPlayer2WonMatches() {
		return player2_matchesWon;
	}
	
}
