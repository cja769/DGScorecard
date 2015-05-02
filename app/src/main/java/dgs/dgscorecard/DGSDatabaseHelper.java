package dgs.dgscorecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danny on 4/3/2015.
 */
public class DGSDatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "database";

    // Table names
    private static final String PLAYER_ITEMS = "players";
    private static final String COURSE_ITEMS = "courses";
    private static final String SCORECARD_ITEMS = "scorecards";
    private static final String PLAYER_SCORES = "player_score";

    // Common Column names (players)
    private static final String COLUMN_ID = "_id";

    //Column names (players)
    private static final String COLUMN_NAME = "player_name";
    private static final String COLUMN_NAMEVIEW = "player_nameview";
    private static final String COLUMN_SCORE = "player_score";
    private static final String COLUMN_PUTTS = "player_putts";
    private static final String COLUMN_TOTALSCORE = "player_totalscore";
    private static final String COLUMN_UNDEROVER = "player_underover";
    private static final String COLUMN_PID = "player_pid";

    // Column names (courses)
    private static final String COLUMN_CNAME = "course_name";
    private static final String COLUMN_PAR = "course_par";
    private static final String COLUMN_HOLES = "course_holes";
    private static final String COLUMN_INDIVPARS = "course_indivpars";
    private static final String COLUMN_CID = "course_cid";

    // Column names (scorecards)
    private static final String COLUMN_SPLAYERID = "scorecard_playerid";
    private static final String COLUMN_SCOURSEID = "scorecard_courseid";
    private static final String COLUMN_SDATE = "scorecard_date";

    //Column names (player scores)
    private static final String COLUMN_SID = "scorecard_id";
    private static final String COLUMN_SPID = "scorecardplayer_id";
    private static final String COLUMN_SCPUTTS = "scorecard_putts";
    private static final String COLUMN_SCSCORES = "scorecard_scores";

    public DGSDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PLAYER_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME + " TEXT UNIQUE)");

        db.execSQL("CREATE TABLE " + COURSE_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_HOLES + " LONG, "
                + COLUMN_INDIVPARS + " TEXT, "
                + COLUMN_CNAME + " TEXT UNIQUE, "
                + COLUMN_PAR + " LONG)");

        db.execSQL("CREATE TABLE " + SCORECARD_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_SCOURSEID + " LONG, "
                + COLUMN_SDATE + " LONG)");

        db.execSQL("CREATE TABLE " + PLAYER_SCORES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_SCPUTTS + " CLOB, "
                + COLUMN_SCSCORES + " CLOB, "
                + COLUMN_SPLAYERID + " LONG, "
                + COLUMN_SID + " LONG)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simple database upgrade operation:
        // 1) drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + SCORECARD_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_SCORES);

        // 2) create a new database
        onCreate(db);
    }

    public void resetDb() {
        SQLiteDatabase db = getReadableDatabase();
        onUpgrade(db,0,0);
    }

    /**
     * retrieve all items from the player database
     */
    public List<Player> getAllPlayerItems(Context context) {
        // initialize the list
        List<Player> items = new ArrayList<Player>();

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        // send query
        Cursor cursor = db.query(PLAYER_ITEMS, new String[]{COLUMN_ID,
                        COLUMN_NAME},
                null, null, null, null, null, null
        ); // get all rows

        if (cursor != null) {
            // add items to the list
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
                items.add(new Player(cursor.getString(1), cursor.getInt(0)));
            }

            // close the cursor
            cursor.close();
        }

        // close the database connection
        db.close();

        // return the list
        return items;
    }

    /**
     * retrieve all items from the course database
     */
    public List<Course> getAllCourseItems(Context context) {
        // initialize the list
        List<Course> items = new ArrayList<Course>();

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        // send query
        Cursor cursor = db.query(COURSE_ITEMS, new String[]{
                        COLUMN_HOLES,
                        COLUMN_INDIVPARS,
                        COLUMN_CNAME,
                        COLUMN_PAR,
                        COLUMN_ID},
                null, null, null, null, null, null
        ); // get all rows

        if (cursor != null) {
            // add items to the list
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {

                String parstring = cursor.getString(1);

                int mNumHoles = Integer.parseInt(cursor.getString(0));
                ArrayList<Integer> mPars = new ArrayList<Integer>(mNumHoles);
                for(int i = 0; i < mNumHoles; i++)
                    mPars.add((int)parstring.charAt(i));

                items.add(new Course(mNumHoles, mPars,
                        cursor.getString(2),
                        context, cursor.getInt(4)));
            }

            // close the cursor
            cursor.close();
        }

        // close the database connection
        db.close();

        // return the list
        return items;
    }

    public Course getCourseByID(int ID){

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        // send query
        Cursor cursor = db.query(COURSE_ITEMS, new String[]{
                        COLUMN_HOLES,
                        COLUMN_INDIVPARS,
                        COLUMN_CNAME,
                        COLUMN_PAR,
                        COLUMN_ID},
                "_id=?",
                new String[]{""+ID}, null, null, null, null
        ); // get all rows

        if (cursor != null) {
            // add items to the list
            cursor.moveToFirst();

            String parstring = cursor.getString(1);

            String[] pars = parstring.split(",");
            ArrayList<Integer> pList = new ArrayList<>();
            for(String par: pars)
                pList.add(Integer.parseInt(par));

            Course item = new Course(cursor.getInt(0), pList,
                    cursor.getString(2), cursor.getInt(4));


            // close the cursor
            cursor.close();

            // close the database connection
            db.close();

            return item;
        }
        else return null;


    }

    public Course getCourseByName(String name){

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        // send query
        Cursor cursor = db.query(COURSE_ITEMS, new String[]{
                        COLUMN_HOLES,
                        COLUMN_INDIVPARS,
                        COLUMN_CNAME,
                        COLUMN_PAR,
                        COLUMN_ID},
                COLUMN_CNAME + "=?",
                new String[]{""+name}, null, null, null, null
        ); // get all rows

        if (cursor != null) {
            // add items to the list
            cursor.moveToFirst();

            String parstring = cursor.getString(1);

            String[] pars = parstring.split(",");
            ArrayList<Integer> pList = new ArrayList<>();
            for(String par: pars)
                pList.add(Integer.parseInt(par));

            Course item = new Course(cursor.getInt(0), pList,
                    cursor.getString(2), cursor.getInt(4));


            // close the cursor
            cursor.close();

            // close the database connection
            db.close();

            return item;
        }
        else return null;

    }
    /**
     * retrieve all basic info for all scorecards: date and course
     */
    public List<Scorecard> getAllSCItems(Context context) {
        // initialize the list
        List<Scorecard> items = new ArrayList<Scorecard>();

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        // send query
        Cursor cursor = db.query(SCORECARD_ITEMS, new String[]{
                        COLUMN_ID,
                        COLUMN_SCOURSEID,
                        COLUMN_SDATE
                },
                null, null, null, null, null, null
        ); // get all rows

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{Date date = formatter.parse(cursor.getString(2));


        if (cursor != null) {
            // add items to the list
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
                items.add(new Scorecard(getCourseByID(Integer.parseInt(cursor.getString(1))),
                        Integer.parseInt(cursor.getString(0)),
                        date
                ));

            }

            // close the cursor
            cursor.close();
        }
        } catch (ParseException e) {
            //handle exception
        }

        // close the database connection
        db.close();

        // return the list
        return items;
    }

