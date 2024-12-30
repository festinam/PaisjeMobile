package com.example.bookmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookmanagerapp.ui.Login;
import com.example.bookmanagerapp.ui.Welcome;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this); // Edge-to-edge support (optional)
        setContentView(R.layout.activity_main);

        // Set window insets to ensure proper layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Redirect to the Welcome page after loading MainActivity
        Intent intent = new Intent(MainActivity.this, Welcome.class);
        startActivity(intent);
        finish(); // Finish MainActivity so it doesn't remain in the back stack
    }

    // Inflate the menu; this adds items to the action bar if it's present
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); // Use the menu_main.xml
        return true;
    }

    // Handle item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle logout action
        if (item.getItemId() == R.id.menuLogout) {
            Intent intent = new Intent(MainActivity.this, Login.class); // Redirect to Login activity
            startActivity(intent);
            finish(); // Close current activity to prevent going back to it
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
