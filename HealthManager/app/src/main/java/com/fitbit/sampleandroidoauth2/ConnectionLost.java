package com.fitbit.sampleandroidoauth2;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class ConnectionLost extends DialogFragment {

    String TAG = getClass().getSimpleName();
    Dialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            dialog = new Dialog(getActivity());
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.setContentView(R.layout.fragment_connectionlost);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog.show();
            Button button_ok = (Button) dialog.findViewById(R.id.button_ok);
            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {

        }
        return dialog;
    }
}
