package rs.ac.bg.etf.ki150362.socceriscoming.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.util.adapters.TeamSpinnerAdapter;

public class NewGameFragment extends Fragment {
    private String[] teamNames = {"Arryn", "Baratheon", "Bolton", "Frey", "Greyjoy", "Martell", "Stark", "Targaryen", "Tully", "Tyrell"};
    private Integer[] images = {R.drawable.player_arryn, R.drawable.player_baratheon, R.drawable.player_bolton, R.drawable.player_frey,
            R.drawable.player_greyjoy, R.drawable.player_martell, R.drawable.player_stark, R.drawable.player_targaryen,
            R.drawable.player_tully, R.drawable.player_tyrell};

    public NewGameFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_game, container, false);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_player1);
        TeamSpinnerAdapter adapter1 = new TeamSpinnerAdapter(getActivity(), R.layout.spinner_value_layout, R.id.spinnerTextView, teamNames, images);

        spinner1.setAdapter(adapter1);

        Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner_player2);
        TeamSpinnerAdapter adapter2 = new TeamSpinnerAdapter(getActivity(), R.layout.spinner_value_layout, R.id.spinnerTextView, teamNames, images);

        spinner2.setAdapter(adapter2);

        return view;
    }


}
