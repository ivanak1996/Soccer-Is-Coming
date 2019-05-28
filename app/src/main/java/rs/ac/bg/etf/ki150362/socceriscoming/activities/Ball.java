package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.graphics.Bitmap;

public class Ball extends Sprite {


    // TODO: maybe randomly choose direction
//    private int directionX = 1;
//    private int directionY = 1;

    public Ball(int screenWidth, int screenHeight) {

        super(screenWidth, screenHeight);
    }

    @Override
    public void setVxVector(float vx) {
        super.setVxVector(vx);
        this.vx *= 0.85;
    }

    @Override
    public void setVyVector(float vy) {
        super.setVyVector(vy);
        this.vy *= 0.85;
    }

    @Override
    protected void updateVelocity() {
        vx *= 0.95;
        vy *= 0.95;
    }

    @Override
    public void init(Bitmap image) {
        super.init(image);

        setX(getScreenWidth() / 2 - getRect().centerX());
        setY(getScreenHeight() / 2 - getRect().centerY());
    }

    public void moveRight() {
        directionX = 1;
    }

    public void moveLeft() {
        directionX = -1;
    }
}
