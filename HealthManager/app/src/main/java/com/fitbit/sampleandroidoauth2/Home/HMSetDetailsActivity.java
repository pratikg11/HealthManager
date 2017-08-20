package com.fitbit.sampleandroidoauth2.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fitbit.sampleandroidoauth2.R;
import com.fitbit.sampleandroidoauth2.SharedPrefUtil;
import com.hanks.library.AnimateCheckBox;

public class HMSetDetailsActivity extends AppCompatActivity {
    Spinner spinner_gender, spinner_goal;
    EditText editText_weeks, editText_desiredWeight, editText_age, editText_weight, editText_height,
            editText_password, editText_name;
    SharedPrefUtil sharedPrefUtil;
    TextView textview_remember;
    AnimateCheckBox checkbox_remember;
    private String id;
    private HMUserClass user;

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public static double getBMR(String gender, int weight, int height, int age) {
        double BMR;
        if (gender.equals("Male")) {
            BMR = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            BMR = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }
        return BMR;
    }

    public static double getGoal(String target, double BMR, int desiredWeight, int currentWeight, int noOfWeeks) {
        double goal;
        if (target.equals("Gain")) {
            goal = (BMR * 1.21) + (500 * (((desiredWeight - currentWeight) * 2.20) / noOfWeeks));
        } else if (target.equals("Reduce")) {
            goal = (BMR * 1.21) - (500 * (((currentWeight - desiredWeight) * 2.20) / noOfWeeks));
        } else {
            goal = BMR * 1.21;
        }
        return goal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_detail);

        setTitle("Profile");
        this.id = getIntent().getStringExtra("user_Id");

        sharedPrefUtil = new SharedPrefUtil();
        checkbox_remember = (AnimateCheckBox) findViewById(R.id.checkbox_remember);
        textview_remember = (TextView) findViewById(R.id.textview_remember);


        editText_name = (EditText) findViewById(R.id.nameText);
        editText_password = (EditText) findViewById(R.id.passwordText);
        editText_height = (EditText) findViewById(R.id.heightText);
        editText_weight = (EditText) findViewById(R.id.weightText);
        editText_age = (EditText) findViewById(R.id.ageText);
        spinner_gender = (Spinner) findViewById(R.id.spinner);
        spinner_goal = (Spinner) findViewById(R.id.spinner2);
        editText_weeks = (EditText) findViewById(R.id.weeksText);
        editText_desiredWeight = (EditText) findViewById(R.id.desiredWeight);

        if (sharedPrefUtil.getSharedPreferenceBoolean(getApplicationContext(), "rememberMe", false)) {
            checkbox_remember.setChecked(true);
        }

