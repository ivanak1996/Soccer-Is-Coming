package rs.ac.bg.etf.ki150362.socceriscoming.activities.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class TerrainSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] contentArray;
    private Integer[] imageArray;

    public TerrainSpinnerAdapter(Context context, int resource, int textViewResourceId, String[] objects, Integer[] imageArray) {
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
        View row = inflater.inflate(R.layout.spinner_terrain_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.spinnerTerrainTextView);
        textView.setText(contentArray[position]);

        ImageView imageView = (ImageView)row.findViewById(R.id.spinnerTerrainImage);
        imageView.setBackgroundResource(imageArray[position]);

        return row;
    }

}
