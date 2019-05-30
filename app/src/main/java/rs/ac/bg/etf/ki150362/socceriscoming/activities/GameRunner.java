package rs.ac.bg.etf.ki150362.socceriscoming.activities;

public class GameRunner extends Thread{

    private Game game;
    private volatile boolean running = true;

    private GameState gameState;

    public GameRunner(Game game) {
        this.game = game;
    }

    public GameRunner(Game game, GameState gameState){
        this(game);
        this.gameState = gameState;
    }

    @Override
    public void run() {

        game.init();

        long lastTime = System.currentTimeMillis();

        while(running) {
            // draw stuff
            /*try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            long now = System.currentTimeMillis();
            long elapsed = now - lastTime;

            // TODO: check part 70 12:00
            // when elapsed is very big

            game.update(elapsed);
            game.draw();

            lastTime = now;
        }

    }

    public void shutdown() {
        running = false;
    }
}
