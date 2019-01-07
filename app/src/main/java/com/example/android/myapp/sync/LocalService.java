package com.example.android.myapp.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.android.myapp.MainActivity;
import com.example.android.myapp.database.AppDB;
import com.example.android.myapp.database.Dish;
import com.example.android.myapp.database.Mitzrahnames;
import com.example.android.myapp.database.ingredients;
import com.example.android.myapp.network.RemService;
import com.example.android.myapp.remote.APIUtils;
import com.example.android.myapp.remote.DishCSClass;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
//    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public static void insertall(Map[] weatherValues, AppDB mDb) {
        for(int i=0;i<weatherValues.length;i++){
            if(weatherValues[i].get("dishName")!="null") {
                Dish a=(Dish) weatherValues[i].get("dish");
                List<Dish> tmp=mDb.dishDao().loadDishByname(a.getDishname());
                if(tmp.size()==0|tmp==null)
                    mDb.dishDao().insertDish(a);
            }
        }
    }

    public static void insertallcs(Map[] weatherValues, AppDB mDb) throws JSONException {
        for(int i=0;i<weatherValues.length;i++){
            if(weatherValues[i].get("dishName")!="null") {
                Dish a=(Dish) weatherValues[i].get("dish");
                List<Dish> tmp=mDb.dishDao().loadDishByname(a.getDishname());
                if(tmp.size()>0) a=tmp.get(0);
                else  mDb.dishDao().insertDish(a);
                JSONArray ingreds = (JSONArray) weatherValues[i].get("ingreds");

                String id =a.getId();
                for(int j=0;j<ingreds.length();j++){
                    String name = ingreds.getString(j);
                    // dish=mDb.dishDao().loadDishByIdnor(iii);
                    //Dish a=(Dish) weatherValues[i].get("dish");
                    List<Mitzrahnames> tmp1=mDb.mitzrahnamesDao().loadIngrediByname(name);

                    Mitzrahnames mitzrahnames;
                    if(tmp1.size()>0)
                        mitzrahnames=tmp1.get(0);
                    else {mitzrahnames = new Mitzrahnames(name);
                        mDb.mitzrahnamesDao().insertingreds(mitzrahnames);}
                    String idmitzrah=mitzrahnames.getIngredientidglobal();
                    ///String a = mitzrahnames.getIngredientidglobal();
                    ///String b= iii;
                    ingredients ingredients22=new ingredients(id,idmitzrah);
                    mDb.ingredientsDao().insertingredsnr(ingredients22);
                }

            }
        }
    }




    public static void insertallv2(AppDB mDb,List<DishCSClass> response){



        List<DishCSClass> ModelList = response;
//                        if (questionsModelList.size() > 0)
//                            tvm.setText(questionsModelList.get(0).getTrip());
        for (int i = 0; i < ModelList.size(); i++) {
            if(ModelList.get(i).getDishName()!="null") {
                Dish a=new Dish(ModelList.get(i).getDishName());
                List<Dish> tmp=mDb.dishDao().loadDishByname(a.getDishname());
                if(tmp.size()>0) a=tmp.get(0);
                else  mDb.dishDao().insertDish(a);
                List<String> ingreds = ModelList.get(i).getIngreds();

                String id =a.getId();
                for(int j=0;j<ingreds.size();j++){
                    String name = ingreds.get(j);
                    // dish=mDb.dishDao().loadDishByIdnor(iii);
                    //Dish a=(Dish) weatherValues[i].get("dish");
                    List<Mitzrahnames> tmp1=mDb.mitzrahnamesDao().loadIngrediByname(name);

                    Mitzrahnames mitzrahnames;
                    if(tmp1.size()>0)
                        mitzrahnames=tmp1.get(0);
                    else {mitzrahnames = new Mitzrahnames(name);
                        mDb.mitzrahnamesDao().insertingreds(mitzrahnames);}
                    String idmitzrah=mitzrahnames.getIngredientidglobal();
                    ///String a = mitzrahnames.getIngredientidglobal();
                    ///String b= iii;
                    ingredients ingredients22=new ingredients(id,idmitzrah);
                    mDb.ingredientsDao().insertingredsnr(ingredients22);


                }



                   /* list = response.body();
                    listView.setAdapter(new UserAdapter(MainActivity.this, R.layout.list_user, list));*/
            }
        }


    }






    public static void upload(AppDB mDb,String[] res,Context context){
        List<DishCSClass> ans = new ArrayList<DishCSClass>();
        int i=0;
        Log.d("fddffdfcss2122121", "Updating list of tasblablael"+res);

        while(i<res.length&&!"end".equals(res[i])){
            Dish tmp=mDb.dishDao().loadDishByIdnor(res[i]);
            DishCSClass tmpans=new DishCSClass();
            tmpans.setDishName(tmp.getDishname());
            List<String> ingreds=mDb.mitzrahnamesDao().loadAllingredfordish(res[i]);
            tmpans.setIngreds(ingreds);
            Log.d("fddffdfcss2122121", "Updating list of tasblablael"+i);

            ans.add(tmpans);
            i++;
        }

        RemService userservice = APIUtils.getUserService();
        Log.d("fddffdfcss2122121", "Updating list of tasblablael"+ans);

        Call<Void> call = userservice.addUser(ans);
        call.enqueue(new Callback<Void>() {


            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                backgroundThreadShortToast(context,"UPLOADED");// Toast.makeText(LocalService.this,"uploaded",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                backgroundThreadShortToast(context,"ERROR");
            }
        });




    }

    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        // final Context context=getApplicationContext();
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static void cleanAll(AppDB mDb){

        mDb.clearAllTables();


    }

}