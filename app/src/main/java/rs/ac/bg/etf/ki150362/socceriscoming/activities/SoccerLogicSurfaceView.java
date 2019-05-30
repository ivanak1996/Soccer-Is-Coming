package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class SoccerLogicSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SoccerFieldView soccerFieldView;

    private GameRunner runner;
    private Game game;

    private InitializerStrategy initStrategy;

    public SoccerLogicSurfaceView(Context context, SoccerFieldView soccerFieldView, InitializerStrategy initStrategy) {
        super(context);

        this.soccerFieldView = soccerFieldView;
        this.initStrategy = initStrategy;

        setZOrderOnTop(true);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSPARENT);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        game.onTouchEvent(event);
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // called when surface is displayed
        // setupSurface();
        Log.d("SoccerLogicSurfaceView", "surfaceCreated called");

        Typeface gameOfThronesTypeface = ResourcesCompat.getFont(getContext(), R.font.got);

        game = new Game(soccerFieldView, holder, getResources(), gameOfThronesTypeface, initStrategy);
        runner = new GameRunner(game);

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
            runner.shutdown();

            try {
                while (runner != null) {
                    runner.join();
                    runner = null;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                game.saveGame(getContext());
            }
        }
    }
}
