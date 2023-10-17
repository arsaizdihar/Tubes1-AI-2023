package bot;

import javafx.util.Pair;

import java.util.List;

public class Chromosome {
    public int value;
    public List<Integer> gene;

    protected char[][] board;

    protected int rowCount = 8;
    protected int colCount = 8;

    private final char ownSymbol;

    public Chromosome(char ownSymbol) {
        this.value = 0;
        this.ownSymbol = ownSymbol;
        this.gene = null;
        this.board = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = 'n';
            }
        }
        this.setCol(6, 0, 'O');
        this.setCol(7, 0, 'O');
        this.setCol(6, 1, 'O');
        this.setCol(7, 1, 'O');

        this.setCol(0, 6, 'X');
        this.setCol(0, 7, 'X');
        this.setCol(1, 6, 'X');
        this.setCol(1, 7, 'X');
        this.evaluateAll();
    }

    public Chromosome(List<Integer> gene, char ownSymbol) {
        this.gene = gene;
        this.ownSymbol = ownSymbol;
        this.board = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = 'n';
            }
        }

        this.setCol(6, 0, 'O');
        this.setCol(7, 0, 'O');
        this.setCol(6, 1, 'O');
        this.setCol(7, 1, 'O');

        this.setCol(0, 6, 'X');
        this.setCol(0, 7, 'X');
        this.setCol(1, 6, 'X');
        this.setCol(1, 7, 'X');

        this.evaluateAll();
    }

    public int getRowCount() {
        return rowCount;
    }
    public int getColCount() {
        return colCount;
    }

    protected Pair<Integer, Integer> translate(int iGene) {
        // Translating index into coordinate
        Integer x = iGene % 8;
        Integer y = iGene / 8;

        return new Pair<>(x, y);
    }

    public void updateValue() {
        // Update chromosome value
        int value = 0;
        char symbol;
        if (this.ownSymbol == 'X') {
            symbol = 'O';
        } else {
            symbol = 'X';
        }
        for (char[] row : this.board) {
            for (char el : row) {
                if (el == this.ownSymbol) {
                    value += 1;
                } else if (el == symbol) {
                    value -= 1;
                }
            }
        }

        this.value = value;
    }

    protected void setCol(int x, int y, char symbol) {
        // Set row and col value of board
        this.board[y][x] = symbol;
    }

    public void evaluateAll() {
        // Evaluates all the gene in chromosome
        boolean alt = ('X' == this.ownSymbol);
        for (Integer i : this.gene) {
            Pair<Integer, Integer> coordinate = translate(i);
            if (alt) {
                this.addSymbol(coordinate.getKey(), coordinate.getValue(), 'X');
            } else {
                this.addSymbol(coordinate.getKey(), coordinate.getValue(), 'O');
            }
            alt = !alt;
        }
        this.updateValue();
        // System.out.println("Value chromosome: " + this.value);
    }

    public void insertGene(Integer i) {
        // Insert a new gene
        this.gene.add(i);
        this.evaluateAll();
    }

    public int addSymbol(int x, int y, char symbol) {
        // Board logic when adding a symbol
        if (this.board[y][x] != 'n') return -1;

        int value = 1;
        this.setCol(x, y, symbol);

        if (symbol == 'X') {
            if (x > 0) {
                if (this.board[y][x - 1] == 'O') {
                    value += 2;
                    this.setCol(x - 1, y, 'X');
                }

                if (y > 0) {
                    if (this.board[y - 1][x - 1] == 'O') {
                        value += 2;
                        this.setCol(x - 1, y - 1, 'X');
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (this.board[y + 1][x - 1] == 'O') {
                        value += 2;
                        this.setCol(x - 1, y + 1, 'X');
                    }
                }
            }

            if (x < this.getColCount() - 1) {
                if (this.board[y][x + 1] == 'O') {
                    value += 2;
                    this.setCol(x + 1, y, 'X');
                }

                if (y > 0) {
                    if (this.board[y - 1][x + 1] == 'O') {
                        value += 2;
                        this.setCol(x + 1, y - 1, 'X');
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (this.board[y + 1][x + 1] == 'O') {
                        value += 2;
                        this.setCol(x + 1, y + 1, 'X');
                    }
                }
            }

            if (y > 0) {
                if (this.board[y - 1][x] == 'O') {
                    value += 2;
                    this.setCol(x, y - 1, 'X');
                }
            }

            if (y < this.getRowCount() - 1) {
                if (this.board[y + 1][x] == 'O') {
                    value += 2;
                    this.setCol(x, y + 1, 'X');
                }
            }
        } else if (symbol == 'O') {
            if (x > 0) {
                if (this.board[y][x - 1] == 'X') {
                    value += 2;
                    this.setCol(x - 1, y, 'O');
                }

                if (y > 0) {
                    if (this.board[y - 1][x - 1] == 'X') {
                        value += 2;
                        this.setCol(x - 1, y - 1, 'O');
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (this.board[y + 1][x - 1] == 'X') {
                        value += 2;
                        this.setCol(x - 1, y + 1, 'O');
                    }
                }
            }

            if (x < this.getColCount() - 1) {
                if (this.board[y][x + 1] == 'X') {
                    value += 2;
                    this.setCol(x + 1, y, 'O');
                }

                if (y > 0) {
                    if (this.board[y - 1][x + 1] == 'X') {
                        value += 2;
                        this.setCol(x + 1, y - 1, 'O');
                    }
                }

                if (y < this.getRowCount() - 1) {
                    if (this.board[y + 1][x + 1] == 'X') {
                        value += 2;
                        this.setCol(x + 1, y + 1, 'O');
                    }
                }
            }

            if (y > 0) {
                if (this.board[y - 1][x] == 'X') {
                    value += 2;
                    this.setCol(x, y - 1, 'O');
                }
            }

            if (y < this.getRowCount() - 1) {
                if (this.board[y + 1][x] == 'X') {
                    value += 2;
                    this.setCol(x, y + 1, 'O');
                }
            }
        }

        return value;
    }
}
