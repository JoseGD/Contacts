package com.geevee.contacts;

import android.content.Context;
import android.content.CursorLoader;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

public class ContactsData {
		
	public static final String[] DETAILS_PROJECTION =
					{Data._ID, Data.CONTACT_ID, Data.DISPLAY_NAME_PRIMARY, Data.MIMETYPE, Data.DATA1};
	
	private static final String DETAILS_SELECTION =
		   		Data.CONTACT_ID + " = ? AND (" + Data.MIMETYPE + " = ? OR " + Data.MIMETYPE + " = ?)";
	private static final String DETAILS_ORDER = "Data.MIMETYPE desc";
	
	public static CursorLoader detailsCursorLoader(Context context, long contactId) {
		String[] args = new String[] {"0", Email.CONTENT_ITEM_TYPE, Phone.CONTENT_ITEM_TYPE};
		args[0] = contactId + "";
		return new CursorLoader(context, Data.CONTENT_URI,
										DETAILS_PROJECTION, DETAILS_SELECTION, args, DETAILS_ORDER);
	}

}
