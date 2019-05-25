package rs.ac.bg.etf.ki150362.socceriscoming.util.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class TeamSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] contentArray;
    private Integer[] imageArray;

    public TeamSpinnerAdapter(Context context, int resource, int textViewResourceId, String[] objects, Integer[] imageArray) {
        super(context, resource, textViewResourceId, objects);

        this.context = context;
        this.contentArray = objects;
        this.imageArray = imageArray;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_value_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
        textView.setText(contentArray[position]);

        ImageView imageView = (ImageView)row.findViewById(R.id.spinnerImages);
        imageView.setImageResource(imageArray[position]);

        return row;
    }
}
