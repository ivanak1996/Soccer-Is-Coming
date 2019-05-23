package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Player extends Sprite {

    // intelligence
    private Random random = new Random();
    private float speed = 0.60f;

    private boolean hasFocus;

    public void setPosition(float x, float y) {
        setX(x-getRect().centerX());
        setY(y-getRect().centerY());
    }

    public void safeSetPosition(float x, float y) {
        safeSetX(x-getRect().centerX());
        safeSetY(y-getRect().centerY());
    }

    public void updateWithIntelligence(long elapsed, Ball ball) {

        setX(decisionLogicX(elapsed, ball));
        setY(decisionLogicY(elapsed, ball));
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

    public void init(Bitmap image, int index) {
        super.init(image);

        if(position == Position.HOME) {
            setX(margin + (index % 2) * margin * 0.75f);
        } else {
            setX(getScreenWidth() - margin - (index % 2) * margin * 0.75f - getRect().centerX());
        }
        setY(getScreenHeight() / 2 - getRect().centerY() + (1 - index) * margin * 1.2f);
    }

    @Override
    public void draw(Canvas canvas) {
        if (hasFocus) {
            drawHighlighted(canvas);
            return;
        }
        super.draw(canvas);
    }

    public boolean isInFocus() {
        return hasFocus;
    }

    public void setInFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
}
