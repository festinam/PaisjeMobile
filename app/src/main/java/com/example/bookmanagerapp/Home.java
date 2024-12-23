package com.example.bookmanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView booksRecyclerView;
    private TextView emptyStateView;
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
        emptyStateView = findViewById(R.id.emptyStateView);
        fabAddBook = findViewById(R.id.fabAddBook);

        // Initialize database helper and book list
        dbHelper = new DB(this);
        bookList = new ArrayList<>();

        // Set up RecyclerView
        booksAdapter = new BooksAdapter(bookList);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerView.setAdapter(booksAdapter);

        updateEmptyState();

        // Set the item click listener for RecyclerView
        booksAdapter.setOnBookClickListener(book -> {
            Toast.makeText(Home.this, "Clicked on: " + book.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // Load books from the database
        loadBooks();

        // Handle FloatingActionButton click
        fabAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, AddBook.class);
            startActivity(intent);
        });
    }

    private void updateEmptyState() {
        if (bookList.isEmpty()) {
            emptyStateView.setVisibility(View.VISIBLE);
            booksRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateView.setVisibility(View.GONE);
            booksRecyclerView.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file for toolbar
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
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

    private void searchBooks(String query) {
        Cursor cursor = dbHelper.searchBooks(query); // Assuming this method exists in your DB class
        if (cursor != null && cursor.moveToFirst()) {
            bookList.clear();
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));

                bookList.add(new Book(id, title, author, rating)); // Pass the ID
            } while (cursor.moveToNext());

            booksAdapter.notifyDataSetChanged();
            cursor.close();

            // Show RecyclerView and hide empty state view
            booksRecyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.emptyStateContainer).setVisibility(View.GONE);
        } else {
            // Show empty state view when no results found
            bookList.clear();
            booksAdapter.notifyDataSetChanged();
            booksRecyclerView.setVisibility(View.GONE);
            findViewById(R.id.emptyStateContainer).setVisibility(View.VISIBLE);
        }
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

                bookList.add(new Book(id, title, author, rating)); // Pass the ID
            } while (cursor.moveToNext());

            booksAdapter.notifyDataSetChanged();
            cursor.close();

            // Show RecyclerView and hide empty state view
            booksRecyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.emptyStateContainer).setVisibility(View.GONE);
        } else {
            // Show empty state view when no books found
            bookList.clear();
            booksAdapter.notifyDataSetChanged();
            booksRecyclerView.setVisibility(View.GONE);
            findViewById(R.id.emptyStateContainer).setVisibility(View.VISIBLE);
        }

    }
}