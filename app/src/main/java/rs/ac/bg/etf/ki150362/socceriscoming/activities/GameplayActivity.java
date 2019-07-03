package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.activities.gaming.GameStartActivity;
import rs.ac.bg.etf.ki150362.socceriscoming.util.asynctasks.EnterFullScreenAsyncTask;
import rs.ac.bg.etf.ki150362.socceriscoming.util.sharedpreferences.GameSettingSharedPreferences;

public class GameplayActivity extends AppCompatActivity {

    private boolean isPausedGame = false;
    private SoccerLogicSurfaceView soccerLogicSurfaceView;

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
            enterFullScreenMode();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gameplay);

        View background = findViewById(R.id.layout_gameplay);
        background.setBackgroundResource(GameSettingSharedPreferences.getTerrainResourceIdPreference(this));

        Intent caller = getIntent();

        InitializerStrategy initStrategy = null;
        int gameStartStrategy = caller.getIntExtra(GameStartActivity.EXTRA_GAME_START_STRATEGY, -1);

        if(GameStartActivity.GAME_START_STRATEGY_NEWGAME == gameStartStrategy) {

            int gameMode = caller.getIntExtra(GameStartActivity.EXTRA_NEW_GAME_MODE, -1);
            String homePlayerName = caller.getStringExtra(GameStartActivity.EXTRA_HOME_PLAYER_NAME);
            String guestPlayerName = caller.getStringExtra(GameStartActivity.EXTRA_GUEST_PLAYER_NAME);
            int homeDrawableResId = caller.getIntExtra(GameStartActivity.EXTRA_HOME_PLAYER_DRAWABLE, -1);
            int guestDrawableResId = caller.getIntExtra(GameStartActivity.EXTRA_GUEST_PLAYER_DRAWABLE, -1);
            initStrategy = new NewGameInitializerStrategy(gameMode, homePlayerName, guestPlayerName, homeDrawableResId, guestDrawableResId);

        } else if(GameStartActivity.GAME_START_STRATEGY_CONTINUE == gameStartStrategy) {

            initStrategy = new ResumeGameInitializerStrategy(GameState.reloadGame(this));

        } else {
            // TODO: not valid
        }

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

        ViewGroup gameplayViewGroup = findViewById(R.id.layout_gameplay_surface);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        gameplayViewGroup.setLayoutParams(params);

        SoccerFieldView soccerFieldView = new SoccerFieldView(this);
        LinearLayout.LayoutParams soccerFieldParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        soccerFieldView.setLayoutParams(soccerFieldParams);

        TextView timeLeftTextView = findViewById(R.id.textView_timeLeft);


        /*Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent result = new Intent();
                result.putExtra(GameStartActivity.EXTRA_FINISHED_GAME_MESSAGE, "Game Finished");
                setResult(RESULT_OK, result);
                finish();
            }
        });*/

        soccerLogicSurfaceView = new SoccerLogicSurfaceView(this, soccerFieldView, initStrategy, timeLeftTextView/*, handler*/);
        LinearLayout.LayoutParams soccerLogicSurfaceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        soccerLogicSurfaceView.setLayoutParams(soccerLogicSurfaceParams);

        gameplayViewGroup.addView(soccerFieldView);
        gameplayViewGroup.addView(soccerLogicSurfaceView);

    }


    public void pauseGameOnClick(View view) {
        if(soccerLogicSurfaceView != null) {
            if(isPausedGame)
                soccerLogicSurfaceView.resumeGame();
            else
                soccerLogicSurfaceView.pauseGame();
            isPausedGame = !isPausedGame;
        }
    }

}
