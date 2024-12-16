package com.maya.kliksoftapp1;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import java.util.List;
import java.util.ArrayList;
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
            }
        });
    }

    public void onNoAccClick(View view) {
        Intent intent = new Intent(this, ActivityRegister.class);
        startActivity(intent);
        finish();
    }
    public void displayAccountName(View view){
        int userId = 1;
        String username = databaseHelper.getUserName(userId);
    }

    public void onSettingsClick(View view){
        settingsDialog.show(); // Show the settings popup dialog
    }
    public void onProfileClick(View view) {
        // Load the profile page layout
        setContentView(R.layout.profile_page);

        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1); // Default to -1 if not found

        // Fetch the username from the database using the user ID
        String username = databaseHelper.getUserName(userId);

        // Find the TextView in the profile_page.xml layout and set the username
        TextView userNameTextView = findViewById(R.id.userName);
        if (username != null) {
            userNameTextView.setText(username);
        } else {
            userNameTextView.setText("Unknown User");
        }
    }



    public void onBackClick(View view){
        setContentView(R.layout.content_main);
        CreateProducts();
    }
    

    public void CreateProducts(){
        GridLayout gridLayoutContainer = findViewById(R.id.products);
        Product product1 = new Product("Komputer 4k rtx 4024", "Dobry komputer do gier uwu", 500, 0);
        Product product2 = new Product("laptop 2k rtx 404", "Dobry laptop uwu", 300, 1);
        Product product3 = new Product("telefon HD intelcore 2", "Dobry telefon", 300, 2);
        Product product4 = new Product("tablet 3k gtx 1090px", "Dobry tablet", 500, 3);
        Product product5 = new Product("telewizor 12k LG", "Tv 12k firmy lg", 5000, 4);


        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);

//        for (int nrproduktu = 0; nrproduktu < 4; nrproduktu++) {
//            products.add(product.getId());
//        }


        for (int i = 0; i < products.size(); i++) {
            Product product = getProductById(products, i);
            // zrobbic takie do automatycznego ten no products.add(product.getProductName());

            databaseHelper.addProduct(product.productName, "Opis "+i, 120);
            // GENERATOR image
            ImageView productImage = new ImageView(this);
            productImage.setImageResource(R.drawable.komputer);

            // CSS for ImageView
            GridLayout.LayoutParams paramsImage = new GridLayout.LayoutParams();
            paramsImage.width = 550; // Width in pixels
            paramsImage.height = 450; // Height
            paramsImage.rowSpec = GridLayout.spec(i * 2, 2); // Row span = 2
            paramsImage.columnSpec = GridLayout.spec(0); // Column number
            paramsImage.setMargins(0, 0, 0, 5); // Bottom margin 5px
            productImage.setLayoutParams(paramsImage);
            gridLayoutContainer.addView(productImage);

            // GENERATOR product name
            TextView nameText = new TextView(this);
            nameText.setText(product.getProductName());
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
            descriptionView.setText(product.getProductDesc());
            descriptionView.setMaxWidth(500);
            descriptionView.setTextAppearance(R.style.productListDescription);
            GridLayout.LayoutParams paramsText2 = new GridLayout.LayoutParams();
            paramsText2.rowSpec = GridLayout.spec(i * 2 + 1);
            paramsText2.columnSpec = GridLayout.spec(1);
            descriptionView.setLayoutParams(paramsText2);
            gridLayoutContainer.addView(descriptionView);
        }
    }
    public static Product getProductById(List<Product> products, int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;  // Jeśli nie znaleziono
    }

    public void onLoginClick(View view) {
        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        if (databaseHelper.checkUser(username, password)) {
            int userId = databaseHelper.getUserId(username);

            // Save user ID in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("USER_ID", userId);
            editor.apply();

            // Navigate to the main content
            setContentView(R.layout.content_main);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Create products or other actions
            CreateProducts();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

}
