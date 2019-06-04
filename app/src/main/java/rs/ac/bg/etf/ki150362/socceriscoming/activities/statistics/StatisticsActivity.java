package rs.ac.bg.etf.ki150362.socceriscoming.activities.statistics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.MatchesTuple;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.MatchesViewModel;
import rs.ac.bg.etf.ki150362.socceriscoming.util.asynctasks.EnterFullScreenAsyncTask;

public class StatisticsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_statistics);

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

        final RecyclerView recyclerView = findViewById(R.id.recycler_statistics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final MatchesAdapter matchesAdapter = new MatchesAdapter();
        recyclerView.setAdapter(matchesAdapter);

        final TextView noContentTextView = findViewById(R.id.statistics_recycler_empty_view);

        matchesViewModel = ViewModelProviders.of(this).get(MatchesViewModel.class);
        matchesViewModel.getAllPlayerPairs().observe(this, new Observer<List<MatchesTuple>>() {
            @Override
            public void onChanged(@Nullable List<MatchesTuple> matchesTuples) {
                matchesAdapter.setMatchesPairs(matchesTuples);
                if(matchesTuples.size() > 0){
                    noContentTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    noContentTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

    }

    public void onClickGoBack(View view) {
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }

    public void deleteAllMatches(View view) {
        matchesViewModel.deleteAllMatches();
    }
}
