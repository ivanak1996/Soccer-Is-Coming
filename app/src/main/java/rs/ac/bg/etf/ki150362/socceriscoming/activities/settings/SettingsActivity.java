package rs.ac.bg.etf.ki150362.socceriscoming.activities.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import rs.ac.bg.etf.ki150362.socceriscoming.MainActivity;
import rs.ac.bg.etf.ki150362.socceriscoming.R;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFERENCES_GOT_SETTINGS = "PREFERENCES_GOT_SETTINGS";
    public static final String PREFERENCE_TERRAIN = "PREFERENCE_TERRAIN";
    public static final String PREFERENCE_GAMELEVEL = "PREFERENCE_GAMELEVEL";
    public static final String PREFERENCE_GOALSNUMBER = "PREFERENCE_GOALSNUMBER";

    public static final String[] terrainsNames = {"Dorn", "Kingswood", "Winterfell"};
    public static final Integer[] terrainImages = {R.drawable.background_dorn_field, R.drawable.background_kingswood_field,R.drawable.background_winterfell_field};

    private Spinner spinner;
    private RadioGroup gameLevelRadioGroup, goalsNumberRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = findViewById(R.id.spinner_terrain);
        gameLevelRadioGroup = findViewById(R.id.radioGroupLevel);
        goalsNumberRadioGroup = findViewById(R.id.radioGroupGoalsNumber);

        initSpinner();

        retrieveSharedPrefs();
    }

    public void onClickGoBack(View view) {
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }

    public void onClickSaveSettings(View view) {
        setNewSettings();
    }

    private void setNewSettings(){

        // TODO: save to shared prefs
        setSharedPreferences();

        Intent result = new Intent();
        result.putExtra(MainActivity.EXTRA_SETTINGS_MESSAGE, "New params set");
        setResult(RESULT_OK, result);

        finish();
    }

    private void setSharedPreferences() {

        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_GOT_SETTINGS, MODE_PRIVATE).edit();
        editor.putString(PREFERENCE_TERRAIN, spinner.getSelectedItem().toString());
        editor.putInt(PREFERENCE_GAMELEVEL, gameLevelRadioGroup.getCheckedRadioButtonId());
        editor.putInt(PREFERENCE_GOALSNUMBER, goalsNumberRadioGroup.getCheckedRadioButtonId());
        editor.apply();

    }

    private void retrieveSharedPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_GOT_SETTINGS, MODE_PRIVATE);

        if(prefs == null) return;

        String terrainValue = prefs.getString(PREFERENCE_TERRAIN, null);
        int gameLevelValue = prefs.getInt(PREFERENCE_GAMELEVEL, -1);
        int goalsValue = prefs.getInt(PREFERENCE_GOALSNUMBER, -1);

        if(terrainValue != null) {
            ArrayAdapter terrainAdapter = (ArrayAdapter) spinner.getAdapter();
            int spinnerPosition = terrainAdapter.getPosition(terrainValue);

            if(spinnerPosition>=0) {
                spinner.setSelection(spinnerPosition);
            }
        }

        if(gameLevelValue != -1) {
            gameLevelRadioGroup.check(gameLevelValue);
        }

        if(goalsValue != -1){
            goalsNumberRadioGroup.check(goalsValue);
        }
    }

    private void initSpinner() {

        TerrainSpinnerAdapter adapter =
                new TerrainSpinnerAdapter(this, R.layout.spinner_terrain_layout, R.id.spinner_terrain, terrainsNames, terrainImages);
        spinner.setAdapter(adapter);
    }
}