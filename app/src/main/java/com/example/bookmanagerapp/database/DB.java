package com.example.bookmanagerapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    // Emri dhe versioni i databazës
    private static final String DATABASE_NAME = "bookmanager.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela e përdoruesve
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_SURNAME = "surname";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";



    // Tabela e librave
    private static final String TABLE_BOOKS = "books";
    private static final String COLUMN_BOOK_ID = "id";
    private static final String COLUMN_BOOK_TITLE = "title";
    private static final String COLUMN_BOOK_AUTHOR = "author";
    private static final String COLUMN_BOOK_DESCRIPTION = "description";
    private static final String COLUMN_BOOK_RATING = "rating";
    private static final String COLUMN_BOOK_IMAGE = "imageResource";
    private static final String COLUMN_BOOK_READ_DATE = "read_date";

    // Konstruktor
    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Krijimi i tabelës së perdorueseve
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT NOT NULL, " +
                COLUMN_USER_SURNAME + " TEXT NOT NULL, " +
                COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL" +")";
        db.execSQL(CREATE_USERS_TABLE);



        // Krijimi i tabelës së librave
        String CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_BOOKS + "(" +
                COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOK_TITLE + " TEXT NOT NULL, " +
                COLUMN_BOOK_DESCRIPTION + " INTEGER, " + // New column for image resource
                COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, " +
                COLUMN_BOOK_RATING + " REAL, " +
                COLUMN_BOOK_IMAGE + " INTEGER, " + // New column for image resource
                COLUMN_BOOK_READ_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_BOOKS_TABLE);
    }

    // Metoda për shtimin e një përdoruesi
    public long addUser(String name, String surname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_SURNAME, surname);
        values.put(COLUMN_USER_EMAIL, email.toLowerCase());
        values.put(COLUMN_USER_PASSWORD, password);

        long userId = db.insert(TABLE_USERS, null, values);
        db.close();
        if (userId == -1) {
            // Handle insertion failure
            return -1;
        }
        return userId;
    }

    //Update user


    // Verifikimi i kredencialeve të përdoruesit
    public boolean verifyUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=? AND " +
                COLUMN_USER_PASSWORD + "=?", new String[]{email, password}, null, null, null);

        boolean userExists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return userExists;
    }


    // Metoda për shtimin e një libri
    public long addBook(String title, String author, String description, float rating, int imageResource) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE, title);
        values.put(COLUMN_BOOK_AUTHOR, author);
        values.put(COLUMN_BOOK_DESCRIPTION, description);
        values.put(COLUMN_BOOK_RATING, rating);
        values.put(COLUMN_BOOK_IMAGE, imageResource);


        long bookId = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return bookId;
    }

    // Metoda për marrjen e të gjithë librave
    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
    }

    // Metoda për përditësimin e një libri
    public void updateBook(int id, String title, String author, String description, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE, title);
        values.put(COLUMN_BOOK_AUTHOR, author);
        values.put(COLUMN_BOOK_DESCRIPTION, description);
        values.put(COLUMN_BOOK_RATING, rating);

        db.update(TABLE_BOOKS, values, COLUMN_BOOK_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Metoda për fshirjen e një libri
    public void deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, COLUMN_BOOK_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to check if the email already exists in the database
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean emailExists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return emailExists;
    }


    public boolean insertBook(String title, String author, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("author", author);
        contentValues.put("rating", rating);

        long result = db.insert("books", null, contentValues);
        return result != -1; // Return true if insert is successful
    }

    @SuppressLint("Range")
    public String getHashedPasswordForUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM users WHERE email = ?", new String[]{email.toLowerCase()});

        String hashedPassword = null;
        if (cursor.moveToFirst()) {
            hashedPassword = cursor.getString(cursor.getColumnIndex("password"));
        }
        cursor.close(); // Ensure the cursor is closed
        db.close();
        return hashedPassword;
    }


    public Cursor searchBooks(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {"%" + query + "%"};
        return db.query("books", null, "title LIKE ? OR author LIKE ?", selectionArgs, null, null, null);
    }
}
