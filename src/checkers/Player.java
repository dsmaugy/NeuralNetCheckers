package checkers;

import org.encog.ml.CalculateScore;
import org.encog.neural.neat.NEATNetwork;

import java.util.ArrayList;

/**
 * Created by Darwin on 1/31/2017.
 */
public abstract class Player  {

    private CheckerPiece[] pieces = new CheckerPiece[12];
    private GameBoard board;
    private boolean canJump = false;

    private NEATNetwork network;

    private ArrayList<ArrayList<LegalMove>> allPossibleMoves = new ArrayList<>();
    private ArrayList<LegalMove> allPossibleValidMoves = new ArrayList<>();

    public Player(GameBoard board) {
        this.board = board;
    }

    public Player() {

    }

    public CheckerPiece[] getPieces() {
        return pieces;
    }

    public boolean allPiecesCaptured() {
        for (CheckerPiece piece : pieces) {
            if (!piece.isCaptured())
                return false;
        }
        return true;
    }

    // Returns array of valid moves (factoring in force jumping)
    public ArrayList<LegalMove> getAllPossibleValidMoves() {
        allPossibleValidMoves.clear();

        // If no moves available, return empty array
        ArrayList<ArrayList<LegalMove>> possibleMoves = getAllPossibleMoves();
        if (possibleMoves.size() == 0)
            return allPossibleValidMoves;

        for (ArrayList<LegalMove> movesArray : possibleMoves) {
            for (LegalMove moves : movesArray) {
                if (moves.getMoveAfter().get(0) == null && moves.getMoveAfter().size() == 1) // If move is "final move" (has no more jumps)
                    if (canJump) { //Am I forced to jump or not
                        if (moves.getJumpedTile() != null) { // Am forced to jump, only add pieces that can jump
                            allPossibleValidMoves.add(moves);
                        }
                    } else {
                        allPossibleValidMoves.add(moves); // Not forced to jump, add non jumped piece
                    }
            }
        }

        // Return values and reset jump flag
        canJump = false;
        return allPossibleValidMoves;
    }

    // Returns every single move that every single piece can make
    private ArrayList<ArrayList<LegalMove>> getAllPossibleMoves() {
        allPossibleMoves.clear();

        for (CheckerPiece piece : pieces) {
            ArrayList<LegalMove> temp = piece.getAllMoves();

            if (temp == null) {
                //System.out.println("Null moves");

            } else if (!temp.isEmpty() && !piece.isCaptured()) {
                allPossibleMoves.add(temp);

                for (LegalMove move : temp) {
                    if (move.getJumpedTile() != null) { //Is there a jumped move
                        canJump = true;
                        break;
                    }
                }
            } else {
                for (LegalMove move : temp) {
                    if (move.getJumpedTile() != null) { //Is there a jumped move
                        canJump = true;
                        break;
                    }
                }
            }
        }

        return allPossibleMoves;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void movePiece(LegalMove move) throws InvalidMoveException {
        boolean isValid = false;

        if (getAllPossibleValidMoves().isEmpty())
            throw new InvalidMoveException("Player has no possible moves");

        for (LegalMove validMove : getAllPossibleValidMoves()) {
            if (validMove.equals(move)) {
                isValid = true;
                break;
            }
        }
        if (isValid) { // Valid Move
            CheckerPiece currentPiece = move.getRootMove().getOldPiece();

            move.captureJumpedPieces();
            currentPiece.movePiece(move.getNewTile());

            // Piece has reached king spot!
            if (checkKing(move.getNewTile())) {
                currentPiece.makeKing();
            }
        } else { // Not valid
            throw new InvalidMoveException("Move is not valid!");
        }
    }

    public void setNetwork(NEATNetwork network) {
        this.network = network;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public NEATNetwork getNetwork() {
        return network;
    }

    public abstract PieceColors getTeamColor();

    public abstract double[] convertBoard();

    protected abstract boolean checkKing(GameBoardTile tile);
}