package com.josegd.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListAdapter extends CursorAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private long mTappedPosition = -1;
	
	private class ViewHolder {
      TextView displayname;
      TextView tels_emails;
      ImageView thumbnail;
   }

	public ContactListAdapter(Context context) {
		super(context, null, 0);
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return (position != mTappedPosition) ? 0 : 1;
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View itemView;
		int pos = cursor.getPosition();
		if (getItemViewType(pos) == 1) {
			itemView = mInflater.inflate(R.layout.contact_item_options, parent, false);
		} else {
			itemView = mInflater.inflate(R.layout.contact_item, parent, false);
			final ViewHolder holder = new ViewHolder();
			holder.displayname = (TextView) itemView.findViewById(android.R.id.text1);
			holder.tels_emails = (TextView) itemView.findViewById(android.R.id.text2);
			holder.thumbnail = (ImageView) itemView.findViewById(android.R.id.icon);
			itemView.setTag(holder);
		}
		return itemView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
      final ViewHolder holder = (ViewHolder) view.getTag();
      final long contactId = cursor.getLong(0);
      final String contactName = cursor.getString(2);
      if (holder == null) {
      	setViewSaveClickListeners(view, contactId, contactName, cursor.getString(1), cursor.getString(3));
      	return;
      }
      holder.displayname.setText(contactName);
      Resources res = mContext.getResources();
      int tels = ContactsData.telNumbersCount(context, contactId);
      int emails = ContactsData.emailsCount(context, contactId);
      holder.tels_emails.setText(String.format(res.getQuantityString(R.plurals.telnos, tels), tels) +
      									" | " + String.format(res.getQuantityString(R.plurals.emails, emails), emails));
      final String thumbnailData = cursor.getString(3);
      if (thumbnailData != null) {
         final Uri contactUri = Contacts.getLookupUri(contactId, cursor.getString(1));
         holder.thumbnail.setImageURI(contactUri);
         final Bitmap thumbnailBitmap = new PhotoLoader(mContext).loadContactPhotoThumbnail(thumbnailData);
         holder.thumbnail.setImageBitmap(thumbnailBitmap);
         holder.thumbnail.setOnClickListener(new OnClickListener() {
      		public void onClick(View v) {
      			Intent i = new Intent(mContext, PhotoActivity.class);
      			i.putExtra("photoextra", contactId);
      			mContext.startActivity(i);
      		}
      	});
      } else {
      	holder.thumbnail.setImageDrawable
      							  (mContext.getResources().getDrawable(R.drawable.ic_contact_picture_holo_light));
      }
	}

	public void setTappedPosition(long pos) {
		mTappedPosition = pos;
	}

	private void setViewSaveClickListeners(View view, final long contactId,
														final String name, final String lookup, final String photo) {
		Button b = (Button) view.findViewById(R.id.view);
		if (b != null) {
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(mContext, ContactDetailsActivity.class);
					i.putExtra("detailsextra", contactId);
					mContext.startActivity(i);
				}
			});
		}
		b = (Button) view.findViewById(R.id.save);
		if (b != null) {
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String msg = "";
					LocalContactsData localdata = LocalContactsData.getInstance(mContext);
					if (localdata.openDB()) {
						int saveOpResult = localdata.saveContactData(contactId, name, lookup, photo);
						switch (saveOpResult) {
							case 0:
								msg = mContext.getString(R.string.saved_contact);
								break;
							case 1:
								msg = mContext.getString(R.string.not_saved_contact);
								break;
							default:
								break;
						}
					}
					Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
}
