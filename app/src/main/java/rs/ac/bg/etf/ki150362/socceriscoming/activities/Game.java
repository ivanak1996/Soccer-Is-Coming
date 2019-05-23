package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.LinkedList;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Game {

    interface CollisionStrategy {
        void detectCollision(Sprite player1, Sprite player2);
    }

    private CollisionStrategy staticCollisionStrategy = new CollisionStrategy() {
        @Override
        public void detectCollision(Sprite player1, Sprite player2) {
            Rect playerRect = player1.getScreenRect();
            Rect ballRect = player2.getScreenRect();

            Point c1 = new Point(playerRect.centerX(), playerRect.centerY());
            Point c2 = new Point(ballRect.centerX(), ballRect.centerY());

            float r1 = playerRect.width() / 2;
            float r2 = ballRect.width() / 2;

            if (pow(c2.x - c1.x, 2) + pow(c1.y - c2.y, 2) <= pow(r1 + r2, 2)) {
                // there is collision
                float distance = (float) sqrt((pow(c2.x - c1.x, 2) + pow(c1.y - c2.y, 2)));
                float overlap = 0.5f * (distance - r1 - r2);

                // displace player
                float newPlayerX = player1.getX() - overlap * (player1.getX() - player2.getX()) / distance;
                float newPlayerY = player1.getY() - overlap * (player1.getY() - player2.getY()) / distance;

                float newOpponentX = player2.getX() + overlap * (player1.getX() - player2.getX()) / distance;
                float newOpponentY = player2.getY() + overlap * (player1.getY() - player2.getY()) / distance;

//            player.safeSetX(newPlayerX);
//            player.safeSetY(newPlayerY);

                player1.safeSetX(newPlayerX);
                player1.safeSetY(newPlayerY);

                player2.safeSetX(newOpponentX);
                player2.safeSetY(newOpponentY);

            }
        }
    };

    private CollisionStrategy dynamicCollisionStrategy = new CollisionStrategy() {
        @Override
        public void detectCollision(Sprite player1, Sprite player2) {
            Rect player1Rect = player1.getScreenRect();
            Rect player2Rect = player2.getScreenRect();

            Point c1 = new Point(player1Rect.centerX(), player1Rect.centerY());
            Point c2 = new Point(player2Rect.centerX(), player2Rect.centerY());

            float r1 = player1Rect.width() / 2;
            float r2 = player2Rect.width() / 2;

            if (pow(c2.x - c1.x, 2) + pow(c1.y - c2.y, 2) <= pow(r1 + r2, 2)) {
                // there is collision
                float distance = (float) sqrt((pow(c2.x - c1.x, 2) + pow(c1.y - c2.y, 2)));
                float overlap = 0.5f * (distance - r1 - r2);

                float newPlayer1X = player1.getX() - overlap * (player1.getX() - player2.getX()) / distance;
                float newPlayer1Y = player1.getY() - overlap * (player1.getY() - player2.getY()) / distance;

                float newPlayer2X = player2.getX() + overlap * (player1.getX() - player2.getX()) / distance;
                float newPlayer2Y = player2.getY() + overlap * (player1.getY() - player2.getY()) / distance;

                player1.safeSetX(newPlayer1X);
                player1.safeSetY(newPlayer1Y);

                player2.safeSetX(newPlayer2X);
                player2.safeSetY(newPlayer2Y);

                float nx = (c1.x - c2.x)/distance;
                float ny = (c1.y - c2.y)/distance;

                float tx = -ny;
                float ty = nx;

                // dot product tangent
                float dpTan1 = player1.getVxVector() * tx + player1.getVyVector() * ty ;
                float dpTan2 = player2.getVxVector() * tx + player2.getVyVector() * ty ;

                // dot product normal
                float dpNorm1 = player1.getVxVector()*nx+player1.getVyVector()*ny;
                float dpNorm2 = player2.getVxVector()*nx+player2.getVyVector()*ny;

                // conversion of momentum in 1D
                float m1 = (dpNorm1 * (r2 - r1) + 2.0f * r2 * dpNorm2) / (r1 + r2);
                float m2 = (dpNorm2 * (r2 - r1) + 2.0f * r1 * dpNorm1) / (r1 + r2);

                player1.setVxVector((tx * dpTan1 + nx * m1));
                player1.setVyVector((ty * dpTan1 + ny * m1));
                player2.setVxVector((tx * dpTan2 + nx * m2));
                player2.setVyVector((ty * dpTan2 + nx * m2));

            }
        }
    };

    private SoccerFieldView soccerFieldView;
    private SurfaceHolder surfaceHolder;
    private Resources resources;

    private Paint textPaint = new Paint();

    private Player inTouchableFocus = null, inFocus = null;
    private int touchedX, touchedY;
    private long touchedTime;

    private Ball ball;
    private Team homeTeam, opponentTeam;
    private Team turn;

    private CollisionStrategy collisionStrategy = staticCollisionStrategy;

    private Bitmap homeTeamImage, opponentTeamImage, ballImage;

//    private Player homePlayer;
//    private Player opponentPlayer;

    private LinkedList<Player> players = new LinkedList<>();

    public Game(SoccerFieldView soccerFieldView, SurfaceHolder surfaceHolder, Resources resources) {

        this.soccerFieldView = soccerFieldView;
        this.surfaceHolder = surfaceHolder;
        this.resources = resources;

        ball = new Ball(soccerFieldView.getSoccerFieldWidth(), soccerFieldView.getSoccerFieldHeight());
//        homePlayer = new Player(soccerFieldView.getSoccerFieldWidth(), soccerFieldView.getSoccerFieldHeight(), Player.Position.HOME);
//        opponentPlayer = new Player(soccerFieldView.getSoccerFieldWidth(), soccerFieldView.getSoccerFieldHeight(), Player.Position.OPPONENT);
        homeTeam = new Team(Player.Position.HOME, soccerFieldView.getSoccerFieldWidth(), soccerFieldView.getSoccerFieldHeight());
        opponentTeam = new Team(Player.Position.OPPONENT, soccerFieldView.getSoccerFieldWidth(), soccerFieldView.getSoccerFieldHeight());

        turn = homeTeam;

        players.addAll(homeTeam.getPlayers());
        players.addAll(opponentTeam.getPlayers());

        Sprite.highlight = Bitmap
                .createScaledBitmap(BitmapFactory
                        .decodeResource(resources, R.drawable.player_shadow),
                        360,360,false);

        textPaint.setARGB(200, 254, 0, 0);
        textPaint.setTextSize(100);
    }

    public void init() {

        Bitmap ballImage  = BitmapFactory.decodeResource(resources, R.drawable.ball_dragon);
        ballImage = Bitmap.createScaledBitmap(ballImage, 100, 100, false);
        this.ballImage = ballImage;

        Bitmap homePlayerImage  = BitmapFactory.decodeResource(resources, R.drawable.player_baratheon);
        homePlayerImage = Bitmap.createScaledBitmap(homePlayerImage, 200, 200, false);
        this.homeTeamImage = homePlayerImage;

        Bitmap opponentPlayerImage  = BitmapFactory.decodeResource(resources, R.drawable.player_martell);
        opponentPlayerImage = Bitmap.createScaledBitmap(opponentPlayerImage, 200, 200, false);
        this.opponentTeamImage = opponentPlayerImage;

        ball.init(ballImage);
        homeTeam.init(homePlayerImage);
        opponentTeam.init(opponentPlayerImage);
//        homePlayer.init(homePlayerImage);
//        opponentPlayer.init(opponentPlayerImage);
    }

    public void switchTurn() {
        if (turn == null) return;
        if (turn == homeTeam) {
            turn = opponentTeam;
        } else {
            turn = homeTeam;
        }
    }

    public void update(long elapsed) {

//        TODO: implement collision detection
//        if(homePlayer.getScreenRect().contains(ball.getScreenRect().left, ball.getScreenRect().centerY())){
//            ball.moveRight();
//        } else if (opponentPlayer.getScreenRect().contains(ball.getScreenRect().right, ball.getScreenRect().centerY())){
//            ball.moveLeft();
//        }

        // HashSet<Pair<Player, Player>> colidedPairs = new HashSet<>();

        for(Player player1 : players) {
            for(Player player2: players) {
                if(player1 != player2){
//                    detectCollisionDynamic(player1, player2);
                    collisionStrategy.detectCollision(player1, player2);
                }
            }
            collisionStrategy.detectCollision(player1, ball);
        }

        // detectCollisionDynamic(homePlayer, opponentPlayer);

        ball.setScreenWidth(soccerFieldView.getSoccerFieldWidth());
        ball.update(elapsed);

        for(Player player:players){
            player.setScreenWidth(soccerFieldView.getSoccerFieldWidth());
            player.update(elapsed);
        }

        if(turn != null) {
            if(soccerFieldView.getOppositeGoal().contains(ball.getScreenRect().centerX(), ball.getScreenRect().centerY())) {
                homeTeam.gain();
                reinit();
            } else if (soccerFieldView.getHomeGoal().contains(ball.getScreenRect().centerX(), ball.getScreenRect().centerY())) {
                opponentTeam.gain();
                reinit();
            }
        }

//        ball.update(elapsed);
//        homePlayer.update(elapsed);
//        opponentPlayer.update(elapsed);
        // opponentPlayer.update(elapsed, ball);
    }

    public void reinit() {

        homeTeam.init(homeTeamImage);
        opponentTeam.init(opponentTeamImage);
        ball.init(ballImage);

        for(Player player:players){
            player.setVxVector(0.0f);
            player.setVyVector(0.0f);
        }

        ball.setVxVector(0.0f);
        ball.setVyVector(0.0f);

    }

    public void draw() {

        Canvas canvas = surfaceHolder.lockCanvas();

        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            ball.draw(canvas);
            for(Player player:players) {
                player.draw(canvas);
            }
//            homePlayer.draw(canvas);
//            opponentPlayer.draw(canvas);

            canvas.drawText("home " + homeTeam.getScore() + " : guest " + opponentTeam.getScore(), 50, 100, textPaint);

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

                if(turn == null) return;
                for(Player player : turn.getPlayers()) {
                    if(player.getScreenRect().contains(x, y)){
                        if (inFocus!=null) {
                            inFocus.setInFocus(false);
                        }
                        inTouchableFocus = inFocus = player;
                        player.setInFocus(true);
                    }
                }
                touchedX = x;
                touchedY = y;
                touchedTime = System.currentTimeMillis();
                collisionStrategy = staticCollisionStrategy;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0; i < players.size(); i++) {
                    Log.d("ACTION_POINTER_DOWN ", "Player" + i + "x=" + players.get(i).getX() + "y=" + players.get(i).getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(turn == null || inTouchableFocus == null) return;
                inTouchableFocus.safeSetPosition(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if(turn == null || inTouchableFocus == null) return;
                long releaseTime = System.currentTimeMillis();
                Log.d("onTouchEvnt up action", ""+(releaseTime - touchedTime));
                float offsetX = x - touchedX;
                float offsetY = y - touchedY;
                float moveDuration = releaseTime - touchedTime;
                Log.d("ACTION UP", "duration = " + moveDuration + " offsetX = " + offsetX + " offsetY = " + offsetY);
                inTouchableFocus.setVxVector(2.0f * offsetX / moveDuration);
                inTouchableFocus.setVyVector(2.0f * offsetY / moveDuration);
                //inTouchableFocus.setInFocus(false);
                inTouchableFocus = null;
                collisionStrategy = dynamicCollisionStrategy;
                this.switchTurn();
        }
    }
}
