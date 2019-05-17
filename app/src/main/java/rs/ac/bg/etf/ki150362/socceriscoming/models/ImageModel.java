package rs.ac.bg.etf.ki150362.socceriscoming.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ImageModel {

    // coordinates
    private float x;
    private float y;

    private int screenWidth;
    private int screenHeight;

    private Bitmap image;
    private Bitmap shadow;

    private Rect bounds;

    public ImageModel(int screenWidth, int screenHeight) {

        x=30;
        y=30;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void init(Bitmap image, Bitmap shadow){
        this.image=image;
        this.shadow=shadow;

        bounds = new Rect(0,0,image.getWidth(),image.getHeight());
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(shadow, x, y, null);
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

    public Rect getRect(){
        return bounds;
    }

}
