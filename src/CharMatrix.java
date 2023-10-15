import javafx.scene.control.Button;

import java.util.Optional;

public class CharMatrix {
    private final char[][] val;
    private final int colCount;
    private final int rowCount;

    public int getColCount() {
        return colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public char[] getRow(int y) {
        return val[y];
    }

    public CharMatrix(Button[][] board) {
        this.rowCount = board.length;
        this.colCount = board[0].length;
        val = new char[this.rowCount][this.colCount];

        for (int i = 0; i < this.rowCount; i++) {
            for (int j = 0; j < this.colCount; j++) {
                if (board[i][j].getText().length() > 0) {
                    val[i][j] = board[i][j].getText().charAt(0);
                } else {
                    val[i][j] = 'n';
                }
            }
        }

    }

    public CharMatrix(CharMatrix copy) {
        this.rowCount = copy.getRowCount();
        this.colCount = copy.getColCount();
        this.val = new char[copy.getRowCount()][copy.getColCount()];
        for (int i = 0; i < copy.getRowCount(); i++) {
            System.arraycopy(copy.getRow(i), 0, val[i], 0, copy.getColCount());
        }
    }

    public char getCol(int x, int y) {
        return this.val[y][x];
    }

    public void setCol(int x, int y, char value) {
        this.val[y][x] = value;
    }

    public int addSymbol(char symbol, int x, int y) {
        return addSymbol(symbol, x, y, null);
    }

    public int addSymbol(char symbol, int x, int y, BoardChange boardChange) {
        if (this.val[y][x] != 'n') return -1;

        int value = 1;
        if (boardChange == null) {
            boardChange = new BoardChange();
        }
        boardChange.reset();
        this.val[y][x] = symbol;
        boardChange.addChange(x, y, 'n');

        if (symbol == 'X') {
            if (x > 0) {
                if (this.val[y][x - 1] == 'O') {
                    value += 2;
                    this.val[y][x - 1] = 'X';
                    boardChange.addChange(x - 1, y, 'O');
                }

                if (y > 0) {
                    if (this.val[y - 1][x - 1] == 'O') {
                        value += 2;
                        this.val[y - 1][x - 1] = 'X';
                        boardChange.addChange(x - 1, y - 1, 'O');
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (this.val[y + 1][x - 1] == 'O') {
                        value += 2;
                        this.val[y + 1][x - 1] = 'X';
                        boardChange.addChange(x - 1, y + 1, 'O');
                    }
                }
            }

            if (x < this.getColCount() - 1) {
                if (this.val[y][x + 1] == 'O') {
                    value += 2;
                    this.val[y][x + 1] = 'X';
                    boardChange.addChange(x + 1, y, 'O');
                }

                if (y > 0) {
                    if (this.val[y - 1][x + 1] == 'O') {
                        value += 2;
                        this.val[y - 1][x + 1] = 'X';
                        boardChange.addChange(x + 1, y - 1, 'O');
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (this.val[y + 1][x + 1] == 'O') {
                        value += 2;
                        this.val[y + 1][x + 1] = 'X';
                        boardChange.addChange(x + 1, y + 1, 'O');
                    }
                }
            }

            if (y > 0) {
                if (this.val[y - 1][x] == 'O') {
                    value += 2;
                    this.val[y - 1][x] = 'X';
                    boardChange.addChange(x, y - 1, 'O');
                }
            }

            if (y < this.getRowCount() - 1) {
                if (this.val[y + 1][x] == 'O') {
                    value += 2;
                    this.val[y + 1][x] = 'X';
                    boardChange.addChange(x, y + 1, 'O');
                }
            }
        } else if (symbol == 'O') {
            if (x > 0) {
                if (this.val[y][x - 1] == 'X') {
                    value += 2;
                    this.val[y][x - 1] = 'O';
                    boardChange.addChange(x - 1, y, 'X');
                }

                if (y > 0) {
                    if (this.val[y - 1][x - 1] == 'X') {
                        value += 2;
                        this.val[y - 1][x - 1] = 'O';
                        boardChange.addChange(x - 1, y - 1, 'X');
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (this.val[y + 1][x - 1] == 'X') {
                        value += 2;
                        this.val[y + 1][x - 1] = 'O';
                        boardChange.addChange(x - 1, y + 1, 'X');
                    }
                }
            }

            if (x < this.getColCount() - 1) {
                if (this.val[y][x + 1] == 'X') {
                    value += 2;
                    this.val[y][x + 1] = 'O';
                    boardChange.addChange(x + 1, y, 'X');
                }

                if (y > 0) {
                    if (this.val[y - 1][x + 1] == 'X') {
                        value += 2;
                        this.val[y - 1][x + 1] = 'O';
                        boardChange.addChange(x + 1, y - 1, 'X');
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (this.val[y + 1][x + 1] == 'X') {
                        value += 2;
                        this.val[y + 1][x + 1] = 'O';
                        boardChange.addChange(x + 1, y + 1, 'X');
                    }
                }
            }

            if (y > 0) {
                if (this.val[y - 1][x] == 'X') {
                    value += 2;
                    this.val[y - 1][x] = 'O';
                    boardChange.addChange(x, y - 1, 'X');
                }
            }

            if (y < this.getRowCount() - 1) {
                if (this.val[y + 1][x] == 'X') {
                    value += 2;
                    this.val[y + 1][x] = 'O';
                    boardChange.addChange(x, y + 1, 'X');
                }
            }
        }

        return value;
    }
}
