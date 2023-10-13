public class BotLocalSearch extends Bot {
    @Override
    public int[] move() {
        // TODO : Implement Local Search Algorithm
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }
}
