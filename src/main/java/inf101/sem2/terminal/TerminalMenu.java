package inf101.sem2.terminal;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import inf101.sem2.game.Game;
import inf101.sem2.game.games.BlobWars;
import inf101.sem2.game.games.ConnectFour;
import inf101.sem2.game.games.Othello;
import inf101.sem2.game.games.TicTacToe;
import inf101.sem2.player.Player;
import inf101.sem2.player.ai.AlphaBetaPlayer;
import inf101.sem2.player.ai.DumbPlayer;
import inf101.sem2.player.ai.RandomPlayer;
import inf101.sem2.player.human.ConsolePlayer;

public class TerminalMenu {

	static Scanner sc = new Scanner(System.in);

	public static Game<?> selectGame(ArrayList<Player> players) {
		System.out.println("Which game do you wish to play?");
		System.out.println("Press 1 for TicTacToe, 2 for Connect 4, 3 for Othello and 4 for Blob Wars");
		Game<?> game;
		while (true) {
			int choice = TerminalInput.readInt(sc);
			switch (choice) {
				case 1:
					game = new TicTacToe(new TerminalGraphics(), players.get(0), players.get(1));
					break;
				case 2:
					game = new ConnectFour(new TerminalGraphics(), players.get(0), players.get(1));
					break;
				case 3:
					game = new Othello(new TerminalGraphics(), players.get(0), players.get(1));
					break;
				case 4: 
					game = new BlobWars(new TerminalGraphics(), players.get(0), players.get(1));
					break;
				default:
					System.err.println("Unexpected value: " + choice);
					continue;
			}
			break;
		}
		return game;
	}

	public static ArrayList<Player> getPlayers() {
		System.out.println("Player 1, what is your name?");
		ArrayList<Player> players = new ArrayList<>();
		players.add(new ConsolePlayer('X'));
		System.out.println("(1) Two players or \n(2) play against computer?");
		int multiplayerChoice = TerminalInput.readInt(new Scanner(System.in));
		switch (multiplayerChoice) {
			case 1:
				players.add(new ConsolePlayer('O'));
				break;
			case 2:
				int ai = selectAI();
				if (ai == 1){
					players.add(new DumbPlayer('O'));
				}
				else if (ai == 2){
					players.add(new RandomPlayer('O'));
				}  
				else if (ai == 3){
					int level = selectLevelAlphaBetaPlayer();
					players.add(new AlphaBetaPlayer('O', level));
				}
				// players.add(new MiniMaxPlayer('O', 5));
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + multiplayerChoice);
		}
		return players;
	}

	public static boolean isDone() {
		System.out.println("Play again? y/n");
		String choice = TerminalInput.readString(sc);
		return !choice.startsWith("y");
	}

	/**
	 * Metode som lar deg velge vanskelighetsgrad på AI-en 
	 * 
	 * @return Valget av AI i from av en string
	 */
	private static int selectAI(){
		int ai;
		System.out.println("Type a number (1-3) to choose player:\n 1: DumbPlayer\n 2: RandomPlayer\n 3: AlphaBetaPlayer");
		ai = TerminalInput.readInt(new Scanner(System.in));
		while (true){
			if (ai >= 1 && ai <=3){
				break;
			}
			System.out.println("Try again: Type a number (1-3) to choose player:\n 1: DumbPlayer\n 2: RandomPlayer\n 3: AlphaBetaPlayer");
		}
		return ai;
	}

	/**
	 * Metode som velger level på AlphaBetaPlayer 
	 * Velger level 1 til 5, fordi 6 og høyere tar latterlig lang tid 
	 * @return levelet til AlphaBetaPlayer
	 */
	private static int selectLevelAlphaBetaPlayer(){
		int level;
		System.out.println("Type a number (1-5) to choose select the level for AlphaBetaPlayer");
		level = TerminalInput.readInt(new Scanner(System.in));
		while (true){
			if (level >= 1 && level <=5){
				break;
			}
			System.out.println("Try again:\n Type a number (1-5) to choose select the level for AlphaBetaPlayer");
		}
		return level;
	}	
}

