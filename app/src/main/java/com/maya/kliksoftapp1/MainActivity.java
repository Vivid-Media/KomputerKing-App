package com.maya.kliksoftapp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextLogin, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Update with your login layout

        databaseHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

    }



    public void onNoAccClick(View view) {
        Intent intent = new Intent(this, ActivityRegister.class);
        startActivity(intent);
        finish();
    }




    public void onLoginClick(View view) {
        String username = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        if (databaseHelper.checkUser(username, password)) {
            setContentView(R.layout.content_main); // Load main content layout
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            // Check if the user exists in the database

            setContentView(R.layout.content_main); // Load main content layout
            // Find the container in content_main.xml
            GridLayout gridLayoutContainer = findViewById(R.id.products);


            for (int i = 0; i < 15; i++) {

                // Generator obrazu
                ImageView obrazek = new ImageView(this);
                obrazek.setImageResource(R.drawable.vivid_media);

// Ustawienia LayoutParams dla ImageView
                GridLayout.LayoutParams paramsObrazek = new GridLayout.LayoutParams();
                paramsObrazek.width = 775; // Szerokość w pikselach
                paramsObrazek.height = 550; // Wysokość

                paramsObrazek.rowSpec = GridLayout.spec(i * 2, 2); // Wiersz , rowspan 2
                paramsObrazek.columnSpec = GridLayout.spec(0); // nr kolumny
                paramsObrazek.setMargins(0, 0, 0, 5); // Margines dolny na 5px

                obrazek.setLayoutParams(paramsObrazek);
                gridLayoutContainer.addView(obrazek); // Dodaj obrazek do kontenera

                // Generator nazwy produktu
                TextView poletekstowe = new TextView(this);
                poletekstowe.setText("lorem ipsum nazwa produktu");
                poletekstowe.setMaxWidth(550);
                poletekstowe.setTextAppearance(R.style.listaproduktównazwa);

                // Ustawienia CSS dla nazwy produktu
                GridLayout.LayoutParams paramsText1 = new GridLayout.LayoutParams();
                paramsText1.rowSpec = GridLayout.spec(0); // Wiersz = w.ob
                paramsText1.rowSpec = GridLayout.spec(i * 2); // Kolumna wiersza =  k.ob+1
                paramsText1.height = 290;
                poletekstowe.setLayoutParams(paramsText1);

                gridLayoutContainer.addView(poletekstowe);  // Dodaj nazwe produktu do kontenera


                // Generator opisu produktu
                TextView poleopisu = new TextView(this);
                poleopisu.setText("lorem ipsum opis produktu");
                poleopisu.setMaxWidth(550);
                poleopisu.setTextAppearance(R.style.listaproduktówopis);

                // Ustawienia CSS dla opisu produktu
                GridLayout.LayoutParams paramsText2 = new GridLayout.LayoutParams();
                paramsText2.rowSpec = GridLayout.spec(i * 2 + 1); // wiersz = w.ob + 1
                paramsText2.columnSpec = GridLayout.spec(1); //Kolumna wiersza =  tylesamo

                poleopisu.setLayoutParams(paramsText2);
                gridLayoutContainer.addView(poleopisu); // Dodaj opis produktu do kontenera


            }


            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    }




