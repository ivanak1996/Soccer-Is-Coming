package rs.ac.bg.etf.ki150362.socceriscoming.room.match;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MatchDao {

    @Insert
    void insert(Match match);

    @Update
    void update(Match match);

    @Delete
    void delete(Match match);

    @Query("SELECT * FROM match_table " +
            "WHERE ( homePlayerName=:player1Name AND guestPlayerName =:player2Name) " +
            "OR (guestPlayerName =:player1Name AND homePlayerName =:player2Name) " +
            "ORDER BY endTime DESC")
    LiveData<List<Match>> getAllMatchesForPlayers(String player1Name, String player2Name);

    @Query("SELECT " +
            "player1Name, " +
            "player2Name, " +
            "sum(case when score1 > score2 then 1 else 0 end) player1Wins, " +
            "sum(case when score2 > score1 then 1 else 0 end) player2Wins  " +
            "FROM " +
            "(SELECT " +
            " CASE WHEN homePlayerName < guestPlayerName THEN homePlayerName ELSE guestPlayerName END player1Name, " +
            " CASE WHEN homePlayerName < guestPlayerName THEN guestPlayerName ELSE homePlayerName END player2Name, " +
            " CASE WHEN homePlayerName < guestPlayerName THEN homePlayerScore ELSE guestPlayerScore END score1, " +
            " CASE WHEN homePlayerName < guestPlayerName THEN guestPlayerScore ELSE homePlayerScore END score2 " +
            "FROM match_table " +
            ") x " +
            "GROUP BY player1Name, player2Name")
    LiveData<List<MatchesTuple>> getAllPlayerPairs();

    @Query("DELETE FROM match_table " +
            "WHERE ( homePlayerName=:player1Name AND guestPlayerName =:player2Name) " +
            "OR (guestPlayerName =:player1Name AND homePlayerName =:player2Name) ")
    void deleteMatchesForPlayers(String player1Name, String player2Name);

    @Query("DELETE FROM match_table")
    void deleteAllMatches();
}
