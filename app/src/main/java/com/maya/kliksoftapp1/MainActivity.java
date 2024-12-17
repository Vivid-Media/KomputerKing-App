package com.maya.kliksoftapp1;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import java.util.List;
import java.util.ArrayList;

import android.text.TextWatcher;
import android.util.Log;
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
    private EditText searchBox;
    private GridLayout gridLayoutContainer;

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

    public void onSettingsClick(View view){
        settingsDialog.show(); // Show the settings popup dialog
    }
    public void onProfileClick(View view){
        setContentView(R.layout.profile_page);
    }

    public void onBackClick(View view){
        setContentView(R.layout.content_main);
        //searchBox = findViewById(R.id.searchBar);
        RenderProducts(searchBox.getText().toString());
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
            ImageView productImage = new ImageView(this);
            productImage.setImageResource(R.drawable.vivid_media);

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

    public void onLoginClick(View view) {
        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        if (databaseHelper.checkUser(username, password)) {
            setContentView(R.layout.content_main); // Load main content layout
            searchBox = findViewById(R.id.searchBar);
            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) { onSearch(view); }
            });
            findViewById(R.id.searchButton).setOnClickListener(this::onSearch);
            gridLayoutContainer = findViewById(R.id.products);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            Log.d("SEARCH STRING EMPTY?", "" + searchBox.getText().toString().isEmpty());
            RenderProducts(searchBox.getText().toString());
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
