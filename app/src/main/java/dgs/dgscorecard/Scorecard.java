package dgs.dgscorecard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private ArrayList<ArrayList<Player>> mCurrentOrder;


    public Scorecard () {
        mScores = new HashMap<Player, ArrayList<Integer>>();
        mPutts = new HashMap<Player, ArrayList<Integer>>();
        mCourse = new Course();
        mCurrentOrder = new ArrayList<ArrayList<Player>>();
    }

    public Scorecard (Course course) {
        mScores = new HashMap<Player, ArrayList<Integer>>();
        mPutts = new HashMap<Player, ArrayList<Integer>>();
        mCourse = course;
        mCurrentOrder = new ArrayList<ArrayList<Player>>();
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

    public void setCurrentOrder(ArrayList<ArrayList<Player>> ps){
        mCurrentOrder = ps;
    }

    public ArrayList<ArrayList<Player>> getCurrentOrder(){ return mCurrentOrder;};

    public void setOrderForHole(ArrayList<Player> ps, int index){
        mCurrentOrder.add(index, ps);
    }

    public ArrayList<Player> getOrderForHole(int index){
        return mCurrentOrder.get(index);
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
        setOrderForHole(players, 0);

    }

    public int calculateTotal(Player player){
        ArrayList<Integer> scores = mScores.get(player);
        int total = 0;
        for(int s: scores)
            total += s;
        return total;

    }

    // Hole is the hole that we're going to zero indexed
    public ArrayList<Player> sortPlayers(int hole){
        ArrayList<Player> temp = new ArrayList<>();
        if(hole == 0){
            mCurrentOrder.add(mPlayers);
            return mPlayers;
        }
        for(Player p: mCurrentOrder.get(hole-1)){
            ArrayList<Integer> scores = mScores.get(p);
            p.setScore(scores.get(hole-1));
            temp.add(p);
        }

        Collections.sort(temp);
        mCurrentOrder.add(hole,temp);
        return temp;
    }


}
