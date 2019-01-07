package com.example.android.myapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.myapp.database.AppDB;
import com.example.android.myapp.database.Dish;
import com.example.android.myapp.database.Mitzrahnames;
import com.example.android.myapp.database.ingredients;
import com.example.android.myapp.network.RemService;
import com.example.android.myapp.remote.APIUtils;
import com.example.android.myapp.remote.DishCSClass;
import com.example.android.myapp.sync.foodSyncIntentService2;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements MyAdapter.MyAdapterOnClickHandler, MyAdapter.MyAdapterCb{
  //  SharedPreferences settings;
    private static final String TAG = MainActivity.class.getSimpleName();
    static LocalServiceMain mService;
    boolean mBound = false;
    RemService userService;
    private RecyclerView mRecyclerView;
    //LocalService mService;
  //  boolean mBound = false;
    private MyAdapter myAdapter;
private FloatingActionButton fabButton;
private int count=0;
    private AppDB mDb;
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalServiceMain.LocalBinderMain binder = (LocalServiceMain.LocalBinderMain) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDb = AppDB.getInstance(getApplicationContext());
       // settings = getSharedPreferences(getResources().getString(R.string.myprefs), 0);
        mRecyclerView = (RecyclerView) findViewById(R.id.myrecy);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         *
         * Although passing in "this" twice may seem strange, it is actually a sign of separation
         * of concerns, which is best programming practice. The ForecastAdapter requires an
         * Android Context (which all Activities are) as well as an onClickHandler. Since our
         * MainActivity implements the ForecastAdapter ForecastOnClickHandler interface, "this"
         * is also an instance of that type of handler.
         */
        myAdapter = new MyAdapter(this, this,this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(myAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                if(swipeDir==ItemTouchHelper.LEFT){
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        ArrayList<Dish> tasks = myAdapter.getDishes();
                        Dish dish=tasks.get(position-1);
                       // SharedPreferences.Editor editor = settings.edit();
                      //  editor.remove(dish.getId());
                      //  mDb.ingredientsDao().DelParams(dish.getId());
                        mDb.dishDao().deleteDish(dish);
                      //  myAdapter.notifyDataSetChanged();

                    }



                    });
                }
/*
                else if(swipeDir==ItemTouchHelper.RIGHT){
                    Toast.makeText(MainActivity.this,"swipe left to delete",Toast.LENGTH_LONG);
                }*/
            }
        }).attachToRecyclerView(mRecyclerView);
        setupViewModel();
       fabButton = findViewById(R.id.floatingActionButton);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                 //   func1();
                Intent intent = new Intent(MainActivity.this, AddDish.class);
                intent.putExtra(AddDish.EXTRA_TASK_ID, count);
                startActivity(intent);
//                final Dish dish = new Dish("my dish name "+count);
                count++;
//                mDb.dishDao().insertDish(dish);
                ///Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
               // startActivity(addTaskIntent);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 3 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    fabButton.hide();//you have reached to the bottom of your recycler view
                }else{fabButton.show();}
            }
        });
     //   new LongOperation().execute("");





        userService = APIUtils.getUserService();
//        getUsersList();




        //   chk();
    }

    private void func1() {


        final Dish dish = new Dish("my dish name "+count);
        count++;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.dishDao().insertDish(dish);
                //List<Dish> dish1 = mDb.dishDao().loadAllDishes();
                //myAdapter.setDishes(dish1);
                finish();
            }
        });
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<Dish>>() {
            @Override
            public void onChanged(@Nullable List<Dish> taskEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                myAdapter.setDishes(taskEntries);
            }
        });
    }

    @Override
    public void onClick(String pos) {

        //todo: this
        Intent intent = new Intent(MainActivity.this, Ingredi.class);
        intent.putExtra(Ingredi.EXTRA_DATA_ID,new String[] {pos,"end"});
        startActivity(intent);

    }

