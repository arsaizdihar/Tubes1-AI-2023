import board.Board;
import bot.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.Cursor;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * The OutputFrameController class.  It controls button input from the users when
 * playing the game.
 *
 * @author Jedid Ahn
 */
public class OutputFrameController {
    @FXML
    private GridPane gameBoard;

    @FXML
    private GridPane scoreBoard;

    @FXML
    private Label roundsLeftLabel;
    @FXML
    private Label playerXName;
    private String playerXType;
    @FXML
    private Label playerOName;
    private String playerOType;
    @FXML
    private HBox playerXBoxPane;
    @FXML
    private HBox playerOBoxPane;
    @FXML
    private Label playerXScoreLabel;
    @FXML
    private Label playerOScoreLabel;
    @FXML
    private Button nextButton;


    private boolean playerXTurn;
    private int playerXScore;
    private int playerOScore;
    private int roundsLeft;
    private boolean isPlayerOFirst;
    private Bot botX = null;
    private Bot botO;

    private static final int ROW = 8;
    private static final int COL = 8;
    private Button[][] buttons = new Button[ROW][COL];
    private Board board;


    /**
     * Set the name of player X (player) to be name1, set the name of player O (bot) to be name2,
     * and the number of rounds played to be rounds. This input is received from
     * the input frame and is output in the score board of the output frame.
     *
     * @param name1          Name of Player 1 (Player).
     * @param name2          Name of Player 2 (Bot).
     * @param rounds         The number of rounds chosen to be played.
     * @param isPlayer2First True if bot is first, false otherwise.
     */
    void getInput(String name1, String type1, String name2, String type2, String rounds, boolean isPlayer2First) {
        this.playerXName.setText(name1);
        this.playerXType = type1;
        this.playerOName.setText(name2);
        this.playerOType = type2;
        this.roundsLeftLabel.setText(rounds);
        this.roundsLeft = Integer.parseInt(rounds);
        this.isPlayerOFirst = isPlayer2First;

        if (this.playerXType.equals("Human")) {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    // Add ActionListener to each button such that when it is clicked, it calls
                    // the selected coordinates method with its i and j coordinates.
                    final int finalI = i;
                    final int finalJ = j;
                    this.buttons[i][j].setOnAction(event -> {
                        if (this.playerXTurn) {
                            this.selectedCoordinates(finalI, finalJ);
                        }
                    });
                }
            }
        } else if (this.playerXType.equals("Bot (Minimax with Alpha-Beta Pruning)")) {
            this.botX = new BotAlphaBetaPrune(board, !isPlayer2First, this.roundsLeft);
        } else if (this.playerXType.equals("Bot (Local Search)")) {
            this.botX = new BotLocalSearch(board, !isPlayer2First);
        } else {
            // Bot (Genetic Algorithm)
            this.botX = new BotGenetic(board, !isPlayer2First, this.roundsLeft);
        }

        if (this.playerOType.equals("Bot (Minimax with Alpha-Beta Pruning)")) {
            this.botO = new BotAlphaBetaPrune(board, isPlayer2First, this.roundsLeft);
        } else if (this.playerOType.equals("Bot (Local Search)")) {
            this.botO = new BotLocalSearch(board, isPlayer2First);
        } else {
            // Bot (Genetic Algorithm)
            this.botO = new BotGenetic(board, isPlayer2First, this.roundsLeft);
        }

        this.playerXTurn = !isPlayer2First;
    }


    /**
     * Construct the 8x8 game board by creating a total of 64 buttons in a 2
     * dimensional array, and construct the 8x2 score board for scorekeeping
     * and then initialize turn and score.
     */
    @FXML
    private void initialize() {
        // Construct game board with 8 rows.
        for (int i = 0; i < ROW; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROW);
            this.gameBoard.getRowConstraints().add(rowConst);
        }

        // Construct game board with 8 columns.
        for (int i = 0; i < COL; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / COL);
            this.gameBoard.getColumnConstraints().add(colConst);
        }

        // Style buttons and construct 8x8 game board.
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.buttons[i][j] = new Button();
                this.buttons[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                this.buttons[i][j].setCursor(Cursor.HAND);
                this.gameBoard.add(this.buttons[i][j], j, i);
            }
        }

        board = new Board(buttons);

        // Setting up the initial game board with 4 X's in bottom left corner and 4 O's in top right corner.
        board.setButton(0, ROW - 2, 'X');
        board.setButton(0, ROW - 1, 'X');
        board.setButton(1, ROW - 2, 'X');
        board.setButton(1, ROW - 1, 'X');
        board.setButton(COL - 2, 0, 'O');
        board.setButton(COL - 1, 0, 'O');
        board.setButton(COL - 2, 1, 'O');
        board.setButton(COL - 1, 1, 'O');


        // Construct score board with 8 rows.
        for (int i = 0; i < ROW; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROW);
            this.scoreBoard.getRowConstraints().add(rowConst);
        }

        // Construct score board with 2 column.
        for (int i = 0; i < 2; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 2);
            this.scoreBoard.getColumnConstraints().add(colConst);
        }


        // Initialize turn and score for the game.
        this.playerXBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
        this.playerOBoxPane.setStyle("-fx-background-color: white; -fx-border-color: #D3D3D3;");
        this.playerXScoreLabel.setText("4");
        this.playerOScoreLabel.setText("4");

        this.playerXTurn = true;
        this.playerXScore = 4;
        this.playerOScore = 4;
    }


    /**
     * Process the coordinates of the button that the user selected on the game board.
     *
     * @param i The row number of the button clicked.
     * @param j The column number of the button clicked.
     */
    private void selectedCoordinates(int i, int j) {
        // Invalid when a button with an X or an O is clicked.
        if (!this.buttons[i][j].getText().equals(""))
            new Alert(Alert.AlertType.ERROR, "Invalid coordinates: Try again!").showAndWait();
            // Button must be blank.
        else {
            if (this.playerXTurn) {
                // Changed background color to green to indicate next player's turn.
                this.playerXBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                board.setButton(j, i, 'X');
                this.playerXScore++;              // Increment the score of player X.

                // Update game board by changing surrounding cells to X if applicable.
                this.updateGameBoard(i, j);
                this.playerXTurn = false;         // Alternate player's turn.

                if (isPlayerOFirst) {
                    this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have played.
                    this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                }

                if (isPlayerOFirst && this.roundsLeft == 0) {
                    this.nextButton.setDisable(true);
                    this.endOfGame();
                }
            } else {
                this.playerXBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                this.playerOBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                board.setButton(j, i, 'O');
                this.playerOScore++;

                this.updateGameBoard(i, j);
                this.playerXTurn = true;

                if (!isPlayerOFirst) {
                    this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have played.
                    this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                }

                if (!isPlayerOFirst && this.roundsLeft == 0) { // Game has terminated.
                    this.nextButton.setDisable(true);
                    this.endOfGame();       // Determine & announce the winner.
                }
            }
        }
    }

    /**
     * Change adjacent cells to X's or O's.
     *
     * @param i The row number of the button clicked.
     * @param j The column number of the button clicked.
     */
    private void updateGameBoard(int i, int j) {
        // Value of indices to control the lower/upper bound of rows and colum ns
        // in order to change surrounding/adjacent X's and O's only on the game board.
        // Four boundaries:  First & last row and first & last column.

        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0)     // If clicked button in first row, no preceding row exists.
            startRow = i;
        else               // Otherwise, the preceding row exists for adjacency.
            startRow = i - 1;

        if (i + 1 >= ROW)  // If clicked button in last row, no subsequent/further row exists.
            endRow = i;
        else               // Otherwise, the subsequent row exists for adjacency.
            endRow = i + 1;

        if (j - 1 < 0)     // If clicked on first column, lower bound of the column has been reached.
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= COL)  // If clicked on last column, upper bound of the column has been reached.
            endColumn = j;
        else
            endColumn = j + 1;


        // Search for adjacency for X's and O's or vice versa, and replace them.
        // Update scores for X's and O's accordingly.
        for (int y = startRow; y <= endRow; y++) {
            for (int x = startColumn; x <= endColumn; x++) {
                if (this.playerXTurn) {
                    if (this.buttons[y][x].getText().equals("O")) {
                        board.setButton(x, y, 'X');
                        this.playerXScore++;
                        this.playerOScore--;
                    }
                } else if (this.buttons[y][x].getText().equals("X")) {
                    board.setButton(x, y, 'O');
                    this.playerOScore++;
                    this.playerXScore--;
                }
            }
        }

        this.playerXScoreLabel.setText(String.valueOf(this.playerXScore));
        this.playerOScoreLabel.setText(String.valueOf(this.playerOScore));
    }


    /**
     * Determine and announce the winner of the game.
     */
    private void endOfGame() {
        // Player X is the winner.
        if (this.playerXScore > this.playerOScore) {
            new Alert(Alert.AlertType.INFORMATION,
                    this.playerXName.getText() + " has won the game!").showAndWait();
            this.playerXBoxPane.setStyle("-fx-background-color: CYAN; -fx-border-color: #D3D3D3;");
            this.playerOBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
            this.playerXName.setText(this.playerXName.getText() + " (Winner!)");
        }

        // Player O is the winner,
        else if (this.playerOScore > this.playerXScore) {
            new Alert(Alert.AlertType.INFORMATION,
                    this.playerOName.getText() + " has won the game!").showAndWait();
            this.playerXBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
            this.playerOBoxPane.setStyle("-fx-background-color: CYAN; -fx-border-color: #D3D3D3;");
            this.playerOName.setText(this.playerOName.getText() + " (Winner!)");
        }

        // Player X and Player O tie.
        else {
            new Alert(Alert.AlertType.INFORMATION,
                    this.playerXName.getText() + " and " + this.playerOName.getText() + " have tied!").showAndWait();
            this.playerXBoxPane.setStyle("-fx-background-color: ORANGE; -fx-border-color: #D3D3D3;");
            this.playerOBoxPane.setStyle("-fx-background-color: ORANGE; -fx-border-color: #D3D3D3;");
        }

        // Disable the game board buttons to prevent from playing further.
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                this.buttons[i][j].setDisable(true);
    }

    @FXML
    private void nextTurn() {
        if (this.playerXTurn) {
            if (!this.playerXType.equals("Human")) {
                movePlayerX();
            } else {
                // Dialog Human's turn
                Dialog<String> dialogHumansTurn = new Dialog<>();
                dialogHumansTurn.setTitle("Information");
                dialogHumansTurn.setContentText("You must select the pane");
                dialogHumansTurn.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
                dialogHumansTurn.showAndWait();
            }
        } else {
            movePlayerO();
        }
    }


    /**
     * Close OutputFrame controlled by OutputFrameController if end game button is clicked.
     */
    @FXML
    private void endGame() {
        System.exit(0);
    }


    /**
     * Reopen InputFrame controlled by InputFrameController if play new game button is clicked.
     */
    @FXML
    private void playNewGame() throws IOException {
        // Close secondary stage/output frame.
        Stage secondaryStage = (Stage) this.gameBoard.getScene().getWindow();
        secondaryStage.close();

        // Reopen primary stage/input frame.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InputFrame.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Adjacency Gameplay");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void movePlayerX() {
        int[] botMove = moveBot(this.botX);
        int i = botMove[0];
        int j = botMove[1];

        if (!this.buttons[i][j].getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Bot Invalid Coordinates. Exiting.").showAndWait();
            System.exit(1);
            return;
        }

        this.selectedCoordinates(i, j);
    }


    private int[] moveBot(Bot bot) {
        if (!(bot instanceof Fallbackable)) {
            return bot.move(this.playerXScore, this.playerOScore);
        }
        AtomicIntegerArray result = new AtomicIntegerArray(2);
        Thread t = new Thread(() -> {
            int[] move = bot.move(this.playerXScore, this.playerOScore);
            if (!Thread.currentThread().isInterrupted()) {
                result.set(0, move[0]);
                result.set(1, move[1]);
            }
        });
        t.start();
        long startTime = System.currentTimeMillis();
        try {
            t.join(4500);
            if (t.isAlive()) {
                t.interrupt();
                t.join();
            } else {
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < 3000) {
                    ((Fallbackable) bot).onFastSuccess();
                }
                System.out.println("SUCCESS");
                return new int[]{result.get(0), result.get(1)};
            }
        } catch (InterruptedException | RuntimeException ignored) {
        }
        return ((Fallbackable) bot).fallback(this.playerXScore, this.playerOScore);
    }

    private void movePlayerO() {
        int[] botMove = moveBot(this.botO);
        int i = botMove[0];
        int j = botMove[1];
        System.out.println("Bot move: " + i + " " + j);

        if (!this.buttons[i][j].getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Bot Invalid Coordinates. Exiting.").showAndWait();
            System.exit(1);
            return;
        }

        this.selectedCoordinates(i, j);
    }
}