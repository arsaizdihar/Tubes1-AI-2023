package bot;

public interface Fallbackable {
    int[] fallback(int Xscore, int Oscore);

    void onFastSuccess();
}
