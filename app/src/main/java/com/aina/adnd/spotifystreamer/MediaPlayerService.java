package com.aina.adnd.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MediaPlayerService extends Service {
    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
