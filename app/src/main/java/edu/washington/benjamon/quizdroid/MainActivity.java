package edu.washington.benjamon.quizdroid;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView Lview = (ListView)findViewById(R.id.bigList);
        String[] topix = new String[] { "Math", "Physics", "Marvel Super Heroes" };
        final ArrayList<String> list = new ArrayList<String>();
        for (int i=0; i < topix.length; i++) {
            list.add(topix[i]);
        }

        final ArrayAdapter<String> fapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topix);

        Lview.setAdapter(fapter);

        ListView listview = (ListView)findViewById(R.id.bigList);
        listview.setOnItemClickListener(this);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        String item = adapter.getItemAtPosition(position).toString();
        Intent myIntent = new Intent(MainActivity.this, SingleQuizActivity.class);
        myIntent.putExtra("topic", item); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }
}
