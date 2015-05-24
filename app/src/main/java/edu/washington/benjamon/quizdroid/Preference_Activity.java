package edu.washington.benjamon.quizdroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Preference_Activity extends PreferenceActivity {
    /*PendingIntent alarmIntent = null;
    BroadcastReceiver alarmReceiver = new prefreceiver();
    AlarmManager am;
    Button savebut;
    EditText frequency;
    EditText url;
    int freqNum;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_xml);

        /*registerReceiver(alarmReceiver, new IntentFilter("edu.washington.benjamon.quizdroid.prefreceiver"));

        savebut.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isint = true;
                try {
                    freqNum = Integer.parseInt(frequency.getText().toString());
                } catch (NumberFormatException e) {
                    isint = false;
                }
                if (isint) {
                    am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent i = new Intent();
                    i.setAction("edu.washington.benjamon.quizdroid.prefreceiver");
                    i.putExtra("url", url.getText().toString());
                    alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
                    am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 10000 * freqNum, alarmIntent);
                }
            }
        });*/
    }
}
