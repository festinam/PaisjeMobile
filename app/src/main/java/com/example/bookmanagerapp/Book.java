package com.example.bookmanagerapp;

public class Book {
    private int id; // Unique identifier for the book
    private String title;
    private String author;
    private float rating;

    // Constructor with ID
    public Book(int id, String title, String author, float rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    // Constructor without ID (for creating new books without pre-existing IDs)
    public Book(String title, String author, float rating) {
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    // Getter and Setter for ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public float getRating() {
        return rating;
    }

    // Setter methods (optional, for updating book details)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}