package com.comp3617.assignment1.anamicakartik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final int QUESTION_INDEX = 0;
    private static final int OPTION_1_INDEX = 1;
    private static final int OPTION_2_INDEX = 2;
    private static final int OPTION_3_INDEX = 3;
    private static final int ANSWER_INDEX = 4;
    public  static final String SCORE_MESSAGE = "com.comp3617.assignment1.quizapp.score";

    private Button      backBtn;
    private Button      nextBtn;
    private RadioGroup  radioGroup;
    private RadioButton selectedRadioBtn;
    private RadioButton radioBtn1;
    private RadioButton radioBtn2;
    private RadioButton radioBtn3;

    private TextView    textView;
    private int         questionIndex = 0;
    private String[]    quizQuestions;
    private String      answerStr;
    private int         maxQuestions = 0;
    private int[]       scores;

    private Map<Integer,int[]>         storeOptionState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backBtn     =   (Button) findViewById(R.id.BackButton);
        nextBtn     =   (Button) findViewById(R.id.NextButton);
        radioGroup  =   (RadioGroup) findViewById(R.id.radioGroup);
        textView    =   (TextView) findViewById( R.id.textView );

        backBtn.setOnClickListener(this);
        nextBtn.setOnClickListener( this );
        radioGroup.setOnClickListener(this);

        quizQuestions = getResources().getStringArray(R.array.questions);
        maxQuestions = quizQuestions.length;
        scores = new int[maxQuestions];
        setQuizQuestion();
        storeOptionState = new HashMap<Integer,int[]>(maxQuestions);

    }

    private void setQuizQuestion() {

        if(questionIndex < maxQuestions) {
            String quizStr[] = quizQuestions[questionIndex].split("\\|");
            String questionStr = quizStr[QUESTION_INDEX];
            String option1Str = quizStr[OPTION_1_INDEX];
            String option2Str = quizStr[OPTION_2_INDEX];
            String option3Str = quizStr[OPTION_3_INDEX];
            answerStr = quizStr[ANSWER_INDEX];


            textView.setText(questionStr);

            System.out.println("The Question String is " + questionStr);

            radioBtn1 = (RadioButton)findViewById(R.id.Radio1);
            radioBtn1.setText(option1Str);

            radioBtn2 = (RadioButton)findViewById(R.id.Radio2);
            radioBtn2.setText(option2Str);

            radioBtn3 = (RadioButton)findViewById(R.id.Radio3);
            radioBtn3.setText(option3Str);

        }
        //radioGroup.clearCheck();
    }

    private void setScore(){
        selectedRadioBtn = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId()));
        String radioValue = null;
        if(selectedRadioBtn != null){
            radioValue = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
            if(radioValue.equals(answerStr)) {
                if(scores[questionIndex] != 1)
                    scores[questionIndex]++;
            }
            else {
                if (scores[questionIndex] == 1)
                    scores[questionIndex]--;
            }
        }
    }

    @Override
    public void onClick( View v) {

        if (v == backBtn) {
            if (questionIndex > 0) {
                backBtn.setText("Back");
                if (questionIndex == maxQuestions - 2) {
                    nextBtn.setText("Next");
                }

                int[] retrieveRadioOptionStates = storeOptionState.get(questionIndex);
                System.out.println("Retrieve  " + storeOptionState.get(questionIndex).toString());

                if (retrieveRadioOptionStates != null){
                    if (retrieveRadioOptionStates[0] == 1) {
                        radioBtn1.setChecked(true);
                        radioBtn1.setSelected(true);
                    } else if (retrieveRadioOptionStates[1] == 1) {
                        radioBtn2.setChecked(true);
                        radioBtn2.setSelected(true);
                    } else if (retrieveRadioOptionStates[2] == 1) {
                        radioBtn3.setChecked(true);
                        radioBtn3.setSelected(true);
                    }
                }
                --questionIndex;
            }

            else {
                backBtn.setText("Start");
            }
            setScore();
            setQuizQuestion();


        } else if (v == nextBtn) {
            if (questionIndex < maxQuestions-1) {
                backBtn.setText("Back");
                setScore();
                if (questionIndex == maxQuestions - 2) {
                    nextBtn.setText("Finish");
                }
                ++questionIndex;
            } else {
                setScore();
                int sum = 0;
                for (int i : scores) {
                    sum += i;
                }
                System.out.println("Score " + String.valueOf(sum));

                // Send this to SCOREACTIVITY
                Intent intent = new Intent(this, ScoreActivity.class);
                StringBuffer sendMessageToScoreActivty = new StringBuffer();
                sendMessageToScoreActivty.append("The score of quiz is :" + String.valueOf(sum));
                intent.putExtra(SCORE_MESSAGE, sendMessageToScoreActivty.toString());
                startActivity(intent);
            }

            System.out.println("Question no." + String.valueOf(questionIndex));

            // Find index of radio button selected
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(radioButtonID);
            int idx = radioGroup.indexOfChild(radioButton);
            int[] listOfRadioBtnStates = new int[3];
            for(int i=0 ; i<3; i++) {
                if(i == idx) {
                    listOfRadioBtnStates[i] = 1;
                }
                else {
                    listOfRadioBtnStates[i] = 0;
                }
            }
            storeOptionState.put(questionIndex, listOfRadioBtnStates);
            setQuizQuestion();
            radioGroup.clearCheck();
        }

    }

}