package com.example.bookmanagerapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView booksRecyclerView;
    private TextView emptyStateView;
    private FloatingActionButton fabAddBook;
    private BooksAdapter booksAdapter;
    private DB dbHelper;
    private List<Book> bookList;

    private MenuItem menuHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Book Collection");
            Log.d("ToolbarDebug", "Title set successfully.");
        }


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

        // Handle FloatingActionButton click
        fabAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, AddBook.class);
            startActivity(intent);
        });

        // Set the item click listener for RecyclerView
        booksAdapter.setOnBookClickListener(book -> {
            Toast.makeText(Home.this, "Clicked on: " + book.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load books from the database
        loadBooks();
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
        // Inflate the menu resource file.
        getMenuInflater().inflate(R.menu.home_menu, menu);

        // Find the MenuItem that triggers the SearchView
        MenuItem searchItem = menu.findItem(R.id.menuSearch);

        // Get the SearchView and check if it is null
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        // Set properties of the SearchView, if it's not null
        if (searchView != null) {
            searchView.setQueryHint("Search books...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Trigger search logic here
                    searchBooks(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Optionally handle text change for suggestions
                    searchBooks(newText);
                    return true;
                }
            });
        } else {
            Log.e("SearchViewError", "Search View is null");
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            // Log out the user and return to the Login screen
//            dbHelper.logout();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.menuSearch) {
            // Already handled in onCreateOptionsMenu
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void searchBooks(String query) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.searchBooks(query); // Assuming this method exists in your DB class
            bookList.clear();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                    int imageResource = cursor.getInt(cursor.getColumnIndexOrThrow("imageResource"));

                    bookList.add(new Book(id, title, author, description, rating, imageResource));
                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null) cursor.close();
        }

        booksAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void loadBooks() {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getAllBooks();
            bookList.clear();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                    int imageResource = cursor.getInt(cursor.getColumnIndexOrThrow("imageResource"));

                    bookList.add(new Book(id, title, author, description, rating, imageResource));
                } while (cursor.moveToNext());
            } else {
                mockBooks(); // Add mock data only if the database is empty
            }

            // Log bookList for debugging
            for (Book book : bookList) {
                System.out.println("Book: " + book.getTitle());
            }

        } finally {
            if (cursor != null) cursor.close();
        }

        booksAdapter.notifyDataSetChanged(); // Notify adapter here
        updateEmptyState();
    }

    private void mockBooks() {
//        bookList.clear(); // Clear existing data
//
//        bookList.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald",
//                "A classic novel set in the Jazz Age, exploring themes of wealth and love.",
//                4.7f, R.drawable.great_gatsby));

//        bookList.add(new Book(2, "To Kill a Mockingbird", "Harper Lee",
//                "A powerful story about racial injustice and childhood.",
//                4.8f, R.drawable.mockingbird));
//
//        bookList.add(new Book(3, "1984", "George Orwell",
//                "A dystopian novel about surveillance and totalitarianism.",
//                4.9f, R.drawable.george));
//        booksAdapter.notifyDataSetChanged(); // Notify the adapter of the changes

        dbHelper.addBook("The Great Gatsby", "F. Scott Fitzgerald", "A classic novel set in the Jazz Age, exploring themes of wealth and love.", 4.7f, R.drawable.great_gatsby);
        dbHelper.addBook("To Kill a Mockingbird", "Harper Lee",
                "A powerful story about racial injustice and childhood.",
                4.8f, R.drawable.mockingbird);
        dbHelper.addBook("1984", "George Orwell",
                "A dystopian novel about surveillance and totalitarianism.",
                4.9f, R.drawable.george);
    }
}
