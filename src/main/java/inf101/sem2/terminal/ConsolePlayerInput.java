package inf101.sem2.terminal;

import static inf101.sem2.terminal.TerminalInput.readInt;

import java.util.Scanner;

import inf101.grid.Location;
import inf101.sem2.GUI.Input;
import inf101.sem2.game.BlobWarsLocations;
import inf101.sem2.game.Game;
import inf101.sem2.player.human.ConsolePlayer;

public class ConsolePlayerInput extends Input {

	/**
	 * The Scanner used to read from the terminal.
	 * We use one single static Scanner for all players
	 * rather than starting a new Scanner as all ConsolePlayers
	 * on the same computer will be typing on the same keyboard.
	 * If this game were to be played over network this might need to change.
	 */
	static Scanner sc = new Scanner(System.in);
	ConsolePlayer player;
	
	public ConsolePlayerInput() {
		super("Terminal input");
	}
	
	public void setPlayer(ConsolePlayer player) {
		this.player = player;
	}
	
	@Override
	public Integer getColumn() {
		System.out.println("Player " + player + " type [Col] to make a move.");
		return readInt(sc);
	}
	
	@Override
	public Location getLocation() {		
		return getLocation("to make a move");
	}

	public Location getLocation(String message) {		
		System.out.println("Player " + player + " type [Row] [Col] "+ message + ".");
		int row = readInt(sc);
		int col = readInt(sc);
		return new Location(row, col);
	}

	@Override
	public BlobWarsLocations getBlobWarsLocations(){
		return getBlobWarsLocation("to make a move");
	}

	public BlobWarsLocations getBlobWarsLocation(String message) {		
		System.out.println("Player " + player + " type [rowFrom] [colFrom] and then [rowTo] [colTo] "+ message + ".");
		int rowFrom = readInt(sc);
		int colFrom = readInt(sc);
		int rowTo = readInt(sc);
		int colTo = readInt(sc);
		return new BlobWarsLocations(new Location(rowFrom, colFrom), new Location(rowTo, colTo));
	}

	@Override
	public <T> T getMove(Game<T> game) {
		T move = super.getMove(game);
		while(!game.validMove(move)) {
			System.out.println("Not a valid move. Try again");
			move = super.getMove(game);
		}
		return move;
	}
}
