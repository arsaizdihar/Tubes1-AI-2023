package board;

import javafx.scene.control.Button;

public class Board {
    private final int colCount;
    private final int rowCount;
    private final Button[][] buttons;
    private final char[][] matrix;

    public Board(Button[][] buttons) {
        this.buttons = buttons;
        this.rowCount = buttons.length;
        this.colCount = buttons[0].length;
        matrix = new char[this.rowCount][this.colCount];

        for (int i = 0; i < this.rowCount; i++) {
            for (int j = 0; j < this.colCount; j++) {
                if (buttons[i][j].getText().length() > 0) {
                    matrix[i][j] = buttons[i][j].getText().charAt(0);
                } else {
                    matrix[i][j] = 'n';
                }
            }
        }
    }

    public int getColCount() {
        return colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public char[] getRow(int y) {
        return matrix[y];
    }

    public char getCol(int x, int y) {
        return matrix[y][x];
    }


    public void setButton(int x, int y, char c) {
        buttons[y][x].setText(String.valueOf(c));
        setCol(x, y, c);
    }

    public void setCol(int x, int y, char value) {
        matrix[y][x] = value;
    }

    public void setCol(int x, int y, char value, BoardChange boardChange) {
        if (boardChange != null) boardChange.addChange(x, y, matrix[y][x]);
        setCol(x, y, value);
    }

    public int addSymbol(char symbol, int x, int y, BoardChange boardChange) {
        if (matrix[y][x] != 'n') return -1;

        int value = 1;
        if (boardChange != null) {
            boardChange.reset();
        }
        setCol(x, y, symbol, boardChange);

        if (symbol == 'X') {
            if (x > 0) {
                if (matrix[y][x - 1] == 'O') {
                    value += 2;
                    setCol(x - 1, y, 'X', boardChange);
                }

                if (y > 0) {
                    if (matrix[y - 1][x - 1] == 'O') {
                        value += 2;
                        setCol(x - 1, y - 1, 'X', boardChange);
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (matrix[y + 1][x - 1] == 'O') {
                        value += 2;
                        setCol(x - 1, y + 1, 'X', boardChange);
                    }
                }
            }

            if (x < this.getColCount() - 1) {
                if (matrix[y][x + 1] == 'O') {
                    value += 2;
                    setCol(x + 1, y, 'X', boardChange);
                }

                if (y > 0) {
                    if (matrix[y - 1][x + 1] == 'O') {
                        value += 2;
                        setCol(x + 1, y - 1, 'X', boardChange);
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (matrix[y + 1][x + 1] == 'O') {
                        value += 2;
                        setCol(x + 1, y + 1, 'X', boardChange);
                    }
                }
            }

            if (y > 0) {
                if (matrix[y - 1][x] == 'O') {
                    value += 2;
                    setCol(x, y - 1, 'X', boardChange);
                }
            }

            if (y < this.getRowCount() - 1) {
                if (matrix[y + 1][x] == 'O') {
                    value += 2;
                    setCol(x, y + 1, 'X', boardChange);
                }
            }
        } else if (symbol == 'O') {
            if (x > 0) {
                if (matrix[y][x - 1] == 'X') {
                    value += 2;
                    setCol(x - 1, y, 'O', boardChange);
                }

                if (y > 0) {
                    if (matrix[y - 1][x - 1] == 'X') {
                        value += 2;
                        setCol(x - 1, y - 1, 'O', boardChange);
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (matrix[y + 1][x - 1] == 'X') {
                        value += 2;
                        setCol(x - 1, y + 1, 'O', boardChange);
                    }
                }
            }

            if (x < this.getColCount() - 1) {
                if (matrix[y][x + 1] == 'X') {
                    value += 2;
                    setCol(x + 1, y, 'O', boardChange);
                }

                if (y > 0) {
                    if (matrix[y - 1][x + 1] == 'X') {
                        value += 2;
                        setCol(x + 1, y - 1, 'O', boardChange);
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (matrix[y + 1][x + 1] == 'X') {
                        value += 2;
                        setCol(x + 1, y + 1, 'O', boardChange);
                    }
                }
            }

            if (y > 0) {
                if (matrix[y - 1][x] == 'X') {
                    value += 2;
                    setCol(x, y - 1, 'O', boardChange);
                }
            }

            if (y < this.getRowCount() - 1) {
                if (matrix[y + 1][x] == 'X') {
                    value += 2;
                    setCol(x, y + 1, 'O', boardChange);
                }
            }
        }

        return value;
    }
}
