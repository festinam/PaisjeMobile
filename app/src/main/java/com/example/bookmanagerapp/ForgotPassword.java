package com.example.bookmanagerapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        // Set OnClickListener for the reset button
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            // Check if email field is empty
            if (email.isEmpty()) {
                Toast.makeText(ForgotPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                // Send password reset email using Firebase Authentication
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnSuccessListener(aVoid -> {
                            // If the email is sent successfully
                            Toast.makeText(ForgotPassword.this, "Password reset link sent to: " + email, Toast.LENGTH_LONG).show();
                            finish(); // Close ForgotPasswordActivity
                        })
                        .addOnFailureListener(e -> {
                            // If an error occurs (e.g., invalid email)
                            Log.e("ForgotPassword", "Error sending reset email", e); // Log the error details
                            Toast.makeText(ForgotPassword.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
