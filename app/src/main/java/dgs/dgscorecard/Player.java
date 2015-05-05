package dgs.dgscorecard;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Jay on 3/23/15.
 */
public class Player implements Comparable<Player> {

    private int pID;
    private String mName;
    private TextView nameField;
    private TextView scoreField;
    private TextView puttField;
    private TextView totalScore;
    private TextView underOver;
    private int score;
    private LinearLayout scoreHolder;


    public Player() { score = 0;};
    public Player(String name){
        mName = name;
        score = 0;
    }


    public Player(String name, int IDnumber){
        mName = name;
        pID = IDnumber;
        score = 0;
    }


    public Player(String name, String nameF, String scoreF, String puttF, String totalS, String underO, int IDnumber, Context context){
        pID = IDnumber;
        mName = name;
        nameField = new TextView(context);
        nameField.setText(name);
        scoreField = new TextView(context);
        scoreField.setText(scoreF);
        puttField = new TextView(context);
        puttField.setText(puttF);
        totalScore = new TextView(context);
        totalScore.setText(totalS);
        underOver = new TextView(context);
        underOver.setText(underO);
        score = 0;
    }


    public TextView getNameField() {
        return nameField;
    }

    public void setNameField(TextView nameField) {
        this.nameField = nameField;
    }

    public TextView getScoreField() {
        return scoreField;
    }

    public void setScoreField(TextView scoreField) {
        this.scoreField = scoreField;
    }

    public TextView getPuttField() {
        return puttField;
    }

    public void setPuttField(TextView puttField) {
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

    public int getScore() { return score;};
    public void setScore(int num) { score = num;};
    public LinearLayout getScoreHolder() { return scoreHolder;};
    public void setScoreHolder(LinearLayout ll) { scoreHolder = ll;};

    public String getTextNameF() {return nameField.getText().toString();}
    public String getTextTScore() {return totalScore.getText().toString();}
    public String getTextUO() {return underOver.getText().toString();}
    public String getValScore() {return scoreField.getText()+"";}
    public String getValPutts() {return puttField.getText()+"";}
    public String getPID() {return Integer.toString(pID);}
    public int getIntPID() { return pID;}

    @Override
    public int compareTo(Player other){
        if(other.getScore() == 0) return 1;
        else if(this.getScore() == 0) return -1;

        if(this.getScore() > other.getScore())
            return 1;
        else if(other.getScore() > this.getScore())
            return -1;
        return 0;
    }


}
