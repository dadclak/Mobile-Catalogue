package com.example.rostyslav.mobilecatalogue;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Provider extends ContentProvider {

    MobileDatabase mobileDatabase;

    //variable storing the path to the Provider
    private static final String IDENTYFICATOR = "com.example.rostyslav.mobilecatalogue.Provider";
    public static final Uri URI_Values = Uri.parse("content://" +
                   IDENTYFICATOR + "/" + MobileDatabase.TABLE_NAME); //exclusive URL link

    private static final int TABLE = 1;
    private static final int ROW = 2;

    //approval of a URI to a content provider(not the same as)
    private static final UriMatcher LikeUri = new UriMatcher(UriMatcher.NO_MATCH);

    static {
          LikeUri.addURI(IDENTYFICATOR, MobileDatabase.TABLE_NAME, TABLE);
           LikeUri.addURI(IDENTYFICATOR, MobileDatabase.TABLE_NAME + "/#", ROW);
    }

    @Override
    public boolean onCreate() {
        //create my database
        mobileDatabase = new MobileDatabase(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int type = LikeUri.match(uri);

         //allow read and write access to the database
         SQLiteDatabase database = mobileDatabase.getWritableDatabase();

         Cursor cursor = null;

        //create a query in the database
           switch (type) {
               case TABLE:
                   cursor = database.query(
                           false,
                           MobileDatabase.TABLE_NAME,
                           projection,
                           selection,
                           selectionArgs,
                           null,
                           null,
                           sortOrder,
                           null,
                           null
                   );
                   break;
               case ROW:
                   cursor = database.query(
                           false,
                           MobileDatabase.TABLE_NAME,
                           projection,
                           addIdToSelection(selection, uri),
                           selectionArgs,
                           null,
                           null,
                           sortOrder,
                           null,
                           null
                   );
                   break;
           }
         //setting register to watch a content URI for changes
           cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    //the function to add array of items in the future to remove them
    private String addIdToSelection(String selection, Uri uri) {

        if(selection != null && !selection.equals(""))
             selection = selection + " AND " + MobileDatabase.ID + "=" + uri.getLastPathSegment();
        else
            selection = MobileDatabase.ID + "=" + uri.getLastPathSegment();

            return selection;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int type = LikeUri.match(uri);
         SQLiteDatabase database = mobileDatabase.getWritableDatabase();

         long addId = 0;

         //adding new products to the database
          switch (type) {
              case TABLE:
                   addId = database.insert(MobileDatabase.TABLE_NAME, null, values);
                   break;
              default:
                  throw new IllegalArgumentException("Nieznane URI: " + uri);
          }

           getContext().getContentResolver().notifyChange(uri, null);
          return Uri.parse(MobileDatabase.TABLE_NAME + "/" + addId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int type = LikeUri.match(uri);
         SQLiteDatabase database = mobileDatabase.getWritableDatabase();

         int deleteId = 0;

         //delete the selected product database
           switch (type) {
               case TABLE:
                   deleteId = database.delete(MobileDatabase.TABLE_NAME, selection, selectionArgs);
                   break;
               case ROW:
                   deleteId = database.delete(MobileDatabase.TABLE_NAME, addIdToSelection(selection, uri), selectionArgs);
                   break;
                default:
                    throw new IllegalArgumentException("Nieznane URI: " + uri);
           }

           getContext().getContentResolver().notifyChange(uri, null);
         return deleteId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int type = LikeUri.match(uri);
         SQLiteDatabase database = mobileDatabase.getWritableDatabase();

        int updateId = 0;

        //editing the selected product
         switch (type) {
             case TABLE:
                updateId = database.update(MobileDatabase.TABLE_NAME, values, selection, selectionArgs);
                break;
             case ROW:
                updateId = database.update(MobileDatabase.TABLE_NAME, values, addIdToSelection(selection, uri), selectionArgs);
                break;
                default:
                    throw new IllegalArgumentException("Nieznane URI: " + uri);
         }

         getContext().getContentResolver().notifyChange(uri, null);
        return updateId;
    }
}