        textview_remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_remember.isChecked()) {
                    checkbox_remember.setChecked(false);
                } else {
                    checkbox_remember.setChecked(true);
                }
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter);

        spinner_goal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_goal.getSelectedItem().toString().equals("Maintain")) {
//                    editText_desiredWeight.setEnabled(false);
//                    editText_weeks.setEnabled(false);
                    Enable(false);
                } else {
//                    editText_desiredWeight.setEnabled(true);
//                    editText_weeks.setEnabled(true);
                    Enable(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (this.id != null)
            user = HMUserClass.getUserFromID(Integer.parseInt(this.id), this);
        if (user != null) {
            editText_name.setText(user.getUserEmail());
            editText_password.setText(user.getUserPassword());
            editText_height.setText(String.valueOf(user.getUserHeight()));
            editText_weight.setText(String.valueOf(user.getUserWeight()));
            editText_age.setText(String.valueOf(user.getUserAge()));
            editText_desiredWeight.setText(String.valueOf(user.getuserDesiredWeight()));
            editText_weeks.setText(String.valueOf(user.getnoOfWeeks()));

            if (user.getUserTarget().toLowerCase().equals("gain")) {
                spinner_goal.setSelection(0);
                Enable(true);
            } else if (user.getUserTarget().toLowerCase().equals("reduce")) {
                Enable(true);
                spinner_goal.setSelection(1);
            } else {
                Enable(false);
                spinner_goal.setSelection(2);
            }

            if (user.getUserGender().toLowerCase().equals("male")) {
                spinner_gender.setSelection(0);
            } else {
                spinner_gender.setSelection(1);

            }
            TextView title=(TextView) findViewById(R.id.tv_title);
            title.setText("Edit Profile");
        }
    }

    private void Enable(boolean b) {
        if (b) {
            editText_weeks.setBackgroundResource(R.drawable.border);
            editText_desiredWeight.setBackgroundResource(R.drawable.border);

            editText_desiredWeight.setEnabled(true);
            editText_weeks.setEnabled(true);
        } else {
            editText_weeks.setBackgroundResource(R.drawable.border_disable);
            editText_desiredWeight.setBackgroundResource(R.drawable.border_disable);

            editText_desiredWeight.setEnabled(false);
            editText_weeks.setEnabled(false);
        }
    }

    public void AddUser(View view) {

        int desiredWeight, noOfWeeks;

        if (editText_name.getText() != null
                && editText_password.getText() != null
                && editText_height.getText() != null
                && !editText_height.getText().toString().isEmpty()
                && editText_weight.getText() != null
                && !editText_weight.getText().toString().isEmpty()
                && editText_age.getText() != null
                && !editText_age.getText().toString().isEmpty()) {

            if (spinner_goal.getSelectedItem().equals("Maintain")) {
                desiredWeight = 0;
                noOfWeeks = 0;
            } else {
                desiredWeight = Integer.parseInt(editText_desiredWeight.getText().toString());
                noOfWeeks = Integer.parseInt(editText_weeks.getText().toString());

            }

            String name = editText_name.getText().toString();
            String passwordText = editText_password.getText().toString();
            int height = Integer.parseInt(editText_height.getText().toString());
            int weight = Integer.parseInt(editText_weight.getText().toString());
            String target = spinner_goal.getSelectedItem().toString();
            int ageText = Integer.parseInt(editText_age.getText().toString());
            String gender = spinner_gender.getSelectedItem().toString();
            if (!isEmailValid(name)) {
                Toast.makeText(this, "Email is Invalid", Toast.LENGTH_SHORT).show();
            } else if (!isPasswordValid(passwordText)) {
                Toast.makeText(this, "Password is Invalid. Length of password should be atleast 6", Toast.LENGTH_SHORT).show();
            } else {
                double BMR = getBMR(gender, weight, height, ageText);
                double goal = getGoal(target, BMR, desiredWeight, weight, noOfWeeks);

                HMDatabaseHandler newDb = new HMDatabaseHandler(this);
                if (user == null) {
                    newDb.insertUser(name, HMUserClass.getEncryptedPassword(passwordText), gender, weight, height, target, ageText, desiredWeight, noOfWeeks, goal);
                    Toast.makeText(this, "User Saved", Toast.LENGTH_SHORT).show();
                    user = HMUserClass.getUserFromEmail(name, this);
                } else if (this.id != null) {
                    newDb.UpdateUser(Integer.parseInt(this.id), name, passwordText, gender, weight, height, target, ageText,
                            desiredWeight, noOfWeeks, goal);
                    Toast.makeText(this, "User Updated", Toast.LENGTH_SHORT).show();
                    user = HMUserClass.getUserFromID(Integer.parseInt(this.id), this);
                }

                if (checkbox_remember.isChecked()) {
                    sharedPrefUtil.setSharedPreferenceString(getApplicationContext(),
                            "userName", name);
                    sharedPrefUtil.setSharedPreferenceString(getApplicationContext(),
                            "password", passwordText);
                    sharedPrefUtil.setSharedPreferenceBoolean(getApplicationContext(),
                            "rememberMe", true);
                } else {
                    sharedPrefUtil.deletePreference(getApplicationContext(), "userName");
                    sharedPrefUtil.deletePreference(getApplicationContext(), "password");
                    sharedPrefUtil.deletePreference(getApplicationContext(), "rememberMe");
                }

                Intent startNewActivity = new Intent(this, HMDailyActivity.class);
                Bundle extras = new Bundle();
                extras.putString("user_Id", String.valueOf(user.getUserID()));
                startNewActivity.putExtras(extras);
                startActivity(startNewActivity);
                finish();
            }
        } else {
            Toast.makeText(this, "Failed. All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
    }
}
