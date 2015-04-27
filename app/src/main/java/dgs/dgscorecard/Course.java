package dgs.dgscorecard;

import java.util.ArrayList;

/**
 * Created by Jay on 3/23/15.
 */
public class Course {

    private int mNumHoles;
    private ArrayList<Integer> mPars;
    private String mName;
    private int par;

    public Course(){
        mNumHoles = 18;
        mPars = new ArrayList<Integer>(18);
        for(int i = 0; i < 18; i++)
            mPars.add(3);
        mName = "";
        calculatePar();
    }

    public Course(int numHoles, ArrayList<Integer> pars, String name, Context context){
        mNumHoles = numHoles;
        mPars = pars;
        mName = name;
        calculatePar();
    }
    
    public void setNumHoles(int numHoles){
        mNumHoles = numHoles;
    }

    public void setPars(ArrayList<Integer> pars){
        mPars = pars;
        mNumHoles = pars.size();
        calculatePar();
    }

    public void setName(String name){
        mName = name;
    }

    public int getNumHoles(){
        return mNumHoles;
    }

    public ArrayList<Integer> getPars(){
        return mPars;
    }

    public String getName(){
        return mName;
    }

    public int getPar(){
        return par;
    }

    private int calculatePar(){
        int total = 0;
        for(int i: mPars)
            total += i;
        par = total;
        return total;
    }


}
