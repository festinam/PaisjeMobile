package com.example.bookmanagerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmanagerapp.R;
import com.example.bookmanagerapp.database.DB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private DB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signUp);

        // Initialize database helper
        dbHelper = new DB(this);

        // Handle login button click
        loginButton.setOnClickListener(v -> handleLogin());

        // Navigate to sign-up screen
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        // Handle forgot password link click
        forgotPassword.setOnClickListener(v -> {
            // Navigate to the ForgotPasswordActivity
            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        // Get input values
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Invalid email format");
            emailInput.requestFocus();
            return;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }
        if (password.length() < 8) {
            passwordInput.setError("Password must be at least 8 characters long");
            passwordInput.requestFocus();
            return;
        }
        if (!password.matches(".*[0-9].*")) {
            passwordInput.setError("Password must contain at least one number");
            passwordInput.requestFocus();
            return;
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            passwordInput.setError("Password must contain at least one special character");
            passwordInput.requestFocus();
            return;
        }

        // Get stored hashed password from the database using the email
//        String storedHashedPassword = dbHelper.getHashedPasswordForUser(email);
//
//        if (storedHashedPassword != null && BCrypt.checkpw(password, storedHashedPassword)) {
//            // Login successful
////            dbHelper.addSession(true); //Shtojme nje sesion per t'a kontrolluar se a jemi te qasur (aktiv)
//            // Navigate to the home screen or main activity
//        } else {
//            // Login failed
//            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
//        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
