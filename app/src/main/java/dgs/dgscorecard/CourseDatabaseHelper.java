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
public class CourseDatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "database";

    // Table name

    private static final String COURSE_ITEMS = "courses";

    // Column names (players)
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "course_name";
    private static final String COLUMN_PAR = "course_par";
    private static final String COLUMN_HOLES = "course_holes";
    private static final String COLUMN_INDIVPARS = "course_indivpars";
    private static final String COLUMN_CID = "course_cid";


    public CourseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + COURSE_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_HOLES + " LONG, "
                + COLUMN_INDIVPARS + " TEXT UNIQUE, "
                + COLUMN_NAME + " TEXT UNIQUE, "
                + COLUMN_PAR + " LONG, "
                + COLUMN_CID + " LONG" +")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simple database upgrade operation:
        // 1) drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_ITEMS);

        // 2) create a new database
        onCreate(db);
    }

    /**
     * retrieve all items from the database
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
                        COLUMN_NAME,
                        COLUMN_PAR,
                        COLUMN_CID},
                null, null, null, null, null, null
        ); // get all rows

        if (cursor != null) {
            // add items to the list
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {

                String parstring = cursor.getString(1);

                int mNumHoles = Integer.parseInt(cursor.getString(0));
                ArrayList<Integer> mPars = new ArrayList<Integer>(mNumHoles);
                for(int i = 0; i < mNumHoles; i++)
                    mPars.add((Integer)(int)parstring.charAt(i));

                items.add(new Course(mNumHoles, mPars,
                        cursor.getString(2),
                        context));
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
     * Add items to the list
     */
    public void addItems(List<Course> items) {
        if (items != null && items.size() > 0) {
            // obtain a readable database
            SQLiteDatabase db = getWritableDatabase();

            for (Course item : items) {
                addItem(db, item);
            }

            // close the database connection
            db.close();
        }
    }

    /**
     * update an existing item
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
    private void addItem(SQLiteDatabase db, Course item) {
        // prepare values
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOLES, item.getNumHoles());
        ArrayList<Integer> pars = item.getPars();
        StringBuilder parstring = new StringBuilder();
        for(int i = 0; i < pars.size(); ++i){
            parstring.append(pars.get(i));
        }
        values.put(COLUMN_INDIVPARS, parstring.toString());
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_PAR, item.getPar());
        //values.put(COLUMN_CID, item.getCID());

        // add the row
        db.insert(COURSE_ITEMS, null, values);

    }
}
