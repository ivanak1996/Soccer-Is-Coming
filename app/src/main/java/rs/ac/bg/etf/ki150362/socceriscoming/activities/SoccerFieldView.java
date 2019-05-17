package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class SoccerFieldView extends View {

    private Paint paint = new Paint();
    private int width, height;

    public SoccerFieldView(Context context) {
        super(context);
    }

    public SoccerFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);

        // outer rect
        canvas.drawRect(new Rect(0, 0, width, height), paint);

        // mid line
        canvas.drawLine(width / 2, 0, width / 2, height, paint);

        // mid circle
        canvas.drawCircle(width / 2, height / 2, height / 4, paint);

        // mid point
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, height * 0.02f, paint);

        // home penalty box
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, height * 0.15f, width * 0.18f, height * 0.85f, paint);

        // home goal box
        canvas.drawRect(0, height * 0.3f, width * 0.10f, height * 0.7f, paint);

        // home goal
        paint.setStrokeWidth(30);
        canvas.drawLine(0, height * 0.4f, 0, height * 0.6f, paint);

        // home penalty point
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width * 0.14f, height / 2, height * 0.005f, paint);

        // home half circle
        paint.setStyle(Paint.Style.STROKE);
        RectF circleHome = new RectF(width * 0.12f, height * 0.3f, width * 0.24f, height * 0.7f);
        canvas.drawArc(circleHome, -90, 180, false, paint);

        // away penalty box
        canvas.drawRect(width * 0.82f, height * 0.15f, width, height * 0.85f, paint);

        // away goal box
        canvas.drawRect(width * 0.9f, height * 0.3f, width, height * 0.7f, paint);

        // away goal
        paint.setStrokeWidth(30);
        canvas.drawLine(width, height * 0.4f, width, height * 0.6f, paint);

        // away penalty point
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width * 0.86f, height / 2, height * 0.005f, paint);

        // away half circle
        paint.setStyle(Paint.Style.STROKE);
        RectF circleAway = new RectF(width * 0.76f, height * 0.3f, width * 0.88f, height * 0.7f);
        canvas.drawArc(circleAway, 90, 180, false, paint);
    }
}
