package edu.washington.benjamon.quizdroid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private DownloadManager dm;
    private long enqueue;
    PendingIntent alarmIntent = null;
    BroadcastReceiver alarmReceiver = new prefreceiver();
    AlarmManager am;
    QuizApp qApp;
    private static final int SETTINGS_RESULT = 1;
    String url = " ";
    int freq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(alarmReceiver, new IntentFilter("edu.washington.benjamon.quizdroid.prefreceiver"));

        qApp = (QuizApp) getApplication();

        ListView Lview = (ListView)findViewById(R.id.bigList);
        final ArrayList<String> list = (ArrayList<String>) qApp.getTopicTitles();

        final ArrayAdapter<String> fapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        Lview.setAdapter(fapter);

        ListView listview = (ListView)findViewById(R.id.bigList);
        listview.setOnItemClickListener(this);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Toast.makeText(getApplicationContext(), sharedPrefs.getString("prefURL","FUCK"),Toast.LENGTH_SHORT).show();
        url = sharedPrefs.getString("prefURL","questions.json");
        freq  = Integer.parseInt(sharedPrefs.getString("prefFreq","1"));
        am = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent i = new Intent();
        i.setAction("edu.washington.benjamon.quizdroid.prefreceiver");
        i.putExtra("url", url);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 60000*freq, alarmIntent);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);

    }

    public void openPrefs() {
        Intent i = new Intent(getApplicationContext(), Preference_Activity.class);
        startActivityForResult(i, SETTINGS_RESULT);
    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (Settings.System.getInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0) == ((PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prefAPMode",false)) ? 1 : 0)) {
            // toggle airplane mode
            Settings.System.putInt(
                    getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prefAPMode",false) ? 0 : 1);

            // Post an intent to reload
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prefAPMode",false));
            sendBroadcast(intent);
        }
        if(requestCode==SETTINGS_RESULT)
        {
            displayUserSettings();
        }

    }

    private void displayUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        url = sharedPrefs.getString("prefURL","questions.json");
        //Toast.makeText(getApplicationContext(), sharedPrefs.getString("prefURL","FUCK"),Toast.LENGTH_SHORT).show();

        boolean isint = true;
        try {
            freq = Integer.parseInt(sharedPrefs.getString("prefFreq","1"));
        } catch (NumberFormatException e) {
            isint = false;
        }

        if (isint) {
            am.cancel(alarmIntent);
            alarmIntent.cancel();
            am = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
            Intent i = new Intent();
            i.setAction("edu.washington.benjamon.quizdroid.prefreceiver");
            i.putExtra("url", url);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
            am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 60000 * freq, alarmIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_prefs:
                openPrefs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        String item = adapter.getItemAtPosition(position).toString();
        Intent myIntent = new Intent(MainActivity.this, SingleQuizActivity.class);
        myIntent.putExtra("topic", item); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }
    /// / This is your receiver that you registered in the onCreate that will receive any messages that match a download-complete like broadcast
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("FUCK", "WE GOT TO ONRECEIVE");
            String action = intent.getAction();

            dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

            Log.i("MyApp BroadcastReceiver", "onReceive of registered download reciever");

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Log.i("MyApp BroadcastReceiver", "download complete!");
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                // if the downloadID exists
                if (downloadID != 0) {

                    // Check status
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    Cursor c = dm.query(query);
                    if(c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        Log.d("DM Sample","Status Check: "+status);
                        switch(status) {
                            case DownloadManager.STATUS_PAUSED:
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_RUNNING:
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                // The download-complete message said the download was "successfu" then run this code
                                ParcelFileDescriptor file;
                                StringBuffer strContent = new StringBuffer("");

                                try {
                                    // Get file from Download Manager (which is a system service as explained in the onCreate)
                                    file = dm.openDownloadedFile(downloadID);
                                    FileInputStream fis = new FileInputStream(file.getFileDescriptor());

                                    StringWriter writer = new StringWriter();
                                    String newFileString = writer.toString();
                                    try {
                                        Log.i("MyApp", "writing downloaded to file");

                                        File phile = new File(getFilesDir().getAbsolutePath(), "questions.json");
                                        FileOutputStream fos = new FileOutputStream(phile);
                                        fos.write(newFileString.getBytes());
                                        fos.close();
                                    }
                                    catch (IOException e) {
                                        Log.e("FUCK", "File write failed: " + e.toString());
                                    }

                                    Log.e("FUCK", newFileString);


                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                Toast.makeText(getApplicationContext(), "This application, much like this application, is Toast.",Toast.LENGTH_LONG);
                                finish();
                                break;
                        }
                    }
                }
            }
        }
    };
}
