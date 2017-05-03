package es.jmoral.simplecomicreader.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import es.jmoral.simplecomicreader.activities.main.MainActivity;

/**
 * Created by owniz on 1/05/17.
 */

public class OpenFileBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("test", intent.getData());
        context.startActivity(i);
    }
}
