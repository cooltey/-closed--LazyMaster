package com.cooltey.lazymaster.lib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

	public class DatabaseHelper extends SQLiteOpenHelper 
	{
		private static final String DATABASE = "database.db";
		
		private static final int DATABASEVERSION = 1;
		
		private SQLiteDatabase db;
		
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE, null, DATABASEVERSION);
			db = this.getWritableDatabase();
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			String DATABASE_CREATE_TABLE = "CREATE TABLE lazy_master (_ID INTEGER PRIMARY KEY, " +
					" title TEXT, " +
					" swticher TEXT, " +
					" start_time TEXT, " +
					" end_time TEXT, " +
					" start_date TEXT, " +
					" end_date TEXT, " +
					" brightness TEXT, " +
					" sounds TEXT, " +
					" repeat_day TEXT, " +
					" week_sun TEXT," +
					" week_mon TEXT," +
					" week_tue TEXT, " +
					" week_wed TEXT," +
					" week_thu TEXT," +
					" week_fri TEXT," +
					" week_sat TEXT);";
			
			db.execSQL(DATABASE_CREATE_TABLE);		
			
			DATABASE_CREATE_TABLE = "CREATE TABLE lazy_master_default (_ID INTEGER PRIMARY KEY, " +
					" brightness TEXT, " +
					" sounds TEXT)";
			
			db.execSQL(DATABASE_CREATE_TABLE);	
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS lazy_master");	
			onCreate(db);
		}
		
		public Cursor getAll(String tableName, String whereStr) 
		{
			Log.d("DB Query", "SELECT * FROM " + tableName + whereStr);
		    return db.rawQuery("SELECT * FROM " + tableName + whereStr, null);
		}
		
		public long insert(String tableName, String[] column, String[] value) 
		{
			ContentValues args = new ContentValues();
			for(int i = 0; i < column.length; i++)
			{
				args.put(column[i], value[i]);	
			}
	 
			return db.insert(tableName, null, args);
	    }
		
		public int delete(String tableName, long rowId) 
		{
			return db.delete(tableName,		
			"_ID = " + rowId,				
			null							
			);
		}
		
		public int update(String tableName, long rowId, String[] column, String[] value) 
		{
			ContentValues args = new ContentValues();
			for(int i = 0; i < column.length; i++)
			{
				args.put(column[i], value[i]);	
			}
			return db.update(tableName,	
			args,						
			"_ID=" + rowId,				
			null						
			);
		}
	}
