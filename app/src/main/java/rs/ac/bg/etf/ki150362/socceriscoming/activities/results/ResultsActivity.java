package rs.ac.bg.etf.ki150362.socceriscoming.activities.results;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.activities.GameState;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.Match;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.MatchesViewModel;
import rs.ac.bg.etf.ki150362.socceriscoming.util.asynctasks.EnterFullScreenAsyncTask;

public class ResultsActivity extends AppCompatActivity {

    public static final String EXTRA_INTENT_ORIGIN = "EXTRA_INTENT_ORIGIN";
    public static final String EXTRA_PLAYER1_NAME = "EXTRA_PLAYER1_NAME";
    public static final String EXTRA_PLAYER2_NAME = "EXTRA_PLAYER2_NAME";

    public static final String INTENT_ORIGIN_GAME_FINISHED = "INTENT_ORIGIN_GAME_FINISHED";
    public static final String INTENT_ORIGIN_STATISTICS = "INTENT_ORIGIN_STATISTICS";

    public static final String SAVEDINSTANCE_PLAYER_NAME_1 = "SAVED_INSTANCE_PLAYER_NAME_1";
    public static final String SAVEDINSTANCE_PLAYER_NAME_2 = "SAVED_INSTANCE_PLAYER_NAME_2";

    String player1Name, player2Name;

    private MatchesViewModel matchesViewModel;

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
        setContentView(R.layout.activity_results);

        matchesViewModel = ViewModelProviders.of(this).get(MatchesViewModel.class);

        Intent caller = getIntent();

        // full screen
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

        if (savedInstanceState == null) {
            if (INTENT_ORIGIN_GAME_FINISHED.equals(caller.getStringExtra(EXTRA_INTENT_ORIGIN))) {
                GameState gameState = GameState.reloadGame(this);
                if (gameState != null && caller.getStringExtra(EXTRA_INTENT_ORIGIN).equals(INTENT_ORIGIN_GAME_FINISHED)) {
                    Match match = new Match(gameState.homePlayerName, gameState.guestPlayerName,
                            gameState.homePlayerScore, gameState.guestPlayerScore,
                            gameState.homePlayerDrawableId, gameState.guestPlayerDrawableId,
                            new Date(System.currentTimeMillis()));

                    matchesViewModel.insert(match);

                    player1Name = match.getHomePlayerName();
                    player2Name = match.getGuestPlayerName();

                    GameState.eraseGameState(this);
                }
            }
            else {
                player1Name = caller.getStringExtra(EXTRA_PLAYER1_NAME);
                player2Name = caller.getStringExtra(EXTRA_PLAYER2_NAME);
            }
        } else {
            player1Name = savedInstanceState.getString(SAVEDINSTANCE_PLAYER_NAME_1);
            player2Name = savedInstanceState.getString(SAVEDINSTANCE_PLAYER_NAME_2);
        }

        final RecyclerView recyclerView = findViewById(R.id.recycler_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TextView noContentTextView = findViewById(R.id.results_recycler_empty_view);

        final MatchesForPlayersAdapter adapter = new MatchesForPlayersAdapter();
        recyclerView.setAdapter(adapter);

        matchesViewModel.getAllMatchesForPlayers(player1Name, player2Name).observe(this, new Observer<List<Match>>() {
            @Override
            public void onChanged(@Nullable List<Match> matches) {
                Log.d("observerChanged", "matches found " + matches.size());
                adapter.setMatches(matches);
                if(matches.size() > 0){
                    noContentTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    noContentTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

    }

    public void deleteAllMatches(View view) {
        matchesViewModel.deleteMatchesForPlayers(player1Name, player2Name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVEDINSTANCE_PLAYER_NAME_1, player1Name);
        outState.putString(SAVEDINSTANCE_PLAYER_NAME_2, player2Name);
    }

    public void onClickGoBack(View view) {
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }
}
