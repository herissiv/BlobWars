package inf101.sem2.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import inf101.sem2.game.Game;
import inf101.sem2.game.games.BlobWars;
import inf101.sem2.game.games.ConnectFour;
import inf101.sem2.game.games.Othello;
import inf101.sem2.game.games.TicTacToe;
import inf101.sem2.player.GameEndedException;
import inf101.sem2.player.Player;
import inf101.sem2.player.RestartException;
import inf101.sem2.player.ai.AlphaBetaPlayer;
import inf101.sem2.player.ai.DumbPlayer;
import inf101.sem2.player.ai.RandomPlayer;
import inf101.sem2.player.human.GuiPlayer;

/**
 * This class is a Game menu which lets you choose which game to play.
 *
 * @author Martin Vatshelle - martin.vatshelle@uib.no
 */
public class MainMenu implements ActionListener {

	private final JButton playConnectFourButton; // Button to start new 4 in row game
	private final JButton playTicTacToeButton; // Button to start new TicTacToe game
	private final JButton playOthelloButton; // Button to start new Othello game
	private final JButton playBlobWarsButton; 
	private final JFrame frame;
	public Game<?> game;
	public GameGUI gui;
	boolean start;

	public MainMenu() {
		// make new main window for the game
		frame = new JFrame();
		frame.setTitle("Game menu");

		// make panel for game buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		buttons.setBorder(new EmptyBorder(10, 10, 30, 10));

		// add one button for each game
		playTicTacToeButton = addButton(buttons, "Tic-Tac-Toe");
		playConnectFourButton = addButton(buttons, "Connect Four");
		playOthelloButton = addButton(buttons, "Othello");
		playBlobWarsButton = addButton(buttons, "Blob Wars");
		int numButtons = 4;

		// add buttons to the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(buttons);
		frame.setPreferredSize(new Dimension(400, 40 + numButtons * 120));
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Adds buttons with a fixed style and add it to the panel
	 * 
	 * @param buttons - The JPanel containing all the buttons.
	 * @param name    - The name to be displayed on the button.
	 * @return
	 */
	JButton addButton(JPanel buttons, String name) {
		JButton button = new JButton();
		button.setText(name);
		button.setFont(new Font("Arial", Font.PLAIN, 40));
		button.addActionListener(this);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setBorder(new RoundedBorder(20)); // 10 is the radius
		buttons.add(Box.createRigidArea(new Dimension(20, 20)));
		buttons.add(button);
		return button;
	}

	// this method is inherited from ActionListener and is the method
	// that gets called when buttons are clicked.
	@Override
	public void actionPerformed(ActionEvent e) {
		if (game != null) {
			System.err.println("Game is in progress, only one game at the time is possible.");
			return;
		}

		GameGUI graphics = new GameGUI();
		Iterable<Player> players = getPlayers(graphics);
		graphics.setPlayers(players);

		if (e.getSource() == playConnectFourButton) {
			game = new ConnectFour(graphics, players);
		}
		if (e.getSource() == playTicTacToeButton) {
			game = new TicTacToe(graphics, players);
		}
		if (e.getSource() == playOthelloButton) {
			game = new Othello(graphics, players);
		}
		if (e.getSource() == playBlobWarsButton){
			game = new BlobWars(graphics, players);
		}
		if (game == null) {
			System.err.println("Button not recognized, no game created.");
		} else {
			gui = graphics;
			gui.ended = false;
			gui.wantRestart = false;
			start = true;
			graphics.display(game.getGameBoard());
		}
	}

	/**
	 * Generates a list of players based on user input
	 *
	 * @return an Iterable of 2 Players
	 */
	public static Iterable<Player> getPlayers(Input input) {
		List<Player> players = new ArrayList<>();
		// add player1
		players.add(new GuiPlayer('X', input));
		// add player2
		if (promptMultiplayer()) {
			players.add(new GuiPlayer('O', input));
		} else {
			String aI = selectAI();
			if (aI.equals("AlphaBetaPlayer")){
				int level = selectLevelAlphaBetaPlayer();
				players.add(new AlphaBetaPlayer('O',level));
			}
			else if (aI.equals("RandomPlayer")){
				players.add(new RandomPlayer('O'));
			}
			else if (aI.equals("DumbPlayer")){
				players.add(new DumbPlayer('O'));
			}
		}
		return players;
	}

	/**
	 * Metode som lar deg velge vanskelighetsgrad på AI-en
	 * PS: Sterkt inspirert av metoden promptMultiplayer() 
	 * 
	 * @return Valget av AI i from av en string
	 */
	private static String selectAI(){
		String[] aiAlternatives = {"DumbPlayer", "RandomPlayer", "AlphaBetaPlayer"};
		String s = (String) JOptionPane.showInputDialog(
			null, 
			"Choose player/difficulty:", 
			"Waking up AI", 
			JOptionPane.PLAIN_MESSAGE, 
			null, 
			aiAlternatives, 
			null);

		if ((s != null) && (s.length() > 0)) {
			System.out.println("Chose " + s);
		}
		return s;
	}

	/**
	 * Metode som velger level på AlphaBetaPlayer 
	 * Velger level 1 til 5, fordi 6 og høyere tar latterlig lang tid 
	 * @return Levelet til AlphaBatePlayer
	 */
	private static int selectLevelAlphaBetaPlayer(){
		String levelString = null;
		int level;
		levelString = JOptionPane.showInputDialog("Type the level you want for the AI (1-5)");
		while (true){
			try {
				level = Integer.parseInt(levelString);
				if (level >= 1 && level <=5){
					break;
				}
			} catch (NumberFormatException ex) {
				levelString = JOptionPane.showInputDialog("Try Again\nType the level you want for the AI (1-5)");
				continue;
			}
			levelString = JOptionPane.showInputDialog("Try Again\nType the level you want for the AI (1-5)");
		}
		return level;
	}

	/**
	 * Helper method that prompts for multiplayer or not
	 *
	 * @return true if multiplayer is selected, false otherwise
	 */
	private static boolean promptMultiplayer() {
		String[] possibilities = { "Multiplayer", "Single Player (against AI)" };
		String s = (String) JOptionPane.showInputDialog(
				null,
				"Welcome:\n" + "Select one or two players",
				"MKGame StartUp",
				JOptionPane.PLAIN_MESSAGE,
				null,
				possibilities,
				null);

		// If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			System.out.println("Received " + s);
		}
		return s.equals(possibilities[0]);
	}

	/**
	 * This method runs an infinite loop checking if a button
	 * has been clicked.
	 */
	public void run() {
		while (true) {
			if (gui != null && gui.ended) {
				game = null;
				start = false;
			}

			if (gui != null && gui.wantRestart) {
				game.restart();
				gui.wantRestart = false;
				start = true;
			}

			if (start) {
				try {
					System.err.println("Starting the game");
					start = false;
					game.run();
				} catch (RestartException e) {
					System.err.println("Restarting the game");
				} catch (GameEndedException e) {
					System.err.println("Game ended");
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
