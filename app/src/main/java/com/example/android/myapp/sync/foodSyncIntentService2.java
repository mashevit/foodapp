package com.example.android.myapp.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.myapp.database.AppDB;
import com.example.android.myapp.remote.DishCSClass;

import java.util.List;

public class foodSyncIntentService2 extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public static final String EXTRA_DATA_ID_Obj="ljjhjhqqsqqqw";
    public static final String EXTRA_DATA_ID="ljjhjhqq";
   // private AppDB mDb;
    public foodSyncIntentService2(String name) {
        super("foodSyncIntentService2");
}

    public foodSyncIntentService2() {
        super("foodSyncIntentService2");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String todohere="";//="syncdish";
        AppDB mdb= AppDB.getInstance(this);
        Log.d("fddffdfcss212221",intent+ intent.getStringExtra(EXTRA_DATA_ID));
        if (intent != null && intent.hasExtra(EXTRA_DATA_ID)) {
        todohere = intent.getStringExtra(EXTRA_DATA_ID);
            Log.d("fddffdfcss212221", todohere);
        }
        if(todohere.equals("syncdish")){
              FoodSyncTask.syncWeather(this,mdb);
            todohere="";
        }else if(todohere.equals("full")){
            List<DishCSClass> a= (List<DishCSClass>) intent.getSerializableExtra(EXTRA_DATA_ID_Obj);
            FoodSyncTask.syncWeatherfullv2(mdb,a);
            todohere="";
        }else if(todohere.equals("clean")){
        FoodSyncTask.clean(mdb);
        todohere="";
    }  else if(todohere.equals("upchked")){

            Log.d("fddffdfcss212221", "Updating list of tasblablael");

            String[] a=intent.getStringArrayExtra(EXTRA_DATA_ID_Obj);
            FoodSyncTask.createupclass(mdb,a,this);
        todohere="";
    }

}}
