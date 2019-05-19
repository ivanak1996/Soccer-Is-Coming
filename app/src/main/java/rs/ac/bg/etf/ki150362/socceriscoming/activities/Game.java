package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Game {

    private SurfaceHolder surfaceHolder;
    private Resources resources;

    private int width;
    private int height;

    Player inFocus = null;
    private int touchedX, touchedY;
    private long touchedTime;

    private Ball ball;
    private Player homePlayer;
    private Player opponentPlayer;

    public Game(int screenWidth, int screenHeight, SurfaceHolder surfaceHolder, Resources resources) {
        this.height=screenHeight;
        this.width=screenWidth;
        this.surfaceHolder = surfaceHolder;
        this.resources = resources;

        ball = new Ball(screenWidth, screenHeight);
        homePlayer = new Player(screenWidth, screenHeight, Player.Position.HOME);
        opponentPlayer = new Player(screenWidth, screenHeight, Player.Position.OPPONENT);
    }

    public void init() {

        Bitmap ballImage  = BitmapFactory.decodeResource(resources, R.drawable.ball_dragon);
        ballImage = Bitmap.createScaledBitmap(ballImage, 150, 150, false);

        Bitmap homePlayerImage  = BitmapFactory.decodeResource(resources, R.drawable.player_baratheon);
        homePlayerImage = Bitmap.createScaledBitmap(homePlayerImage, 200, 200, false);

        Bitmap opponentPlayerImage  = BitmapFactory.decodeResource(resources, R.drawable.player_martell);
        opponentPlayerImage = Bitmap.createScaledBitmap(opponentPlayerImage, 200, 200, false);

        ball.init(ballImage);
        homePlayer.init(homePlayerImage);
        opponentPlayer.init(opponentPlayerImage);
    }

    public void update(long elapsed) {

//        TODO: implement collision detection
//        if(homePlayer.getScreenRect().contains(ball.getScreenRect().left, ball.getScreenRect().centerY())){
//            ball.moveRight();
//        } else if (opponentPlayer.getScreenRect().contains(ball.getScreenRect().right, ball.getScreenRect().centerY())){
//            ball.moveLeft();
//        }
        detectCollision(homePlayer, opponentPlayer);
        ball.update(elapsed);
        homePlayer.update(elapsed);
        //opponentPlayer.update(elapsed);
        // opponentPlayer.update(elapsed, ball);
    }

    public void draw() {

        Canvas canvas = surfaceHolder.lockCanvas();

        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            ball.draw(canvas);
            homePlayer.draw(canvas);
            opponentPlayer.draw(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void onTouchEvent(MotionEvent event) {

        int x = (int)event.getX();
        int y = (int)event.getY();

//        if(!homePlayer.getScreenRect().contains(x, y)) {
//            Log.d("onTouchEvent", "homePlayer does not contain the event");
//            return;
//        }

        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if(homePlayer.getScreenRect().contains(x, y)){
                    inFocus = homePlayer;
                }
                touchedX = x;
                touchedY = y;
                touchedTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if(inFocus == null) return;
                inFocus.setPosition(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if(inFocus == null) return;
                long releaseTime = System.currentTimeMillis();
                Log.d("onTouchEvnt up action", ""+(releaseTime - touchedTime));
                inFocus.setVx(2.0f * (inFocus.getX() - touchedX) / (releaseTime - touchedTime));
                inFocus.setVy(2.0f * (inFocus.getY() - touchedY) / (releaseTime - touchedTime));
                inFocus = null;
        }
    }

    private void detectCollision(Player player, Player opponent) {

        Rect playerRect = player.getScreenRect();
        Rect ballRect = opponent.getScreenRect();

        Point c1 = new Point(playerRect.centerX(), playerRect.centerY());
        Point c2 = new Point(ballRect.centerX(), ballRect.centerY());

        float r1 = playerRect.width() / 2;
        float r2 = ballRect.width() / 2;

        if (pow(c2.x - c1.x, 2) + pow(c1.y - c2.y, 2) <= pow(r1 + r2, 2)) {
            // there is collision
            float distance = (float) sqrt((pow(c2.x - c1.x, 2) + pow(c1.y - c2.y, 2)));
            float overlap = 0.5f * (distance - r1 - r2);

            // displace player
            float newPlayerX = player.getX() - overlap * (player.getX() - opponent.getX()) / distance;
            //player.safeSetX(newPlayerX);
            player.setX(newPlayerX);

            float newPlayerY = player.getY() - overlap * (player.getY() - opponent.getY()) / distance;
            player.setY(newPlayerY);


            // displace player
            float newOpponentX = opponent.getX() + overlap * (player.getX() - opponent.getX()) / distance;
            opponent.safeSetX(newOpponentX);

            float newOpponentY = opponent.getY() + overlap * (player.getY() - opponent.getY()) / distance;
            opponent.safeSetY(newOpponentY);

        }

    }
}
