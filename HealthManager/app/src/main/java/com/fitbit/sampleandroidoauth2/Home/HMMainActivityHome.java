package com.fitbit.sampleandroidoauth2.Home;

/**
 * Created by Pratik on 8/2/2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fitbit.sampleandroidoauth2.PostRequest;
import com.fitbit.sampleandroidoauth2.R;
import com.fitbit.sampleandroidoauth2.fragments.RootActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HMMainActivityHome extends AppCompatActivity implements PostRequest.Response {

    private static final int EDITOR_REQUEST_CODE = 1001;
    public List<String> itemList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView list;
    JSONObject jsonResponse = null;
    FloatingActionButton fab_fitbit;
    ProgressBar progressBar_home;
    private long lng;
    private int id;
    private int userId;
    // public static List<String> brandList = new ArrayList<String>();
    // public static List<String> calorieList = new ArrayList<String>();

    public static boolean checkFoodName(String food) {
        return !food.equals("");
    }

    public static boolean checkFromValue(String from) {
        return !from.equals("");
    }

    public static boolean checkToValue(String to) {
        return !to.equals("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //String ab="hello"+System.getProperty("line.separator")+"prat"
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        setTitle("Search and Add Food");
        Intent intent = getIntent();
        Bundle extras1 = intent.getExtras();
        userId = Integer.parseInt(extras1.getString("user_Id"));

        progressBar_home = (ProgressBar) findViewById(R.id.progressBar_home);

        FloatingActionButton getData = (FloatingActionButton) findViewById(R.id.button);
        getData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText et_searchFood = (EditText) findViewById(R.id.edt_searchFood);
                String food = et_searchFood.getText().toString();
                EditText et_fromCalories = (EditText) findViewById(R.id.edt_fromCalories);
                String fromValue = et_fromCalories.getText().toString();
                EditText et_toCalories = (EditText) findViewById(R.id.edt_toCalories);
                String toValue = et_toCalories.getText().toString();
                if (!checkFoodName(food)) {
                    Toast.makeText(HMMainActivityHome.this, "Please Enter Food Name", Toast.LENGTH_LONG).show();
                } else if (!checkFromValue(fromValue)) {
                    Toast.makeText(HMMainActivityHome.this, "Please Enter from Value", Toast.LENGTH_LONG).show();
                } else if (!checkToValue(toValue)) {
                    Toast.makeText(HMMainActivityHome.this, "Please Enter to Value", Toast.LENGTH_LONG).show();
                } else {
                    progressBar_home.setVisibility(View.VISIBLE);
                    JSONObject requestObject = getJSONObject(food, fromValue, toValue);
                    callRestAPI(requestObject);
                }
            }
        });
        show();

        fab_fitbit = (FloatingActionButton) findViewById(R.id.fab_fitbit);
        fab_fitbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HMMainActivityHome.this, "Redirecting", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HMMainActivityHome.this, RootActivity.class);
                startActivityForResult(intent, 8);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                //    String selectedFromList =(String) (list.getItemAtPosition(myItemInt));
                lng = mylng;
                id = myItemInt;
                DialogInterface.OnClickListener dialogClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int button) {
                                if (button == DialogInterface.BUTTON_POSITIVE) {
                                    //Insert Data management code here
                                    openProfile(lng, id);
                                    Toast.makeText(HMMainActivityHome.this,
                                            "Item Added",
                                            Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(HMMainActivityHome.this);
                builder.setMessage("Do you want to add this item?")
                        .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                        .show();
            }
        });

        setTitle("Search and Add Food");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (8 == requestCode) {
                Toast.makeText(this, "Cal burnt: " + data.getStringExtra("cal"), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void callRestAPI(JSONObject requestObject) {
        String restURL = "https://api.nutritionix.com/v1_1/search";
        itemList.clear();
        new PostRequest(HMMainActivityHome.this, "GET_DATA").execute(restURL, requestObject.toString());
    }

    public void openProfile(long id, int myItemInt) {

        JSONArray jsonMainNode = jsonResponse.optJSONArray("hits");
        JSONObject jsonMainNode1 = null;
        JSONObject jsonchildNode = null;
        try {
            jsonMainNode1 = jsonMainNode.getJSONObject(myItemInt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonchildNode = jsonMainNode1 != null ? jsonMainNode1.getJSONObject("fields") : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String item_Name = jsonchildNode != null ? jsonchildNode.optString("item_name") : null;
        String brand_Name = jsonchildNode != null ? jsonchildNode.optString("brand_name") : null;
        String _calories = jsonchildNode != null ? jsonchildNode.optString("nf_calories") : null;
        String serving_Size = jsonchildNode != null ? jsonchildNode.optString("nf_serving_size_qty") : null;
        String serving_Size_unit = jsonchildNode != null ? jsonchildNode.optString("nf_serving_size_unit") : null;

        Intent intent = new Intent(this, HMDailyActivity.class);
        Uri uri = Uri.parse(HMDataProvider.CONTENT_URI + "/" + id);
        Bundle extras = new Bundle();
        intent.putExtra(HMDataProvider.CONTENT_ITEM_TYPE, uri);
        extras.putString("itemName", item_Name);
        extras.putString("brandName", brand_Name);
        extras.putString("calories", _calories);
        extras.putString("servingSize", serving_Size);
        extras.putString("servingSize_unit", serving_Size_unit);
        extras.putString("user_Id", String.valueOf(userId));
        intent.putExtras(extras);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    public JSONObject getJSONObject(String food, String fromValue, String toValue) {
        String data = "{" +
                " \"appId\":\"e7d9dedf\",\"appKey\":\"889754945d3653c1c1e7c42af16d22ae\",\"fields\": [\"item_name\",\"brand_name\",\"nf_calories\",\"nf_sodium\",\"nf_serving_size_qty\",\"nf_serving_size_unit\",\"item_type\"" +
                "  ],\"query\":\"" + food + "\",\"filters\": {\"nf_calories\": {\"from\": \"" + fromValue + "\",\"to\":\"" + toValue + "\"}}}";
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    /**
     *
     */
    public void show() {
        list = (ListView) findViewById(android.R.id.list);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, itemList);
        list.setAdapter(adapter);

    }

    @Override
    public void getPostResult(String response, String requestCode, int responseCode) {
        progressBar_home.setVisibility(View.GONE);
        if (response != null && !response.isEmpty()) {
            if ("GET_DATA".equals(requestCode)) {
                try {
                    jsonResponse = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonMainNode = jsonResponse.optJSONArray("hits");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonMainNode1 = null;
                    JSONObject jsonchildNode = null;
                    try {
                        jsonMainNode1 = jsonMainNode.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonchildNode = jsonMainNode1 != null ? jsonMainNode1.getJSONObject("fields") : null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String itemName = jsonchildNode != null ? jsonchildNode.optString("item_name") : null;
                    String brandName = jsonchildNode != null ? jsonchildNode.optString("brand_name") : null;
                    String calories = jsonchildNode != null ? jsonchildNode.optString("nf_calories") : null;
                    String servingSize = jsonchildNode != null ? jsonchildNode.optString("nf_serving_size_qty") : null;
                    String servingSize_unit = jsonchildNode != null ? jsonchildNode.optString("nf_serving_size_unit") : null;
                    itemList.add("Item Name : " + itemName + System.getProperty("line.separator") +
                            "Brand : " + brandName + System.getProperty("line.separator") +
                            "Serving :" + servingSize + " " + servingSize_unit + System.getProperty("line.separator") +
                            "Calories : " + calories + System.getProperty("line.separator"));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
