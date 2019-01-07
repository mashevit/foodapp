package com.example.android.myapp.sync;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.myapp.AppExecutors;
import com.example.android.myapp.R;
import com.example.android.myapp.database.AppDB;
import com.example.android.myapp.database.Dish;
import com.example.android.myapp.network.RemService;
import com.example.android.myapp.remote.APIUtils;
import com.example.android.myapp.remote.DishCSClass;
import com.example.android.myapp.utilities.NetworkUtils;
import com.example.android.myapp.utilities.jsonutils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodSyncTask extends Activity {


    static LocalService mService;
    boolean mBound = false;
    private static final String STATIC_WEATHER_URL =
            "https://andfun-weather.udacity.com/staticweather";

    private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;

   // private AppDB mDb;


    synchronized public static void syncWeather(Context context,final AppDB mDb) {


        try {
        /*
        URL weatherRequestUrl = NetworkUtils.getUrl(context);

            /* Use the URL to retrieve the JSON */
          URL weatherRequestUrl = NetworkUtils.getUrl(context);
            Log.v("going to url", "URL: " + weatherRequestUrl.toString());
          String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

          /* Parse the JSON into a list of weather values */
          final Map[] weatherValues = jsonutils.getWeatherContentValuesFromJson(/*context,*/ jsonWeatherResponse);

            mService.insertall(weatherValues,mDb);
          //String description = mEditText.getText().toString();
          //int priority = getPriorityFromViews();
          // Date date = new Date();

          //final Dish task = new Dish(description );



          //   mTaskId = DEFAULT_TASK_ID;


      } catch (Exception e) {
          /* Server probably invalid */
          e.printStackTrace();
      }
    }





    synchronized public static void syncWeatherfull(final AppDB mDb) {


        try {
        /*
        URL weatherRequestUrl = NetworkUtils.getUrl(context);

            /* Use the URL to retrieve the JSON */
            URL weatherRequestUrl = NetworkUtils.buildUrlfullsync();
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            /* Parse the JSON into a list of weather values */
            final Map[] weatherValues = jsonutils.getWeatherContentValuesFromJsonfull( jsonWeatherResponse);

            mService.insertallcs(weatherValues,mDb);
            //String description = mEditText.getText().toString();
            //int priority = getPriorityFromViews();
            // Date date = new Date();

            //final Dish task = new Dish(description );



            //   mTaskId = DEFAULT_TASK_ID;


        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }




    synchronized public static void syncWeatherfullv2(final AppDB mDb,List<DishCSClass> response) {
       // RemService userService = APIUtils.getUserService();


//        Call<List<DishCSClass>> call = userService.getcsdishes();
//        call.enqueue(new Callback<List<DishCSClass>>() {
//            @Override
//            public void onResponse(Call<List<DishCSClass>> call, Response<List<DishCSClass>> response) {
//                if(response.isSuccessful()){
        Log.d( "fddffdf882122121", "Updating list of tasks from LiveData in ViewModel" );

                    mService.insertallv2(mDb,response);
//
//    }}
//
//            @Override
//            public void onFailure(Call<List<DishCSClass>> call, Throwable t) {
//
//            }
//        });
}

    synchronized public static void createupclass(final AppDB mDb,String[] response,Context context) {
        // RemService userService = APIUtils.getUserService();


//        Call<List<DishCSClass>> call = userService.getcsdishes();
//        call.enqueue(new Callback<List<DishCSClass>>() {
//            @Override
//            public void onResponse(Call<List<DishCSClass>> call, Response<List<DishCSClass>> response) {
//                if(response.isSuccessful()){
        Log.d( "fddffdf882122121", "Upload start task" );

        mService.upload(mDb,response,context);
//
//    }}
//
//            @Override
//            public void onFailure(Call<List<DishCSClass>> call, Throwable t) {
//
//            }
//        });
    }


            synchronized public static void clean(final AppDB mDb) {


        try {

            mService.cleanAll(mDb);



        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }

    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) */
    public void onButtonClick(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            //int num = mService.getRandomNumber();
        //    Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}

