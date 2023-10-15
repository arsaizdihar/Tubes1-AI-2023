import javafx.scene.control.Button;

public class BotLocalSearch extends Bot {
    public int[] move() {
        // TODO : Implement Local Search Algorithm
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    @Override
    public int[] move(Button[][] board, int Xscore, int Oscore) {
        return new int[0];
    }
}
