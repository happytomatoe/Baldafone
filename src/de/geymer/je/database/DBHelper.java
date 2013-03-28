/**
 * 
 */
package de.geymer.je.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Columns;

/**
 * @author babkamen
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	public static final String COLUMN_WORD="word";
	public static final String TABLE_NAME = "dictionary";
	private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"  ("+COLUMN_WORD+"  varchar(20)   )";
	public DBHelper(Context context) {
		super(context, "mydb", null,1);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("DROP TABLE   IF EXISTS Mytable");
	db.execSQL(CREATE_TABLE);
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	public void refresh(){
		onCreate(getWritableDatabase());	
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
