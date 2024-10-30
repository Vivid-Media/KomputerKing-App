package com.maya.kliksoftapp1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText editTextLogin, editTextPassword, editTextRepeatPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
    }

    // This method is called when the Login button is clicked
    public void onLoginClick(View view) {
        EditText editTextLogin = findViewById(R.id.editTextLogin);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        // Check if the user exists in the database
        if (databaseHelper.checkUser(username, password)) {
            // Load the content_main layout if login is successful
            setContentView(R.layout.content_main);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            // Show an error message if login fails
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNoAccClick(View view){
        setContentView(R.layout.activity_register);
    }

    public void onRegisterClick(View view) {
        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();

        if (validateInputs(username, password, repeatPassword)) {
            boolean isInserted = databaseHelper.addUser(username, password);
            if (isInserted) {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Validate user inputs
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
