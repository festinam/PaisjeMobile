package com.example.bookmanagerapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
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

    private static final String CHANNEL_ID = "welcome_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Krijo kanalin e njoftimit
        createNotificationChannel();

        // Shfaq njoftimin mirëseardhës
        showWelcomeNotification();

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
        getMenuInflater().inflate(R.menu.home_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menuSearch);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null) {
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
        } else {
            Log.e("SearchViewError", "Search View is null");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void searchBooks(String query) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.searchBooks(query);
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
            }

        } finally {
            if (cursor != null) cursor.close();
        }

        booksAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "WelcomeChannel";
            String description = "Channel for welcome notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showWelcomeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Welcome to Book Manager!")
                .setContentText("Start organizing your books today!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
}
