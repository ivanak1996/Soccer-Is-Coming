package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.LinkedList;

public class Team {

    public static int NUMBER_OF_PLAYERS = 3;

    private ArrayList<Player> players = new ArrayList<>();
    private Player inFocus = null;
    private int score = 0;
    private int screenWidth;
    private int screenHeight;
    private Player.Position position;


    public Team(Player.Position position, int screenWidth, int screenHeight) {

        this.position = position;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            Player newPlayer = new Player(screenWidth, screenHeight, position);
            players.add(newPlayer);
        }
    }

    public void init(Bitmap logo) {

        for (int i=0; i<NUMBER_OF_PLAYERS; i++) {
//            Player newPlayer = new Player(screenWidth, screenHeight, position);
            players.get(i).init(logo, i);
        }
    }

    public void gain() {
        ++this.score;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getScore() {
        return score;
    }

    public Player focusedPlayer(int x, int y){
        for(Player player : players) {
            if(player.getScreenRect().contains(x, y)){
                return player;
            }
        }
        return null;
    }

}
