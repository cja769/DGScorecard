package dgs.dgscorecard;

/**
 * Created by Jay on 3/23/15.
 */
public class Course {

    private int mNumHoles;
    private int[] mPars;
    private String mName;
    private int par;

    public Course(){
        mNumHoles = 18;
        mPars = new int[mNumHoles];
        for(int i = 0; i < 18; i++)
            mPars[i] = 3;
        mName = "";
        calculatePar();
    }

    public Course(int numHoles, int[] pars, String name){
        mNumHoles = numHoles;
        mPars = pars;
        mName = name;
        calculatePar();
    }

    public void setNumHoles(int numHoles){
        mNumHoles = numHoles;
    }

    public void setPars(int[] pars){
        mPars = pars;
        calculatePar();
    }

    public void setName(String name){
        mName = name;
    }

    public int getNumHoles(){
        return mNumHoles;
    }

    public int[] getPars(){
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
