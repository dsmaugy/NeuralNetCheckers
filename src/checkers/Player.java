package checkers;

import org.encog.ml.CalculateScore;
import org.encog.neural.neat.NEATNetwork;

import java.util.ArrayList;

/**
 * Created by Darwin on 1/31/2017.
 */
public interface Player  {
    boolean allPiecesCaptured();

    CheckerPiece[] getPieces();
    ArrayList<LegalMove> getAllPossibleValidMoves();
    GameBoard getBoard();
    void setNetwork(NEATNetwork network);
    NEATNetwork getNetwork();
}
