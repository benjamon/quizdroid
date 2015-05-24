package edu.washington.benjamon.quizdroid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
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

    }

    public void openPrefs() {
        Intent i = new Intent(getApplicationContext(), Preference_Activity.class);
        startActivityForResult(i, SETTINGS_RESULT);
    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

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
}
