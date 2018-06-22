package com.example.rostyslav.mobilecatalogue;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity  implements View.OnClickListener{

    private SQLiteDatabase database;

    private EditText eMobile, eModel, eVersion, eWWW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //creating new bundle for activity
        Bundle catalogy = getIntent().getExtras();

        //opening the database for reading and writing
         MobileDatabase mobileDatabase = new MobileDatabase(this);
          database = mobileDatabase.getWritableDatabase();

          //read input fields
          eMobile = (EditText)findViewById(R.id.editMobile);
           eModel = (EditText)findViewById(R.id.editModel);
            eVersion = (EditText)findViewById(R.id.editVersion);
             eWWW = (EditText)findViewById(R.id.editWWW);

             //check whether it is empty bundle
          if (catalogy != null) {
               if (catalogy.containsKey("id")) {

                    long id = catalogy.getLong("id");

                    //cursor creation and query element
                   Cursor cursor = getContentResolver().query(
                           ContentUris.withAppendedId(Provider.URI_Values, id), null, null, null, null);

                   //put courses on the first element
                     cursor.moveToFirst();

                     //writing to variables first the ID of the element and its value
                       int idMobile = cursor.getColumnIndexOrThrow(MobileDatabase.COLUMN_Mobile);
                         String mobile = cursor.getString(idMobile);
                           eMobile.setText(mobile);
                        int idModel = cursor.getColumnIndexOrThrow(MobileDatabase.COLUMN_Model);
                          String model = cursor.getString(idModel);
                            eModel.setText(model);
                         int idVersion = cursor.getColumnIndexOrThrow(MobileDatabase.COLUMN_Version);
                           String version = cursor.getString(idVersion);
                             eVersion.setText(version);
                          int idWWW = cursor.getColumnIndexOrThrow(MobileDatabase.COLUMN_WWW);
                            String www = cursor.getString(idWWW);
                              eWWW.setText(www);
               }
          }
    }

    @Override
    public void onClick(View v) {

        //checking which button is pressed
        switch (v.getId()) {
            case R.id.btnSave:
                //checking whether input fields are empty
              if (eMobile.getText().toString().equals("") ||
                      eModel.getText().toString().equals("") ||
                      eVersion.getText().toString().equals("") ||
                      eWWW.getText().toString().equals("")) {
                  Toast.makeText(this, "Wpis wszystkie pola!", Toast.LENGTH_SHORT).show();
              } else {

                  ContentValues contentValues = new ContentValues();
                   //adding data to the database
                     contentValues.put(MobileDatabase.COLUMN_Mobile, eMobile.getText().toString());
                      contentValues.put(MobileDatabase.COLUMN_Model, eModel.getText().toString());
                       contentValues.put(MobileDatabase.COLUMN_Version, eVersion.getText().toString());
                        contentValues.put(MobileDatabase.COLUMN_WWW, eWWW.getText().toString());

                    Bundle catalogy = getIntent().getExtras();
                    if(catalogy != null) {
                        if (catalogy.containsKey("id")) {
                            long id = catalogy.getLong("id");
                            getContentResolver().update(
                                    ContentUris.withAppendedId(Provider.URI_Values, id), contentValues, null, null);
                        }
                    } else {
                            getContentResolver().insert(Provider.URI_Values, contentValues);
                        }

                        finish();
              }
              break;
            case R.id.btnBack:
                //return to the home page
                finish();
                break;
            case R.id.btnWWW:
                //go to browser link
                if(!eWWW.getText().toString().equals("")) {
                     String www = eWWW.getText().toString();
                     //adding if there is no input field
                     if (!www.startsWith("https://")){
                         www = "https://" + www;
                     }

                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(www));
                      startActivity(intent);
                } else {
                    Toast.makeText(this, "Wpis poprawny link!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
