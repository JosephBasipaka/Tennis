package com.learning.hello.controller;
public class TennisController {
	public int player;
	public String playerName;
	public Game game;
	
	public TennisController() {
		game = new Game();
	}
		//int player=0;
	public void play(int point) {
		boolean checker=false;
//		while(checker==false) {
//			player = Math.random() < 0.5 ? 1 : 0;
//			System.out.println("hello");
//			player = addPoint(playerName);
			System.out.println(point);
//			checker = Game.scoregenerator(player);
			game.scoregenerator(point);
			
//			}
	}
	
	public int addPoint(String playerName) {
//		System.out.println("hii");
		int player=0;
		if(playerName != null) {
//			System.out.println("are u there add point");
			if(playerName.equals("Player1")) {
//				System.out.println("yeah there");
				player = 0;
			}
			else 
				player = 1;
			
		}
		play(player);
		return player;
 	}
	;
	public int getPoint() {
		return player;
	}
	
	@Override
	public String toString() {
		return "player1" + player;
	}
	
	
	

}
