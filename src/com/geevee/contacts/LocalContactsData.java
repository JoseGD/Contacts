package com.geevee.contacts;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.provider.ContactsContract.Contacts;

public class LocalContactsData extends SQLiteOpenHelper {

	private static final String BD_NAME = "localcontacts.db";
	private static final int BD_VERSION = 1;
	private static final String TABLE_CREATION =
							"CREATE TABLE contacts (_id INTEGER PRIMARY KEY, " + Contacts.LOOKUP_KEY + " TEXT, " +
															Contacts.DISPLAY_NAME_PRIMARY + " TEXT, " + Contacts.PHOTO_THUMBNAIL_URI + " TEXT)";
	private static final String INSERT_REPLACE_CONTACT =
							"INSERT OR REPLACE INTO contacts VALUES (?, ?, ?, ?)";
	
	private static final String LOCAL_CONTACTS_QUERY =	"SELECT * FROM contacts " +
																		"ORDER BY " + Contacts.DISPLAY_NAME_PRIMARY;
	
	private static LocalContactsData mInstance = null;
	private SQLiteDatabase mDatabase;
	
	public static LocalContactsData getInstance(Context ctx) {
		if (mInstance == null)
			mInstance = new LocalContactsData(ctx.getApplicationContext());
		return mInstance;
	}
	
	public CursorLoader localContactsCursorLoader(Context context) {
		return new SQLiteCursorLoader(context, mDatabase);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public boolean openDB() {
		try {
			mDatabase = getReadableDatabase();
			return true;
		} catch (SQLiteException E) {
			return false;
		}
	}
	
	public int saveContactData(long contactId, String name, String lookup, String photo) {
		try {
			//mDatabase.execSQL("delete from contacts");  // Handy for testing
			mDatabase.execSQL(INSERT_REPLACE_CONTACT, new String[] {contactId + "", lookup, name, photo});
			return 0;
		} catch (SQLiteException E) {
			return 1;
		}
	}

	private LocalContactsData(Context context) {
		super(context, BD_NAME, null, BD_VERSION);
	}

	static class SQLiteCursorLoader extends CursorLoader {

		final SQLiteDatabase db;
		final ForceLoadContentObserver mObserver;
		CancellationSignal mCancellationSignal;
		
		public SQLiteCursorLoader(Context context, SQLiteDatabase db) {
			super(context);
			this.db = db;
			mObserver = new ForceLoadContentObserver();
		}
		
		@Override
		public Cursor loadInBackground() {
			synchronized (this) {
				if (isLoadInBackgroundCanceled()) {
					throw new OperationCanceledException();
            }
            mCancellationSignal = new CancellationSignal();
			}
			try {
            Cursor cursor = db.rawQuery(LOCAL_CONTACTS_QUERY, null, mCancellationSignal);
            if (cursor != null) {
            	try {
            		cursor.getCount();
                  cursor.registerContentObserver(mObserver);
               } catch (RuntimeException ex) {
               	cursor.close();
                  throw ex;
               }
            }
            return cursor;
        } finally {
            synchronized (this) {
            	mCancellationSignal = null;
            }
        }
		}
		
	}
	
}