public void chk(){
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final List<ingredients> myObjectList = mDb.ingredientsDao().loadAllingredsnr();
                    if (myObjectList==null) Log.i("nuuuuuuul","nulllllllllllllll");else{
                        for(int i=0;i<myObjectList.size();i++){

                            ingredients ingredients1=myObjectList.get(i);
                            Log.i("ijkjasas","Dishid= "+ingredients1.getDishId()+" ingrediantnameid = "+ingredients1.getIngredientname1()+" pk= "+ingredients1.getPk());


                        }}
                }
            });
            /*          if (mTaskId == DEFAULT_TASK_ID) {*/
            // insert new task

        /*    if (myObjectList==null) Log.i("nuuuuuuul","nulllllllllllllll");else{
            for(int i=0;i<myObjectList.size();i++){

                ingredients ingredients1=myObjectList.get(i);
                Log.i("ijkjasas","Dishid= "+ingredients1.getDishId()+" ingrediantnameid = "+ingredients1.getIngredientname()+" pk= "+ingredients1.getPk());


            }}*/


          //  myAdapter.setingreds(myObjectList);
               /* } else {
                    //update task
                    task.setId(mTaskId);
                    mDb.dishDao().updateDish(task);
                }*/

            //myAdapter.setingreds(myObjectList);
            finish();
        }
    });




}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.mymenu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.forward) {
            startActivity(new Intent(this, Ingredi.class));
            return true;
        }else if (id == R.id.showChecked) {
            ArrayList<Dish> mDish = myAdapter.getDishes();

            String[] ind = getJ(mDish);

            int j = ind.length;
            if (j < mDish.size())
                ind[j] = "end";
            Intent intent = new Intent(MainActivity.this, Ingredi.class);
            intent.putExtra(Ingredi.EXTRA_DATA_ID, ind);
            startActivity(intent);
        }else if (id == R.id.upchked) {
            ArrayList<Dish> mDish = myAdapter.getDishes();
            Log.d("fddffdfcs2122121", "Updating list of tasblablael");

            String[] ind = getJ(mDish);

            int j = ind.length;
//            if (j < mDish.size())
//                ind[j] = "end";




          //  List<DishCSClass> ModelList = response.body();
            Log.d("fddffdfcss2122128", "Updating list of tasblablael");

            Intent intentToSyncImmediately = new Intent(MainActivity.this, foodSyncIntentService2.class);
            intentToSyncImmediately.putExtra(foodSyncIntentService2.EXTRA_DATA_ID_Obj, ind);
            intentToSyncImmediately.putExtra(foodSyncIntentService2.EXTRA_DATA_ID, "upchked");
            this.startService(intentToSyncImmediately);
return true;


//            Intent intent = new Intent(MainActivity.this, Ingredi.class);
//            intent.putExtra(Ingredi.EXTRA_DATA_ID, ind);
//            startActivity(intent);
//

//        }else if (id == R.id.sync1) {
//            Intent intentToSyncImmediately = new Intent(MainActivity.this, foodSyncIntentService2.class);
//            intentToSyncImmediately.putExtra(foodSyncIntentService2.EXTRA_DATA_ID, "syncdish");
//
//            MainActivity.this.startService(intentToSyncImmediately);
//            return true;
//        }
        }else if (id == R.id.syncFull) {
//        Intent intentToSyncImmediately = new Intent(MainActivity.this, foodSyncIntentService2.class);
//            intentToSyncImmediately.putExtra(foodSyncIntentService2.EXTRA_DATA_ID, "full");
//            //intent.putExtra(MyService.EXTRA_DATA_ID1, bool);
//            //startService(intentToSyncImmediately);
//
//        MainActivity.this.startService(intentToSyncImmediately);
        getUsersList();
            //getUsersList();
        return true;
    }else if (id == R.id.cleanAll) {
        //    mDb.clearAllTables();
            Intent intentToSyncImmediately = new Intent(MainActivity.this, foodSyncIntentService2.class);
            intentToSyncImmediately.putExtra(foodSyncIntentService2.EXTRA_DATA_ID, "clean");
            //intent.putExtra(MyService.EXTRA_DATA_ID1, bool);
            //startService(intentToSyncImmediately);
            MainActivity.this.startService(intentToSyncImmediately);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }*/

  /*  @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }*/

  /*  *//** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) *//*
    public void onButtonClick(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }

    */
    private String[] getJ(ArrayList<Dish> mDish) {// todo: this apply in CUD rest persist

        String[] ind = new String[mDish.size()];
        int j = 0;
        for (int i = 0; i < mDish.size(); i++) {

            Dish dish = mDish.get(i);
            //settings = getSharedPreferences(getResources().getString(R.string.myprefs), 0);
            //  boolean ans = ;//settings.getBoolean(mDish.get(i).getId(), false);
            if (dish.isChecked()) {
                ind[j] = dish.getId();
                j++;
            }

        }
        if (j < mDish.size())
            ind[j] = "end";
        return ind;
    }

