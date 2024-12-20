package com.example.bookmanagerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI elements
        emailInput = findViewById(R.id.emailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        // Set OnClickListener for the button
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                // Simulate sending a password reset link (Replace with actual logic if needed)
                Toast.makeText(ForgotPassword.this, "Password reset link sent to: " + email, Toast.LENGTH_LONG).show();
                // Optionally, navigate back to Login activity
                finish(); // Close ForgotPasswordActivity
            }
        });
    }
}
