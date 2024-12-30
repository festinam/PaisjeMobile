package com.example.bookmanagerapp;

public class Book {
    private int id; // Unique identifier for the book
    private String title; // Title of the book
    private String author; // Author of the book
    private String description; // Description of the book
    private float rating; // Rating of the book
    private int imageResource; // Resource ID for the book image

    // Constructor with ID
    public Book(int id, String title, String author, String description, float rating, int imageResource) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.rating = rating;
        this.imageResource = imageResource;
    }

    // Constructor without ID (for new books without pre-existing IDs)
    public Book(String title, String author, String description, float rating, int imageResource) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.rating = rating;
        this.imageResource = imageResource;
    }

    // Getter and Setter for ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for Author
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // Getter and Setter for Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for Rating
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    // Getter and Setter for Image Resource
    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
