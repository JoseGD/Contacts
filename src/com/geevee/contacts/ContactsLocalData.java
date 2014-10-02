package com.geevee.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract.Contacts;

public class ContactsLocalData extends SQLiteOpenHelper {

	private static final String BD_NAME = "localcontacts.db";
	private static final int BD_VERSION = 1;
	private static final String TABLE_CREATION =
							"CREATE TABLE contacts (_id INTEGER PRIMARY KEY, " + Contacts.LOOKUP_KEY + " TEXT, " +
															Contacts.DISPLAY_NAME_PRIMARY + " TEXT, " + Contacts.PHOTO_THUMBNAIL_URI + " TEXT)";
	private static final String INSERT_REPLACE_CONTACT =
							"INSERT OR REPLACE INTO contacts VALUES (?, ?, ?, ?)";
	
	private static final String[] LOCAL_CONTACTS_PROJECTION =
							{Contacts._ID, Contacts.LOOKUP_KEY,
			 				 Contacts.DISPLAY_NAME_PRIMARY, Contacts.PHOTO_THUMBNAIL_URI};
	
	private static ContactsLocalData mInstance = null;
	private SQLiteDatabase mDatabase;
	
	public static ContactsLocalData getInstance(Context ctx) {
		if (mInstance == null)
			mInstance = new ContactsLocalData(ctx.getApplicationContext());
		return mInstance;
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
	
	public int saveContactData(long contactId, String lookup, String name, String photoUri) {
		try {
			mDatabase.execSQL(INSERT_REPLACE_CONTACT, new String[] {contactId + "", lookup, name, photoUri});
			return 0;
		} catch (SQLiteException E) {
			return 1;
		}
	}

	private ContactsLocalData(Context context) {
		super(context, BD_NAME, null, BD_VERSION);
	}

}
