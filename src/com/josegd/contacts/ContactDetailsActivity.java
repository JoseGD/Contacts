package com.josegd.contacts;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

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
	   	getListView().setDivider(null);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return ContactsData.detailsCursorLoader(this, mContactId);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = mCursorAdapter.addSeparatorRowsToCursor(data);
		mCursorAdapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.swapCursor(null);	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		CursorAdapter adapter = (CursorAdapter) parent.getAdapter();
		Cursor c = ((ContactDetailsListAdapter) adapter).getCursor();
		c.moveToPosition(position);
		if (c.getString(3).equals(Phone.CONTENT_ITEM_TYPE)) {
			WSCallDialogFragment dialog = new WSCallDialogFragment();
			Bundle args = new Bundle();
			args.putString(WSCallDialogFragment.ARG_PHONE_NUMBER, c.getString(4));
			dialog.setArguments(args);			
			dialog.show(getFragmentManager(), "tag_irrelevant_here");
		}
	}

}
