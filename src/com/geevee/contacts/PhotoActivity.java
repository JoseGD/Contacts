package com.geevee.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class PhotoActivity extends Activity {

	ImageView contactPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		//getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		contactPhoto = (ImageView) findViewById(R.id.photo);
		Intent i = getIntent();
		long contactId = i.getLongExtra("photoextra", -1);
		if (contactId != -1) {
			contactPhoto.setImageBitmap( new PhotoLoader(this).loadContactPhotoHighRes(contactId) ); 
		}
	}

}
