package com.fitbit.sampleandroidoauth2.Home;

/**
 * Created by pratik on 7/30/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.fitbit.sampleandroidoauth2.R;

public class HMListOfFood extends CursorAdapter {
    public HMListOfFood(Context context, Cursor c, int flags) {
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

    }
}
