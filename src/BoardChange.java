import java.util.ArrayList;
import java.util.Optional;

public class BoardChange {
    private final ArrayList<int[]> locations;
    private final ArrayList<Character> symbols;

    public BoardChange() {
        this.locations = new ArrayList<>();
        this.symbols = new ArrayList<>();
    }

    public void rollback(CharMatrix board) {
        for (int i = 0; i < locations.size(); i++) {
            int[] location = locations.get(i);
            board.setCol(location[0], location[1], symbols.get(i));
        }
    }

    public void reset() {
        locations.clear();
        symbols.clear();
    }

    public void addChange(int x, int y, char symbol) {
        locations.add(new int[]{x, y});
        symbols.add(symbol);
    }
}
