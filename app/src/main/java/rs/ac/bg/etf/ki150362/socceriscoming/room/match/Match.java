package rs.ac.bg.etf.ki150362.socceriscoming.room.match;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/*@Entity(tableName = "match_table",
        foreignKeys = {
        @ForeignKey(entity = Player.class, parentColumns = "id", childColumns = "homePlayerId", onDelete = CASCADE),
        @ForeignKey(entity = Player.class, parentColumns = "id", childColumns = "guestPlayerId", onDelete = CASCADE)
})*/
@Entity(tableName = "match_table")
public class Match {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String homePlayerName;

    private String guestPlayerName;

    private int homePlayerScore;

    private int guestPlayerScore;

    private int homePlayerDrawableId;

    private int getGuestPlayerDrawableId;

    // TODO: how?
    private Date endTime;

    public Match(String homePlayerName, String guestPlayerName, int homePlayerScore, int guestPlayerScore, int homePlayerDrawableId, int getGuestPlayerDrawableId, Date endTime) {
        this.homePlayerName = homePlayerName;
        this.guestPlayerName = guestPlayerName;
        this.homePlayerScore = homePlayerScore;
        this.guestPlayerScore = guestPlayerScore;
        this.homePlayerDrawableId = homePlayerDrawableId;
        this.getGuestPlayerDrawableId = getGuestPlayerDrawableId;
        this.endTime = endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getHomePlayerName() {
        return homePlayerName;
    }

    public String getGuestPlayerName() {
        return guestPlayerName;
    }

    public int getHomePlayerScore() {
        return homePlayerScore;
    }

    public int getGuestPlayerScore() {
        return guestPlayerScore;
    }

    public int getHomePlayerDrawableId() {
        return homePlayerDrawableId;
    }

    public int getGetGuestPlayerDrawableId() {
        return getGuestPlayerDrawableId;
    }

    public Date getEndTime() {
        return endTime;
    }
}
