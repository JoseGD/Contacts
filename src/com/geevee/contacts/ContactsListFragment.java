package com.geevee.contacts;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

public class ContactsListFragment extends ListFragment
											 implements LoaderManager.LoaderCallbacks<Cursor>,
											 				AdapterView.OnItemClickListener {

	private static final String[] PROJECTION = {Contacts._ID, Contacts.LOOKUP_KEY,
															  Contacts.DISPLAY_NAME_PRIMARY, Contacts.PHOTO_THUMBNAIL_URI};
	private static final String SELECTION = Contacts.IN_VISIBLE_GROUP + " = 1";
	private final static String ORDER_BY = "display_name";
	
	long mContactId;
   String mContactKey;
   Uri mContactUri;
   private ContactListAdapter mCursorAdapter;
	
	public ContactsListFragment() {
	}

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
   	super.onActivityCreated(savedInstanceState);
   	getLoaderManager().initLoader(0, null, this);
   	mCursorAdapter = new ContactListAdapter(getActivity());
   	setListAdapter(mCursorAdapter);
   	getListView().setOnItemClickListener(this);
   }	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);
		return rootView;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), Contacts.CONTENT_URI, PROJECTION, SELECTION, null, ORDER_BY);
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
		Cursor cursor = ((CursorAdapter) parent.getAdapter()).getCursor();
		cursor.moveToPosition(position);
      mContactId = cursor.getLong(0);
      mContactKey = cursor.getString(1);
      mContactUri = Contacts.getLookupUri(mContactId, mContactKey);
	}
	
}

