package com.example.bookmanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView booksRecyclerView;
    private FloatingActionButton fabAddBook;
    private BooksAdapter booksAdapter;
    private DB dbHelper;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);

        // Initialize views
        booksRecyclerView = findViewById(R.id.booksRecyclerView);
        fabAddBook = findViewById(R.id.fabAddBook);

        // Initialize database helper and book list
        dbHelper = new DB(this);
        bookList = new ArrayList<>();

        // Set up RecyclerView
        booksAdapter = new BooksAdapter(bookList);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerView.setAdapter(booksAdapter);

        // Load books from the database
        loadBooks();

        // Handle FloatingActionButton click
        fabAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, AddBook.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuLogout) {
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish(); // Close the Home activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBooks() {
        Cursor cursor = dbHelper.getAllBooks();
        if (cursor != null && cursor.moveToFirst()) {
            bookList.clear();
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));

                bookList.add(new Book(id, title, author, rating));
            } while (cursor.moveToNext());

            booksAdapter.notifyDataSetChanged();
            cursor.close();
        } else {
            Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show();
        }
    }
}
