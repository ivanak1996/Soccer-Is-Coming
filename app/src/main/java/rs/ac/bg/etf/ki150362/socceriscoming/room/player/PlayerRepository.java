package rs.ac.bg.etf.ki150362.socceriscoming.room.player;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import rs.ac.bg.etf.ki150362.socceriscoming.room.GameDatabase;

public class PlayerRepository {
    private PlayerDao playerDao;
    private LiveData<List<Player>> allPlayers;

    public PlayerRepository(Application application) {
        GameDatabase database = GameDatabase.getInstance(application);
        playerDao = database.playerDao();
        allPlayers = playerDao.getAllPlayers();
    }

    public void insert(Player player) {
        new InsertUserAsyncTask(playerDao).execute(player);
    }

    public LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    private static class InsertUserAsyncTask extends AsyncTask<Player, Void, Void> {

        private PlayerDao playerDao;

        public InsertUserAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... players) {
            playerDao.insert(players[0]);
            return null;
        }
    }
}
