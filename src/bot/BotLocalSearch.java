package bot;

import board.Board;
import bot.Bot;

public class BotLocalSearch extends Bot {
    public BotLocalSearch(Board board) {
        super(board);
    }

    public int[] move() {
        // TODO : Implement Local Search Algorithm
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    @Override
    public int[] move(int Xscore, int Oscore) {
        return new int[0];
    }
}
