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

    public void deleteAllMatches() {repository.deleteAllMatches();}

    public void deleteMatchesForPlayers(String player1, String player2) {
        repository.deleteMatchesForPlayers(player1, player2);
    }

    public LiveData<List<MatchesTuple>> getAllPlayerPairs() {
        return allPlayerPairs;
    }

    public LiveData<List<Match>> getAllMatchesForPlayers(String player1, String player2) {
        return repository.getAllMatchesForPlayers(player1, player2);
    }

}
