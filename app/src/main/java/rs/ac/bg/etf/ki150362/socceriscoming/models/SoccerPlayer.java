package rs.ac.bg.etf.ki150362.socceriscoming.models;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class SoccerPlayer {

    private float x, y;
    private AppCompatImageView playerView;

    public SoccerPlayer(Context context, float x, float y) {
        this.x = x;
        this.y = y;

        playerView = new AppCompatImageView(context);
        playerView.setImageResource(R.drawable.player_stark);
    }

    public AppCompatImageView getPlayerView() {
        return playerView;
    }

}
