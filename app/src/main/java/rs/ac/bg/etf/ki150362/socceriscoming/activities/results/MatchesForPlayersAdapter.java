package rs.ac.bg.etf.ki150362.socceriscoming.activities.results;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.Match;

public class MatchesForPlayersAdapter extends RecyclerView.Adapter<MatchesForPlayersAdapter.MatchesForPlayersHolder> {

    private List<Match> matches = new ArrayList<>();

    @NonNull
    @Override
    public MatchesForPlayersHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_result_item, parent, false);
        return new MatchesForPlayersHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MatchesForPlayersHolder matchesForPlayersHolder, int i) {
        Match current = matches.get(i);

        matchesForPlayersHolder.player1ImageView.setBackgroundResource(current.getHomePlayerDrawableId());
        matchesForPlayersHolder.player2ImageView.setBackgroundResource(current.getGetGuestPlayerDrawableId());

        matchesForPlayersHolder.player1TextView.setText(current.getHomePlayerName());
        matchesForPlayersHolder.player2TextView.setText(current.getGuestPlayerName());

        matchesForPlayersHolder.player1ScoreTextView.setText(String.valueOf(current.getHomePlayerScore()));
        matchesForPlayersHolder.player2ScoreTextView.setText(String.valueOf(current.getGuestPlayerScore()));

//        matchesForPlayersHolder.timeFinishedTextView.setText(String.valueOf(current.getEndTime().toString()));

        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        Date gameFinished = current.getEndTime();
        String gameFinishedString = df.format(gameFinished);
        matchesForPlayersHolder.timeFinishedTextView.setText(gameFinishedString);

        /*long val = 1346524199000l;
        Date date=new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(date);*/
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    class MatchesForPlayersHolder extends RecyclerView.ViewHolder {

        ImageView player1ImageView;
        ImageView player2ImageView;

        TextView player1TextView;
        TextView player2TextView;

        TextView player1ScoreTextView;
        TextView player2ScoreTextView;

        TextView timeFinishedTextView;

        public MatchesForPlayersHolder(@NonNull View itemView) {
            super(itemView);

            player1ImageView = itemView.findViewById(R.id.matchResult_player1image);
            player2ImageView = itemView.findViewById(R.id.matchResult_player2image);

            player1TextView = itemView.findViewById(R.id.matchResult_player1name);
            player2TextView = itemView.findViewById(R.id.matchResult_player2name);

            player1ScoreTextView = itemView.findViewById(R.id.matchResult_player1score);
            player2ScoreTextView = itemView.findViewById(R.id.matchResult_player2score);

            timeFinishedTextView = itemView.findViewById(R.id.matchResult_timeFinished);
        }
    }
}
