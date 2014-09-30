package com.geevee.contacts;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

public class ContactsListFragment extends ListFragment
											 implements LoaderManager.LoaderCallbacks<Cursor>,
											 				AdapterView.OnItemClickListener {

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
		return ContactsData.contactsCursorLoader(getActivity());
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


