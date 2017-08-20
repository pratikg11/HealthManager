package com.fitbit.sampleandroidoauth2.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fitbit.sampleandroidoauth2.R;

/**
 * Created by Pratik on 8/6/2017.
 */

public class HMManualAddFood extends AppCompatActivity {
    private int userId;
    private static final int EDITOR_REQUEST_CODE = 1003;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_add_food);

        setTitle("Add Food");
        Intent intent1 = getIntent();
        Bundle extras1 = intent1.getExtras();
        userId=Integer.parseInt(extras1.getString("user_Id"));

        final Intent intent = new Intent(this, HMDailyActivity.class);

        Button ManuallyAddFood = (Button) findViewById(R.id.btn_ManuallyAdd);
        ManuallyAddFood.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText et_addFood = (EditText) findViewById(R.id.ed_addFoodName);
                String foodName = et_addFood.getText().toString();
                EditText et_addCalories = (EditText) findViewById(R.id.ed_addCal);
                String Cal = et_addCalories.getText().toString();

                if(foodName.equals("")){
                    Toast.makeText(HMManualAddFood.this, "Please Enter Food Name", Toast.LENGTH_LONG).show();
                }
                else if(Cal.equals("")){
                    Toast.makeText(HMManualAddFood.this, "Please Enter Calories of the food", Toast.LENGTH_LONG).show();
                }
                else {
                    Uri uri = Uri.parse(HMDataProvider.CONTENT_URI + "/" + 1);
                    Bundle extras = new Bundle();
                    intent.putExtra(HMDataProvider.CONTENT_ITEM_TYPE, uri);
                    extras.putString("itemName", foodName);
                    extras.putString("brandName", "ManualEntry");
                    extras.putString("calories", Cal);
                    extras.putString("servingSize", "ManualEntry");
                    extras.putString("servingSize_unit", "ManualEntry");
                    extras.putString("user_Id", String.valueOf(userId));
                    intent.putExtras(extras);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                    finish();
                }
            }
        });


    }
}