/** Defines callbacks for service binding, passed to bindService() *//*
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
*/


    @Override
    public void onCb(final String pos,final boolean bool) {

    /*    if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }*/

  /*      AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                Dish dish=mDb.dishDao().loadDishByIdw(pos);
                dish.setChecked(bool);

                mDb.dishDao().updateDish(dish);
                finish();*/
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(MyService.EXTRA_DATA_ID, pos);
        intent.putExtra(MyService.EXTRA_DATA_ID1, bool);
        startService(intent);
       // holder.itemView.setSelected(position == selectedItem);
  /*      final Dish dish = myAdapter.getDishes().get(pos);

        final String id = mDish.get(position).getId();
        holder.mCheckBox.setChecked(dish.isChecked());*/
        /*}});*/
        }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LocalServiceMain.class);
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

    public void getUsersList(){
        Call<List<DishCSClass>> call = userService.getcsdishes();
        call.enqueue(new Callback<List<DishCSClass>>() {
            @Override
            public void onResponse(Call<List<DishCSClass>> call, Response<List<DishCSClass>> response) {
                if(response.isSuccessful()){


                    Log.d(TAG + "fddffdfdfdfdw2122121", "Updating list of tasks from LiveData in ViewModel" + response.body());
                    if (response != null) {
                        List<DishCSClass> ModelList = response.body();

                        Intent intentToSyncImmediately = new Intent(MainActivity.this, foodSyncIntentService2.class);
                        intentToSyncImmediately.putExtra(foodSyncIntentService2.EXTRA_DATA_ID_Obj, (Serializable) ModelList);
                        intentToSyncImmediately.putExtra(foodSyncIntentService2.EXTRA_DATA_ID, "full");
                        MainActivity.this.startService(intentToSyncImmediately);

                    }
                    //
//
//                    if (response != null) {
//                        List<DishCSClass> ModelList = response.body();
////                        if (questionsModelList.size() > 0)
////                            tvm.setText(questionsModelList.get(0).getTrip());
//                        for (int i = 0; i < ModelList.size(); i++) {
//                            if(ModelList.get(i).getDishName()!="null") {
//                                Dish a=new Dish(ModelList.get(i).getDishName());
//                                List<Dish> tmp=mDb.dishDao().loadDishByname(a.getDishname());
//                                if(tmp.size()>0) a=tmp.get(0);
//                                else  mDb.dishDao().insertDish(a);
//                                List<String> ingreds = ModelList.get(i).getIngreds();
//
//                                String id =a.getId();
//                                for(int j=0;j<ingreds.size();j++){
//                                    String name = ingreds.get(j);
//                                    // dish=mDb.dishDao().loadDishByIdnor(iii);
//                                    //Dish a=(Dish) weatherValues[i].get("dish");
//                                    List<Mitzrahnames> tmp1=mDb.mitzrahnamesDao().loadIngrediByname(name);
//
//                                    Mitzrahnames mitzrahnames;
//                                    if(tmp1.size()>0)
//                                        mitzrahnames=tmp1.get(0);
//                                    else {mitzrahnames = new Mitzrahnames(name);
//                                        mDb.mitzrahnamesDao().insertingreds(mitzrahnames);}
//                                    String idmitzrah=mitzrahnames.getIngredientidglobal();
//                                    ///String a = mitzrahnames.getIngredientidglobal();
//                                    ///String b= iii;
//                                    ingredients ingredients22=new ingredients(id,idmitzrah);
//                                    mDb.ingredientsDao().insertingredsnr(ingredients22);
//                                }
//
//                            }
//
//
//
//                   /* list = response.body();
//                    listView.setAdapter(new UserAdapter(MainActivity.this, R.layout.list_user, list));*/
//                        }
//                    }


                }

            }

            @Override
            public void onFailure(Call<List<DishCSClass>> call, Throwable t) {

            }

        });}














