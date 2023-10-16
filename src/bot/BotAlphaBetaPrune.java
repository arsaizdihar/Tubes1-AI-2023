package bot;

import board.Board;
import board.BoardChange;
import bot.Bot;

public class BotAlphaBetaPrune extends Bot implements Fallbackable {
    private int nRounds = 1;
    private int currentDepth;
    private final char ownSymbol;
    private final char enemySymbol;


    public BotAlphaBetaPrune(Board board, boolean first, int nOfRounds) {
        super(board);
        if (first) {
            ownSymbol = 'X';
            enemySymbol = 'O';
            this.currentDepth = 0;
        } else {
            ownSymbol = 'O';
            this.currentDepth = 1;
            enemySymbol = 'X';
        }
        this.nRounds = nOfRounds;
    }

    public int[] move(int Xscore, int Oscore, int maxDepth) {
        int diff;
        if (ownSymbol == 'X')
            diff = Xscore - Oscore;
        else
            diff = Oscore - Xscore;

        // limiting depth to maxDepth
        int startDepth = Math.max(currentDepth, nRounds * 2 - maxDepth);
        System.out.println("START DEPTH " + startDepth);
        System.out.println(nRounds);
        int[] result = minimax(getBoard(), Integer.MIN_VALUE, Integer.MAX_VALUE, diff, -1, -1, startDepth, true);
        if (!Thread.currentThread().isInterrupted()) {
            currentDepth += 2;
        }

        return new int[]{result[2], result[1]};
    }

    @Override
    public int[] move(int Xscore, int Oscore) {
        return move(Xscore, Oscore, 8);
    }

    public int[] minimax(
            Board board,
            int alpha,
            int beta,
            int diff,
            int xloc,
            int yloc,
            int depth,
            boolean ismax
    ) {
        if (Thread.currentThread().isInterrupted() || depth == nRounds * 2) {
            return new int[]{diff, xloc, yloc};
        }
        int coorx = xloc;
        int coory = yloc;

        if (ismax) {
            int maxval = Integer.MIN_VALUE;
            outerLoop:
            for (int y = 0; y < board.getRowCount(); y++) {
                for (int x = 0; x < board.getColCount(); x++) {
                    if (Thread.currentThread().isInterrupted()) {
                        break outerLoop;
                    }
                    if (board.getCol(x, y) != 'n') continue;
                    BoardChange boardChange = new BoardChange();
                    int value = board.addSymbol(ownSymbol, x, y, boardChange);
                    int[] result = minimax(board, alpha, beta, diff + value, x, y, depth + 1, false);
                    boardChange.rollback(board);
                    maxval = Math.max(result[0], maxval);
                    if (alpha < maxval || coorx == -1) {
                        alpha = maxval;
                        coorx = x;
                        coory = y;
                    }
                    if (beta <= alpha) break;
                }
                if (beta <= alpha) break;
            }

            return new int[]{maxval, coorx, coory};
        } else {
            int minval = Integer.MAX_VALUE;
            outerLoop:
            for (int y = 0; y < board.getRowCount(); y++) {
                for (int x = 0; x < board.getColCount(); x++) {
                    if (Thread.currentThread().isInterrupted()) {
                        break outerLoop;
                    }
                    if (board.getCol(x, y) != 'n') continue;
                    BoardChange boardChange = new BoardChange();
                    int value = board.addSymbol(enemySymbol, x, y, boardChange);
                    int[] result = minimax(board, alpha, beta, diff - value, x, y, depth + 1, true);
                    boardChange.rollback(board);

                    minval = Math.min(result[0], minval);
                    if (beta > minval || coorx == -1) {
                        beta = minval;
                        coorx = x;
                        coory = y;
                    }
                    if (beta <= alpha) break;
                }
                if (beta <= alpha) break;
            }
            return new int[]{minval, coorx, coory};
        }
    }

    @Override
    public int[] fallback(int Xscore, int Oscore) {
        System.out.println("FALLBACK");
        return move(Xscore, Oscore, 2);
    }
}
