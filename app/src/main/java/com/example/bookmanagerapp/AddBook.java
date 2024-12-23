package com.example.bookmanagerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBook extends AppCompatActivity {

    private EditText bookTitleInput, authorInput, ratingInput;
    private Button addBookButton;
    private DB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize views
        bookTitleInput = findViewById(R.id.bookTitleInput);
        authorInput = findViewById(R.id.authorInput);
        ratingInput = findViewById(R.id.ratingInput);
        addBookButton = findViewById(R.id.addBookButton);

        // Initialize database helper
        dbHelper = new DB(this);

        // Save book on button click
        addBookButton.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = bookTitleInput.getText().toString().trim();
        String author = authorInput.getText().toString().trim();
        String ratingStr = ratingInput.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float rating = Float.parseFloat(ratingStr);
            if (rating < 0 || rating > 5) {
                Toast.makeText(this, "Rating must be between 0 and 5", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = dbHelper.insertBook(title, author, rating);
            if (isInserted) {
                Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close AddBook activity
            } else {
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid rating. Please enter a valid number.", Toast.LENGTH_SHORT).show();
        }
    }
}