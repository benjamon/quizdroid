package edu.washington.benjamon.quizdroid;

import android.app.Application;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ben on 5/12/2015.
 */
public class QuizApp extends Application implements TopicRepository {
    private static QuizApp instance;
    ArrayList<Topic> tList = new ArrayList<Topic>();

    public QuizApp() {
            if (instance == null) {
                instance = this;
            } else {
                Log.e("OnCreate","THERE CAN ONLY BE ONE");
            }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String json = null;

        try {
            InputStream inputStream = getAssets().open("questions.json");

            JsonReader jRead = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            // I didn't use spaces because I find my bulky mountain of code majestic.

            jRead.beginArray();
            while (jRead.hasNext()) {
                jRead.beginObject();
                String tittle = "";
                String descr = "";
                List<Quiz> qList = new ArrayList<Quiz>();
                while(jRead.hasNext()) {
                    String nam = jRead.nextName();
                    if (nam.equals("title")) {
                        tittle = jRead.nextString();
                    } else if (nam.equals("desc")) {
                        descr = jRead.nextString();
                    } else if (nam.equals("questions")) {
                        jRead.beginArray();
                        while(jRead.hasNext()) {
                            String question = "";
                            int correct = 0;
                            List<String> answers = new ArrayList<String>();
                            jRead.beginObject();
                            while(jRead.hasNext()) {
                                String ham = jRead.nextName();
                                if (ham.equals("text")) {
                                    question = jRead.nextString();
                                } else if (ham.equals("answer")){
                                    correct = jRead.nextInt();
                                } else if (ham.equals("answers")) {
                                    jRead.beginArray();
                                        while(jRead.hasNext()) {
                                            answers.add(jRead.nextString());
                                        }
                                    jRead.endArray();
                                }
                            }
                            jRead.endObject();
                            qList.add(new Quiz(question, answers, correct));
                        }
                        jRead.endArray();
                    } else {
                        jRead.skipValue();
                    }
                }
                jRead.endObject();
                tList.add(new Topic(tittle,descr,qList));
                Log.e("tuckface", tList.get(tList.size()-1).toString());
            }
            jRead.endArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Topic getTopic(int tNumber) {
        return tList.get(tNumber);
    }

    public Quiz getQuiz(int tNumber, int qNumber) {
        return tList.get(tNumber).quiz.get(qNumber);
    }

    public List<String> getTopicTitles() {
        List<String> ls = new ArrayList<String>();
        for (int i = 0; i <= tList.size() - 1; i++) {
            ls.add(tList.get(i).tTitle);
        }
        return ls;
    }

    public List<String> getAnswers(int tNumber, int qNumber) {
        List<String> ls = new ArrayList<String>();
        for (int i = 0; i <= tList.get(tNumber).quiz.get(qNumber).answers.size()-1; i++) {
            ls.add(tList.get(tNumber).quiz.get(qNumber).answers.get(i));
        }
        return ls;
    }

    private String readJSONFile(InputStream inputStream) throws IOException {
        int size = inputStream.available();
        byte[] buffer =  new byte[size];
        inputStream.read(buffer);
        inputStream.close();

        return new String(buffer,"UTF-8");
    }
}

class Topic {
    String tTitle;
    String tdesc;
    List<Quiz> quiz;

    public Topic(String topic, String desc, List<Quiz> quizzes) {
        tTitle = topic;
        tdesc = desc;
        quiz = quizzes;
    }

    public String toString() {
        return tTitle + " " + tdesc + " " + quiz.toString();
    }
}

class Quiz {
    String quest;
    List<String> answers;
    int rightAnswer;

    public Quiz(String tquest, List<String> tans, int rightA) {
        quest = tquest;
        answers = tans;
        rightAnswer = rightA;
    }

    public String toString() {
        return quest;
    }
}

interface TopicRepository {
    public Topic getTopic(int tNumber);

    public Quiz getQuiz(int tNumber, int qNumber);

    public List<String> getTopicTitles();

    public List<String> getAnswers(int tNumber, int qNumber);
}