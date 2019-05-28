package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

// TODO: maybe add shadow
public abstract class Sprite {

    public static Bitmap highlight;

    private int screenHeight;
    private int screenWidth;

    // coordinates of the upper left corner of the object
    private float x;
    private float y;

    // velocity and direction of the vector
    protected float vx, vy;
    protected int directionX, directionY;

    private Bitmap image;
    private Bitmap shadow;

    private Paint grayScalePaint = new Paint();

    private Rect bounds;

    {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.5f);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        this.grayScalePaint.setColorFilter(filter);
    }

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

    public void drawGrayscale(Canvas canvas) {
        canvas.drawBitmap(image, x, y, grayScalePaint);
    }

    public void drawHighlighted(Canvas canvas) {
        canvas.drawBitmap(highlight, x-80, y-80, null);
        canvas.drawBitmap(image, x, y, null);
    }

    protected void updateVelocity() {
        // TODO: create global constants file
        // empiric constants
        vx *= 0.92f;
        vy *= 0.92f;
    }

    public void update(long elapsed) {

        // no moving
        if (vx < 0.00001f && vy < 0.00001f) return;

        updateVelocity();

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

    // checks if the given x value is between the valid bounds
    public void safeSetX(float x) {
        if(Float.isNaN(x)) return;
        this.x = x;
        if (getScreenRect().left <= 0) {
            this.x = 0;
        } else if (getScreenRect().right >= getScreenWidth()) {
            this.x = getScreenWidth() - getScreenRect().width();
        }
    }

    // checks if the given y value is between the valid bounds
    public void safeSetY(float y) {
        if(Float.isNaN(x)) return;
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

    public Rect getRect() {
        return bounds;
    }

    public float getVxVector() {
        return vx * directionX;
    }

    public void setVxVector(float vx) {
        if(Float.isNaN(vx)) return;
        this.vx = Math.abs(vx);
        directionX = vx < 0 ? -1 : 1;
    }

    public float getVyVector() {
        return vy * directionY;
    }

    public void setVyVector(float vy) {
        if(Float.isNaN(vy)) return;
        this.vy = Math.abs(vy);
        directionY = vy < 0 ? -1 : 1;
    }

}
