package rs.ac.bg.etf.ki150362.socceriscoming.room.match;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MatchesViewModel extends AndroidViewModel {

    private MatchesRepository repository;
    private LiveData<List<MatchesTuple>> allPlayerPairs;

    public MatchesViewModel(@NonNull Application application) {
        super(application);
        repository = new MatchesRepository(application);
        allPlayerPairs = repository.getPlayerPairs();
    }

    public void insert(Match match) {
        repository.insert(match);
    }

    public LiveData<List<MatchesTuple>> getAllPlayerPairs() {
        return allPlayerPairs;
    }
}
