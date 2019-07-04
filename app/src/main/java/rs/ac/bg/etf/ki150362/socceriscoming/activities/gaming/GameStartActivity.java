package rs.ac.bg.etf.ki150362.socceriscoming.activities.gaming;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.activities.GameplayActivity;
import rs.ac.bg.etf.ki150362.socceriscoming.fragments.GameStartFragment;
import rs.ac.bg.etf.ki150362.socceriscoming.fragments.NewGameFragment;
import rs.ac.bg.etf.ki150362.socceriscoming.room.player.Player;
import rs.ac.bg.etf.ki150362.socceriscoming.room.player.PlayerViewModel;
import rs.ac.bg.etf.ki150362.socceriscoming.util.asynctasks.EnterFullScreenAsyncTask;

public class GameStartActivity extends AppCompatActivity {

    public static final int REQ_CODE_RESUME_GAME = 256;
    public static final int REQ_CODE_NEW_GAME = 257;

    public static final int GAME_START_STRATEGY_NEWGAME = 0;
    public static final int GAME_START_STRATEGY_CONTINUE = 1;

    public static final int GAME_MODE_SINGLE_PLAYER = 0;
    public static final int GAME_MODE_MULTI_PLAYER = 1;

    public static final String EXTRA_GAME_START_STRATEGY = "EXTRA_GAME_START_STRATEGY";
    public static final String EXTRA_HOME_PLAYER_NAME = "EXTRA_HOME_PLAYER_NAME";
    public static final String EXTRA_GUEST_PLAYER_NAME = "EXTRA_GUEST_PLAYER_NAME";
    public static final String EXTRA_HOME_PLAYER_DRAWABLE = "EXTRA_HOME_PLAYER_DRAWABLE";
    public static final String EXTRA_GUEST_PLAYER_DRAWABLE = "EXTRA_GUEST_PLAYER_DRAWABLE";
    public static final String EXTRA_NEW_GAME_MODE = "EXTRA_NEW_GAME_MODE";
    public static final String EXTRA_FINISHED_GAME_MESSAGE = "EXTRA_FINISHED_GAME_MESSAGE";


    private GameStartFragment gameStartFragment;
    private NewGameFragment newGameFragment;

    private PlayerViewModel playerViewModel;

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

        if(playerViewModel == null) {
            playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        }
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

        int selectedNewGameMode = getSelectedGameMode();

        String player1Name = ((AutoCompleteTextView)findViewById(R.id.autocomplete_player1name)).getText().toString();
        String player2Name = ((AutoCompleteTextView)findViewById(R.id.autocomplete_player2name)).getText().toString();

        int player1TeamIndex = ((Spinner) findViewById(R.id.spinner_player1)).getSelectedItemPosition();
        int player1DrawableId = NewGameFragment.images[player1TeamIndex];

        int player2TeamIndex = ((Spinner) findViewById(R.id.spinner_player2)).getSelectedItemPosition();
        int player2DrawableId = NewGameFragment.images[player2TeamIndex];

        List<String> currentPlayersList = playerViewModel.getAllPlayersNames().getValue();

        if (!currentPlayersList.contains(player1Name) && !player1Name.isEmpty()) {
            playerViewModel.insert(new Player(player1Name));
        }
        if (!currentPlayersList.contains(player2Name) && !player2Name.isEmpty()) {
            playerViewModel.insert(new Player(player2Name));
        }

        if(selectedNewGameMode == GAME_MODE_SINGLE_PLAYER) {
            player2Name = "Robot";
            List<Integer> imagesList = new ArrayList<Integer>(Arrays.asList(NewGameFragment.images));
            imagesList.remove(imagesList.indexOf(player1DrawableId));
            Random rand = new Random();
            int randomElement = imagesList.get(rand.nextInt(imagesList.size()));
            player2DrawableId = randomElement;
        }

        if(player1Name.isEmpty() || player2Name.isEmpty()) {
            Toast.makeText(this, "Please insert player's name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(player1Name.equals(player2Name)) {
            Toast.makeText(this, "Players cannot have the same name", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent gameplayIntent = new Intent(GameStartActivity.this, GameplayActivity.class);

        gameplayIntent.putExtra(EXTRA_GAME_START_STRATEGY, GAME_START_STRATEGY_NEWGAME);

        gameplayIntent.putExtra(EXTRA_HOME_PLAYER_NAME, player1Name);
        gameplayIntent.putExtra(EXTRA_GUEST_PLAYER_NAME, player2Name);
        gameplayIntent.putExtra(EXTRA_HOME_PLAYER_DRAWABLE, player1DrawableId);
        gameplayIntent.putExtra(EXTRA_GUEST_PLAYER_DRAWABLE, player2DrawableId);
        gameplayIntent.putExtra(EXTRA_NEW_GAME_MODE, selectedNewGameMode);


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

        Log.d("GameStartAct", "Returned to GameStartActivity with req code and res code "+ requestCode + ", "+resultCode);

        if(resultCode == RESULT_CANCELED) {
            setFragmentGameStart(null);
            String msg = data.getStringExtra("message");
            if(msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == REQ_CODE_NEW_GAME && resultCode == RESULT_OK) {
            Toast.makeText(this, "Game finished", Toast.LENGTH_SHORT).show();
        }
    }

    private int getSelectedGameMode() {
        RadioGroup newGameRadioGroup = findViewById(R.id.radioGroupNewGameMode);

        switch (newGameRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radioSinglePlayer:
                return GAME_MODE_SINGLE_PLAYER;
            case R.id.radioMultiPlayer:
                return GAME_MODE_MULTI_PLAYER;
            default:
                return -1;
        }
    }

    public void onClickSetRadioNewGameMode(View view) {

        Log.d("GameStartActivity", "Radio button clicked");

        RadioGroup newGameRadioGroup = findViewById(R.id.radioGroupNewGameMode);
        LinearLayout player2Layout = findViewById(R.id.layoutPlayer2);

        switch (newGameRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radioSinglePlayer:
                player2Layout.setVisibility(View.GONE);
                break;
            case R.id.radioMultiPlayer:
                player2Layout.setVisibility(View.VISIBLE);
                break;
        }

    }
}
