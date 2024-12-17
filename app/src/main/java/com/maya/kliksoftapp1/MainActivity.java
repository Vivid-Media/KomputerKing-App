package com.maya.kliksoftapp1;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextLogin, editTextPassword;
    private List<Integer> cartProductIds = new ArrayList<>();

    private Dialog settingsDialog, shoppingCartDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Initialize dialogs
        setupSettingsDialog();
        setupShoppingCartDialog();
    }

    // Setup the Settings Dialog
    private void setupSettingsDialog() {
        settingsDialog = new Dialog(this);
        settingsDialog.setContentView(R.layout.popup_layout);
        settingsDialog.setCancelable(true);
        settingsDialog.findViewById(R.id.button_back).setOnClickListener(view -> settingsDialog.dismiss());
    }

    // Setup the Shopping Cart Dialog
    private void setupShoppingCartDialog() {
        shoppingCartDialog = new Dialog(this);
        shoppingCartDialog.setContentView(R.layout.shopping_carft_popup);
        shoppingCartDialog.setCancelable(true);
        shoppingCartDialog.findViewById(R.id.button_back_shopping).setOnClickListener(view -> shoppingCartDialog.dismiss());
    }

    public void onNoAccClick(View view) {
        startActivity(new Intent(this, ActivityRegister.class));
        finish();
    }

    public void onSettingsClick(View view) {
        settingsDialog.show();
    }

    public void onShoppingCartClick(View view) {
        shoppingCartDialog.show();

        // Access the Dialog's layout
        Window window = shoppingCartDialog.getWindow();
        if (window == null) return;

        // Setup a ScrollView containing a GridLayout for cart items
        ScrollView scrollView = new ScrollView(this);
        LinearLayout cartContainer = new LinearLayout(this);
        cartContainer.setOrientation(LinearLayout.VERTICAL);

        scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.MATCH_PARENT  // Height
        ));

        // Create a GridLayout for the product items
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(2);  // Two columns
        gridLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add the gridLayout to the container
        cartContainer.addView(gridLayout);

        // Display all cart products
        for (int productId : cartProductIds) {
            Product product = getProductById(getSampleProducts(), productId);
            if (product != null) {
                // Product Image
                ImageView productImage = new ImageView(this);
                int imageRes = getResources().getIdentifier("zdjecie" + product.getId(), "drawable", getPackageName());
                productImage.setImageResource(imageRes);
                productImage.setLayoutParams(new GridLayout.LayoutParams());
                productImage.setLayoutParams(new LinearLayout.LayoutParams(300, 300)); // Fixed size

                // Product Details
                TextView productDetails = new TextView(this);
                productDetails.setText(product.getProductName() + "\nPrice: " + product.getProductPrice());
                productDetails.setPadding(16, 8, 16, 8);

                // Add the product image and details to the GridLayout
                gridLayout.addView(productImage);
                gridLayout.addView(productDetails);
            }
        }

        // Set the ScrollView as the Dialog content
        scrollView.addView(cartContainer);
        window.setContentView(scrollView);
    }




    public void onProfileClick(View view) {
        setContentView(R.layout.profile_page);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        TextView userNameTextView = findViewById(R.id.userName);
        String username = databaseHelper.getUserName(userId);

        if (username != null) {
            userNameTextView.setText(username);
        } else {
            userNameTextView.setText("Unknown User");
        }
    }

    public void onBackClick(View view) {
        setContentView(R.layout.content_main);
        createProducts();
    }

    public void createProducts() {
        GridLayout gridLayoutContainer = findViewById(R.id.products);

        // Creating sample products
        Product product1 = new Product("Komputer 4k rtx 4024", "Dobry komputer do gier uwu", 500, 0);
        Product product2 = new Product("laptop 2k rtx 404", "Dobry laptop uwu", 300, 1);
        Product product3 = new Product("telefon HD intelcore 2", "Dobry telefon", 300, 2);
        Product product4 = new Product("tablet 3k gtx 1090px", "Dobry tablet", 500, 3);
        Product product5 = new Product("telewizor 12k LG", "Tv 12k firmy lg", 5000, 4);

        // Adding products to the list
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);

        // Loop to add each product
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);  // Fetch each product

            // Generate product image
            String imageName = "zdjecie" + (i);  // Image name pattern
            ImageView productImage = new ImageView(this);
            int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
            productImage.setImageResource(imageResource);

            // Setting ImageView layout parameters
            GridLayout.LayoutParams paramsImage = new GridLayout.LayoutParams();
            paramsImage.width = 300;  // Width in pixels
            paramsImage.height = 300; // Height
            paramsImage.rowSpec = GridLayout.spec(i * 5);  // Each product starts from a new row
            paramsImage.columnSpec = GridLayout.spec(0);  // First column
            paramsImage.setMargins(0, 0, 0, 2); // Bottom margin
            productImage.setLayoutParams(paramsImage);
            gridLayoutContainer.addView(productImage);

            // Add to Cart Button
            TextView addToCartButton = new TextView(this);
            addToCartButton.setText("Add to Cart");
            addToCartButton.setTextColor(getResources().getColor(R.color.black));  // Assuming the color is defined in your colors.xml
            addToCartButton.setPadding(8, 8, 8, 8);
            addToCartButton.setOnClickListener(view -> {
                cartProductIds.add(product.getId());  // Assuming cartProductIds is properly initialized
                Toast.makeText(this, product.getProductName() + " added to cart!", Toast.LENGTH_SHORT).show();
            });
            GridLayout.LayoutParams paramsButton = new GridLayout.LayoutParams();
            paramsButton.rowSpec = GridLayout.spec(i * 5 + 1);  // Button directly under the image (next row)
            paramsButton.columnSpec = GridLayout.spec(0);  // First column
            addToCartButton.setLayoutParams(paramsButton);
            gridLayoutContainer.addView(addToCartButton);

            // Product name TextView
            TextView nameText = new TextView(this);
            nameText.setText(product.getProductName());
            nameText.setMaxWidth(550);
            nameText.setTextAppearance(R.style.productListName);  // Assuming you have a style for product names
            GridLayout.LayoutParams paramsText1 = new GridLayout.LayoutParams();
            paramsText1.rowSpec = GridLayout.spec(i * 5 + 2);  // Row below the button
            paramsText1.columnSpec = GridLayout.spec(1);  // First column
            nameText.setLayoutParams(paramsText1);
            gridLayoutContainer.addView(nameText);

            // Product price TextView
            TextView priceText = new TextView(this);
            priceText.setText("Cena " + product.getProductPrice() + " zł");
            priceText.setMaxWidth(550);
            priceText.setTextAppearance(R.style.productListName);  // Assuming you have a style for price
            GridLayout.LayoutParams paramsText2 = new GridLayout.LayoutParams();
            paramsText2.rowSpec = GridLayout.spec(i * 5 + 3);  // Row below the name
            paramsText2.columnSpec = GridLayout.spec(1);  // First column
            priceText.setLayoutParams(paramsText2);
            gridLayoutContainer.addView(priceText);

            // Product description TextView
            TextView descriptionView = new TextView(this);
            descriptionView.setText(product.getProductDesc());
            descriptionView.setMaxWidth(500);
            descriptionView.setTextAppearance(R.style.productListDescription);  // Assuming you have a style for description
            GridLayout.LayoutParams paramsText3 = new GridLayout.LayoutParams();
            paramsText3.rowSpec = GridLayout.spec(i * 5 + 4);  // Row below the price
            paramsText3.columnSpec = GridLayout.spec(1);  // First column
            descriptionView.setLayoutParams(paramsText3);
            gridLayoutContainer.addView(descriptionView);
        }
    }




    public void onLoginClick(View view) {
        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        if (databaseHelper.checkUser(username, password)) {
            int userId = databaseHelper.getUserId(username);

            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            sharedPreferences.edit().putInt("USER_ID", userId).apply();

            setContentView(R.layout.content_main);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            createProducts();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Komputer 4k RTX 4024", "Gaming PC", 500, 0));
        products.add(new Product("Laptop 2k RTX 404", "Good laptop", 300, 1));
        products.add(new Product("Telefon HD IntelCore 2", "Good phone", 300, 2));
        products.add(new Product("Tablet 3k GTX 1090px", "Good tablet", 500, 3));
        products.add(new Product("Telewizor 12k LG", "12K TV by LG", 5000, 4));
        return products;
    }

    private Product getProductById(List<Product> products, int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
}
