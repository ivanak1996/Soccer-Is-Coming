package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Ball extends Sprite {

    private float speedX = 0.5f;
    private float speedY = 0.5f;

    // TODO: maybe randomly choose direction
    private int directionX = 1;
    private int directionY = 1;

    public Ball(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
    }

    @Override
    public void init(Bitmap image) {
        super.init(image);

        setX(getScreenWidth() / 2 - getRect().centerX());
        setY(getScreenHeight() / 2 - getRect().centerY());
    }

    public void update(long elapsed) {

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

        x += directionX * speedX * elapsed;
        y += directionY * speedY * elapsed;

        setX(x);
        setY(y);

    }

    public void moveRight() {
        directionX = 1;
    }

    public void moveLeft() {
        directionX = -1;
    }
}
