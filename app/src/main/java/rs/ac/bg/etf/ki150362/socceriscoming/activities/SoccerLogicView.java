package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.models.SoccerTeam;

public class SoccerLogicView extends RelativeLayout {

    private ArrayList<AppCompatImageView> soccerPlayersImageViews = new ArrayList<>();

    private int xDelta;
    private int yDelta;

    public SoccerLogicView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupSoccerLogic();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupSoccerLogic() {

        for (int i = 0; i < SoccerTeam.NUMBER_OF_PLAYERS; i++) {

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
            layoutParams.leftMargin = (((i + 2) % 2) + 1) * 100;
            layoutParams.topMargin = 2 * (i + 1) * 100;

            AppCompatImageView soccerPlayerImageView = new AppCompatImageView(getContext());
            soccerPlayerImageView.setImageResource(R.drawable.player_stark);
            soccerPlayerImageView.setLayoutParams(layoutParams);
            soccerPlayerImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();

                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                    view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;

                        case MotionEvent.ACTION_UP:
                            Toast.makeText(getContext(),
                                    "thanks for new location!", Toast.LENGTH_SHORT)
                                    .show();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                    .getLayoutParams();
                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
//                            layoutParams.rightMargin = 0;
//                            layoutParams.bottomMargin = 0;
                            view.setLayoutParams(layoutParams);
                            break;
                    }
                    invalidate();
                    return true;
                }
            });

            soccerPlayersImageViews.add(soccerPlayerImageView);
            addView(soccerPlayerImageView);
        }
    }


}
