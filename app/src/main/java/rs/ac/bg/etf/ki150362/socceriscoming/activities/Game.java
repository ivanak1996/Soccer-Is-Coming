package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.Log;
import android.view.*;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Game {

    //  region Collision Strategies

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

                float nx = (c1.x - c2.x) / distance;
                float ny = (c1.y - c2.y) / distance;

                float tx = -ny;
                float ty = nx;

                // dot product tangent
                float dpTan1 = player1.getVxVector() * tx + player1.getVyVector() * ty;
                float dpTan2 = player2.getVxVector() * tx + player2.getVyVector() * ty;

                // dot product normal
                float dpNorm1 = player1.getVxVector() * nx + player1.getVyVector() * ny;
                float dpNorm2 = player2.getVxVector() * nx + player2.getVyVector() * ny;

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

    //  endregion

    // region Graphics

    Resources resources;
    private SurfaceHolder surfaceHolder;

    private Bitmap homePlayerImageScore;
    private Bitmap guestPlayerImageScore;

    private Typeface gameOfThronesTypeface;

    private Paint playerWithTurnTextPaint = new Paint();
    private Paint playerWithoutTurnTextPaint = new Paint();
    private Paint playerWithTurnAlphaPaint = new Paint();
    private Paint playerWithoutTurnAlphaPaint = new Paint();
    private Paint timeElapsedPaint = new Paint();


    Bitmap homeTeamImage, guestTeamImage, ballImage;
    int homePlayerDrawableId;
    int guestPlayerDrawableId;

    // endregion

    // region Fields

    // TODO refactor opponent to guest everywhere
    Team homeTeam;
    Team guestTeam;
    Team turn;

    private Player inTouchableFocus = null;
    private Player inFocus = null;

    Ball ball;

    private SoccerFieldView soccerFieldView;

    private int actionDownX, actionDownY;
    private long actionDownTime;

    private CollisionStrategy collisionStrategy = staticCollisionStrategy;

    String homePlayerName;
    String guestPlayerName;

    private long lastTurnSwitchTime;
    long gameStartedTime;

    private LinkedList<Player> players = new LinkedList<>();

    // endregion

    private InitializerStrategy initStrategy;

    public Game(SoccerFieldView soccerFieldView, SurfaceHolder surfaceHolder, Resources resources, Typeface typeface, InitializerStrategy initStrategy) {

        this.soccerFieldView = soccerFieldView;
        this.surfaceHolder = surfaceHolder;
        this.resources = resources;
        this.gameOfThronesTypeface = typeface;
        this.initStrategy = initStrategy;

        ball = new Ball(soccerFieldView.getSoccerFieldWidth(), soccerFieldView.getSoccerFieldHeight());
        homeTeam = new Team(Player.Position.HOME, soccerFieldView.getSoccerFieldWidth(), soccerFieldView.getSoccerFieldHeight());
        guestTeam = new Team(Player.Position.OPPONENT, soccerFieldView.getSoccerFieldWidth(), soccerFieldView.getSoccerFieldHeight());

        setTurn(homeTeam);

        players.addAll(homeTeam.getPlayers());
        players.addAll(guestTeam.getPlayers());

        setupGraphics();

        gameStartedTime = System.currentTimeMillis();
    }

    // sets up the game
    public void init() {
        initStrategy.init(this);
    }

    public void update(long elapsed) {

        for (Player player1 : players) {
            for (Player player2 : players) {
                if (player1 != player2) {
                    collisionStrategy.detectCollision(player1, player2);
                }
            }
            collisionStrategy.detectCollision(player1, ball);
        }

        ball.setScreenWidth(soccerFieldView.getSoccerFieldWidth());
        ball.update(elapsed);

        for (Player player : players) {
            //player.setScreenWidth(soccerFieldView.getSoccerFieldWidth());
            player.update(elapsed);
        }

        if (turn != null) {
            if (soccerFieldView.getOppositeGoal().contains(ball.getScreenRect().centerX(), ball.getScreenRect().centerY())) {
                homeTeam.gain();
                reinit();
            } else if (soccerFieldView.getHomeGoal().contains(ball.getScreenRect().centerX(), ball.getScreenRect().centerY())) {
                guestTeam.gain();
                reinit();
            }
        }

        if (System.currentTimeMillis() - lastTurnSwitchTime > 5000) {
            switchTurn();
        }

    }

    // reset all players and ball to the default position
    private void reinit() {

        homeTeam.init(homeTeamImage);
        guestTeam.init(guestTeamImage);
        ball.init(ballImage);

        for (Player player : players) {
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
            for (Player player : players) {
                if (turn.getPlayers().contains(player))
                    player.draw(canvas);
                else
                    player.drawGrayscale(canvas);
            }

            // score

            Paint homePlayerTextPaint = turn == homeTeam ? playerWithTurnTextPaint : playerWithoutTurnTextPaint;
            Paint guestPlayerTextPaint = turn == guestTeam ? playerWithTurnTextPaint : playerWithoutTurnTextPaint;

            Paint homePlayerAlphaPaint = turn == homeTeam ? playerWithTurnAlphaPaint : playerWithoutTurnAlphaPaint;
            Paint guestPlayerAlphaPaint = turn == guestTeam ? playerWithTurnAlphaPaint : playerWithoutTurnAlphaPaint;

            String homePlayerResultText = homePlayerName + " " + homeTeam.getScore();
            String guestTeamResultText = guestTeam.getScore() + " " + guestPlayerName;

            Rect textBounds = new Rect();
            guestPlayerTextPaint.getTextBounds(guestTeamResultText, 0, guestTeamResultText.length(), textBounds);

            canvas.drawBitmap(homePlayerImageScore, 0, canvas.getHeight() - homePlayerImageScore.getHeight(), homePlayerAlphaPaint);
            canvas.drawBitmap(guestPlayerImageScore, canvas.getWidth() - guestPlayerImageScore.getWidth(), canvas.getHeight() - guestPlayerImageScore.getHeight(), guestPlayerAlphaPaint);

            canvas.drawText(homePlayerResultText, 50, canvas.getHeight() - 50, homePlayerTextPaint);
            canvas.drawText(guestTeamResultText, canvas.getWidth() - textBounds.width() - 50, canvas.getHeight() - 50, guestPlayerTextPaint);

            //time
            long elapsed = System.currentTimeMillis() - gameStartedTime;

            @SuppressLint("DefaultLocale")
            String timeElapsedString = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(elapsed) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsed)),
                    TimeUnit.MILLISECONDS.toSeconds(elapsed) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed)));

            Log.d("elapsed", timeElapsedString);

            Rect timeElapsedTextBounds = new Rect();
            timeElapsedPaint.getTextBounds("00:00", 0, timeElapsedString.length(), timeElapsedTextBounds);

            canvas.drawText(timeElapsedString, 40, 80, timeElapsedPaint);

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    public void saveGame(Context context) {

        GameState gameState = new GameState();

        gameState.timeElapsed = gameStartedTime; //TODO change this

        gameState.homePlayerName = this.homePlayerName;
        gameState.guestPlayerName = this.guestPlayerName;

        gameState.homePlayerScore = this.homeTeam.getScore();
        gameState.guestPlayerScore = this.guestTeam.getScore();

        float[][] homePlayersCoordinates = new float[Team.NUMBER_OF_PLAYERS][2];
        float[][] guestPlayersCoordinates = new float[Team.NUMBER_OF_PLAYERS][2];
        float[][] homePlayersVelocities = new float[Team.NUMBER_OF_PLAYERS][2];
        float[][] guestPlayersVelocities = new float[Team.NUMBER_OF_PLAYERS][2];

        for (int i = 0; i < Team.NUMBER_OF_PLAYERS; i++) {

            homePlayersCoordinates[i][0] = homeTeam.getPlayers().get(i).getX();
            homePlayersCoordinates[i][1] = homeTeam.getPlayers().get(i).getY();

            homePlayersVelocities[i][0] = homeTeam.getPlayers().get(i).getVxVector();
            homePlayersVelocities[i][1] = homeTeam.getPlayers().get(i).getVyVector();

            guestPlayersCoordinates[i][0] = guestTeam.getPlayers().get(i).getX();
            guestPlayersCoordinates[i][1] = guestTeam.getPlayers().get(i).getY();

            guestPlayersVelocities[i][0] = guestTeam.getPlayers().get(i).getVxVector();
            guestPlayersVelocities[i][1] = guestTeam.getPlayers().get(i).getVyVector();

        }

        gameState.homePlayersCoordinates = homePlayersCoordinates;
        gameState.homePlayersVelocities = homePlayersVelocities;

        gameState.guestPlayersCoordinates = guestPlayersCoordinates;
        gameState.guestPlayersVelocities = guestPlayersVelocities;

        gameState.homePlayersTurn = turn == homeTeam;

        gameState.ballX = ball.getX();
        gameState.ballY = ball.getY();
        gameState.ballVx = ball.getVxVector();
        gameState.ballVy = ball.getVyVector();

        gameState.homePlayerDrawableId = this.homePlayerDrawableId;
        gameState.guestPlayerDrawableId = this.guestPlayerDrawableId;

        gameState.saveGame(context);
    }

    public void onSurfaceChanged(int width, int height) {
        for (Player player : players) {
            player.setScreenWidth(width);
            player.setScreenHeight(height);
        }
        ball.setScreenWidth(width);
        ball.setScreenHeight(height);
    }


    public void onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                if (turn == null) return;
                for (Player player : turn.getPlayers()) {
                    if (player.getScreenRect().contains(x, y)) {
                        if (inFocus != null) {
                            inFocus.setInFocus(false);
                        }
                        inTouchableFocus = inFocus = player;
                        player.setInFocus(true);
                    }
                }
                actionDownX = x;
                actionDownY = y;
                actionDownTime = System.currentTimeMillis();
                collisionStrategy = staticCollisionStrategy;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0; i < players.size(); i++) {
                    Log.d("ACTION_POINTER_DOWN ", "Player" + i + "x=" + players.get(i).getX() + "y=" + players.get(i).getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                if(turn == null || inTouchableFocus == null) return;
//                inTouchableFocus.safeSetPosition(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (turn == null || inTouchableFocus == null) return;
                long releaseTime = System.currentTimeMillis();
                Log.d("onTouchEvnt up action", "" + (releaseTime - actionDownTime));
                float offsetX = x - actionDownX;
                float offsetY = y - actionDownY;
                float moveDuration = releaseTime - actionDownTime;
                Log.d("ACTION UP", "duration = " + moveDuration + " offsetX = " + offsetX + " offsetY = " + offsetY);
                inTouchableFocus.setVxVector(1.2f * offsetX / moveDuration);
                inTouchableFocus.setVyVector(1.2f * offsetY / moveDuration);
                inTouchableFocus = null;
                collisionStrategy = dynamicCollisionStrategy;
                this.switchTurn();
        }
    }

    private void switchTurn() {
        if (turn == null) return;
        if (turn == homeTeam) {
            setTurn(guestTeam);
        } else {
            setTurn(homeTeam);
        }
    }

    private void setTurn(Team team) {
        this.turn = team;
        lastTurnSwitchTime = System.currentTimeMillis();
    }

    private void setupGraphics() {
        homePlayerImageScore = BitmapFactory.decodeResource(resources, R.drawable.leftplayer_score);
        homePlayerImageScore = Bitmap.createScaledBitmap(homePlayerImageScore, 700, 200, false);

        guestPlayerImageScore = BitmapFactory.decodeResource(resources, R.drawable.rightplayer_score);
        guestPlayerImageScore = Bitmap.createScaledBitmap(guestPlayerImageScore, 700, 200, false);

        Sprite.highlight = Bitmap
                .createScaledBitmap(BitmapFactory
                                .decodeResource(resources, R.drawable.player_shadow),
                        360, 360, false);

        timeElapsedPaint.setARGB(200, 179, 0, 59);
        timeElapsedPaint.setTextSize(85);
        timeElapsedPaint.setTypeface(gameOfThronesTypeface);

        playerWithTurnTextPaint.setARGB(200, 179, 0, 59);
        playerWithTurnTextPaint.setTextSize(60);
        playerWithTurnTextPaint.setTypeface(gameOfThronesTypeface);

        playerWithoutTurnTextPaint.setARGB(150, 39, 12, 12);
        playerWithoutTurnTextPaint.setTextSize(60);
        playerWithoutTurnTextPaint.setTypeface(gameOfThronesTypeface);

        playerWithTurnAlphaPaint.setAlpha(150);
        playerWithoutTurnAlphaPaint.setAlpha(75);
    }

}
