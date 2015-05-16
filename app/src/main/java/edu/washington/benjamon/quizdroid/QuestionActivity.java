package edu.washington.benjamon.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;


public class QuestionActivity extends ActionBarActivity {
    QuizApp qApp;
    int q;
    String t;
    String choseAnswer[];
    int checkrd = 0;
    int correct;
    int thiscorrect;
    String thuscorrect;
    String guess = "na";
    Topic tapic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_question);

        qApp = (QuizApp) getApplication();

        final RadioGroup rg = (RadioGroup)findViewById(R.id.rgRebeccaBlack);
        TextView temxt = (TextView)findViewById(R.id.questiontext);

        final RadioButton r1 = (RadioButton)findViewById(R.id.q1);
        final RadioButton r2 = (RadioButton)findViewById(R.id.q2);
        final RadioButton r3 = (RadioButton)findViewById(R.id.q3);
        final RadioButton r4 = (RadioButton)findViewById(R.id.q4);

        Button but = (Button)findViewById(R.id.submitbut);

        Intent implorement = getIntent();
        q = implorement.getIntExtra("currentquestion", 0);
        t = implorement.getStringExtra("topic");
        correct = implorement.getIntExtra("correct",0);

        if (t.equals("Science!")) {
            tapic = qApp.getTopic(0);
        } else if (t.equals("Marvel Super Heroes")) {
            tapic = qApp.getTopic(1);
        } else if (t.equals("Mathematics")) {
            tapic = qApp.getTopic(2);
        }

        temxt.setText((CharSequence) tapic.quiz.get(q).quest);
        thiscorrect = tapic.quiz.get(q).rightAnswer;
        r1.setText((CharSequence) tapic.quiz.get(q).answers.get(0));
        r2.setText((CharSequence) tapic.quiz.get(q).answers.get(1));
        r3.setText((CharSequence) tapic.quiz.get(q).answers.get(2));
        r4.setText((CharSequence) tapic.quiz.get(q).answers.get(3));

        switch(thiscorrect) {
            case 1:
                thuscorrect = r1.getText().toString();
            break;
            case 2:
                thuscorrect = r2.getText().toString();
            break;
            case 3:
                thuscorrect = r3.getText().toString();
            break;
            case 4:
                thuscorrect = r4.getText().toString();
            break;
        }

        r1.setOnClickListener( new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       guess = r1.getText().toString();
                                   }
                               }
        );
        r2.setOnClickListener( new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       guess = r2.getText().toString();
                                   }
                               }
        );
        r3.setOnClickListener( new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       guess = r3.getText().toString();
                                   }
                               }
        );
        r4.setOnClickListener( new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       guess = r4.getText().toString();
                                   }
                               }
        );

        but.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!guess.equals("na")) {
                        if (guess.equals(thuscorrect)) {
                            correct += 1;
                        }
                        Intent intorent = new Intent(QuestionActivity.this, AnswerActivity.class);
                        intorent.putExtra("topic", t);
                        intorent.putExtra("correct", correct);
                        intorent.putExtra("currentquestion", q);
                        intorent.putExtra("useranswer", guess);
                        intorent.putExtra("correctanswer", thuscorrect);
                        intorent.putExtra("question", tapic.quiz.get(q).quest);
                        QuestionActivity.this.startActivity(intorent);
                    }
                }
            }

        );

        rg.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      int checkedId = rg.getCheckedRadioButtonId();
                      Toast.makeText(getApplicationContext(), "humbug", Toast.LENGTH_SHORT);
                      if (checkedId == R.id.q1) {
                          Toast.makeText(getApplicationContext(), "choice 1", Toast.LENGTH_SHORT);
                      } else if (checkedId == R.id.q2) {
                          Toast.makeText(getApplicationContext(), "choice 2", Toast.LENGTH_SHORT);
                      } else if (checkedId == R.id.q3) {
                          Toast.makeText(getApplicationContext(), "choice 3", Toast.LENGTH_SHORT);
                      } else if (checkedId == R.id.q4) {
                          Toast.makeText(getApplicationContext(), "choice 4", Toast.LENGTH_SHORT);
                      }

                  }
              }

        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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
