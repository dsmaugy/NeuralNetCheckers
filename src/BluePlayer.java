import java.util.ArrayList;

/**
 * Represents the Blue Player
 */
public class BluePlayer {

    private CheckerPiece[] bluePieces = new CheckerPiece[12];

    public BluePlayer() {

    }

    public CheckerPiece[] getPieces() {
        return bluePieces;
    }

    // Returns every single move that every single piece can make
    private ArrayList<ArrayList<LegalMove>> getAllPossibleMoves() {
        ArrayList<ArrayList<LegalMove>> allPossibleMoves = new ArrayList<>();

        for (CheckerPiece piece : bluePieces) {
            ArrayList<LegalMove> temp = piece.getAllMoves();

            if (temp == null) {
                System.out.println("Null moves");

            } else if (!temp.isEmpty() && !piece.isCaptured())
                allPossibleMoves.add(temp);
        }

        return allPossibleMoves;
    }

    // Returns array of valid moves (factoring in force jumping)
    public ArrayList<LegalMove> getAllPossibleValidMoves() {
        ArrayList<LegalMove> allPossibleValidMoves = new ArrayList<>();

        for (ArrayList<LegalMove> movesArray : getAllPossibleMoves()) {
            for (LegalMove moves : movesArray) {
                if (moves.getMoveAfter().get(0) == null && moves.getMoveAfter().size() == 1)
                    allPossibleValidMoves.add(moves);
            }
        }

        return allPossibleValidMoves;
    }

    public void movePiece(LegalMove move) throws InvalidMoveException {
        boolean isValid = false;

        if (getAllPossibleValidMoves().isEmpty())
            throw new InvalidMoveException("Blue Player has no possible moves");

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
            if (move.getNewTile().returnY() == 0) {
                currentPiece.makeKing();
            }
        } else { // Not valid
            throw new InvalidMoveException("Move is not valid!");
        }
    }
}
