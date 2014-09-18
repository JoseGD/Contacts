package com.geevee.contacts;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactListAdapter extends CursorAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	
	private class ViewHolder {
      TextView displayname;
      TextView emails_tels;
      ImageView thumbnail;
   }

	public ContactListAdapter(Context context) {
		super(context, null, 0);
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View itemView = mInflater.inflate(R.layout.contact_item, parent, false);
		final ViewHolder holder = new ViewHolder();
		holder.displayname = (TextView) itemView.findViewById(android.R.id.text1);
		holder.emails_tels = (TextView) itemView.findViewById(android.R.id.text2);
		holder.thumbnail = (ImageView) itemView.findViewById(android.R.id.icon);
		itemView.setTag(holder);
		return itemView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
      final long contactId = cursor.getLong(0);
      final ViewHolder holder = (ViewHolder) view.getTag();
      holder.displayname.setText(cursor.getString(2));
      holder.emails_tels.setText(String.format(mContext.getString(R.string.emails_telnos),
				 											  emailsCount(contactId), telNumbersCount(contactId)));
      final String photoData = cursor.getString(3);
      if (photoData != null) {
         final Uri contactUri = Contacts.getLookupUri(contactId, cursor.getString(1));
         holder.thumbnail.setImageURI(contactUri);
         Bitmap thumbnailBitmap = loadContactPhotoThumbnail(photoData);
         holder.thumbnail.setImageBitmap(thumbnailBitmap);		
      } else {
      	holder.thumbnail.setImageDrawable(mContext.getResources()
      													.getDrawable(R.drawable.ic_contact_picture_holo_light));
      }
	}

	private Bitmap loadContactPhotoThumbnail(String photoData) {
      AssetFileDescriptor afd = null;
      try {
	      Uri thumbUri;
	      thumbUri = Uri.parse(photoData);
	      try {
	      	afd = mContext.getContentResolver().openAssetFileDescriptor(thumbUri, "r");
		      FileDescriptor fileDescriptor = afd.getFileDescriptor();
		      if (fileDescriptor != null) {
		      	return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
		      }
	      } catch (FileNotFoundException e) {
	         Log.d("GeeVee", "FileNotFoundException en loadContactPhotoThumbnail - No se pudo obtener foto de contacto");
	      }
      } finally {
      	if (afd != null) {
      		try {
      			afd.close();
            } catch (IOException e) {}
          }
      }
      return null;
	}	

	private int emailsCount(long id) {
		Cursor c = mContext.getContentResolver().query(CommonDataKinds.Email.CONTENT_URI, null,
																	  CommonDataKinds.Email.CONTACT_ID + " = ?",
																	  new String[] {String.valueOf(id)}, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	private int telNumbersCount(long id) {
		Cursor c = mContext.getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI, null,
				  													  CommonDataKinds.Phone.CONTACT_ID + " = ?",
				  													  new String[] {String.valueOf(id)}, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
}
