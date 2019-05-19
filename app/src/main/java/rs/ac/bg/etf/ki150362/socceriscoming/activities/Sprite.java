package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

// TODO: maybe add shadow
public class Sprite {

    private int screenHeight;
    private int screenWidth;

    private float x;
    private float y;

    private Bitmap image;
    private Bitmap shadow;

    private Rect bounds;

    public Sprite(int screenWidth, int screenHeight) {

        x = 0;
        y = 0;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /*public void init(Bitmap image, Bitmap shadow){
        this.image=image;
        // this.shadow=shadow;

        bounds = new Rect(0,0,image.getWidth(),image.getHeight());
    }*/

    public void init(Bitmap image) {
        this.image = image;
        bounds = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    public void draw(Canvas canvas) {
        // canvas.drawBitmap(shadow, x, y, null);
        canvas.drawBitmap(image, x, y, null);
    }

    public Rect getScreenRect() {
        return new Rect((int) x, (int) y, (int) x + getRect().width(), (int) y + getRect().height());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void safeSetX(float x) {
        this.x = x;
        if (getScreenRect().left <= 0) {
            this.x = 0;
        } else if (getScreenRect().right >= getScreenWidth()) {
            this.x = getScreenWidth() - getScreenRect().width();
        }
    }

    public void safeSetY(float y) {
        this.y = y;
        if (getScreenRect().top <= 0) {
            this.y = 0;
        } else if (getScreenRect().bottom >= getScreenHeight()) {
            this.y = getScreenHeight() - getScreenRect().height();
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public Rect getRect() {
        return bounds;
    }
}
