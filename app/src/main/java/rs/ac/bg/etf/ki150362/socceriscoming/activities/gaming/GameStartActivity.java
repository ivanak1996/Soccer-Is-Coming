package rs.ac.bg.etf.ki150362.socceriscoming.activities.gaming;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.activities.GameplayActivity;
import rs.ac.bg.etf.ki150362.socceriscoming.fragments.GameStartFragment;
import rs.ac.bg.etf.ki150362.socceriscoming.fragments.NewGameFragment;
import rs.ac.bg.etf.ki150362.socceriscoming.util.asynctasks.EnterFullScreenAsyncTask;

public class GameStartActivity extends AppCompatActivity {

    public static final int REQ_CODE_RESUME_GAME = 256;
    public static final int REQ_CODE_NEW_GAME = 257;

    public static final int GAME_START_STRATEGY_NEWGAME = 0;
    public static final int GAME_START_STRATEGY_CONTINUE = 1;

    public static final String EXTRA_GAME_START_STRATEGY = "EXTRA_GAME_START_STRATEGY";
    public static final String EXTRA_HOME_PLAYER_NAME = "EXTRA_HOME_PLAYER_NAME";
    public static final String EXTRA_GUEST_PLAYER_NAME = "EXTRA_GUEST_PLAYER_NAME";
    public static final String EXTRA_HOME_PLAYER_DRAWABLE = "EXTRA_HOME_PLAYER_DRAWABLE";
    public static final String EXTRA_GUEST_PLAYER_DRAWABLE = "EXTRA_GUEST_PLAYER_DRAWABLE";
    public static final String EXTRA_FINISHED_GAME_MESSAGE = "EXTRA_FINISHED_GAME_MESSAGE";


    private GameStartFragment gameStartFragment;
    private NewGameFragment newGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFragmentGameStart(null);

        setContentView(R.layout.activity_game_start);

        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            (new EnterFullScreenAsyncTask(getWindow().getDecorView())).execute();
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            // full screen
            enterFullScreenMode();
        }
    }

    void enterFullScreenMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void setFragmentGameStart(View view) {
        if(gameStartFragment == null) {
            gameStartFragment = new GameStartFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_gamestart_activity, gameStartFragment);
        transaction.commit();
    }

    public void setFragmentNewGame(View view) {
        if(newGameFragment == null) {
            newGameFragment = new NewGameFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_gamestart_activity, newGameFragment);
        transaction.commit();
    }


    public void playGameOnClick(View view) {

        String player1Name = ((AutoCompleteTextView)findViewById(R.id.autocomplete_player1name)).getText().toString();
        String player2Name = ((AutoCompleteTextView)findViewById(R.id.autocomplete_player2name)).getText().toString();

        int player1TeamIndex = ((Spinner) findViewById(R.id.spinner_player1)).getSelectedItemPosition();
        int player1DrawableId = NewGameFragment.images[player1TeamIndex];

        int player2TeamIndex = ((Spinner) findViewById(R.id.spinner_player2)).getSelectedItemPosition();
        int player2DrawableId = NewGameFragment.images[player2TeamIndex];

        Intent gameplayIntent = new Intent(GameStartActivity.this, GameplayActivity.class);

        gameplayIntent.putExtra(EXTRA_GAME_START_STRATEGY, GAME_START_STRATEGY_NEWGAME);

        gameplayIntent.putExtra(EXTRA_HOME_PLAYER_NAME, player1Name);
        gameplayIntent.putExtra(EXTRA_GUEST_PLAYER_NAME, player2Name);
        gameplayIntent.putExtra(EXTRA_HOME_PLAYER_DRAWABLE, player1DrawableId);
        gameplayIntent.putExtra(EXTRA_GUEST_PLAYER_DRAWABLE, player2DrawableId);

        //GameStartActivity.this.startActivity(gameplayIntent);
        startActivityForResult(gameplayIntent, REQ_CODE_NEW_GAME);
    }

    public void onClickFinishActivity(View view) {
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }

    public void resumeGameOnClick(View view) {

        Intent gameplayIntent = new Intent(GameStartActivity.this, GameplayActivity.class);

        gameplayIntent.putExtra(EXTRA_GAME_START_STRATEGY, GAME_START_STRATEGY_CONTINUE);

        GameStartActivity.this.startActivity(gameplayIntent);

        // startActivityForResult(resumeGameIntent, REQ_CODE_RESUME_GAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE_RESUME_GAME && resultCode == RESULT_OK) {

        }
        if(requestCode == REQ_CODE_NEW_GAME && resultCode == RESULT_OK) {
            Toast.makeText(this, "Game finished", Toast.LENGTH_SHORT).show();
        }
    }
}
