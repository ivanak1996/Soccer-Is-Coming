package rs.ac.bg.etf.ki150362.socceriscoming.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Date;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.Match;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.MatchDao;
import rs.ac.bg.etf.ki150362.socceriscoming.room.player.Player;
import rs.ac.bg.etf.ki150362.socceriscoming.room.player.PlayerDao;

@Database(entities = {Player.class, Match.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class GameDatabase extends RoomDatabase {

    private static GameDatabase instance;

    public abstract PlayerDao playerDao();
    public abstract MatchDao matchDao();

    public static synchronized GameDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GameDatabase.class, "game_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private PlayerDao playerDao;
        private MatchDao matchDao;

        public PopulateDbAsyncTask(GameDatabase db) {

            playerDao = db.playerDao();
            matchDao = db.matchDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Player ivana = new Player("Ivana");
            Player daenerys = new Player("Daenerys");
            playerDao.insert(ivana);
            playerDao.insert(daenerys);
            matchDao.insert(new Match(
                            ivana.getName(),
                            daenerys.getName(),
                            3,
                            0,
                            R.drawable.player_stark,
                            R.drawable.player_targaryen,
                            new Date(2019,5,20)
                    ));
            return null;
        }
    }

}