//
//
//
//    private void login() {
//        //relativeLayout.setVisibility(View.VISIBLE);
//
///*        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ImgViewModel.delAll();
//                //    takeAction();
//
//            }
//        }, 3000);*/
//        DataServiceGenerator dataServiceGenerator = new DataServiceGenerator();
//        Service service = dataServiceGenerator.createService(Service.class);
//        Call<Void> call = service.gettoken(new User("admin", "admin"));
//        call.enqueue(new Callback<Void>() {
//
//
//                         @Override
//                         public void onResponse(Call<Void> call, Response<Void> response) {
//                             ServiceGenerator.Token=response.headers().get("Authorization");
//
//                             fetchData();
//                         }
//
//                         @Override
//                         public void onFailure(Call<Void> call, Throwable t) {
//
//                         }
//                     }
//        );
//    }

/*
    private class LongOperation1 extends AsyncTask<String, Void, Void> {
        private AppDB appDB;
        public LongOperation1(AppDB appDB) {
            super();
            this.appDB=appDB;
        }

        @Override
        protected Void doInBackground(String... voids) {
            Dish dish=mDb.dishDao().loadDishByIdw(pos);
            dish.setChecked(bool);

            mDb.dishDao().updateDish(dish);
            }

            final List<Mitzrahnames> myObjectList1 = mDb.mitzrahnamesDao().loadAllingredsnr();
            Log.i("nuuuuuuul1", myObjectList1.size()+"");
            if (myObjectList1 == null) Log.i("nuuuuuuul", "nulllllllllllllll");
            else {
                for (int i = 0; i < myObjectList1.size(); i++) {

                    Mitzrahnames ingredients1 = myObjectList1.get(i);
                    Log.i("ijkjasasmkk", "name= " + ingredients1.getIngredientname() + " ingrediantnameid = " + ingredients1.getIngredientidglobal() );


                }
            }
            return null;
        }*/
    private class LongOperation extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... voids) {
            final List<ingredients> myObjectList = mDb.ingredientsDao().loadAllingredsnr();
            Log.i("nuuuuuuul1", myObjectList.size()+"");
            if (myObjectList == null) Log.i("nuuuuuuul", "nulllllllllllllll");
            else {
                for (int i = 0; i < myObjectList.size(); i++) {

                    ingredients ingredients1 = myObjectList.get(i);
                    Log.i("ijkjasas", "Dishid= " + ingredients1.getDishId() + " ingrediantnameid = " + ingredients1.getIngredientname1() + " pk= " + ingredients1.getPk());


                }
            }

            final List<Mitzrahnames> myObjectList1 = mDb.mitzrahnamesDao().loadAllingredsnr();
            Log.i("nuuuuuuul1", myObjectList1.size()+"");
            if (myObjectList1 == null) Log.i("nuuuuuuul", "nulllllllllllllll");
            else {
                for (int i = 0; i < myObjectList1.size(); i++) {

                    Mitzrahnames ingredients1 = myObjectList1.get(i);
                    Log.i("ijkjasasmkk", "name= " + ingredients1.getIngredientname() + " ingrediantnameid = " + ingredients1.getIngredientidglobal() );


                }
            }
            return null;
        }
    }







}
