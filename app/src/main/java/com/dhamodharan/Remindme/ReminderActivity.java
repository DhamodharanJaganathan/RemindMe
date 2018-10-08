package com.dhamodharan.Remindme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ReminderActivity extends AppCompatActivity {
    private TextView mtoDoTextTextView;
    private StoreRetrieveData storeRetrieveData;
    private ArrayList<ToDoItem> mToDoItems;
    private ToDoItem mItem;
    public static final String EXIT = "com.avjindersekhon.exit";

    String theme;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        theme = getSharedPreferences(MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
        if(theme.equals(MainActivity.LIGHTTHEME)){
            setTheme(R.style.CustomStyle_LightTheme);
        }
        else{
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_layout);
        storeRetrieveData = new StoreRetrieveData(this, MainActivity.FILENAME);
        mToDoItems = MainActivity.getLocallyStoredData(storeRetrieveData);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));



        Intent i = getIntent();
        UUID id = (UUID)i.getSerializableExtra(TodoNotificationService.TODOUUID);
        mItem = null;
        for(ToDoItem toDoItem : mToDoItems){
            if (toDoItem.getIdentifier().equals(id)){
                mItem = toDoItem;
                break;
            }
        }



        mtoDoTextTextView = (TextView)findViewById(R.id.toDoReminderTextViewBody);
        mtoDoTextTextView.setText(mItem.getToDoText());

        if(theme.equals(MainActivity.LIGHTTHEME)){

        }
        else{


        }





    }

    private void closeApp(){
        Intent i = new Intent(ReminderActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra(EXIT, true);
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(EXIT, true);
        editor.apply();
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
        return true;
    }
    private void changeOccurred(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MainActivity.CHANGE_OCCURED, true);
//        editor.commit();
        editor.apply();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toDoReminderDoneMenuItem:

                mToDoItems.remove(mItem);
                changeOccurred();
                saveData();
                closeApp();
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void saveData(){
        try{
            storeRetrieveData.saveToFile(mToDoItems);
        }
        catch (JSONException | IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onBackPressed()
    {
        mToDoItems.remove(mItem);
        changeOccurred();
        saveData();
        closeApp();
        finish();

        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
}
