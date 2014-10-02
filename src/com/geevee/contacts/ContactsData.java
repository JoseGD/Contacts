package com.geevee.contacts;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;

public class ContactsData {
		
	private static final String[] CONTACTS_PROJECTION =
					{Contacts._ID, Contacts.LOOKUP_KEY,
		  			 Contacts.DISPLAY_NAME_PRIMARY, Contacts.PHOTO_THUMBNAIL_URI};
	
	private static final String CONTACTS_SELECTION = Contacts.IN_VISIBLE_GROUP + " = 1";
	private final static String CONTACTS_ORDER = "display_name";

	public static final String[] DETAILS_PROJECTION =
					{Data._ID, Data.CONTACT_ID, Data.DISPLAY_NAME_PRIMARY, Data.MIMETYPE, Data.DATA1};
	
	private static final String DETAILS_SELECTION =
		   		Data.CONTACT_ID + " = ? AND (" + Data.MIMETYPE + " = ? OR " + Data.MIMETYPE + " = ?)";
	private static final String DETAILS_ORDER = "Data.MIMETYPE desc";
	
	public static CursorLoader contactsCursorLoader(Context context) {
		return new CursorLoader(context, Contacts.CONTENT_URI, CONTACTS_PROJECTION,
								      CONTACTS_SELECTION, null, CONTACTS_ORDER);
	}

	public static CursorLoader detailsCursorLoader(Context context, long contactId) {
		String[] args = new String[] {"0", Email.CONTENT_ITEM_TYPE, Phone.CONTENT_ITEM_TYPE};
		args[0] = contactId + "";
		return new CursorLoader(context, Data.CONTENT_URI,
										DETAILS_PROJECTION, DETAILS_SELECTION, args, DETAILS_ORDER);
	}
	
	public static int telNumbersCount(Context context, long contactId) {
		Cursor c = context.getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI, null,
				  													 CommonDataKinds.Phone.CONTACT_ID + " = ?",
				  													 new String[] {String.valueOf(contactId)}, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	public static int emailsCount(Context context, long contactId) {
		Cursor c = context.getContentResolver().query(CommonDataKinds.Email.CONTENT_URI, null,
																	 CommonDataKinds.Email.CONTACT_ID + " = ?",
																	 new String[] {String.valueOf(contactId)}, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
}
