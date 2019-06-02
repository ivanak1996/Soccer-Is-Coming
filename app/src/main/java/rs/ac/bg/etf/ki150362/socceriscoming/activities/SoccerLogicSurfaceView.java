package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.activities.gaming.GameStartActivity;

import static android.app.Activity.RESULT_OK;

public class SoccerLogicSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SoccerFieldView soccerFieldView;

    private GameRunner runner;
    private Game game;
    private TextView timeLeftTextView;
    //private Handler gameFinishHandler;

    private InitializerStrategy initStrategy;

    public SoccerLogicSurfaceView(Context context, SoccerFieldView soccerFieldView, InitializerStrategy initStrategy, TextView timeLeftTextView) {
        super(context);

        this.soccerFieldView = soccerFieldView;
        this.initStrategy = initStrategy;
        this.timeLeftTextView = timeLeftTextView;

        setZOrderOnTop(true);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSPARENT);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(game.isGameFinished()) {
            Intent result = new Intent();
            result.putExtra(GameStartActivity.EXTRA_FINISHED_GAME_MESSAGE, "Game Finished");
            ((Activity) getContext()).setResult(RESULT_OK, result);
            ((Activity) getContext()).finish();
        } else
            game.onTouchEvent(event);
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d("SoccerLogicSurfaceView", "surfaceCreated called");

        Typeface gameOfThronesTypeface = ResourcesCompat.getFont(getContext(), R.font.got);

        game = new Game(soccerFieldView, holder, getResources(), gameOfThronesTypeface, initStrategy);
        runner = new GameRunner(game);
        runner.timeLeftTextView = timeLeftTextView;

        runner.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("SoccerLogicSurfaceView", "surfaceChanged called");
        game.onSurfaceChanged(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("SoccerLogicSurfaceView", "surfaceDestroyed called");

        if (runner != null) {
            runner.finishGame();
            game.saveGame(getContext());
        }
    }

    public void pauseGame() {
        if (runner != null) {
            runner.pauseGame();
        }
    }

    public void resumeGame() {
        if (runner != null) {
            runner.resumeGame();
        }
    }
}
