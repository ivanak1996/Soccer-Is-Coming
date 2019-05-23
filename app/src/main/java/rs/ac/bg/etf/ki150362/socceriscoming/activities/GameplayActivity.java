package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class GameplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gameplay);

        ViewGroup gameplayViewGroup = findViewById(R.id.layout_gameplay);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        gameplayViewGroup.setLayoutParams(params);

        SoccerFieldView soccerFieldView = new SoccerFieldView(this);
        FrameLayout.LayoutParams soccerFieldParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        soccerFieldView.setLayoutParams(soccerFieldParams);

        gameplayViewGroup.addView(soccerFieldView);

        SoccerLogicSurfaceView soccerLogicSurfaceView = new SoccerLogicSurfaceView(this, soccerFieldView);
        FrameLayout.LayoutParams soccerLogicSurfaceParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        soccerLogicSurfaceView.setLayoutParams(soccerLogicSurfaceParams);

        gameplayViewGroup.addView(soccerLogicSurfaceView);

    }




}
