package dgs.dgscorecard;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by Jay on 3/23/15.
 */
public class Player {

    private int pID;
    private String mName;
    private TextView nameField;
    private NumberPicker scoreField;
    private NumberPicker puttField;
    private TextView totalScore;
    private TextView underOver;

    public Player(String name){
        mName = name;
    }


    public Player(String name, int IDnumber){
        mName = name;
        pID = IDnumber;
    }


    public Player(String name, String nameF, String scoreF, String puttF, String totalS, String underO, int IDnumber, Context context){
        pID = IDnumber;
        mName = name;
        nameField = new TextView(context);
        nameField.setText(name);
        scoreField = new NumberPicker(context);
        scoreField.setValue(Integer.parseInt(scoreF));
        puttField = new NumberPicker(context);
        puttField.setValue(Integer.parseInt(puttF));
        totalScore = new TextView(context);
        totalScore.setText(totalS);
        underOver = new TextView(context);
        underOver.setText(underO);
    }

    public TextView getNameField() {
        return nameField;
    }

    public void setNameField(TextView nameField) {
        this.nameField = nameField;
    }

    public NumberPicker getScoreField() {
        return scoreField;
    }

    public void setScoreField(NumberPicker scoreField) {
        this.scoreField = scoreField;
    }

    public NumberPicker getPuttField() {
        return puttField;
    }

    public void setPuttField(NumberPicker puttField) {
        this.puttField = puttField;
    }

    public TextView getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(TextView totalScore) {
        this.totalScore = totalScore;
    }

    public TextView getUnderOver() {
        return underOver;
    }

    public void setUnderOver(TextView underOver) {
        this.underOver = underOver;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getTextNameF() {return nameField.getText().toString();}
    public String getTextTScore() {return totalScore.getText().toString();}
    public String getTextUO() {return underOver.getText().toString();}
    public String getValScore() {return Integer.toString(scoreField.getValue());}
    public String getValPutts() {return Integer.toString(puttField.getValue());}
    public String getPID() {return Integer.toString(pID);}

}
