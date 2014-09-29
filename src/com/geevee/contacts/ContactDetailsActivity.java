package com.geevee.contacts;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class ContactDetailsActivity extends ListActivity
												implements LoaderManager.LoaderCallbacks<Cursor>,
														     AdapterView.OnItemClickListener {

	private ContactDetailsListAdapter mCursorAdapter;
	private long mContactId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		mContactId = i.getLongExtra("detailsextra", -1);
		if (mContactId != -1) {
			getLoaderManager().initLoader(0, null, this);
	   	mCursorAdapter = new ContactDetailsListAdapter(this);
	   	setListAdapter(mCursorAdapter);
	   	getListView().setOnItemClickListener(this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return ContactsData.detailsCursorLoader(this, mContactId);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mCursorAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.swapCursor(null);	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		
	}

}
