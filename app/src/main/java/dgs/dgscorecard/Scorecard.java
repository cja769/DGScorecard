package dgs.dgscorecard;

import android.text.format.DateFormat;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
    private int ID;
    private Date date;


    public Scorecard () {
        mScores = new HashMap<Player, ArrayList<Integer>>();
        mPutts = new HashMap<Player, ArrayList<Integer>>();
        mCourse = new Course();
        mCurrentOrder = new ArrayList<ArrayList<Player>>();
        ID = -1;
        date = new Date();
    }

    public Scorecard (Course course, int id, Date mDate) {
        mScores = new HashMap<Player, ArrayList<Integer>>();
        mPutts = new HashMap<Player, ArrayList<Integer>>();
        mCourse = course;
        mCurrentOrder = new ArrayList<ArrayList<Player>>();
        ID = id;
        date = mDate;
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

    public int getID() { return ID; }

    public Date getDate() { return date; }

    public void setDate(Date d) { date = d;}

    public void setDate(String d) {
        java.text.DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date s = format.parse(d);
            date = s;
        }
        catch(Exception e){
            return;
        }
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

    public void setID(int sid){
        ID = sid;
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
