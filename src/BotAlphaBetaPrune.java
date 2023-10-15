import javafx.scene.control.Button;

public class BotAlphaBetaPrune extends Bot {
    private int filled = 8; //diset pake berapa blok yang udah keisi duluan
    private int maxDepth = 1;

    private int currentDepth;
    private final char ownSymbol;
    private final char enemySymbol;


    public BotAlphaBetaPrune(boolean first, int nOfRounds) {
        if (first) {
            filled--;
            ownSymbol = 'X';
            enemySymbol = 'O';
            this.currentDepth = 0;
        } else {
            ownSymbol = 'O';
            this.currentDepth = 1;
            enemySymbol = 'X';
        }
        this.maxDepth = nOfRounds * 2;
    }

    @Override
    public int[] move(Button[][] board, int Xscore, int Oscore) {
        CharMatrix matrix = new CharMatrix(board);

        filled += 1;
        int boardsize = matrix.getColCount() * matrix.getRowCount();
        if (boardsize - filled < maxDepth) maxDepth = boardsize - filled;

        System.out.println("filled is: " + filled);

        int diff;
        if (ownSymbol == 'X')
            diff = Xscore - Oscore;
        else
            diff = Oscore - Xscore;
        System.out.println("Score is: " + diff);

        System.out.println("Result (diff, x, y):");
        int[] result = minimax(matrix, Integer.MIN_VALUE, Integer.MAX_VALUE, diff, -1, -1, currentDepth, true);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);

        filled += 1;
        currentDepth += 2;

        return new int[]{result[2], result[1]};
    }

    public int[] minimax(
            CharMatrix board,
            int alpha,
            int beta,
            int diff,
            int xloc,
            int yloc,
            int depth,
            boolean ismax
    ) {
        if (depth == maxDepth) {
            return new int[]{diff, xloc, yloc};
        }
        int coorx = xloc;
        int coory = yloc;

        if (ismax) {
            int maxval = Integer.MIN_VALUE;
            for (int y = 0; y < board.getRowCount(); y++) {
                for (int x = 0; x < board.getColCount(); x++) {
                    if (board.getCol(x, y) != 'n') continue;
                    BoardChange boardChange = new BoardChange();
                    int value = board.addSymbol(ownSymbol, x, y, boardChange);
                    int[] result = minimax(board, alpha, beta, diff + value, x, y, depth + 1, false);
                    boardChange.rollback(board);
                    maxval = Math.max(result[0], maxval);
                    if (alpha < maxval) {
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
            for (int y = 0; y < board.getRowCount(); y++) {
                for (int x = 0; x < board.getColCount(); x++) {
                    if (board.getCol(x, y) != 'n') continue;
                    BoardChange boardChange = new BoardChange();
                    int value = board.addSymbol(enemySymbol, x, y, boardChange);
                    int[] result = minimax(board, alpha, beta, diff - value, x, y, depth + 1, true);
                    boardChange.rollback(board);

                    minval = Math.min(result[0], minval);
                    if (beta > minval) {
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
}
