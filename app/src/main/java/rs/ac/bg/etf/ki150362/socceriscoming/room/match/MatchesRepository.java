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

    public LiveData<List<Match>> getAllMatchesForPlayers(String player1, String player2) {
        return matchDao.getAllMatchesForPlayers(player1, player2);
    }

    public void insert(Match match) {
        new InsertMatchAsyncTask(matchDao).execute(match);
    }

    public void deleteAllMatches() {
        new DeleteAllMatchesAsyncTask(matchDao).execute();
    }

    public void deleteMatchesForPlayers(String player1, String player2) {
        new DeleteMatchesForPlayersAsyncTask(matchDao).execute(player1, player2);
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

    private static class DeleteAllMatchesAsyncTask extends AsyncTask<Void, Void, Void> {

        private MatchDao matchDao;

        public DeleteAllMatchesAsyncTask(MatchDao matchDao) {
            this.matchDao = matchDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            matchDao.deleteAllMatches();
            return null;
        }
    }

    private static class DeleteMatchesForPlayersAsyncTask extends AsyncTask<String, Void, Void> {

        private MatchDao matchDao;

        public DeleteMatchesForPlayersAsyncTask(MatchDao matchDao) {
            this.matchDao = matchDao;
        }

        @Override
        protected Void doInBackground(String... players) {
            matchDao.deleteMatchesForPlayers(players[0], players[1]);
            return null;
        }
    }
}
