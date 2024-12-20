package com.example.bookmanagerapp;

public class Book {
    private String title;
    private String author;
    private float rating;

    // Constructor
    public Book(String title, String author, float rating) {
        this.title = title;
        this.author = author;
        this.rating = rating;
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
