package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class SoccerLogicSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap ball;

    private GameRunner runner;
    private Game game;

    public SoccerLogicSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setZOrderOnTop(true);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSPARENT);

        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball_dragon);
        ball = Bitmap.createScaledBitmap(ball, 250, 250, false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        game.onTouchEvent(event);
        return true;
    }

    private boolean setupSurface() {
        SurfaceHolder holder = getHolder();

        Canvas canvas = holder.lockCanvas();

        if (canvas != null) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(ball, 10, 10, null);

            holder.unlockCanvasAndPost(canvas);
        }

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // called when surface is displayed
        // setupSurface();
        game = new Game(getWidth(), getHeight(), holder, getResources());
        runner = new GameRunner(game);
        runner.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
        }
    }
}
