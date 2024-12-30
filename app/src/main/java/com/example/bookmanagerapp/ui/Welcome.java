package com.example.bookmanagerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmanagerapp.R;
import com.example.bookmanagerapp.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Welcome extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Aktivizimi i ViewBinding
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (isLoginSessionActive()){
            Intent intent = new Intent(Welcome.this, Home.class);
            startActivity(intent);
            finish();
        }

        // Animacion për butonin
        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);
        if (bounceAnimation != null) {
            binding.getStartedButton.startAnimation(bounceAnimation);
        }

        // Kalimi në faqen e Login me efekt fade-in/out
        binding.getStartedButton.setOnClickListener(view -> {
            Intent intent = new Intent(Welcome.this, Login.class);
            startActivity(intent);
            applyCustomTransition();
            finish();
        });
    }

    private Boolean isLoginSessionActive() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return false;
        }else {
            return true;
        }
    }

    private void applyCustomTransition() {
        // Use a modern approach for transitions if overridePendingTransition is deprecated
        try {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (NoSuchMethodError e) {
            // In case overridePendingTransition is not supported, log or handle gracefully
            e.printStackTrace();
        }
    }
}
