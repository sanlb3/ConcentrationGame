package com.example.concentrationgame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.concentrationgame.R;

public class MusicService extends Service {

    private MediaPlayer musicPlayer;

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Create and start the MediaPlayer object
        musicPlayer = MediaPlayer.create(this, R.raw.bg_music);
        musicPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Return START_NOT_STICKY to indicate that the service should not be restarted if it is killed
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // stop music when app is closed or destroyed
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public void startMusic() {
        if (musicPlayer != null && !musicPlayer.isPlaying()) {
            musicPlayer.start();
        }
    }

    public void stopMusic() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.pause();

        }
    }
}
