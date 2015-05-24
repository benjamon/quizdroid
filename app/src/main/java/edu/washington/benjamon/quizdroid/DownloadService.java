package edu.washington.benjamon.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by eric on 5/17/15.
 */
public class DownloadService extends IntentService {
    private DownloadManager dm;
    private long enqueue;
    public static final int BENS_ALARM = 123;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() { super.onCreate();
        Log.e("FUCK", "dservice created"); }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.i("DownloadService", "entered onHandleIntent()");
        // Hooray! This method is called where the AlarmManager shouldve started the download service and we just received it here!

        // Specify the url you want to download here
        String url = PreferenceManager.getDefaultSharedPreferences(this).getString("url","http://tednewardsandbox.site44.com/questions.json");

        Log.e("FUCK", "should be downloading here");

        // Star the download
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));


        enqueue = dm.enqueue(request);

    }

    public static void startOrStopAlarm(Context context, boolean on) {
        Log.e("FUCK", "WE GOT TO STARTORSTOPALARM");

        Log.i("DownloadService", "startOrStopAlarm on = " + on);

        Intent alarmReceiverIntent = new Intent(context, prefreceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, BENS_ALARM, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if (on) {
            int refreshInterval = 5 * 60000; // 5 min x 60,000 milliseconds = total ms in 5 min

            Log.i("DownloadService", "setting alarm to " + refreshInterval);

            // Start the alarm manager to repeat
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), refreshInterval, pendingIntent);
        }
        else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();

            Log.i("DownloadService", "Stopping alarm");
        }
    }
}