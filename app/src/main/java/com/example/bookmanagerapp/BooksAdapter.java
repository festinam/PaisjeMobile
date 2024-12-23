package com.example.bookmanagerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private List<Book> bookList;
    private OnBookClickListener onBookClickListener;

    // Constructor
    public BooksAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    // Interface for click listener
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    // Setter for the click listener
    public void setOnBookClickListener(OnBookClickListener listener) {
        this.onBookClickListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.ratingTextView.setText(String.format("⭐ %.1f", book.getRating()));

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (onBookClickListener != null) {
                onBookClickListener.onBookClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, ratingTextView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
        }
    }
}