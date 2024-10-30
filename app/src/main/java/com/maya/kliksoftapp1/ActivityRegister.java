package com.maya.kliksoftapp1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegister extends AppCompatActivity {

    private static final String TAG = "ActivityRegister"; // Log tag for debugging
    private DatabaseHelper databaseHelper;
    private EditText editTextLogin, editTextPassword, editTextRepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Make sure the layout file is correctly referenced

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
    }

    public void onRegisterClick(View view) {
        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();

        if (validateInputs(username, password, repeatPassword)) {
            boolean isInserted = databaseHelper.addUser(username, password);
            if (isInserted) {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();

                // Log all users to Logcat
                databaseHelper.dumpUsersToLog();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean validateInputs(String username, String password, String repeatPassword) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
