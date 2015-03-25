package dgs.dgscorecard;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by Jay on 3/23/15.
 */
public class Player {

    private String mName;
    private TextView nameField;
    private NumberPicker scoreField;
    private NumberPicker puttField;
    private TextView totalScore;
    private TextView underOver;

    public Player(String name){
        mName = name;
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

}
