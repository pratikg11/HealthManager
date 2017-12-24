package com.fitbit.sampleandroidoauth2.Home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fitbit.authentication.AuthenticationManager;
import com.fitbit.sampleandroidoauth2.ConnectionLost;
import com.fitbit.sampleandroidoauth2.NetworkCheck;
import com.fitbit.sampleandroidoauth2.R;
import com.fitbit.sampleandroidoauth2.SharedPrefUtil;
import com.fitbit.sampleandroidoauth2.fragments.ActivitiesFragment;
import com.fitbit.sampleandroidoauth2.fragments.RootActivity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class HMDailyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        ActivitiesFragment.GetCal {

    private static final int EDITOR_REQUEST_CODE = 1002;
    HMDatabaseHandler helper = new HMDatabaseHandler(this);
    ListView list;
    String itemName;
    String brandName;
    String calories;
    String servingSize;
    String servingSize_unit;
    FloatingActionButton fab_fitbit;
    TextView tv_exer;
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Date date = new Date();
    ConnectionLost connectionLostFragment;
    Timer timer;
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    private CursorAdapter cursorAdapter;
    private int userId;
    private HMUserClass user;
    int steps;
    double weight, calFromSteps;
    SharedPrefUtil sharedPrefUtil;

    Timer timer_localSensor, timer_fitbit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_home);

        sharedPrefUtil = new SharedPrefUtil();
        startService(new Intent(this, HMStepsService.class));

        tv_exer = (TextView) findViewById(R.id.tv_exercise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Daily Activities");

        connectionLostFragment = new ConnectionLost();

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_shortcut);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getTitle().toString()) {
                    case "Add Food":
                        Intent intent = new Intent(HMDailyActivity.this, HMManualAddFood.class);
                        intent.putExtra("user_Id", String.valueOf(userId));
                        startActivityForResult(intent, EDITOR_REQUEST_CODE);
                        break;

                    case "Add Fitbit":
                        if (NetworkCheck.isInternetAvailable(getApplicationContext())) {
                            Toast.makeText(HMDailyActivity.this, "Redirecting", Toast.LENGTH_LONG).show();
                            Intent intent_fitbit = new Intent(HMDailyActivity.this, RootActivity.class);
                            startActivityForResult(intent_fitbit, 8);
                        } else {
                            connectionLostFragment.show(HMDailyActivity.this.getFragmentManager(), "dialogFragment");
                        }
                        break;
                    case "Search & Add Food":
                        Intent intent2 = new Intent(HMDailyActivity.this, HMMainActivityHome.class);
                        intent2.putExtra("user_Id", String.valueOf(userId));
                        startActivityForResult(intent2, EDITOR_REQUEST_CODE);
                        break;
                }
                return false;
            }
        });

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(HMDataProvider.CONTENT_ITEM_TYPE);
        if (uri != null) {
            Bundle extras1 = intent.getExtras();
            itemName = extras1.getString("itemName");
            brandName = extras1.getString("brandName");
            calories = extras1.getString("calories");
            servingSize = extras1.getString("servingSize");
            servingSize_unit = extras1.getString("servingSize_unit");
            userId = Integer.parseInt(extras1.getString("user_Id"));
            insertFood(userId, itemName, brandName, calories, servingSize, servingSize_unit);
        } else {
            Bundle extras1 = intent.getExtras();
            userId = Integer.parseInt(extras1.getString("user_Id"));
        }
        getValues();

        cursorAdapter = new HMFoodAdaptorClass(this, null, 0);
        getLoaderManager().initLoader(0, null, this);
        list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        setTitle("HMDailyActivity");
    }

