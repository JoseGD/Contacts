package com.geevee.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ContactListAdapter extends SimpleCursorAdapter {

	Context mContext;
	
	public ContactListAdapter(Context context, int layout, Cursor c,
									  String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Cursor contacts = getCursor();
		contacts.moveToPosition(position);
		long contactId = contacts.getLong(0);
		View row = super.getView(position, convertView, parent);
		TextView tv = (TextView) row.findViewById(android.R.id.text2);
		if (tv != null) {
			tv.setText(String.format(mContext.getString(R.string.emails_telnos),
											 numberOfEmails(contactId), numberOfTelephoneNo(contactId)));
		}
		return row;
	}
	
	private int numberOfEmails(long id) {
		Cursor c = mContext.getContentResolver().query(CommonDataKinds.Email.CONTENT_URI, null,
																	  CommonDataKinds.Email.CONTACT_ID + " = ?",
																	  new String[] {String.valueOf(id)}, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	private int numberOfTelephoneNo(long id) {
		Cursor c = mContext.getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI, null,
				  													  CommonDataKinds.Phone.CONTACT_ID + " = ?",
				  													  new String[] {String.valueOf(id)}, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
}
