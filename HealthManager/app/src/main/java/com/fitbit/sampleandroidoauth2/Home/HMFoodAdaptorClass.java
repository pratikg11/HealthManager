package com.fitbit.sampleandroidoauth2.Home;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.fitbit.sampleandroidoauth2.R;

/**
 * Created by pratik on 7/30/2017.
 */

public class HMFoodAdaptorClass extends CursorAdapter {
    public HMFoodAdaptorClass(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(
                R.layout.activity_list_of_food, viewGroup, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String foodName = cursor.getString(
                cursor.getColumnIndex(HMDatabaseHandler.FOOD_COLUMN_NAME)
        );
        String calories = cursor.getString(
                cursor.getColumnIndex(HMDatabaseHandler.FOOD_COLUMN_CALORIES)
        );
        TextView tv1 = (TextView) view.findViewById(R.id.tv_returnedFoodName);
        tv1.setText(foodName);
        TextView tv2 = (TextView) view.findViewById(R.id.tv_returnedCalories);
        tv2.setText(calories);
    }
}
