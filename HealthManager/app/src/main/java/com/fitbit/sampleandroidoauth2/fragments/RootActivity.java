package com.fitbit.sampleandroidoauth2.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fitbit.authentication.AuthenticationHandler;
import com.fitbit.authentication.AuthenticationManager;
import com.fitbit.authentication.AuthenticationResult;
import com.fitbit.authentication.Scope;
import com.fitbit.sampleandroidoauth2.R;

import java.util.Set;


public class RootActivity extends AppCompatActivity implements AuthenticationHandler,
        ActivitiesFragment.GetCal {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        Toast.makeText(this, "Please wait. Processing!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "AuthenticationManager.isLoggedIn(): " + AuthenticationManager.isLoggedIn());
        if (AuthenticationManager.isLoggedIn()) {
            onLoggedIn();
        } else {
            AuthenticationManager.login(this);
        }
    }

    public void onLoggedIn() {
        Log.e("TAG", "onLoggedin");
        ActivitiesFragment fragment = new ActivitiesFragment();
        fragment.getCal = RootActivity.this;
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.root_main, fragment, "Hello");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onLoginClick(View view) {
        AuthenticationManager.login(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!AuthenticationManager.onActivityResult(requestCode, resultCode, data, this)) {
            Log.e("TAG","DATA: "+data);
        }

    }


    public void onAuthFinished(AuthenticationResult authenticationResult) {
//        binding.setLoading(false);
        Log.e("TAG", "onAuthFinished:  "+authenticationResult.isSuccessful());
        if (authenticationResult.isSuccessful()) {
            Log.e("TAG", "onAuthFinished");
            onLoggedIn();
        } else {
            displayAuthError(authenticationResult);
        }
    }

    private void displayAuthError(AuthenticationResult authenticationResult) {
        String message = "";
        Log.e("TAG", "authenticationResult.getStatus(): "+authenticationResult.getStatus());
        switch (authenticationResult.getStatus()) {
            case dismissed:
                message = getString(R.string.login_dismissed);
                break;
            case error:
                message = authenticationResult.getErrorMessage();
                break;
            case missing_required_scopes:
                Set<Scope> missingScopes = authenticationResult.getMissingScopes();
                String missingScopesText = TextUtils.join(", ", missingScopes);
                message = getString(R.string.missing_scopes_error) + missingScopesText;
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.login_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }

    @Override
    public void calCount(String data) {
        Log.e("TAG", "in Root calCount: " + data);
        Intent output = new Intent();
        output.putExtra("cal", data);
        setResult(RESULT_OK, output);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}