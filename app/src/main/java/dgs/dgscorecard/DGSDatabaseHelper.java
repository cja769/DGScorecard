package dgs.dgscorecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String COLUMN_SCORE = "player_score";
    private static final String COLUMN_PUTTS = "player_putts";
    private static final String COLUMN_TOTALSCORE = "player_totalscore";
    private static final String COLUMN_UNDEROVER = "player_underover";

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
                + COLUMN_SDATE + " TEXT)");

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
        db.close();
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
        Course item = null;
        if (cursor != null) {
            // add items to the list
            cursor.moveToFirst();
            if(cursor.isAfterLast() == false) {


                String parstring = cursor.getString(1);

                String[] pars = parstring.split(",");
                ArrayList<Integer> pList = new ArrayList<>();
                for (String par : pars)
                    pList.add(Integer.parseInt(par));

                item = new Course(cursor.getInt(0), pList,
                        cursor.getString(2), cursor.getInt(4));
            }


            // close the cursor
            cursor.close();

            // close the database connection
            db.close();

            return item;
        }
        else {
            db.close();
            return null;
        }


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
        else {
            db.close();
            return null;
        }

    }

    public boolean deleteCourse(String course){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(COURSE_ITEMS, new String[] {COLUMN_ID, COLUMN_CNAME}, COLUMN_CNAME + "=?",
                                    new String[] {""+course}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            Cursor cursor2 = db.query(SCORECARD_ITEMS, new String[] {COLUMN_ID}, COLUMN_SCOURSEID + "=?",
                                    new String[] {""+cursor.getInt(0)}, null, null, null);
            if(cursor2 != null) {
                cursor2.moveToFirst();
                for(; !cursor2.isAfterLast(); cursor2.moveToNext()){
                    deleteScorecard(cursor2.getInt(0));
                }
            }
        }
        int rows = db.delete(COURSE_ITEMS, COLUMN_CNAME + " = '" + course + "'", null);
        if(rows == 0 )
            return false;
        else
            return true;
    }

    public boolean deleteScorecard(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PLAYER_SCORES, COLUMN_SID + " = " + id, null);
        int rows = db.delete(SCORECARD_ITEMS, COLUMN_ID + " = " + id, null);
        if(rows == 0)
            return false;
        else
            return true;
    }


    public Player getPlayerByName(String name){

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        // send query
        Cursor cursor = db.query(PLAYER_ITEMS, new String[]{
                        COLUMN_ID,
                        COLUMN_NAME},
                COLUMN_NAME + "=?",
                new String[]{""+name}, null, null, null, null
        ); // get all rows

        if (cursor != null) {
            // add items to the list
            cursor.moveToFirst();

            Player p = new Player(cursor.getString(1),cursor.getInt(0));


            // close the cursor
            cursor.close();

            // close the database connection
            db.close();

            return p;
        }
        else {
            db.close();
            return null;
        }

    }

    public boolean deletePlayer(String name){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(PLAYER_ITEMS,new String[]{COLUMN_ID},COLUMN_NAME + " = '" + name + "'", null, null, null, null);
        if(cursor == null)
            return false;
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        int rows = db.delete(PLAYER_ITEMS, COLUMN_ID + " = " + id, null);
        if(rows > 0){
            db.delete(PLAYER_SCORES, COLUMN_SPLAYERID + " = " + id, null);
            return true;
        }
        else
            return false;
    }

    public boolean deletePlayer(Player p){
        return deletePlayer(p.getName());
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

        cursor.moveToFirst();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{

            if (cursor != null) {
                // add items to the list
                for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {

                    Date date = formatter.parse(cursor.getString(2));
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

    public ArrayList<Scorecard> getAllFullScorecards() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(SCORECARD_ITEMS, new String[] {COLUMN_ID}, null, null, null, null, null);
        ArrayList<Scorecard> all = new ArrayList<>();
        for(cursor.moveToFirst(); cursor != null && !cursor.isAfterLast(); cursor.moveToNext()){
            all.add(getFullScorecard(cursor.getInt(0), null));
        }
        return all;
    }

/*
Once a scorecard is selected, its players and scores will be gotten from the database
 */
    public Scorecard getFullScorecard(int id, Context context) {

        if(id == -1)
            return null;
        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();
        Scorecard item = new Scorecard();
        String argument = String.valueOf(id);
        Map<Player, ArrayList<Integer>> mScores = new HashMap<Player, ArrayList<Integer>>();
        Map<Player, ArrayList<Integer>> mPutts= new HashMap<Player, ArrayList<Integer>>();
        Cursor scorecardCursor = db.query(SCORECARD_ITEMS, new String[] {
                                COLUMN_ID,
                                COLUMN_SCOURSEID,
                                COLUMN_SDATE},
                                COLUMN_ID + " = ?",
                                new String[] {argument}, null, null, null, null);
        if(scorecardCursor != null){
            scorecardCursor.moveToFirst();
            item.setCourse(getCourseByID(scorecardCursor.getInt(1)));
            item.setDate(scorecardCursor.getString(2));
            item.setID(id);
            scorecardCursor.close();
        }
        else {
            //db.close();
            return null;
        }

        // send query
        if(!db.isOpen())
            db = getReadableDatabase();
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


                //add player to arraylist that will be added to scorecard
                // send query
                Cursor cursor2 = db.query(PLAYER_ITEMS, new String[]{COLUMN_ID,
                                COLUMN_NAME},
                        "_id = ?",
                        new String[]{"" + cursor.getString(0)}, null, null, null, null
                ); // get row

                //add to arraylist
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    Player newPlayer = new Player(cursor2.getString(1), cursor2.getInt(0));
                    items.add(newPlayer);

                    // add scores to the scorecard
                    String str = cursor.getString(2);
                    mScores.put(newPlayer, stringToList(str));

                    //add putts to the scorecard
                    str = cursor.getString(1);
                    mPutts.put(newPlayer, stringToList(str));
                    // close the cursor
                    cursor2.close();

                }

            }

            item.setScores(mScores);
            item.setPutts(mPutts);
            item.setPlayers(items);



            // close the cursor
            cursor.close();
        }

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
     * Add a new item
     */
    private void addPlayerItem(SQLiteDatabase db, Player item) {
        // prepare values
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());

        // add the row
        db.insert(PLAYER_ITEMS, null, values);
        db.close();

    }


    /**
     * Add a new item
     */
    private void addCourseItem(SQLiteDatabase db, Course item) {

        ContentValues values = setCourseValues(item);
        // add the row
        db.insert(COURSE_ITEMS, null, values);
        db.close();

    }

    public boolean updateCourse(Course name){
        Log.v("In update Course", name.getName());
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = setCourseValues(name);
        int rows = db.update(COURSE_ITEMS, values, COLUMN_CNAME + " = '" + name.getName() + "'", null);
        if(rows == 0)
            return false;
        else
            return true;
    }

    private ContentValues setCourseValues(Course item) {
        // prepare values
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOLES, item.getNumHoles());
        ArrayList<Integer> pars = item.getPars();
        values.put(COLUMN_INDIVPARS, listToString(pars));
        values.put(COLUMN_CNAME, item.getName());
        values.put(COLUMN_PAR, item.getPar());
        return values;
    }

    public ArrayList<String> dumpCourses(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(COURSE_ITEMS, new String[] {COLUMN_ID, COLUMN_CNAME},null, null, null,null,null);
        cursor.moveToFirst();
        ArrayList<String> courses = new ArrayList<String>();
        while(!cursor.isAfterLast()){
            courses.add(cursor.getString(1) + ": " + cursor.getInt(0));
            cursor.moveToNext();
        }
        return courses;
    }

    public ArrayList<String> dumpScorecards(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(SCORECARD_ITEMS, new String[] {COLUMN_ID, COLUMN_SCOURSEID},null, null, null,null,null);
        cursor.moveToFirst();
        ArrayList<String> scoreCards = new ArrayList<String>();
        while(!cursor.isAfterLast()){
            scoreCards.add("Card id: " + cursor.getInt(0) + ": " + "Course id: " + cursor.getInt(1));
            cursor.moveToNext();
        }
        return scoreCards;
    }

    public void addScorecardItem(Scorecard item) {
        SQLiteDatabase db = getWritableDatabase();
        if(item.getID() == -1)
            addScorecardItem(db, item);
        else
            updateScorecardItem(db, item);
        db.close();
    }

    private void addScorecardItem(SQLiteDatabase db, Scorecard item) {
        // prepare values
        ContentValues values = new ContentValues();

        //get course ID from course
        int CID = item.getCourse().getID();

        values.put(COLUMN_SCOURSEID, CID);

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(item.getDate());
        Log.v("add: Date",s);

        values.put(COLUMN_SDATE, s);

        db.insert(SCORECARD_ITEMS, null, values);

        //get scorecard's database ID
        Cursor cursor = db.query(SCORECARD_ITEMS, new String[]{COLUMN_ID}, COLUMN_SDATE + "= ?",
                new String[] {s}, null, null, null, null
        ); // get all rows
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
            item.setID(id);
        }

        List<Player> playerArray = new ArrayList<Player>();
        playerArray = item.getPlayers();

        for (Player each: playerArray){
            values.clear();

            values.put(COLUMN_SPLAYERID, each.getPID());
            values.put(COLUMN_SID, item.getID());
            values.put(COLUMN_SCSCORES, listToString(item.getScores().get(each)));
            values.put(COLUMN_SCPUTTS, listToString(item.getPutts().get(each)));
            db.insert(PLAYER_SCORES, null, values);

        }


    }


    private void updateScorecardItem(SQLiteDatabase db, Scorecard item) {
        // prepare values
        ContentValues values = new ContentValues();

        List<Player> playerArray = new ArrayList<Player>();
        playerArray = item.getPlayers();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(item.getDate());
        Log.v("update: Date", s);

        for (Player each: playerArray){
            values.clear();

            values.put(COLUMN_SPLAYERID, each.getPID());
            values.put(COLUMN_SID, item.getID());
            values.put(COLUMN_SCSCORES, listToString(item.getScores().get(each)));
            values.put(COLUMN_SCPUTTS, listToString(item.getPutts().get(each)));

            db.update(PLAYER_SCORES, values,
                    COLUMN_SID + "=? AND "
                    + COLUMN_SPLAYERID + "=?",
                    new String[]{""+item.getID(),""+each.getPID()});

        }


    }

    private String listToString(ArrayList<Integer> list){
        StringBuilder string = new StringBuilder();
        for(Integer i: list){
            string.append(i+",");
        }
        return string.substring(0,string.lastIndexOf(","));
    }

    private ArrayList<Integer> stringToList(String str){
        ArrayList<Integer> l = new ArrayList<>();
        String[] s = str.split(",");
        for (String st : s) {
            if (!st.equals("")) {
                l.add(Integer.parseInt(st));
            }
        }
        return l;
    }

}
