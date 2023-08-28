package com.learning.hello.controller;

public class Game {
	
	static int player2_score=0;
	static int player1_score=0;
	static int player2_score_final=0;
	static int player1_score_final=0;
	static int game_count = 1;
	static int player1_gamescore = 0;
	static int player2_gamescore = 0;
	public setscore set;
	public Game(int score1, int score2) {
		this.player1_score = score1;
		this.player2_score = score2;
	}
	public Game() {
		set = new setscore();
	}
	
	public boolean scoregenerator(int player){
		setscore s= new setscore();
		boolean checker = false;
		
		if((player1_score_final == 4 || player2_score_final == 4) && Math.abs(player1_score_final - player2_score_final)>=2) {
			player1_score_final=0;
			player2_score_final = 0;
		}
		if((player1_gamescore == 6 || player2_gamescore == 6) && Math.abs(player1_gamescore - player2_gamescore )>=2) {
			player1_gamescore=0;
			player2_gamescore = 0;
		}
		if(player == 0) {
			player1_score++;
			player1_score_final++;
			System.out.println(player1_score);
		}
		if(player == 1) {
			player2_score ++;
			player2_score_final++;
			System.out.println(player2_score);
		}
		
		if(player1_score==4) {
			player1_gamescore++;
		}
		else if(player2_score==4){
			player2_gamescore++;
		}
		
		
		if((player1_score - player2_score>=2 && player1_score >=4) ) {
			
			//player1_gamescore++;
//		player1_score_final=0;
			System.out.println("Player1gamescore : " + player1_gamescore);

			if(player1_gamescore >=6  && player1_gamescore - player2_gamescore >=2) {
				checker  = s.setscore1(player1_gamescore,player2_gamescore,player);
				if(checker == true) {
					return true;
				}
				else {
				player1_score=0;
				player2_score=0;
				
				return false;
				}
			}
			player1_score=0;
			player2_score=0;
			
			game_count++;
			return false;
		}
		else if((player2_score-player1_score>=2 && player2_score >= 4)){
			
			//player2_gamescore++;
			//player2_score_final=0;
			System.out.println("Player2gamescore : "+player2_gamescore);
			 if (player2_gamescore >= 6 && player2_gamescore - player1_gamescore >=2)  {
					player=1;
					checker  = s.setscore1(player1_gamescore,player2_gamescore,player);
					if(checker == true) {
						return true;
					}
					else {
					player1_score=0;
					player2_score=0;
					
					return false;
					}
				}
			 player1_score=0;
				player2_score=0;
				
				game_count++;
				
				return false;
		}
		else if (player1_score ==4 && player2_score==4) {
			//System.out.println("Deuce!");
			return false;
		}
		else {
			
			return false;
			}
	}
	
	public int getPlayer1Score() {
		return player1_score_final;
	}
	public int getPlayer2Score() {
		return player2_score_final;
	}
	public int getPlayer1GameScore() {
		return player1_gamescore;
	}
	public int getPlayer2GameScore() {
		return player2_gamescore;
	}
}
