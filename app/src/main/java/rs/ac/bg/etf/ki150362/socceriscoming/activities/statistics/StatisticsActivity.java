package rs.ac.bg.etf.ki150362.socceriscoming.activities.statistics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.MatchesTuple;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.MatchesViewModel;

public class StatisticsActivity extends AppCompatActivity {

    private MatchesViewModel matchesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        RecyclerView recyclerView = findViewById(R.id.recycler_statistics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final MatchesAdapter matchesAdapter = new MatchesAdapter();
        recyclerView.setAdapter(matchesAdapter);

        matchesViewModel = ViewModelProviders.of(this).get(MatchesViewModel.class);
        matchesViewModel.getAllPlayerPairs().observe(this, new Observer<List<MatchesTuple>>() {
            @Override
            public void onChanged(@Nullable List<MatchesTuple> matchesTuples) {
                matchesAdapter.setMatchesPairs(matchesTuples);
            }
        });

    }
}
