package bot;

import board.Board;
import board.BoardChange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BotLocalSearch extends Bot {
    private final char ownSymbol;
    public BotLocalSearch(Board board, boolean first) {
        super(board);
        this.ownSymbol = first ? 'X' : 'O';
    }

    @Override
    public int[] move(int Xscore, int Oscore) {
        int obj = Integer.MIN_VALUE;
        ArrayList<int []> locations = new ArrayList<>();
        Board board = this.getBoard();
        for (int y = 0; y < getBoard().getRowCount(); y++) {
            for (int x = 0; x < getBoard().getColCount(); x++) {
                if (this.getBoard().getCol(x, y) != 'n') continue;
                BoardChange boardChange = new BoardChange();
                int val = board.addSymbol(ownSymbol, x, y, boardChange);
                boardChange.rollback(board);
                if (val > obj) {
                    obj = val;
                    locations.clear();
                    locations.add(new int[]{y, x});
                } else if (val == obj) {
                    locations.add(new int[]{y, x});
                }
            }
        }
        
        return locations.get(new Random().nextInt(locations.size()));
    }
}
