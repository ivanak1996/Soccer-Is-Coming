package rs.ac.bg.etf.ki150362.socceriscoming.activities.statistics;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.room.match.MatchesTuple;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchHolder> {

    private List<MatchesTuple> matchesPairs = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public MatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_pair_item, parent, false);
        return new MatchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchHolder matchHolder, int i) {
        MatchesTuple currentMatchPair = matchesPairs.get(i);
        matchHolder.player1NameTextView.setText(currentMatchPair.player1Name);
        matchHolder.player2NameTextView.setText(currentMatchPair.player2Name);
        matchHolder.player1ScoreTextView.setText(String.valueOf(currentMatchPair.player1Wins));
        matchHolder.player2ScoreTextView.setText(String.valueOf(currentMatchPair.player2Wins));
    }

    public void setMatchesPairs(List<MatchesTuple> pairs) {
        this.matchesPairs = pairs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return matchesPairs.size();
    }

    class MatchHolder extends RecyclerView.ViewHolder {
        TextView player1NameTextView;
        TextView player2NameTextView;
        TextView player1ScoreTextView;
        TextView player2ScoreTextView;

        public MatchHolder(@NonNull View itemView) {
            super(itemView);

            player1NameTextView = itemView.findViewById(R.id.textview_player1);
            player2NameTextView = itemView.findViewById(R.id.textview_player2);
            player1ScoreTextView = itemView.findViewById(R.id.textview_player1score);
            player2ScoreTextView = itemView.findViewById(R.id.textview_player2score);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null)
                        listener.onItemClick(matchesPairs.get(position));
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(MatchesTuple matchesTuple);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
