package rs.ac.bg.etf.ki150362.socceriscoming.room.player;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private PlayerRepository repository;
    private LiveData<List<Player>> allPlayers;
    private LiveData<List<String>> allPlayersNames;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        repository = new PlayerRepository(application);
        allPlayers = repository.getAllPlayers();
        allPlayersNames = repository.getAllPlayersNames();
    }

    public void insert(Player player) {
        repository.insert(player);
    }

    public LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    public LiveData<List<String>> getAllPlayersNames() {
        return allPlayersNames;
    }
}
