package dgs.dgscorecard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay on 3/23/15.
 */
public class Scorecard {

    private Map<Player, ArrayList<Integer>> mScores;
    private Map<Player, ArrayList<Integer>> mPutts;
    private Course mCourse;
    private ArrayList<Player> mPlayers;


    public Scorecard () {
        mScores = new HashMap<Player, ArrayList<Integer>>();
        mPutts = new HashMap<Player, ArrayList<Integer>>();
        mCourse = new Course();
    }

    public Scorecard (Course course) {
        mScores = new HashMap<Player, ArrayList<Integer>>();
        mPutts = new HashMap<Player, ArrayList<Integer>>();
        mCourse = course;
    }

    public Map<Player, ArrayList<Integer>> getScores() {
        return mScores;
    }

    public Map<Player, ArrayList<Integer>> getPutts() {
        return mPutts;
    }

    public Course getCourse(){
        return mCourse;
    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public void setScores(Map<Player, ArrayList<Integer>> mScores) {
        this.mScores = mScores;
    }

    public void setPutts(Map<Player, ArrayList<Integer>> mPutts) {
        this.mPutts = mPutts;
    }

    public void setCourse(Course course){
        mCourse = course;
    }

    public void setPlayers(ArrayList<Player> players){
        mPlayers = players;
        for(Player player: players){
            ArrayList<Integer> s = new ArrayList<Integer>(mCourse.getNumHoles());
            for(int i = 0; i < mCourse.getNumHoles(); i++)
                s.add(mCourse.getPars().get(i));
            mScores.put(player, s);
            ArrayList<Integer> l = new ArrayList<Integer>(mCourse.getNumHoles());
            for(int i = 0; i < mCourse.getNumHoles(); i++)
                l.add(0);
            mPutts.put(player, l);
        }
    }

    public int calculateTotal(Player player){
        ArrayList<Integer> scores = mScores.get(player);
        int total = 0;
        for(int s: scores)
            total += s;
        return total;

    }


}
