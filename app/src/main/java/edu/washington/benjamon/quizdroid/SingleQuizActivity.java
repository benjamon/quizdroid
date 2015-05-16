package edu.washington.benjamon.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Scanner;


public class SingleQuizActivity extends ActionBarActivity {

    String theTopic;
    int currentQNumber = 1;
    int currentCorrectNumber = 0;
    String userGuess = "na";
    String actualAnswer;
    String lastQuestion;
    int qArrayLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_quiz);

        Intent thisIntent = getIntent();
        theTopic = thisIntent.getStringExtra("topic");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.quizframe, new TopicPage())
                    .commit();
        }
    }

    public void openQuestion() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.quizframe, new QuestionPage())
                .setTransition(0)
                .commit();
    }

    public void openAnswer() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.quizframe, new AnswerPage())
                .setTransition(0)
                .commit();
    }

    public void backToTopics() {
        Intent ntj = new Intent(SingleQuizActivity.this, MainActivity.class);
        SingleQuizActivity.this.startActivity(ntj);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_quiz, menu);
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

    public class QuestionPage extends Fragment {

        QuizApp qApp;
        Topic tapic;
        TextView axt;
        Button submitButton;
        String sa[];
        String sq[];
        int thisCorrect = 0;

        public QuestionPage() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.quiz_question, container, false);

            qApp = (QuizApp) getApplication();

            axt = (TextView)rootView.findViewById(R.id.questiontext);
            submitButton = (Button)rootView.findViewById(R.id.submitbut);

            axt.setText(theTopic);

            final RadioGroup rg = (RadioGroup)rootView.findViewById(R.id.rgRebeccaBlack);

            final RadioButton r1 = (RadioButton)rootView.findViewById(R.id.q1);
            final RadioButton r2 = (RadioButton)rootView.findViewById(R.id.q2);
            final RadioButton r3 = (RadioButton)rootView.findViewById(R.id.q3);
            final RadioButton r4 = (RadioButton)rootView.findViewById(R.id.q4);

            if (theTopic.equals("Science!")) {
                sq = getResources().getStringArray(R.array.mathqarray);
                sa = getResources().getStringArray(R.array.mathaarray);
                tapic = qApp.getTopic(0);
            } else if (theTopic.equals("Marvel Super Heroes")) {
                sq = getResources().getStringArray(R.array.physicsqarray);
                sa = getResources().getStringArray(R.array.physicsaarray);
                tapic = qApp.getTopic(1);
            } else if (theTopic.equals("Physics")) {
                sq = getResources().getStringArray(R.array.marvelqarray);
                sa = getResources().getStringArray(R.array.marvelaarray);
                tapic = qApp.getTopic(2);
            }

            qArrayLength = tapic.quiz.size()-1;

            axt.setText((CharSequence) tapic.quiz.get(currentQNumber - 1).quest);
            thisCorrect = tapic.quiz.get(currentQNumber - 1).rightAnswer;
            r1.setText((CharSequence) tapic.quiz.get(currentQNumber - 1).answers.get(0));
            r2.setText((CharSequence) tapic.quiz.get(currentQNumber - 1).answers.get(1));
            r3.setText((CharSequence) tapic.quiz.get(currentQNumber - 1).answers.get(2));
            r4.setText((CharSequence) tapic.quiz.get(currentQNumber - 1).answers.get(3));

            switch(thisCorrect) {
                case 1:
                    actualAnswer = r1.getText().toString();
                    break;
                case 2:
                    actualAnswer = r2.getText().toString();
                    break;
                case 3:
                    actualAnswer = r3.getText().toString();
                    break;
                case 4:
                    actualAnswer = r4.getText().toString();
                    break;
            }

            r1.setOnClickListener( new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           userGuess = r1.getText().toString();
                                       }
                                   }
            );
            r2.setOnClickListener( new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           userGuess = r2.getText().toString();
                                       }
                                   }
            );
            r3.setOnClickListener( new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           userGuess = r3.getText().toString();
                                       }
                                   }
            );
            r4.setOnClickListener( new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           userGuess = r4.getText().toString();
                                       }
                                   }
            );

            submitButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!userGuess.equals("na")) {
                        if (userGuess.equals(actualAnswer)) {
                            currentCorrectNumber += 1;
                        }
                        openAnswer();
                    }
                }
            });

            return rootView;
        }
    }

    public class TopicPage extends Fragment {

        Topic tapic;
        QuizApp qApp;
        Button startQuizButton;

        public TopicPage() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.topic_page, container, false);

            qApp = (QuizApp) getApplication();

            TextView descriptionBox = (TextView)rootView.findViewById(R.id.descriptiontext);
            TextView titleBox = (TextView)rootView.findViewById(R.id.topictitle);
            startQuizButton = (Button)rootView.findViewById(R.id.nextbut);

            startQuizButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuestion();
                }
            });

            titleBox.setText(theTopic);

            if (theTopic.equals("Science!")) {
                tapic = qApp.getTopic(0);
            } else if (theTopic.equals("Marvel Super Heroes")) {
                tapic = qApp.getTopic(1);
            } else if (theTopic.equals("Mathematics")) {
                tapic = qApp.getTopic(2);
            }
            descriptionBox.setText(tapic.tdesc);
            return rootView;
        }
    }

    public class AnswerPage extends Fragment {

        Button startQuizButton;
        QuizApp qApp;

        public AnswerPage() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_answer, container, false);

            qApp = (QuizApp) getApplication();

            TextView lastQ = (TextView)rootView.findViewById(R.id.theQuestion);
            TextView userA = (TextView)rootView.findViewById(R.id.yourAnswer);
            TextView corA = (TextView)rootView.findViewById(R.id.correctAnswer);
            TextView toteC = (TextView)rootView.findViewById(R.id.totalCorrect);
            Button butN = (Button)rootView.findViewById(R.id.nextQ);
            lastQ.setText(lastQuestion);
            userA.setText(userGuess);
            corA.setText(actualAnswer);
            toteC.setText(currentCorrectNumber + " out of " + currentQNumber + " correct");

            if (currentQNumber + 1 >= qArrayLength) {
                butN.setText("FINISH QUIZ");
            }

            butN.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentQNumber + 1 >= qArrayLength) {
                        backToTopics();
                    } else {
                        userGuess = "na";
                        currentQNumber += 1;
                        openQuestion();
                    }
                }
            });

            return rootView;
        }
    }
}
