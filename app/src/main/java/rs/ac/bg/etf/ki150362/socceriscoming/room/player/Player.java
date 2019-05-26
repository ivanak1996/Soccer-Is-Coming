package rs.ac.bg.etf.ki150362.socceriscoming.room.player;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_table")
public class Player {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public Player(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
