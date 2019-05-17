package rs.ac.bg.etf.ki150362.socceriscoming.dialog.about;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class AboutDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_about_dialog, null);

        builder.setView(view)
                .setTitle("About");

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(150,204,153,0)));
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_image_alert);

        return dialog;
    }
}
