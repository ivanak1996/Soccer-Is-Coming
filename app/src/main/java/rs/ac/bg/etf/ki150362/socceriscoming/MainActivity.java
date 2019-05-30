package rs.ac.bg.etf.ki150362.socceriscoming;

import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import rs.ac.bg.etf.ki150362.socceriscoming.activities.gaming.GameStartActivity;
import rs.ac.bg.etf.ki150362.socceriscoming.activities.settings.SettingsActivity;
import rs.ac.bg.etf.ki150362.socceriscoming.activities.statistics.StatisticsActivity;
import rs.ac.bg.etf.ki150362.socceriscoming.dialog.about.AboutDialog;
import rs.ac.bg.etf.ki150362.socceriscoming.util.asynctasks.EnterFullScreenAsyncTask;
import rs.ac.bg.etf.ki150362.socceriscoming.util.service.MusicService;

public class MainActivity extends AppCompatActivity {

    // settings activity constants
    public static final int REQ_CODE_SETTINGS_ACTIVITY = 128;
    public static final String EXTRA_SETTINGS_MESSAGE = "EXTRA_SETTINGS_MESSAGE";

    private boolean mIsBound = false;

    private MusicService mServ;

    private ServiceConnection sCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServ = ((MusicService.ServiceBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }

    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class), sCon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(sCon);
            mIsBound = false;
        }
    }

    void enterFullScreenMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            // full screen
            enterFullScreenMode();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.layout_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                try {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception ex) {

                }
                return true;
            }
        });

        // TODO: if this is set I cannot scroll the spinners
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        // full screen
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            (new EnterFullScreenAsyncTask(getWindow().getDecorView())).execute();
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });

        ImageButton aboutImageButton = findViewById(R.id.imagebutton_about);
        aboutImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutDialog();
            }
        });

        // sound

        doBindService();

        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        startService(music);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    void openAboutDialog() {
        AboutDialog aboutDialog = new AboutDialog();
        aboutDialog.show(getSupportFragmentManager(), "about");
    }

    public void onClickOpenSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, REQ_CODE_SETTINGS_ACTIVITY);
    }

    public void onClickOpenStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    public void onClickGameStart(View view) {
        Intent intent = new Intent(this, GameStartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE_SETTINGS_ACTIVITY && resultCode == RESULT_OK) {
            String message = data.getStringExtra(EXTRA_SETTINGS_MESSAGE);
            if(message != null) Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }
}
