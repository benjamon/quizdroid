package edu.washington.benjamon.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AnswerActivity extends ActionBarActivity {

    int numcorrect;
    int curnum;
    String topit;
    String sq[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView correct = (TextView)findViewById(R.id.correctAnswer);
        TextView guess = (TextView)findViewById(R.id.yourAnswer);
        TextView question = (TextView)findViewById(R.id.theQuestion);
        TextView total = (TextView)findViewById(R.id.totalCorrect);

        final Button nextbut = (Button)findViewById(R.id.nextQ);

        Intent mop = getIntent();
        guess.setText("Your Guess: " + mop.getStringExtra("useranswer"));
        correct.setText("The Answer: " + mop.getStringExtra("correctanswer"));
        question.setText(mop.getStringExtra("question"));
        total.setText(mop.getIntExtra("correct",0) + " / " + mop.getIntExtra("currentquestion",0) + " Correct");
        numcorrect = mop.getIntExtra("correct", 0);
        curnum = mop.getIntExtra("currentquestion", 0);
        topit = mop.getStringExtra("topic");

        if (topit.equals("Math")) {
            sq = getResources().getStringArray(R.array.mathqarray);
        } else if (topit.equals("Physics")) {
            sq = getResources().getStringArray(R.array.physicsqarray);
        } else if (topit.equals("Marvel Super Heroes")) {
            sq = getResources().getStringArray(R.array.marvelqarray);
        }

        if (curnum + 1 == sq.length) {
            nextbut.setText("FINISH QUIZ");
        }

        nextbut.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nextbut.getText().toString().equals("FINISH QUIZ")) {
                        Intent ntj = new Intent(AnswerActivity.this, MainActivity.class);
                        AnswerActivity.this.startActivity(ntj);
                    } else {
                        Intent i = new Intent(AnswerActivity.this, QuestionActivity.class);
                        i.putExtra("topic", topit);
                        i.putExtra("currentquestion", curnum + 1);
                        i.putExtra("correct", numcorrect);
                        AnswerActivity.this.startActivity(i);
                    }
                }
            }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer, menu);
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
