package com.maya.kliksoftapp1;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import java.util.List;
import java.util.ArrayList;

import android.text.TextWatcher;
import android.util.Log;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextLogin, editTextPassword;
    private Dialog settingsDialog;
    private EditText searchBox;
    private GridLayout gridLayoutContainer;
    private List<Integer> cartProductIds = new ArrayList<>();
    private Dialog shoppingCartDialog;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        res = getResources();

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
                productDetails.setText(String.format(res.getString(R.string.productDisplay), product.getProductName(), product.getProductPrice()));
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

    private List<Product> getSampleProducts() {
        ArrayList<Product> prodList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT rowid, name, description, price FROM products";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() < 1) {
            return null;
        }
        while (cursor.moveToNext()) {
            Log.d("DATA FROM THE DATABASE", String.format("%s; %s; %d", cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3))));
            Product product = new Product(
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(0))
            );
            prodList.add(product);
        }
        cursor.close();
        return prodList;
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
            userNameTextView.setText(R.string.unknown_user);
        }
    }

    public void onBackClick(View view) {
        setContentView(R.layout.content_main);
        initializeViews();
        onSearch(view);
    }
    public boolean RenderProducts(String searchString) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT rowid, name, description, price FROM products WHERE name MATCH ? OR description MATCH ?";
        Cursor cursor;
        if (searchString.isEmpty()) {
            query = "SELECT rowid, name, description, price FROM products";
            cursor = db.rawQuery(query, null);
            Log.d("ROWS", "" + cursor.getCount());
        } else {
            cursor = db.rawQuery(query, new String[]{searchString, searchString});
        }

        int i = 0;
        while (cursor.moveToNext()) {
            Log.d("DATA FROM THE DATABASE", String.format("%s; %s; %d", cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3))));
            Product product = new Product(
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(0))
            );
            // zrobbic takie do automatycznego ten no products.add(product.getProductName());

            //databaseHelper.addProduct(product.productName, "Opis "+i, 120);
            // GENERATOR image

            // Generate product image
            String imageName = "zdjecie" + (product.getId() - 1);  // Image name pattern
            Log.d("PRODUCT ID", ""+product.getId());

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
            addToCartButton.setText(R.string.add_to_cart);
            addToCartButton.setTextColor(getResources().getColor(R.color.black));  // Assuming the color is defined in your colors.xml
            addToCartButton.setPadding(8, 8, 8, 8);
            addToCartButton.setOnClickListener(view -> {
                cartProductIds.add(product.getId());  // Assuming cartProductIds is properly initialized
                Toast.makeText(this, String.format(res.getString(R.string.addedToCart), product.getProductName()), Toast.LENGTH_SHORT).show();
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
            priceText.setText(String.format(res.getString(R.string.cena_display), product.getProductPrice()));
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
            i++;
        }
        cursor.close();
        db.close();
        return i > 0;
    }

    /** Is used by both the search box and button, depends on the input string in the search box */
    public void onSearch(View view) {
        String query = searchBox.getText().toString();
        gridLayoutContainer.removeAllViews(); // empty the container for search results
        boolean hasAnyResults = RenderProducts(query);
        if (!hasAnyResults) {
            TextView noResultsView = new TextView(this);
            noResultsView.setText(R.string.noResultsText);
            gridLayoutContainer.addView(noResultsView);
        }
    }

    public void initializeViews() {
        searchBox = findViewById(R.id.searchBar);
        gridLayoutContainer = findViewById(R.id.products);
    }
    public void onLoginClick(View view) {
        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        if (databaseHelper.checkUser(username, password)) {
            int userId = databaseHelper.getUserId(username);

            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            sharedPreferences.edit().putInt("USER_ID", userId).apply();

            setContentView(R.layout.content_main); // Load main content layout
            initializeViews();

            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    onSearch(view);
                }
            });
            findViewById(R.id.searchButton).setOnClickListener(this::onSearch);

            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            Log.d("SEARCH STRING EMPTY?", "" + searchBox.getText().toString().isEmpty());
            RenderProducts(searchBox.getText().toString());
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private Product getProductById(List<Product> products, int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT rowid, name, description, price FROM products WHERE rowid=?";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() < 1) {
            return null;
        }
        Product prod = new Product(
            cursor.getString(1),
            cursor.getString(2),
            Integer.parseInt(cursor.getString(3)),
            Integer.parseInt(cursor.getString(0))
        );
        cursor.close();
        return prod;
    }
}
