package neural; /**
 * Created by ethanm on 1/25/17.
 */

import checkers.CheckersGame;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

public class NeuralPlayerRandom {
    private NEATNetwork network;

    public NeuralPlayerRandom(NEATNetwork network) {
        this.network = network;
    }


    public int scorePlayer() {
        int n = 0;
        for (int i=0; i<100; i++) {
            System.out.print("I: " + i + " ");
            n += this.doIteration();
        }
        return n/2;
    }

    private int doIteration() {

        CheckersGame game = new CheckersGame();
        game.initializeGame();
        game.getBluePlayer().setNetwork(this.network);
        //game.playGUI();
        while (game.getWinner() == -2) {
            game.turn();
        }
        return game.getWinner();
    }
}

