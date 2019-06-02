package rs.ac.bg.etf.ki150362.socceriscoming.activities.results;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.MatchesViewModel;

public class ResultsActivity extends AppCompatActivity {

    private MatchesViewModel matchesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        matchesViewModel = ViewModelProviders.of(this).get(MatchesViewModel.class);

        /*GameState gameState = GameState.reloadGame(this);
        if (gameState != null) {
            Match match = new Match(gameState.homePlayerName, gameState.guestPlayerName,
                    gameState.homePlayerScore, gameState.guestPlayerScore,
                    gameState.homePlayerDrawableId, gameState.guestPlayerDrawableId,
                    new java.sql.Date(System.currentTimeMillis()));

            matchesViewModel.insert(match);
        }*/

    }
}
