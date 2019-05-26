package rs.ac.bg.etf.ki150362.socceriscoming.room.match;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import rs.ac.bg.etf.ki150362.socceriscoming.room.GameDatabase;

public class MatchesRepository {
    private MatchDao matchDao;
    private LiveData<List<MatchesTuple>> playerPairs;

    public MatchesRepository(Application application) {
        GameDatabase database = GameDatabase.getInstance(application);
        this.matchDao = database.matchDao();
        playerPairs = matchDao.getAllPlayerPairs();
    }

    public void insert(Match match) {
        new InsertMatchAsyncTask(matchDao).execute(match);
    }

    public LiveData<List<MatchesTuple>> getPlayerPairs() {
        return playerPairs;
    }

    private static class InsertMatchAsyncTask extends AsyncTask<Match, Void, Void> {

        private MatchDao matchDao;

        public InsertMatchAsyncTask(MatchDao matchDao) {
            this.matchDao = matchDao;
        }

        @Override
        protected Void doInBackground(Match... matches) {
            matchDao.insert(matches[0]);
            return null;
        }
    }
}
