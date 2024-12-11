package com.example.bookmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailInput;
    private Button sendResetLinkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI elements
        emailInput = findViewById(R.id.emailInput);
        sendResetLinkButton = findViewById(R.id.sendResetLinkButton);

        // Set an OnClickListener for the button
        sendResetLinkButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                // Simulate sending a password reset link (You can replace this with actual logic)
                Toast.makeText(ForgotPassword.this, "Password reset link sent to " + email, Toast.LENGTH_LONG).show();
                // Optionally, navigate back to Login activity
                finish();  // Close ForgotPasswordActivity and go back to previous screen (Login)
            }
        });
    }
}