//If fitbit connected, take calories burned value from fitbit, else use Step Detector sensor value to calculate calories burned.
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "onResume");
        if (AuthenticationManager.isLoggedIn()) {
            if (timer_localSensor != null) {
                timer_localSensor.purge();
                timer_localSensor.cancel();
            }

            timer_fitbit = new Timer();
            timer_fitbit.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.e("TAG", "runnable_fitbit");
                    try {
                        if (AuthenticationManager.isLoggedIn()) {
                            ActivitiesFragment fragment = new ActivitiesFragment();
                            fragment.getCal = HMDailyActivity.this;
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.add(R.id.activity_profile, fragment, "Hello");
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    } catch (Exception e) {

                    }
                }
            }, 0, 30000);
        }
        if (!AuthenticationManager.isLoggedIn()) {
            timer_localSensor = new Timer();
            timer_localSensor.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.e("TAG", "runnable_local step");
                    try {
                        steps = sharedPrefUtil.getSharedPreferenceInt(getApplicationContext(), "steps", 0);
                        Log.e("TAG","steps_activity: "+steps);
                        Calculate(steps);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 30000);
        }
    }

    //Calculate calories burned based on number of steps.
    public void Calculate(final int steps){
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            public void run() {
                weight = user.getUserWeight() * 2.204;
                double calBurnedPerMile = weight * 0.535;
                int stepToMile = 1800;
                double conversionFactor = calBurnedPerMile / stepToMile;

                Log.e("TAG", "Cal Burned for " + steps +": "+(conversionFactor * steps));
                tv_exer.setText(new DecimalFormat("#0.00").format(conversionFactor * steps));
                getValues();
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer_fitbit != null) {
            timer_fitbit.cancel();
            timer_fitbit.purge();
        }
        if (timer_localSensor != null) {
            timer_localSensor.purge();
            timer_localSensor.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(HMDailyActivity.this, HMSetDetailsActivity.class);
        intent.putExtra("user_Id", String.valueOf(userId));
        startActivityForResult(intent, EDITOR_REQUEST_CODE);

        return super.onOptionsItemSelected(item);
    }

    private void getValues() {
        double calorieCount = 0;
        String query1 = "SELECT " + HMDatabaseHandler.FOOD_COLUMN_CALORIES + " FROM " + HMDatabaseHandler.FOOD_TABLE_NAME +
                " WHERE " + HMDatabaseHandler.FOOD_COLUMN_USERID + " = " + userId + " and " + HMDatabaseHandler.FOOD_COLUMN_DATE
                + " = " + dateFormat.format(date);
        SQLiteDatabase database;
        HMDatabaseHandler helper = new HMDatabaseHandler(this);
        database = helper.getWritableDatabase();
        Cursor data = database.rawQuery(query1, null);
        data.moveToFirst();
        while (!data.isAfterLast()) {
            calorieCount = calorieCount + Double.parseDouble(data.getString(0));
            data.moveToNext();
        }
        TextView tv_intake = (TextView) findViewById(R.id.tv_todaysIntake);
        tv_intake.setText(new DecimalFormat("#0.00").format(calorieCount));

        TextView tv_goal = (TextView) findViewById(R.id.tv_goal);
        user = HMUserClass.getUserFromID(userId, this);
        tv_goal.setText(String.valueOf(user.getGoal()));

        String exercise = tv_exer.getText().toString();

        String goal = tv_goal.getText().toString();
        double remaining;
        try {
            remaining = Double.parseDouble(goal) - calorieCount + Integer.parseInt(exercise);
        }catch(Exception e) {
            remaining = Double.parseDouble(goal) - calorieCount + Double.parseDouble(exercise);
        }

        TextView tv_remianing = (TextView) findViewById(R.id.tv_remaining);
        tv_remianing.setText(new DecimalFormat("#0.00").format(remaining));
    }


    /**
     * @param item_Name
     * @param brand_Name
     * @param _calories
     * @param serving_Size
     * @param serving_Size_unit
     */
    private void insertFood(int userId, String item_Name, String brand_Name, String _calories, String serving_Size, String serving_Size_unit) {
        ContentValues values = new ContentValues();
        values.put(HMDatabaseHandler.FOOD_COLUMN_USERID, userId);
        values.put(HMDatabaseHandler.FOOD_COLUMN_NAME, item_Name);
        values.put(HMDatabaseHandler.FOOD_COLUMN_BRAND, brand_Name);
        values.put(HMDatabaseHandler.FOOD_COLUMN_CALORIES, _calories);
        values.put(HMDatabaseHandler.FOOD_COLUMN_SERVING_SIZE, serving_Size);
        values.put(HMDatabaseHandler.FOOD_COLUMN_SERVING_SIZE_UNIT, serving_Size_unit);
        values.put(HMDatabaseHandler.FOOD_COLUMN_DATE, dateFormat.format(date));
        getContentResolver().insert(HMDataProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String query = HMDatabaseHandler.FOOD_COLUMN_DATE + " = " + dateFormat.format(date) + " and " +
                HMDatabaseHandler.FOOD_COLUMN_USERID + " = " + userId;
        return new CursorLoader(this, HMDataProvider.CONTENT_URI,
                null, query, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (8 == requestCode) {
                Toast.makeText(this, "Login Successful\nCalorie Burnt: " + data.getStringExtra("cal"), Toast.LENGTH_SHORT).show();
                tv_exer.setText(data.getStringExtra("cal"));
                getValues();
            } else if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
                restartLoader();
            }
        }
    }

    //Set exercise value in UI.
    @Override
    public void calCount(String data) {
        Log.e("TAG", "data: " + data);
        tv_exer.setText(data);
        getValues();
    }

}
