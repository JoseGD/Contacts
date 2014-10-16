package com.josegd.contacts;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ContactDetailsListAdapter extends CursorAdapter {

	private static final String NO_MIME_TYPE = "-";
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	private class ViewHolder {
      TextView contactData;
   }
	
	public ContactDetailsListAdapter(Context context) {
		super(context, null, 0);
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	public Cursor addSeparatorRowsToCursor(Cursor c) {
		MatrixCursor mc = new MatrixCursor(ContactsData.DETAILS_PROJECTION);
		boolean phonesOk = false, emailsOk = false;
		c.moveToFirst();
		while (!c.isAfterLast()) {
			if (c.getString(3).equals(Phone.CONTENT_ITEM_TYPE) && !phonesOk) {
				mc.addRow(new Object[] {9900, 0, "", NO_MIME_TYPE, mContext.getString(R.string.phones)});
				phonesOk = true;
			} else
				if (c.getString(3).equals(Email.CONTENT_ITEM_TYPE) && !emailsOk) {
					mc.addRow(new Object[] {9901, 0, "", NO_MIME_TYPE, mContext.getString(R.string.emails)});
					emailsOk = true;
				}
			mc.addRow(new Object[] {c.getLong(0), c.getString(1),
											c.getString(2), c.getString(3), c.getString(4)});
			c.moveToNext();
		}
		return mc;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		Cursor c = getCursor();
		c.moveToPosition(position);
		return c.getString(3).equals(NO_MIME_TYPE) ? 0 : 1;
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View itemView;
		int pos = cursor.getPosition();
		if (getItemViewType(pos) == 1) {
			itemView = mInflater.inflate(R.layout.detail_item, parent, false);
		} else {
			itemView = mInflater.inflate(R.layout.detail_item_separator, parent, false);
		}	
		final ViewHolder holder = new ViewHolder();
		holder.contactData = (TextView) itemView.findViewById(R.id.contact_data);
		itemView.setTag(holder);
		return itemView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
      final ViewHolder holder = (ViewHolder) view.getTag();
      holder.contactData.setText(cursor.getString(4));
	}
	
}
