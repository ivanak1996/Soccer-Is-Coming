package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GameState implements Serializable {

    public static final String FILE_NAME = "saved game file";

    public long elapsedInTotal;

    public String homePlayerName, guestPlayerName;
    public int homePlayerScore, guestPlayerScore;

    public float[][] homePlayersCoordinates, guestPlayersCoordinates;
    public float[][] homePlayersVelocities, guestPlayersVelocities;

    public boolean homePlayersTurn;

    public float ballX, ballY;
    public float ballVx, ballVy;

    public int homePlayerDrawableId, guestPlayerDrawableId;

    public boolean saveGame(Context context) {

        try {

            File file = new File(context.getFilesDir(), FILE_NAME);

            if (!file.exists()) {
                Log.d("saveGame", "File successfully created.");
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);

            objectOutputStream.close();
            fileOutputStream.close();

            Log.d("saveGame", "Progress successfully saved to file.");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static GameState reloadGame(Context context) {

        try {
            FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            GameState gameState = (GameState) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            Log.d("reloadGame", "Progress successfully reloaded.");

            return gameState;

        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void eraseGameState(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);

        if (file.exists()) {
            file.delete();
            Log.d("eraseGameState", "File deleted.");
        }
    }

}
