package edu.washington.benjamon.quizdroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 */
public class topPageActivity extends Fragment {

    Button startQuizButton;
    String theTopic;

    public topPageActivity(String topek) {

        theTopic = topek;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.topic_page, container, false);
        TextView descriptionBox = (TextView)rootView.findViewById(R.id.descriptiontext);
        TextView titleBox = (TextView)rootView.findViewById(R.id.topictitle);
        startQuizButton = (Button)rootView.findViewById(R.id.nextbut);

        startQuizButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        titleBox.setText(theTopic);

        if (theTopic.equals("Math")) {
            descriptionBox.setText(R.string.mathd);
        } else if (theTopic.equals("Physics")) {
            descriptionBox.setText(R.string.physicsd);
        } else if (theTopic.equals("Marvel Super Heroes")) {
            descriptionBox.setText(R.string.marveld);
        }
        return rootView;
    }
}