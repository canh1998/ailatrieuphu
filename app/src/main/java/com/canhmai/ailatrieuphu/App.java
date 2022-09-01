package com.canhmai.ailatrieuphu;

import android.app.Application;

import androidx.room.Room;

import com.canhmai.ailatrieuphu.db.AppDB;

public class App extends Application {
    private static App instance;
    private Storage storage;
    private AppDB db;
    private MediaManager mediaManager;
    private boolean allowbacked;

    public static App getInstance() {
        return instance;
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }

    public AppDB getDb() {
        return db;
    }


    public Storage getStorage() {
        return storage;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        storage = new Storage();
        mediaManager = new MediaManager(this);
        db = Room.databaseBuilder(this, AppDB.class, "Question")
                .createFromAsset("db/Question.db")
                .build();
        allowbacked = true;
    }


    public boolean isAllowbacked() {
        return allowbacked;
    }

    public void setAllowbacked(boolean allowbacked) {
        this.allowbacked = allowbacked;
    }
}
