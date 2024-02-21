package inf101.sem2.game.games;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf101.grid.Location;
import inf101.sem2.GUI.DummyGraphics;
import inf101.sem2.game.BlobWarsLocations;
import inf101.sem2.game.Game;
import inf101.sem2.game.TestGame;
import inf101.sem2.player.Player;

public class BlobWarsTest extends TestGame{
    BlobWars game;

    /**
     * Denne metoden sørger for at brettet resettes før hver test
     */
    @BeforeEach
	protected void init() {
		super.init();
		game = new BlobWars(new DummyGraphics(), player1, player2);
        assertEquals(player1,game.getCurrentPlayer());
	}
	
    @Override
    public Game<?> getGame(){
		return game;
	}

    /**
     * Tester om det er spillere i hvert hjørnet 
     * To av dem er player 1 og to er player 2. 
     */
    @Test 
    void cornerPlayersInitionalizedTest(){
        assertEquals(player1, game.getGameBoard().get(new Location(0, 0)));
        assertEquals(player1, game.getGameBoard().get(new Location(7, 0)));
        assertEquals(player2, game.getGameBoard().get(new Location(0, 7)));
        assertEquals(player2, game.getGameBoard().get(new Location(7, 7)));
    }

    /**
     * Dette sjekker om spillerene kan flytte seg  
     */
    @Test 
    void validMoveTest(){
        assertTrue(game.validMove(new BlobWarsLocations(new Location(0, 0), new Location(0,1))));
        game.makeMove(new BlobWarsLocations(new Location(0, 0), new Location(0,1)));
        assertTrue(game.validMove(new BlobWarsLocations(new Location(0, 1), new Location(0,3))));

        game.nextPlayer();
        assertTrue(game.validMove(new BlobWarsLocations(new Location(0, 7), new Location(0,5))));
        assertFalse(game.validMove(new BlobWarsLocations(new Location(0, 7), new Location(0,4))));  
    }

    /**
     * Gjør et flytt og sjekker om alt stemmer etter 
     */
    @Test
    void makeMoveTest(){
        BlobWarsLocations move = new BlobWarsLocations(new Location(0, 0), new Location(0,1));
        assertTrue(game.validMove(move));
        game.makeMove(move);
		game.nextPlayer();
		assertEquals(player1, game.getGameBoard().get(move.getToLocation()));
        //Sjekker om brikken kopieres (indirekte test for switchTeam() meotden)
        assertEquals(player1, game.getGameBoard().get(move.getFromLocation()));
        assertEquals(player2, game.getCurrentPlayer());
    }

    @Test
	void testGetPossibleMoves() {
		List<BlobWarsLocations> moves = game.getPossibleMoves();
		assertEquals(16, moves.size());
        
        game.makeMove(new BlobWarsLocations(new Location(0, 0), new Location(2, 2)));
        List<BlobWarsLocations> moves1 = game.getPossibleMoves();
        assertEquals(32, moves1.size());

	}

    @Test 
    void testCopy() {
		TestGame.testCopy(game);
		assertTrue(Arrays.equals("test".getBytes(),"test".getBytes()));
	}

    @Test 
    void testIsWinnerAndGameOver() {
        //P1 -> 0,2
        //P2 -> 0,5
        //P1 -> 0,4
        //P2 -> 7,5
        //P1 -> 7,2
        //P2 -> 7,4
        //P1 -> 6,4
        assertEquals(player1,game.getCurrentPlayer());
        game.makeMove(new BlobWarsLocations(new Location(0, 0), new Location(0, 2)));
        game.nextPlayer();
        assertEquals(player2,game.getCurrentPlayer());
        game.makeMove(new BlobWarsLocations(new Location(0, 7), new Location(0, 5)));
        game.nextPlayer();
        assertEquals(player1,game.getCurrentPlayer());
        game.makeMove(new BlobWarsLocations(new Location(0, 2), new Location(0, 4)));
        game.nextPlayer();
        assertEquals(player2,game.getCurrentPlayer());
        game.makeMove(new BlobWarsLocations(new Location(7, 7), new Location(7, 5)));
        game.nextPlayer();
        assertEquals(player1,game.getCurrentPlayer());
        game.makeMove(new BlobWarsLocations(new Location(7, 0), new Location(7, 2)));
        game.nextPlayer();
        assertEquals(player2,game.getCurrentPlayer());
        game.makeMove(new BlobWarsLocations(new Location(7, 5), new Location(7, 4)));
        game.nextPlayer();
        assertEquals(player1,game.getCurrentPlayer());
        game.makeMove(new BlobWarsLocations(new Location(7, 2), new Location(6, 4)));
        game.nextPlayer();
        assertTrue(game.isWinner(player1));
        assertTrue(game.gameOver());
    }

    /*
    Beskrivelse av hvordan jeg skulle ha testa den private metoden distanceFromToLocations(from, to):
        Lagt til en from location og en to locations som står diagnoalt over hverandre, 
        med Chebychev avtand på 1,2 og 3 og sjekker om metoden gir samma tall.
    */
}
