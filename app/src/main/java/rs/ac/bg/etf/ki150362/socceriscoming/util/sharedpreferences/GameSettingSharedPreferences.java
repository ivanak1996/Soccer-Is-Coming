package rs.ac.bg.etf.ki150362.socceriscoming.util.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

import static android.content.Context.MODE_PRIVATE;

public class GameSettingSharedPreferences {

    public static final String PREFERENCES_GOT_SETTINGS = "PREFERENCES_GOT_SETTINGS";
    public static final String PREFERENCE_TERRAIN = "PREFERENCE_TERRAIN";
    public static final String PREFERENCE_GAMELEVEL = "PREFERENCE_GAMELEVEL";
    public static final String PREFERENCE_GOALSNUMBER = "PREFERENCE_GOALSNUMBER";

    public static final String[] terrainsNames = {"Dorn", "Kingswood", "Winterfell"};
    public static final Integer[] terrainImages = {R.drawable.background_dorn_field, R.drawable.background_kingswood_field, R.drawable.background_winterfell_field};
    public static final Integer[] gameLevelsValues = {1, 2, 3};
    public static final Integer[] goalNumberValues = {3, 5, 10};

    public static final int defaultTerrainResourceId = terrainImages[0];
    public static final int defaultGoalsNumber = goalNumberValues[1];
    public static final int defaultGameLevel = gameLevelsValues[0];

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_GOT_SETTINGS, MODE_PRIVATE);
    }

    public static void setSharedPreferences(Context context, String terrainName, int gameLevel, int goalsNumber) {

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_TERRAIN, terrainName);
        editor.putInt(PREFERENCE_GAMELEVEL, gameLevel);
        editor.putInt(PREFERENCE_GOALSNUMBER, goalsNumber);
        editor.apply();
    }

    public static String getTerrainNamePreference(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(PREFERENCE_TERRAIN, null);
    }

    public static int getTerrainResourceIdPreference(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        String terrainName = prefs.getString(PREFERENCE_TERRAIN, null);
        int index = -1;

        for (int i = 0; i < terrainsNames.length; i++) {
            if (terrainName.equals(terrainsNames[i])) {
                index = i;
                break;
            }
        }

        try {
            return terrainImages[index];
        } catch (Exception e) {
        }

        return defaultTerrainResourceId;
    }

    public static int getNumberOfGoalsPreference(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        int value = prefs.getInt(PREFERENCE_GOALSNUMBER, -1);
        return value != -1 ? value : defaultGoalsNumber;
    }

    public static int getGameLevelPreference(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        int value = prefs.getInt(PREFERENCE_GAMELEVEL, -1);
        return value != -1 ? value : defaultGameLevel;
    }

}
