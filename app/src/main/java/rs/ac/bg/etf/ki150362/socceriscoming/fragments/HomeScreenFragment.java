package rs.ac.bg.etf.ki150362.socceriscoming.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import rs.ac.bg.etf.ki150362.socceriscoming.R;
import rs.ac.bg.etf.ki150362.socceriscoming.dialog.about.AboutDialog;

public class HomeScreenFragment extends Fragment {

    private ImageButton aboutImageButton;

    public HomeScreenFragment() {
    }


    void openAboutDialog() {
        AboutDialog aboutDialog = new AboutDialog();
        aboutDialog.show(getActivity().getSupportFragmentManager(), "about");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        // about
        aboutImageButton = view.findViewById(R.id.imagebutton_about);
        aboutImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutDialog();
            }
        });

        return view;
    }

}
