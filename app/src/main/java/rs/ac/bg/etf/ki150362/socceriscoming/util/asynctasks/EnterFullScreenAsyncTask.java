package rs.ac.bg.etf.ki150362.socceriscoming.util.asynctasks;

import android.os.AsyncTask;
import android.view.View;

public class EnterFullScreenAsyncTask extends AsyncTask<Void, Void, Void> {

    private View mainView;

    public EnterFullScreenAsyncTask(View view) {
        mainView = view;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        enterFullScreenMode();
    }

    void enterFullScreenMode() {
        mainView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

}
