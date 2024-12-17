package com.example.bookmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmanagerapp.databinding.ActivityWelcomeBinding;

public class Welcome extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Aktivizimi i ViewBinding
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Animacion për butonin
        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);
        binding.getStartedButton.startAnimation(bounceAnimation);

        // Kalimi në faqen e Login me efekt fade-in/out
        binding.getStartedButton.setOnClickListener(view -> {
            Intent intent = new Intent(Welcome.this, Login.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }
}
