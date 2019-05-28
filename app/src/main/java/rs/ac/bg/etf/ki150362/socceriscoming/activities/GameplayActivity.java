package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import rs.ac.bg.etf.ki150362.socceriscoming.MainActivity;
import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.util.asynctasks.EnterFullScreenAsyncTask;

public class GameplayActivity extends AppCompatActivity {

    void enterFullScreenMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            // full screen
            enterFullScreenMode();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gameplay);

        /*
        gameplayIntent.putExtra(EXTRA_HOME_PLAYER_NAME, player1Name);
        gameplayIntent.putExtra(EXTRA_GUEST_PLAYER_NAME, player2Name);
        gameplayIntent.putExtra(EXTRA_HOME_PLAYER_DRAWABLE, player1DrawableId);
        gameplayIntent.putExtra(EXTRA_GUEST_PLAYER_DRAWABLE, player2DrawableId);
         */

        Intent caller = getIntent();
        String homePlayerName = caller.getStringExtra(MainActivity.EXTRA_HOME_PLAYER_NAME);
        String guestPlayerName = caller.getStringExtra(MainActivity.EXTRA_GUEST_PLAYER_NAME);
        int homeDrawableResId = caller.getIntExtra(MainActivity.EXTRA_HOME_PLAYER_DRAWABLE, -1);
        int guestDrawableResId = caller.getIntExtra(MainActivity.EXTRA_GUEST_PLAYER_DRAWABLE, -1);

        // full screen
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            // enterFullScreenMode();
                            (new EnterFullScreenAsyncTask(getWindow().getDecorView())).execute();
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });

        ViewGroup gameplayViewGroup = findViewById(R.id.layout_gameplay);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        gameplayViewGroup.setLayoutParams(params);

        SoccerFieldView soccerFieldView = new SoccerFieldView(this);
        FrameLayout.LayoutParams soccerFieldParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        soccerFieldView.setLayoutParams(soccerFieldParams);

        SoccerLogicSurfaceView soccerLogicSurfaceView = new SoccerLogicSurfaceView(this, soccerFieldView, homePlayerName, guestPlayerName, homeDrawableResId, guestDrawableResId);
        FrameLayout.LayoutParams soccerLogicSurfaceParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        soccerLogicSurfaceView.setLayoutParams(soccerLogicSurfaceParams);

        gameplayViewGroup.addView(soccerFieldView);
        gameplayViewGroup.addView(soccerLogicSurfaceView);

    }

}
