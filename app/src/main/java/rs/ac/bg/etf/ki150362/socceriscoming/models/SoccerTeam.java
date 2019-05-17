package rs.ac.bg.etf.ki150362.socceriscoming.models;

import android.content.Context;

import java.util.ArrayList;

public class SoccerTeam {

    // members per team
    public static final int NUMBER_OF_PLAYERS = 3;

    public static final float[][] TEAM_INITIAL_POSITIONS_OFFSET = {
            {0.2f, 0.2f},
            {0.3f, 0.5f},
            {0.2f, 0.7f}
    };

    private Context context;
    private String name;
    private ArrayList<SoccerPlayer> players;

    public SoccerTeam(Context context, String name) {

        this.context = context;
        this.name = name;

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {

        }

    }
}
