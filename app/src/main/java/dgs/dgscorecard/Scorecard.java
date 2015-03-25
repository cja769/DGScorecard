package dgs.dgscorecard;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay on 3/23/15.
 */
public class Scorecard {

    private Map<Player, int[]> mScores;
    private Map<Player, int[]> mPutts;
    private Course mCourse;
    private Player[] mPlayers;


    public Scorecard () {
        mScores = new HashMap<Player, int[]>();
        mPutts = new HashMap<Player, int[]>();
        mCourse = new Course();
    }

    public Scorecard (Course course) {
        mScores = new HashMap<Player, int[]>();
        mPutts = new HashMap<Player, int[]>();
        mCourse = course;
    }

    public Map<Player, int[]> getScores() {
        return mScores;
    }

    public Map<Player, int[]> getPutts() {
        return mPutts;
    }

    public Course getCourse(){
        return mCourse;
    }

    public Player[] getPlayers() {
        return mPlayers;
    }

    public void setScores(Map<Player, int[]> mScores) {
        this.mScores = mScores;
    }

    public void setPutts(Map<Player, int[]> mPutts) {
        this.mPutts = mPutts;
    }

    public void setCourse(Course course){
        mCourse = course;
    }

    public void setPlayers(Player[] players){
        mPlayers = players;
        for(Player player: players){
            int[] s = new int[mCourse.getNumHoles()];
            for(int i = 0; i < mCourse.getNumHoles(); i++)
                s[i] = mCourse.getPars()[i];
            mScores.put(player, s);
            int[] l = new int[mCourse.getNumHoles()];
            mPutts.put(player, l);
        }
    }

    public int calculateTotal(Player player){
        int[] scores = mScores.get(player);
        int total = 0;
        for(int s: scores)
            total += s;
        return total;

    }


}
