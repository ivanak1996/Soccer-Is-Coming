package rs.ac.bg.etf.ki150362.socceriscoming.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.room.player.PlayerViewModel;
import rs.ac.bg.etf.ki150362.socceriscoming.util.adapters.TeamSpinnerAdapter;

public class NewGameFragment extends Fragment {
    public static String[] teamNames = {"Arryn", "Baratheon", "Bolton", "Frey", "Greyjoy", "Martell", "Stark", "Targaryen", "Tully", "Tyrell"};
    public static  Integer[] images = {R.drawable.player_arryn, R.drawable.player_baratheon, R.drawable.player_bolton, R.drawable.player_frey,
            R.drawable.player_greyjoy, R.drawable.player_martell, R.drawable.player_stark, R.drawable.player_targaryen,
            R.drawable.player_tully, R.drawable.player_tyrell};

    private LinkedList<String> teamsNames = new LinkedList<>();

    private PlayerViewModel playerViewModel;

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

        setupAutoCompletePlayerNames(view);

        return view;
    }

    private void setupAutoCompletePlayerNames(View view) {
        AutoCompleteTextView player1TextView = view.findViewById(R.id.autocomplete_player1name);
        AutoCompleteTextView player2TextView = view.findViewById(R.id.autocomplete_player2name);

        if(playerViewModel == null) {
            playerViewModel = ViewModelProviders.of(getActivity()).get(PlayerViewModel.class);
        }

        final ArrayAdapter<String> autocompletetextAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());

        playerViewModel.getAllPlayersNames().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                autocompletetextAdapter.addAll(strings);
            }
        });

        player1TextView.setAdapter(autocompletetextAdapter);
        player2TextView.setAdapter(autocompletetextAdapter);

    }

}
