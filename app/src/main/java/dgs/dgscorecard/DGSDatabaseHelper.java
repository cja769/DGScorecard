package dgs.dgscorecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
    private static final String COLUMN_SID = "scorecard_id";
    private static final String COLUMN_SPLAYERID = "scorecard_playerid";
    private static final String COLUMN_SCOURSEID = "scorecard_courseid";
    private static final String COLUMN_SDATE = "scorecard_date";

    //Column names (scorecardplayers)
    private static final String COLUMN_SPID = "scorecardplayer_id";

    //Column names (scorecardputts)
    private static final String COLUMN_SCPUTTS = "scorecard_putts";

    //Column names (scorecardputts)
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
                        COLUMN_SID
                        //time?
                },
                null, null, null, null, null, null
        ); // get all rows

        if (cursor != null) {
            // add items to the list
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
        //        items.add(new Scorecard(cursor.getString(0), cursor.getString(1),
           //             cursor.getString(2), context));

            }

            // close the cursor
            cursor.close();
        }

        // close the database connection
        db.close();

        // return the list
        return items;
    }


    public Scorecard getFullScorecard(int SID, Context context) {
        // initialize the list
        Scorecard item = new Scorecard();

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        // send query
        Cursor cursor = db.query(SCORECARD_ITEMS, new String[]{
                        COLUMN_ID,
                        COLUMN_SCOURSEID,
                        COLUMN_SID},
                null, null, null, null, null, null
        ); // get all rows

        if (cursor != null) {
            // add items to the list
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {



            }

            // close the cursor
            cursor.close();
        }

        //get course id from scorecard
        //make scorecard item ; get course w/ ID and make it and add it to scorecard
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
            parstring.append(pars.get(i));
        }
        values.put(COLUMN_INDIVPARS, parstring.toString());
        values.put(COLUMN_CNAME, item.getName());
        values.put(COLUMN_PAR, item.getPar());

        // add the row
        db.insert(COURSE_ITEMS, null, values);

    }

//    private void addScorecardItem(SQLiteDatabase db, Scorecard item) {
//        // prepare values
//        ContentValues values = new ContentValues();
//
//        int CID = item.getCourse().getID();
//
//        values.put(COLUMN_SCOURSEID, CID);
//        values.put(COLUMN_SID, item.getID());
//
//        db.insert(SCORECARD_ITEMS, null, values);
//
//
//        List<Player> playerArray = new ArrayList<Player>();
//        playerArray = item.getPlayers();
//
//        for (Player each: playerArray){
//            values.clear();
//
//            values.put(COLUMN_SPID, each.getPID());
//            values.put(COLUMN_SID, item.getID());
//
//            db.insert(SCORECARDPLAYER_ITEMS, null, values);
//
//
//            for(Integer eachScore: item.getScores().get(each)) {
//                values.clear();
//                values.put(COLUMN_SCSCORES, eachScore);
//                values.put(COLUMN_SPID, each.getPID());
//
//                db.insert(SCORECARDSCORE_ITEMS, null, values);
//            }
//
//            for(Integer eachPutt: item.getPutts().get(each)) {
//                values.clear();
//                values.put(COLUMN_SCPUTTS, eachPutt);
//                values.put(COLUMN_SPID, each.getPID());
//
//                db.insert(SCORECARDPUTT_ITEMS, null, values);
//
//            }
//
//        }
//
//
//    }
}