/*
Once a scorecard is selected, its players and scores will be gotten from the database
 */
    public Scorecard getFullScorecard(Scorecard item, Context context) {

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        String argument = String.valueOf(item.getID());

        // send query
        Cursor cursor = db.query(PLAYER_SCORES, new String[]{
                        COLUMN_SPLAYERID,
                        COLUMN_SCPUTTS,
                        COLUMN_SCSCORES,
                        COLUMN_SID},
                "scorecard_id = ?",
                new String[] {argument}, null, null, null, null
        ); // get all rows


        ArrayList<Player> items = new ArrayList<Player>();

        if (cursor != null) {
            //for every player on the scorecard...
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
                Map<Player, ArrayList<Integer>> mScores = new HashMap<Player, ArrayList<Integer>>();
                Map<Player, ArrayList<Integer>> mPutts= new HashMap<Player, ArrayList<Integer>>();

                //add player to arraylist that will be added to scorecard
                // send query
                Cursor cursor2 = db.query(PLAYER_ITEMS, new String[]{COLUMN_ID,
                                COLUMN_NAME},
                        "_id = ?",
                        new String[] {""+cursor.getString(0)}, null, null, null, null
                ); // get row

                //add to arraylist
                Player newPlayer = new Player(cursor2.getString(1), cursor2.getInt(0));
                items.add(newPlayer);

                // add scores to the scorecard
                String str = new String(cursor.getString(2));
                ArrayList aList= new ArrayList<Integer>();

                for (String splitter: str.split(",")){
                    aList.add(Integer.valueOf(splitter));
                }
                mScores.put(newPlayer, aList);
                item.setScores(mScores);

                //add putts to the scorecard
                str =  cursor.getString(1);
                ArrayList bList= new ArrayList<Integer>();

                mPutts.put(newPlayer, bList);
                item.setPutts(mPutts);

                // close the cursor
                cursor2.close();
            }



            item.setPlayers(items);


            // close the cursor
            cursor.close();
        }

        //find all players in scorecardplayer_items w/ scorecardid
        //make them and add them to scorecard w/ setPlayers(arraylist<players>)
        //loop
        //get scores from scorecardscores_items w/ player id ...
        // make map w/ player:scores and add them to scorecard w/ setScores(map)
        //same with putts





        // close the database connection
        db.close();

        // return the list
        return item;
    }

    /**
     * Add items to the list
     */
    public void addPlayerItems(List<Player> items) {
        if (items != null && items.size() > 0) {
            // obtain a readable database
            SQLiteDatabase db = getWritableDatabase();

            for (Player item : items) {
                addPlayerItem(db, item);
            }

            // close the database connection
            db.close();
        }
    }

    public void addCourseItems(List<Course> items) {
        if (items != null && items.size() > 0) {
            // obtain a readable database
            SQLiteDatabase db = getWritableDatabase();

            for (Course item : items) {
                addCourseItem(db, item);
            }

            // close the database connection
            db.close();
        }
    }

    /**
     * update an existing item
     */
    public void updateItem(Player item) {
        if (item != null) {
            // obtain a readable database
            SQLiteDatabase db = getWritableDatabase();

            // prepare values
            ContentValues values = new ContentValues();
            values.put(COLUMN_SCORE, item.getValScore());
           // values.put(COLUMN_PUTTS, item.getValPutts());
            values.put(COLUMN_TOTALSCORE, item.getTextTScore());
            values.put(COLUMN_UNDEROVER, item.getTextUO());

            // send query for the row id
            Cursor cursor = db.query(PLAYER_ITEMS,
                    new String[]{COLUMN_ID},
                    COLUMN_NAME + "=?",
                    new String[]{item.getName()},
                    null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    // update the row
                    db.update(PLAYER_ITEMS, values,
                            COLUMN_ID + "=?",
                            new String[]{cursor.getString(0)});
                }

                cursor.close();
            }

            // close the database connection
            db.close();
        }
    }

    /**
     * update an existing course item
     public void updateItem(Course item) {
     if (item != null) {
     // obtain a readable database
     SQLiteDatabase db = getWritableDatabase();

     // prepare values
     ContentValues values = new ContentValues();
     values.put(COLUMN_SCORE, item.getValScore());
     // values.put(COLUMN_PUTTS, item.getValPutts());
     values.put(COLUMN_TOTALSCORE, item.getTextTScore());
     values.put(COLUMN_UNDEROVER, item.getTextUO());

     // send query for the row id
     Cursor cursor = db.query(PLAYER_ITEMS,
     new String[]{COLUMN_ID},
     COLUMN_NAME + "=?",
     new String[]{item.getName()},
     null, null, null, null);

     if (cursor != null) {
     if (cursor.moveToFirst()) {
     // update the row
     db.update(PLAYER_ITEMS, values,
     COLUMN_ID + "=?",
     new String[]{cursor.getString(0)});
     }

     cursor.close();
     }

     // close the database connection
     db.close();
     }
     }

     */

    /**
     * Add a new item
     */
    private void addPlayerItem(SQLiteDatabase db, Player item) {
        // prepare values
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());

        // add the row
        db.insert(PLAYER_ITEMS, null, values);

    }


    /**
     * Add a new item
     */
    private void addCourseItem(SQLiteDatabase db, Course item) {
        // prepare values
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOLES, item.getNumHoles());
        ArrayList<Integer> pars = item.getPars();
        StringBuilder parstring = new StringBuilder();
        for(int i = 0; i < pars.size(); ++i){
            parstring.append(pars.get(i) + ",");
        }
        parstring.substring(0,parstring.length()-1);
        values.put(COLUMN_INDIVPARS, parstring.toString());
        values.put(COLUMN_CNAME, item.getName());
        values.put(COLUMN_PAR, item.getPar());

        // add the row
        db.insert(COURSE_ITEMS, null, values);

    }

    public void addScorecardItem(SQLiteDatabase db, Scorecard item) {
        // prepare values
        ContentValues values = new ContentValues();

        //get course ID from course
        int CID = item.getCourse().getID();

        values.put(COLUMN_SCOURSEID, CID);

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(item.getDate());

        values.put(COLUMN_SDATE, s);

        db.insert(SCORECARD_ITEMS, null, values);

        //get scorecard's database ID
        String query = "SELECT ROWID from MYTABLE order by ROWID DESC limit 1";
        Cursor c = db.rawQuery(query,null);
        if (c != null && c.moveToFirst()) {
            int id = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
            item.setID(id);
        }

        List<Player> playerArray = new ArrayList<Player>();
        playerArray = item.getPlayers();

        for (Player each: playerArray){
            values.clear();

            values.put(COLUMN_SPID, each.getPID());
            values.put(COLUMN_SID, item.getID());


            StringBuilder concatScores = new StringBuilder();
            for(Integer eachScore: item.getScores().get(each)) {
                //concatenate every score into a string, use "," between each score for
                //future delimiting
                concatScores.append(""+eachScore);
                concatScores.append(",");
            }
            values.put(COLUMN_SCSCORES, concatScores.toString());


            StringBuilder concatPutts = new StringBuilder();
            for(Integer eachPutt: item.getPutts().get(each)) {
                //concatenate every score into a string, use "," between each score for
                //future delimiting
                concatScores.append(""+eachPutt);
                concatScores.append(",");
            }
            values.put(COLUMN_SCPUTTS, concatPutts.toString());


            db.insert(PLAYER_SCORES, null, values);

        }


    }


    public void updateScorecardItem(SQLiteDatabase db, Scorecard item) {
        // prepare values
        ContentValues values = new ContentValues();

        List<Player> playerArray = new ArrayList<Player>();
        playerArray = item.getPlayers();

        for (Player each: playerArray){
            values.clear();

            values.put(COLUMN_SPID, each.getPID());
            values.put(COLUMN_SID, item.getID());


            StringBuilder concatScores = new StringBuilder();
            for(Integer eachScore: item.getScores().get(each)) {
                //concatenate every score into a string, use "," between each score for
                //future delimiting
                concatScores.append(""+eachScore);
                concatScores.append(",");
            }
            values.put(COLUMN_SCSCORES, concatScores.toString());


            StringBuilder concatPutts = new StringBuilder();
            for(Integer eachPutt: item.getPutts().get(each)) {
                //concatenate every score into a string, use "," between each score for
                //future delimiting
                concatScores.append(""+eachPutt);
                concatScores.append(",");
            }
            values.put(COLUMN_SCPUTTS, concatPutts.toString());


            db.insert(PLAYER_SCORES, null, values);

            db.update(PLAYER_SCORES, values,
                    COLUMN_ID + "=?",
                    new String[]{""+each.getPID()});

        }


    }

}
