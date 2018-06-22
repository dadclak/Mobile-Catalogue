package com.example.rostyslav.mobilecatalogue;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CatalogueActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

   private static final int REQUEST_ADD_ITEM = 1;

   //added arrays of phone IDs and values
   private static final String[] from = new String[]{MobileDatabase.COLUMN_Mobile, MobileDatabase.COLUMN_Model};
   private static final int[] to = new  int[]{R.id.txtMobile, R.id.txtModel};

   //matching columns with the cursor in Textview
     MobileDatabase mobileDatabase;
      SimpleCursorAdapter simpleCursorAdapter;
       ListView listView;

      // @Target(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        mobileDatabase = new MobileDatabase(this);
         simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.activity_row, null, from, to, 0);

         listView = (ListView)findViewById(R.id.mobileList);
          listView.setAdapter(simpleCursorAdapter);

          //the definition of the button item
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent = new Intent(CatalogueActivity.this, UpdateActivity.class);
                    intent.putExtra("id", id);
                     startActivity(intent);
               }
           });


           listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                     menuInflater.inflate(R.menu.delete, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.deleteMobile:
                            delete();
                           return  true;
                    }
                    return false;
                }

                private void  delete()  {
                    long select[] = listView.getCheckedItemIds();

                    for (int i = 0; i < select.length; i++ ) {
                        getContentResolver().delete(
                                ContentUris.withAppendedId(Provider.URI_Values, select[i]), null, null);
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });

            getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
         String[] projection = {MobileDatabase.ID, MobileDatabase.COLUMN_Mobile, MobileDatabase.COLUMN_WWW, MobileDatabase.COLUMN_Model, MobileDatabase.COLUMN_Version};
         return new CursorLoader(this, Provider.URI_Values, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
               simpleCursorAdapter.swapCursor(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
         menuInflater.inflate(R.menu.menu, menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMobile:
                Intent intent = new Intent(CatalogueActivity.this, UpdateActivity.class);
                 startActivityForResult(intent, REQUEST_ADD_ITEM);
                 break;
                 default:
                     return  super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_ITEM:
                    break;
            }
        } super.onActivityResult(requestCode, resultCode, data);
    }
}
