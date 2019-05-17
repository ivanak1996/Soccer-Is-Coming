package rs.ac.bg.etf.ki150362.socceriscoming.models;

import android.graphics.Rect;

public class SoccerBallModel extends ImageModel {

    private float speedX = 0.05f;
    private float speedY = 0.05f;

    private int directionX = 1;
    private int directionY = 1;

    public SoccerBallModel(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
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

    }

}
