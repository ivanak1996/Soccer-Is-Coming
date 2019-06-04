package rs.ac.bg.etf.ki150362.socceriscoming;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class SoccerIsComingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
