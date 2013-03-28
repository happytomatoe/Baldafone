/**
 * 
 */
package de.geymer.je.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author babkamen
 * 
 */
public class Database {
    /**
     * DBHelper- repsonsible for connection and maintaining database
     */
    private DBHelper dbhelper;

    /**
     * @return the dbhelper
     */
    public DBHelper getDbhelper() {
        return dbhelper;
    }

    /**
     * @param dbhelper
     *            the dbhelper to set
     */
    public void setDbhelper(DBHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    /**
     * main database connection
     */
    private SQLiteDatabase db;

    /**
     * Default constructor
     * 
     * @param context
     *            -context to set
     */
    public Database(Context context) {
        dbhelper = new DBHelper(context);
        db = dbhelper.getWritableDatabase();
    }

    /**
     * 
     * @return database
     */
    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * closes database connection
     */
    public void close() {
        dbhelper.close();
    }

    /**
     * 
     * @param db
     *            -database to set
     */
    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 
     * @return random row from table
     */
    public String getRandomWord() {
        if (db == null)
            db = dbhelper.getWritableDatabase();
        ;
        Cursor c = db.query(DBHelper.TABLE_NAME, null, null, null, null, null,
                "RANDOM()", "1");
        c.moveToFirst();
        if (c.getCount() == 0) {
            Log.d("Cursor", "Cursor is empty");
            return "режим";
        }
        return c.getString(c.getColumnIndex(DBHelper.COLUMN_WORD));
    }

}
