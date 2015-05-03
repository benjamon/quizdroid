package edu.washington.benjamon.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TopicPageActivity extends ActionBarActivity {

    String toptit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_page);

        TextView tenxt = (TextView)findViewById(R.id.topictitle);
        TextView desxt = (TextView)findViewById(R.id.descriptiontext);
        Button nexet = (Button)findViewById(R.id.nextbut);

        Intent intertent = getIntent();
        String titil = intertent.getStringExtra("topic");
        toptit = titil;

        if (toptit.equals("Math")) {
            desxt.setText(R.string.mathd);
        } else if (toptit.equals("Physics")) {
            desxt.setText(R.string.physicsd);
        } else if (toptit.equals("Marvel Super Heroes")) {
            desxt.setText(R.string.marveld);
        }

        tenxt.setText(titil);

        nexet.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irkent = new Intent(TopicPageActivity.this, QuestionActivity.class);
                irkent.putExtra("currentquestion", 1);
                irkent.putExtra("topic", toptit);
                irkent.putExtra("correct",0);
                TopicPageActivity.this.startActivity(irkent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic_page, menu);
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
}
