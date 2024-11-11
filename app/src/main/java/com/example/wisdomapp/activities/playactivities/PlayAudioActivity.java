package com.example.wisdomapp.activities.playactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wisdomapp.R;
import com.example.wisdomapp.activities.libraryactivities.AudioLibraryActivity;

public class PlayAudioActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 101;
    private MediaController mediaController;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_audio);

        Button btVolver = findViewById(R.id.btnVolver);
        btVolver.setOnClickListener(view -> {
            Intent intent = new Intent(PlayAudioActivity.this, AudioLibraryActivity.class);
            startActivity(intent);
        });

        view = findViewById(R.id.audio);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(view);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}