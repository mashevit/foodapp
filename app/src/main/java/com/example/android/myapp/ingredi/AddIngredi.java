package com.example.android.myapp.ingredi;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.android.myapp.AddDish;
import com.example.android.myapp.AddDishViewModel;
import com.example.android.myapp.AddDishViewModelFactory;
import com.example.android.myapp.AppExecutors;
import com.example.android.myapp.R;
import com.example.android.myapp.database.AppDB;
import com.example.android.myapp.database.Dish;
import com.example.android.myapp.database.Mitzrahnames;
import com.example.android.myapp.database.ingredients;

public class AddIngredi extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = AddDish.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;
    Dish dish;
    private String mTaskId = DEFAULT_TASK_ID+"";

    // Member variable for the Database
    private AppDB mDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

  setContentView(R.layout.ingredi_layout);
mEditText=findViewById(R.id.editText2);
        //initViews();*//*
mButton=findViewById(R.id.button3);

        mDb = AppDB.getInstance(getApplicationContext());

      /*  if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }
*/
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            //  mButton.setText(R.string.update_button);
            if (mTaskId.equals(DEFAULT_TASK_ID+"")) {
                // populate the UI
                mTaskId = intent.getStringExtra(EXTRA_TASK_ID);
                //Log.i("maaaasss",mTaskId+" "+ )
               // dish=mDb.dishDao().loadDishById(mTaskId).getValue();

                Log.i("maaaasss",mTaskId+" "/*+dish.getDishname()*/ );
                // COMPLETED (9) Remove the logging and the call to loadTaskById, this is done in the ViewModel now
                // COMPLETED (10) Declare a AddTaskViewModelFactory using mDb and mTaskId
                AddIngrediViewModelFactory factory = new AddIngrediViewModelFactory(mDb, mTaskId);
                // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
                // for that use the factory created above AddTaskViewModel
                final AddIngrediViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddIngrediViewModel.class);

                // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
                viewModel.getIngredi().observe(this, new Observer<Mitzrahnames>() {
                    @Override
                    public void onChanged(@Nullable Mitzrahnames taskEntry) {
                        viewModel.getIngredi().removeObserver(this);
                        // populateUI(taskEntry);
                    }
                });

              //  onSaveButtonClicked(mTaskId);


            }
        }
mButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onSaveButtonClicked(mTaskId);

    }
});
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }


/**
 * initViews is called from onCreate to init the member variable views
 *//*

    private void initViews() {
   */
/*     mEditText = findViewById(R.id.editTextTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);

        mButton = findViewById(R.id.saveButton);*//*

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    */
    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param //task the taskEntry to populate the UI
     *//*

   private void populateUI(Dish task) {
        if (task == null) {
            return;
        }

       */
/* mEditText.setText(task.getDescription());
        setPriorityInViews(task.getPriority());*//*

    }

    */
    /*
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */

    public void onSaveButtonClicked(final String iii ){
        //   String description = mEditText.getText().toString();
        //int priority = getPriorityFromViews();
        // Date date = new Date();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                /*          if (mTaskId == DEFAULT_TASK_ID) {*/
                // insert new task

                Log.i("maaaassst",iii+" "/*+dish.getDishname()*/ );
                String name = mEditText.getText().toString();
                dish=mDb.dishDao().loadDishByIdnor(iii);
                final Mitzrahnames mitzrahnames = new Mitzrahnames(name/* +" "+ dish.getDishname()*/);
                mDb.mitzrahnamesDao().insertingreds(mitzrahnames);
                String a = mitzrahnames.getIngredientidglobal();
                String b= iii;
                ingredients ingredients22=new ingredients(b,a);
                mDb.ingredientsDao().insertingredsnr(ingredients22);
               /* } else {
                    //update task
                    task.setId(mTaskId);
                    mDb.dishDao().updateDish(task);
                }*/
                finish();
            }
        });
      //  mTaskId = DEFAULT_TASK_ID;

    }


/**
 * getPriority is called whenever the selected priority needs to be retrieved
 *//*

     */
/*  public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }*//*


     */
/**
 * setPriority is called when we receive a task from MainActivity
 *
 * @param priority the priority value
 *//*

     */
/*    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }*/

}

