package com.example.bookmanagerapp;

public class Book {
    private int id;
    private String title;
    private String author;
    private float rating;

    public Book(int id, String title, String author, float rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public float getRating() {
        return rating;
    }
}
