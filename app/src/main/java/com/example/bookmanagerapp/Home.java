package com.example.bookmanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;


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
        booksRecyclerView = findViewById(R.id.bookListRecyclerView); // Ensure correct RecyclerView ID
        fabAddBook = findViewById(R.id.fabAddBook);

        // Initialize database helper and book list
        dbHelper = new DB(this);
        bookList = new ArrayList<>();

        // Set up RecyclerView
        booksAdapter = new BooksAdapter(bookList);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerView.setAdapter(booksAdapter);

        // Set the item click listener
        booksAdapter.setOnBookClickListener(book -> {
            // Navigate to book detail/edit page
            Intent intent = new Intent(Home.this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        });

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
        // Inflate menu resource file for toolbar
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuLogout) {
            // Navigate to Login Activity
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish(); // Close the Home activity
            return true;
        }
        if (item.getItemId() == R.id.menuSearch) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Search books...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchBooks(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchBooks(newText);
                    return true;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

<<<<<<< HEAD
    /**
     * Load books from the database into the RecyclerView.
     */
=======
    private void searchBooks(String query) {
        Cursor cursor = dbHelper.searchBooks(query); // Implement this method in DB to search by title or author
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
            Toast.makeText(this, "No books found matching \"" + query + "\"", Toast.LENGTH_SHORT).show();
        }
    }

>>>>>>> ce66018933bba4767eb78a551a93648a3f4de56f
    private void loadBooks() {
        Cursor cursor = dbHelper.getAllBooks();
        if (cursor != null && cursor.moveToFirst()) {
            bookList.clear();
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                bookList.add(new Book(title, author, rating));
            } while (cursor.moveToNext());
            booksAdapter.notifyDataSetChanged();
            cursor.close();
        } else {
            // Display empty state if no books found
            Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show();
            // Optionally, show an empty state layout or image
            findViewById(R.id.emptyStateView).setVisibility(View.VISIBLE); // You can create a TextView or ImageView for empty state
        }
    }

}

