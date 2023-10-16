package bot;

import board.Board;
import javafx.scene.control.Button;

public abstract class Bot {
    private final Board board;

    protected Bot(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }


    public abstract int[] move(int Xscore, int Oscore);

    protected int getObjectiveFunc() {
        return 0;
    }
}
