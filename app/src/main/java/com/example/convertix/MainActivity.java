package com.example.convertix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        loadImagesFragment();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if(itemId == R.id.bottom_menu_image)
                {
                    loadImagesFragment();
                }
                else if(itemId == R.id.bottom_menu_pdf)
                {
                    loadPDFFragment();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Invalid Click", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }


    private void loadImagesFragment() {
        setTitle("Images");

        ImageListFragment imageListFragment = new ImageListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, imageListFragment, "ImageListFragment");
        fragmentTransaction.commit();
    }

    private void loadPDFFragment() {
        setTitle("PDFs");

        pdfListFragment pdfListFragment = new pdfListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, pdfListFragment, "PdfListFragment");
        fragmentTransaction.commit();
    }
}