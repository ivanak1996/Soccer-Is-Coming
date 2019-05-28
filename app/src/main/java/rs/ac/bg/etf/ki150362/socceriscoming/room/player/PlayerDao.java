package rs.ac.bg.etf.ki150362.socceriscoming.room.player;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert
    void insert(Player player);

    @Update
    void update(Player player);

    @Delete
    void delete(Player player);

    @Query("DELETE FROM user_table")
    void deleteAllPlayers();

    @Query("SELECT * FROM user_table ORDER BY name DESC")
    LiveData<List<Player>> getAllPlayers();

    @Query("SELECT name from user_table ORDER BY name desc")
    LiveData<List<String>> getAllPlayersNames();

}
