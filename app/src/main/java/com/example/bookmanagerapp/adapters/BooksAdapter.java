package com.example.bookmanagerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmanagerapp.models.Book;
import com.example.bookmanagerapp.R;

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
        void onBookClick(Book book, int position);
        void onBookLongClicked(Book book, int position);
    }

    // Setter for the click listener
    public void setOnBookClickListener(OnBookClickListener listener) {
        this.onBookClickListener = listener;
    }

    public void setOnBookLongClickedListener(OnBookClickListener listener){
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
        holder.descriptionTextView.setText(book.getDescription());
        holder.ratingTextView.setText(String.format("⭐ %.1f", book.getRating()));
        holder.bookImageView.setImageResource(book.getImageResource());


        // Set a click listener for the item (if needed)
        holder.itemView.setOnClickListener(v -> {
            if (onBookClickListener != null) {
                onBookClickListener.onBookClick(book, position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onBookClickListener != null) {
                onBookClickListener.onBookLongClicked(book, position);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list
        return bookList.size();
    }
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, descriptionTextView, ratingTextView;
        ImageView bookImageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
        }
    }


    public void deleteBook(int position) {
        bookList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bookList.size());
        // Additional deletion code such as updating the database can go here
    }

    public void updateBook(int position, String newTitle, String newAuthor, String newDescription, float newRating) {
        Book book = bookList.get(position);
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setDescription(newDescription);
        book.setRating(newRating);
        notifyItemChanged(position);
        // Update the database or any other storage
    }

}