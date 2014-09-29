package com.geevee.contacts;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ContactDetailsListAdapter extends CursorAdapter {

	//private Context mContext;
	private LayoutInflater mInflater;
	//private long mTappedPosition = -1;
	
	private class ViewHolder {
      TextView contactData;
   }
	
	public ContactDetailsListAdapter(Context context) {
		super(context, null, 0);
		//mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
//	@Override
//	public int getViewTypeCount() {
//		return 2;
//	}
//	
//	@Override
//	public int getItemViewType(int position) {
//		return (position != mTappedPosition) ? 0 : 1;
//	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View itemView;
//		int pos = cursor.getPosition();
		itemView =  mInflater.inflate(R.layout.detail_item, parent, false);
		final ViewHolder holder = new ViewHolder();
		holder.contactData = (TextView) itemView.findViewById(R.id.contact_data);
		itemView.setTag(holder);
		return itemView;
//		if (getItemViewType(pos) == 1) {
//			itemView = mInflater.inflate(R.layout.contact_item_options, parent, false);
//		} else {
//			itemView = mInflater.inflate(R.layout.contact_item, parent, false);
//			final ViewHolder holder = new ViewHolder();
//			holder.displayname = (TextView) itemView.findViewById(android.R.id.text1);
//			holder.emails_tels = (TextView) itemView.findViewById(android.R.id.text2);
//			holder.thumbnail = (ImageView) itemView.findViewById(android.R.id.icon);
//			itemView.setTag(holder);
//		}
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
      final ViewHolder holder = (ViewHolder) view.getTag();
      holder.contactData.setText(cursor.getString(4));
//      final long contactId = cursor.getLong(0);
	}
	
//	public void setTappedPosition(long pos) {
//		mTappedPosition = pos;
//	}

}
