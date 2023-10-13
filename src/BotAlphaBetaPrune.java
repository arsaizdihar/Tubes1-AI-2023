import javafx.scene.control.Button;

public class BotAlphaBetaPrune extends Bot {
    private int filled = 4; //diset pake berapa blok yang udah keisi duluan
    private int maxdepth = 1;
    private final char ownsymbol = 'O';
    private final char enemysymbol = 'X';

    public BotAlphaBetaPrune(boolean first){
        if(first) filled--;
    }

    public int[] move(Button[][] board, int Xscore, int Oscore) {
        CharMatrix matrix = new CharMatrix(board);

        filled += 1;
        int boardsize = matrix.getColCount() * matrix.getRowCount();
        if(boardsize - filled < maxdepth) maxdepth = boardsize - filled;

        System.out.println("filled is: " + filled);

        int diff;
        diff = Oscore - Xscore;
        System.out.println("Score is: " + diff);

        System.out.println("Result (diff, x, y):");
        int[] result = minimax(matrix, Integer.MIN_VALUE, Integer.MAX_VALUE, diff, -1, -1, 0, true);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);

        filled += 1;


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
    ){
        if(depth == maxdepth){
            return new int[]{diff, xloc, yloc};
        }
        int coorx = xloc;
        int coory = yloc;

        if(ismax){
            int maxval = Integer.MIN_VALUE;
            for(int y = 0; y < board.getRowCount(); y++){
                for(int x = 0; x < board.getColCount(); x++){
                    CharMatrix tempBoard = new CharMatrix(board);
                    int value = tempBoard.addSymbol(ownsymbol, x, y);

                    if(value != -1){
                        int[] result = minimax(tempBoard, alpha, beta, diff + value, x, y, depth - 1, false);
                        maxval = Math.max(result[0], maxval);
                        if(alpha < maxval){
                            alpha = maxval;
                            coorx = x;
                            coory = y;
                        }
                        if(beta <= alpha) break;
                    }
                }
                if(beta <= alpha) break;
            }

            return new int[]{maxval, coorx, coory};
        }

        else{
            int minval = Integer.MAX_VALUE;
            for(int y = 0; y < board.getRowCount(); y++){
                for(int x = 0; x < board.getColCount(); x++){
                    CharMatrix tempBoard = new CharMatrix(board);
                    int value = tempBoard.addSymbol(enemysymbol, x, y);

                    if(value != -1){
                        int[] result = minimax(tempBoard, alpha, beta, diff - value, x, y, depth - 1, true);
                        minval = Math.min(result[0], minval);
                        if(beta > minval){
                            beta = minval;
                            coorx = x;
                            coory = y;
                        }
                        if(beta <= alpha) break;
                    }
                }
                if(beta <= alpha) break;
            }
            return new int[]{minval, coorx, coory};
        }
    }

    @Override
    public int[] move() {
        return new int[0];
    }
}
