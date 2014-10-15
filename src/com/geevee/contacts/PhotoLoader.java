package com.geevee.contacts;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

public class PhotoLoader {
	
	ContentResolver mContentR;
	
	public PhotoLoader(Context context) {
		mContentR = context.getContentResolver();
	}

	public Bitmap loadContactPhotoThumbnail(String photoData) {
      AssetFileDescriptor afd = null;
      try {
	      Uri photoUri = Uri.parse(photoData);
	      try {
	      	afd = mContentR.openAssetFileDescriptor(photoUri, "r");
		      FileDescriptor fileDescriptor = afd.getFileDescriptor();
		      if (fileDescriptor != null) {
		      	return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
		      }
	      } catch (FileNotFoundException e) {
	         Log.d("Contacts", "FileNotFoundException en loadContactPhotoThumbnail - No se pudo obtener foto de contacto");
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

	public Bitmap loadContactPhotoHighRes(long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		InputStream photoStream = Contacts.openContactPhotoInputStream(mContentR, contactUri, true);
		if (photoStream != null) {
			BufferedInputStream bufIS = new BufferedInputStream(photoStream);
	      return BitmapFactory.decodeStream(bufIS);
		} else
			return null;
	}
	
}
