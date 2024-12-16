package com.maya.kliksoftapp1;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextLogin, editTextPassword;
    private Dialog settingsDialog;
    private Dialog shoppingCartDialog;

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
        // Initialize shopping cart dialog
        shoppingCartDialog = new Dialog(this);
        shoppingCartDialog.setContentView(R.layout.shopping_carft_popup);
        shoppingCartDialog.setCancelable(true);
        // Set up the back button within the dialog to dismiss it
        settingsDialog.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.dismiss(); // Close the dialog
            }
        });
        shoppingCartDialog.findViewById(R.id.button_back_shopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingCartDialog.dismiss();
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
    public void onShoppingCartClick(View view){
        shoppingCartDialog.show(); // Show the settings popup dialog
    }
    public void onProfileClick(View view) {
        setContentView(R.layout.profile_page);

        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1); // Default to -1 if not found

        // Fetch the username from the database using the user ID
        String username = databaseHelper.getUserName(userId);

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
        GridLayout popupshoppingcart = findViewById(R.id.shopping_cart_items_layout);
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



        for (int i = 0; i < products.size(); i++) {

            Product product = getProductById(products, i);

            databaseHelper.addProduct(product.productName, "Opis "+i, 120);
            // GENERATOR image
            String imageName = "zdjecie" + (i);
            ImageView productImage = new ImageView(this);
            int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
            productImage.setImageResource(imageResource);



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


            // GENERATOR product cena
            TextView priceText = new TextView(this);
            priceText.setText(product.getProductPrice());
            priceText.setMaxWidth(550);
            priceText.setTextAppearance(R.style.productListName);
            GridLayout.LayoutParams paramsText2 = new GridLayout.LayoutParams();
            paramsText2.rowSpec = GridLayout.spec(i * 2);
            paramsText2.columnSpec = GridLayout.spec(1);
            nameText.setLayoutParams(paramsText2);
            gridLayoutContainer.addView(priceText);


            // GENERATOR product description
            TextView descriptionView = new TextView(this);
            descriptionView.setText(product.getProductDesc());
            descriptionView.setMaxWidth(500);
            descriptionView.setTextAppearance(R.style.productListDescription);
            GridLayout.LayoutParams paramsText3 = new GridLayout.LayoutParams();
            paramsText3.rowSpec = GridLayout.spec(i * 2 + 1);
            paramsText3.columnSpec = GridLayout.spec(1);
            descriptionView.setLayoutParams(paramsText3);
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
