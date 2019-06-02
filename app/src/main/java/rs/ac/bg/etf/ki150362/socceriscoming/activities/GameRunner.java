package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class GameRunner extends Thread{

    private Game game;
    Handler handler = new Handler();

    public TextView timeLeftTextView;

    private volatile boolean running = true, finished = false;
    private long pausedTime, resumedTime, lastTime;

    public GameRunner(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

        game.init();

        lastTime = System.currentTimeMillis();

        try {
            while (!interrupted()) {
                synchronized (this) {
                    if (!running) wait();
                }

                long now = System.currentTimeMillis();
                long elapsed = now - lastTime;

                finished = game.isGameFinished();
                game.update(elapsed);
                game.draw();
                setTimeElapsed();
                lastTime = now;

                if(finished) break;
            }
        } catch (InterruptedException ie) {
        }

    }

    private void setTimeElapsed() {

        if (timeLeftTextView == null || game == null) return;

        final long timeLeft = 300000 - game.elapsedInTotal;

        // if(timeLeftToPlay<0) || game.leadingScore >= game.goalsToWin ... TODO implement this
        /*if (timeLeft < 0 || game.leadingScore >= 3) {
            finished = true;
            return;
        }*/

        @SuppressLint("DefaultLocale") final String timeLeftString = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeLeft) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft)),
                TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft)));

        handler.post(new Runnable() {
            @Override
            public void run() {
                timeLeftTextView.setText(timeLeftString);
                if (timeLeft < 15000)
                    timeLeftTextView.setTextColor(Color.RED);
            }
        });
    }

    public synchronized void resumeGame() {
        Log.d("GameRunner", "Game RESUMED");
        resumedTime = System.currentTimeMillis();
        lastTime += resumedTime - pausedTime;
        running = true;
        notify();
    }

    public synchronized void pauseGame() {
        Log.d("GameRunner", "Game PAUSED");
        pausedTime = System.currentTimeMillis();
        running = false;
    }

    public void finishGame() {
        Log.d("GameRunner", "Game FINISHED");
        interrupt();
    }

}
