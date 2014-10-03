package com.geevee.contacts;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

public class ContactsListFragment extends ListFragment
											 implements LoaderManager.LoaderCallbacks<Cursor>,
											 				AdapterView.OnItemClickListener {

   public static final String ARG_SECTION_NUMBER = "arg_section";
	
   private static final int ADDRESS_BOOK_LIST = 1;
   private static final int LOCAL_CONTACTS_LIST = 2;
   
	private ContactListAdapter mCursorAdapter;
	private int listNumber;
	
	public ContactsListFragment() {
	}

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
   	super.onActivityCreated(savedInstanceState);
   	listNumber = getArguments().getInt(ARG_SECTION_NUMBER);
   	getLoaderManager().initLoader(listNumber, null, this);
   	mCursorAdapter = new ContactListAdapter(getActivity());
   	setListAdapter(mCursorAdapter);
   	getListView().setOnItemClickListener(this);
		Log.d("Contacts", "Showing list number " + listNumber);
   }	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);
		return rootView;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {
			case ADDRESS_BOOK_LIST:
				return ContactsData.contactsCursorLoader(getActivity());
			case LOCAL_CONTACTS_LIST:
				LocalContactsData localdata = LocalContactsData.getInstance(getActivity());
				if (localdata.openDB()) {
					return localdata.localContactsCursorLoader(getActivity());
				} else
					return null;
			default:
				return null;
		}
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
		CursorAdapter adapter = (CursorAdapter) parent.getAdapter();
		((ContactListAdapter) adapter).setTappedPosition(position);
      adapter.getView(position, null, parent);  // convertView null to force row update (according to CursorAdapter.getView() implementation)
      getListView().invalidateViews();
	}
	
}


