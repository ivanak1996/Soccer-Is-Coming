package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class SoccerLogicSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap ball;
    private SoccerFieldView soccerFieldView;

    String homePlayerName, guestPlayerName;
    private int homePlayerDrawableId, guestPlayerDrawableId;

    private GameRunner runner;
    private Game game;

    public SoccerLogicSurfaceView(Context context, SoccerFieldView soccerFieldView,
                                  String homePlayerName, String guestPlayerName,
                                  int homePlayerDrawableId, int guestPlayerDrawableId) {
        super(context);

        this.soccerFieldView = soccerFieldView;

        this.homePlayerName = homePlayerName;
        this.guestPlayerName = guestPlayerName;

        this.homePlayerDrawableId = homePlayerDrawableId;
        this.guestPlayerDrawableId = guestPlayerDrawableId;

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
        Log.d("SoccerLogicSurfaceView", "surfaceCreated called");

        Typeface gameOfThronesTypeface = ResourcesCompat.getFont(getContext(), R.font.got);
        game = new Game(soccerFieldView, holder, getResources(), homePlayerName, guestPlayerName, homePlayerDrawableId, guestPlayerDrawableId, gameOfThronesTypeface);

        GameState gameState = GameState.reloadGame(getContext());

        if (gameState != null) {
            runner = new GameRunner(game, gameState);
        } else {
            runner = new GameRunner(game);
        }

        runner.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("SoccerLogicSurfaceView", "surfaceChanged called");
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
