package org.ligi.setec_astronomy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class IntentDatabaseSQLHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "packages_db.db";
	private static final int DB_VERSION = 1;
	
	public static final String TABLE="packages";
	
	// Columns
	public static final String LAST_SEEN = "time";
	public static final String PACKAGE = "package";
	public static final String INTENT2START = "i2s";
	public static final String LAST_CMD = "last_cmd";
	
	public IntentDatabaseSQLHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + "( " + BaseColumns._ID
				+ " integer primary key autoincrement, " + LAST_SEEN + " integer, "
				+ PACKAGE + " text not null UNIQUE ON CONFLICT REPLACE, "+ LAST_CMD + " text ," + INTENT2START +" " +
						"text );"; 
		
		db.execSQL(sql);
		
		
		String[] predef_kill_list={"com.android.settings","com.google.android.apps.plus","com.fsck.k9","com.rechild.advancedtaskkiller","com.android.vending" };
		for (String toKill:predef_kill_list) {
			sql="insert into " + TABLE + " ( " +  PACKAGE  + " , " + INTENT2START + " ) values ( '" + toKill +"' , 'KILL');";
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
