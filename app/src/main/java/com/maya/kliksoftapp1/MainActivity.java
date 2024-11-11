package com.maya.kliksoftapp1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maya.kliksoftapp1.databinding.ActivityMainBinding;
import com.maya.kliksoftapp1.databinding.ContentMainBinding;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextLogin, editTextPassword;
    private Dialog settingsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Initialize the settings dialog
        settingsDialog = new Dialog(this);
        settingsDialog.setContentView(R.layout.popup_layout);
        settingsDialog.setCancelable(true); // Allows dismissing the dialog when back is pressed

        // Set up the back button within the dialog to dismiss it
        settingsDialog.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.dismiss(); // Close the dialog
                Toast.makeText(MainActivity.this, "Returning to Main App", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onNoAccClick(View view) {
        Intent intent = new Intent(this, ActivityRegister.class);
        startActivity(intent);
        finish();
    }

    public void onSettingsClick(View view){
        Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        settingsDialog.show(); // Show the settings popup dialog
    }

    public void onLoginClick(View view) {
        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        if (databaseHelper.checkUser(username, password)) {
            setContentView(R.layout.content_main); // Load main content layout
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Find the container in content_main.xml
            GridLayout gridLayoutContainer = findViewById(R.id.products);

            for (int i = 0; i < 15; i++) {
                // GENERATOR image
                ImageView productImage = new ImageView(this);
                productImage.setImageResource(R.drawable.vivid_media);

                // CSS for ImageView
                GridLayout.LayoutParams paramsImage = new GridLayout.LayoutParams();
                paramsImage.width = 750; // Width in pixels
                paramsImage.height = 550; // Height
                paramsImage.rowSpec = GridLayout.spec(i * 2, 2); // Row span = 2
                paramsImage.columnSpec = GridLayout.spec(0); // Column number
                paramsImage.setMargins(0, 0, 0, 5); // Bottom margin 5px
                productImage.setLayoutParams(paramsImage);
                gridLayoutContainer.addView(productImage);

                // GENERATOR product name
                TextView nameText = new TextView(this);
                nameText.setText("lorem ipsum nazwa produktu");
                nameText.setMaxWidth(550);
                nameText.setTextAppearance(R.style.productListName);
                GridLayout.LayoutParams paramsText1 = new GridLayout.LayoutParams();
                paramsText1.rowSpec = GridLayout.spec(i * 2);
                paramsText1.columnSpec = GridLayout.spec(1);
                paramsText1.height = 290;
                nameText.setLayoutParams(paramsText1);
                gridLayoutContainer.addView(nameText);

                // GENERATOR product description
                TextView descriptionView = new TextView(this);
                descriptionView.setText("lorem ipsum opis produktu");
                descriptionView.setMaxWidth(550);
                descriptionView.setTextAppearance(R.style.productListDescription);
                GridLayout.LayoutParams paramsText2 = new GridLayout.LayoutParams();
                paramsText2.rowSpec = GridLayout.spec(i * 2 + 1);
                paramsText2.columnSpec = GridLayout.spec(1);
                descriptionView.setLayoutParams(paramsText2);
                gridLayoutContainer.addView(descriptionView);
            }
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
