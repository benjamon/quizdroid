package edu.washington.benjamon.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class prefreceiver extends BroadcastReceiver {
    public prefreceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,intent.getStringExtra("url"),Toast.LENGTH_SHORT).show();
    }
}
