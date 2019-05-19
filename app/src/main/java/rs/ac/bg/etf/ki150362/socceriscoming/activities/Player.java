package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

public class Player extends Sprite {

    // intelligence
    private Random random = new Random();
    private int directionX, directionY;
    private float speed = 0.60f;

    private float vx, vy;
    private float ax, ay;

    public void setPosition(float x, float y) {
        setX(x-getRect().centerX());
        setY(y-getRect().centerY());
    }

    public void safeSetPosition(float x, float y) {
        safeSetX(x - getRect().centerX());
        safeSetY(y - getRect().centerY());
    }

    public void update(long elapsed, Ball ball) {

        setX(decisionLogicX(elapsed, ball));
        setY(decisionLogicY(elapsed, ball));
    }

    public void update(long elapsed) {

        // no moving
        if (vx < 0.0001f && vy < 0.0001f) return;
        vx *= 0.88f;
        vy *= 0.88f;

        float x = getX();
        float y = getY();

        Rect screenRect = getScreenRect();

        if (screenRect.left <= 0) {
            directionX = 1;
        } else if (screenRect.right >= getScreenWidth()) {
            directionX = -1;
        }

        if (screenRect.top <= 0) {
            directionY = 1;
        } else if (screenRect.bottom >= getScreenHeight()) {
            directionY = -1;
        }

        x += directionX * vx * elapsed;
        y += directionY * vy * elapsed;

        setX(x);
        setY(y);

    }

    private float decisionLogicX(long elapsed, Ball ball) {

        int decision = random.nextInt(20);

        if(decision == 0){
            directionX = 0;
        }
        else if(decision == 1){
            directionX = random.nextInt(2)*2-1;
        }
        else if(decision < 4){
            if(ball.getScreenRect().centerX() < getScreenRect().centerX()){
                directionX = -1;
            }
            else {
                directionX = 1;
            }
        }

        if(getScreenRect().left <= 0){
            directionX=1;
        } else if(getScreenRect().right >= getScreenWidth()){
            directionX = -1;
        }

        float x = getX();

        x += directionX * speed * elapsed;

        return x;
    }

    private float decisionLogicY(long elapsed, Ball ball) {

        int decision = random.nextInt(20);

        if(decision == 0){
            directionY = 0;
        }
        else if(decision == 1){
            directionY = random.nextInt(2)*2-1;
        }
        else if(decision < 4){
            if(ball.getScreenRect().centerY() < getScreenRect().centerY()){
                directionY = -1;
            }
            else {
                directionY = 1;
            }
        }

        if(getScreenRect().top <= 0){
            directionY=1;
        } else if(getScreenRect().bottom >= getScreenHeight()){
            directionY = -1;
        }

        float y = getY();

        y += directionY * speed * elapsed;

        return y;
    }

    public enum Position {
        HOME, OPPONENT
    }

    private Position position;

    private static final int margin = 200;

    public Player(int screenWidth, int screenHeight, Position position) {
        super(screenWidth, screenHeight);

        this.position = position;
    }

    @Override
    public void init(Bitmap image) {
        super.init(image);

        setY(getScreenHeight()/2 - getRect().centerY());

        if(position == Position.HOME) {
            setX(margin);
        } else {
            setX(getScreenWidth() - margin - getRect().centerX());
        }

        directionX = random.nextInt(2)*2-1;
        directionY = random.nextInt(2)*2-1;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = Math.abs(vx);
        directionX = vx < 0 ? -1 : 1;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = Math.abs(vy);
        directionY = vy < 0 ? -1 : 1;
    }

    public float getAx() {
        return ax;
    }

    public void setAx(float ax) {
        this.ax = ax;
    }

    public float getAy() {
        return ay;
    }

    public void setAy(float ay) {
        this.ay = ay;
    }
}
