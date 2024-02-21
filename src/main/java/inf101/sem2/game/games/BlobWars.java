package inf101.sem2.game.games;
import inf101.sem2.game.Game;
import inf101.grid.Location;


import java.util.ArrayList;
import java.util.List;


import inf101.grid.GridDirection;
import inf101.grid.Location;
import inf101.sem2.GUI.GameGUI;
import inf101.sem2.GUI.Graphics;
import inf101.sem2.game.Game;
import inf101.sem2.game.GameBoard;
import inf101.sem2.player.Player;
import inf101.sem2.game.BlobWarsLocations;

public class BlobWars extends Game<BlobWarsLocations> {

    // Konstruktørene under er godt inspireret av konstruktørene til de andre spilla:)) 
    /**
     * Lager et BlobWars spill med to spillere, der player1 starter. 
     * 
     * @param graphics - Grafikken som brukes for å vise spillet for menneskelige spillere 
     * @param player1  - Player 1, personen som starter, som i dette scenarioet er den som alltid er en mennskelig spiller
     * @param player2  - Player 2, andre mann, kan være et menneske eller AI 
     */
    public BlobWars(Graphics graphics, Player player1, Player player2){
        this(graphics);
        addPlayer(player1);
        addPlayer(player2);
        initializeBoard();
    }

    public BlobWars(Graphics graphics){
        // Valgte 8x8 brett, siden det var det nettversjonen brukte som standard. 
        // Variablen board
        super(new GameBoard(8, 8), graphics);
        
    }

    public BlobWars(Graphics graphics, Iterable<Player> players) {
		super(new GameBoard(8, 8), graphics, players);
        initializeBoard();
	}

    private void initializeBoard() { 
        board.set(new Location(0, 0), getCurrentPlayer());
        board.set(new Location(7, 0), getCurrentPlayer());
        players.nextPlayer();
        board.set(new Location(7, 7), getCurrentPlayer());
        board.set(new Location(0, 7), getCurrentPlayer());
        players.nextPlayer();
        
        
    }


    // Må implementere de abstakte metodene: 

    @Override
    public Game<BlobWarsLocations> copy() {
        Game<BlobWarsLocations> newGame = new BlobWars(this.graphics, players);
		copyTo(newGame);
		return newGame;
    }

    @Override
    public boolean isWinner(Player player) {
        int countPlayer = board.countPieces(player);
        int countOpponentPlayer = 0; 

        // Siden det bare er to spillere så går dette fint
        for (Player p : players){
            if (p != player){
                countOpponentPlayer = board.countPieces(p);
            }
        }

        //Mangler uavgjort 
        //(Om man fjerner kommentaren på if setningen under så får man et mer 
        //effektivt spill etter min mening, men står ikke i oppgava)
        // if (countPlayer>countOpponentPlayer && getPossibleMoves().size()==0) {
        //     return true; 
        // }
        
        if (countOpponentPlayer+countPlayer == 64 && countPlayer>countOpponentPlayer){
            return true;
        }
      
        if (countOpponentPlayer == 0) {
            return true;
        }

        return false;

    }

  
    @Override
    public List<BlobWarsLocations> getPossibleMoves() {
        ArrayList<BlobWarsLocations> moves = new ArrayList<>();
		for (Location to : board.locations()) {
            for (Location from : board.neighbourLocations(to, getCurrentPlayer())){
                BlobWarsLocations possibleToLocations = new BlobWarsLocations(from, to);
                if (validMove(possibleToLocations)) {
                    moves.add(possibleToLocations);
                } 
            }
		}
		return moves;
    }


    @Override
    public String getName() {
        return "Blob Wars";
    }
    
    @Override 
    public boolean gameOver() {
        for (Player p : players) { 
            if (isWinner(p)){
                return true;
            }
        }
        return false; 
    }


    
    /**
     * Lager en metode switchTeam(), som gjør at brikkene som skal byttes bytter lag. 
     * Det vil si at når en spiller havner ved siden av motstandern så vil motstandern bli en friendly player 
     * @param move
     */
    private void switchTeam(Location move){
        for (GridDirection direction : GridDirection.EIGHT_DIRECTIONS){
            try {
                players.nextPlayer();
                if (board.get(move.getNeighbor(direction)) == getCurrentPlayer()){
                    players.nextPlayer();
                    board.swap(move.getNeighbor(direction), getCurrentPlayer());
                    players.nextPlayer();
                }
                players.nextPlayer();
            } catch (Exception indexOutOfBoundsException) {
                players.nextPlayer();
                continue;
            }
            
        }
    } 

    /**
     * Metode som gir Chebyshev avstanden mellom to brikker. 
     * Det er denne avstanden spillet avhenger av når brikkene skal flyttes
     * @param from
     * @param to
     * @return
     */
    private int distanceFromToLocations(Location from, Location to){
        int avstand = Math.max(Math.abs(to.row-from.row), Math.abs(to.col-from.col));
        return avstand;
    }

    @Override
    public void makeMove(BlobWarsLocations move) {
        if (!validMove(move)){
	 		throw new IllegalArgumentException("Cannot make move:\n" + move);
        }
        if (validMove(move) && distanceFromToLocations(move.getFromLocation(), move.getToLocation())==2) {
            board.movePiece(move.getFromLocation(),move.getToLocation());
        }
        else if (validMove(move) && distanceFromToLocations(move.getFromLocation(), move.getToLocation())==1) {
            board.set(move.getToLocation(), getCurrentPlayer());
        }
        switchTeam(move.getToLocation());
        displayBoard();
    }

    @Override
    public boolean validMove(BlobWarsLocations move) {
        if (board.get(move.getToLocation()) == null && board.get(move.getFromLocation())==getCurrentPlayer()){
            if (distanceFromToLocations(move.getFromLocation(), move.getToLocation())==2 || distanceFromToLocations(move.getFromLocation(), move.getToLocation())==1){
                return true;
            }
            //Denne metoden kunne blitt lagd på en linje, men da hadde linjen blitt unødvendig lang. 
        }
        return false;
    }
}
