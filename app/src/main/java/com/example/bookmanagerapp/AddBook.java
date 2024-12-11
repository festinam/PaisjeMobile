package com.example.bookmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBook extends AppCompatActivity {

    private EditText etTitle, etAuthor, etRating;
    private Button btnSave;
    private DB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize views
        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etRating = findViewById(R.id.etRating);
        btnSave = findViewById(R.id.btnSave);

        // Initialize database helper
        dbHelper = new DB(this);

        // Save book on button click
        btnSave.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float rating = Float.parseFloat(ratingStr);
            if (dbHelper.insertBook(title, author, rating)) {
                Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close AddBook activity
            } else {
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid rating", Toast.LENGTH_SHORT).show();
        }
    }
}
