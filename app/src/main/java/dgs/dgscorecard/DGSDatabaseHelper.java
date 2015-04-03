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

    // Table name
    private static final String PLAYER_ITEMS = "players";

    // Column names (players)
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "player_name";
    private static final String COLUMN_NAMEVIEW = "player_nameview";
    private static final String COLUMN_SCORE= "player_score";
    private static final String COLUMN_PUTTS = "player_putts";
    private static final String COLUMN_TOTALSCORE= "player_totalscore";
    private static final String COLUMN_UNDEROVER = "player_underover";
    private static final String COLUMN_PID = "player_pid";


    public DGSDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PLAYER_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT UNIQUE NOT NULL,"
                + COLUMN_NAMEVIEW + " TEXT UNIQUE NOT NULL,"
                + COLUMN_SCORE + " LONG NOT NULL"
                + COLUMN_PUTTS + " LONG NOT NULL"
                + COLUMN_TOTALSCORE + " LONG NOT NULL"
                + COLUMN_UNDEROVER + " LONG NOT NULL"
                + COLUMN_PID + " LONG NOT NULL" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simple database upgrade operation:
        // 1) drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_ITEMS);

        // 2) create a new database
        onCreate(db);
    }

    /**
     * retrieve all items from the database
     */
    public List<Player> getAllPlayerItems() {
        // initialize the list
        List<Player> items = new ArrayList<Player>();

        // obtain a readable database
        SQLiteDatabase db = getReadableDatabase();

        // send query
        Cursor cursor = db.query(PLAYER_ITEMS, new String[]{
                        COLUMN_NAME,
                        COLUMN_NAMEVIEW,
                        COLUMN_SCORE,
                        COLUMN_PUTTS,
                        COLUMN_TOTALSCORE,
                        COLUMN_UNDEROVER,
                        COLUMN_PID},
                null, null, null, null, null, null); // get all rows

        if (cursor != null) {
            // add items to the list
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
                items.add(new Player(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),
                        Integer.parseInt(cursor.getString(6))));
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
    public void addItems(List<Player> items) {
        if (items != null && items.size() > 0) {
            // obtain a readable database
            SQLiteDatabase db = getWritableDatabase();

            for (Player item : items) {
                addItem(db, item);
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
            values.put(COLUMN_PUTTS, item.getValPutts());
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
    private void addItem(SQLiteDatabase db, Player item) {
        // prepare values
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_NAMEVIEW, item.getTextNameF());
        values.put(COLUMN_SCORE, item.getValScore());
        values.put(COLUMN_PUTTS, item.getValPutts());
        values.put(COLUMN_TOTALSCORE, item.getTextTScore());
        values.put(COLUMN_UNDEROVER, item.getTextUO());
        values.put(COLUMN_PID, item.getPID());

        // add the row
        db.insert(PLAYER_ITEMS, null, values);

    }
}
