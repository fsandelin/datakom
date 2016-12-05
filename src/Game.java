public class Game{
    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        GameThread game = new GameThread(800, 600);
        System.out.println(game);
	game.run();
    }
}
