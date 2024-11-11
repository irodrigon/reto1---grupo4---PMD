package com.example.wisdomapp.activities.playactivities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;

import com.example.wisdomapp.R;
import com.example.wisdomapp.activities.libraryactivities.VideoLibraryActivity;

public class WatchVideoActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 101;
    private MediaController mediaController;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_watch_video);

        Button btVolver = findViewById(R.id.btIrApuntes);
        btVolver.setOnClickListener(view -> {
            Intent intent = new Intent(WatchVideoActivity.this, VideoLibraryActivity.class);
            startActivity(intent);
        });

        videoView = findViewById(R.id.video);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        if (checkAndRequestPermissions()) {
            setupVideoView();
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean checkAndRequestPermissions() {
        // Only request specific permissions if the app is running on Android 13 (API 33) or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_PERMISSION_CODE);
                return false;
            }
        } else {
            // For Android 12 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }


    private void setupVideoView() {
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        String videoPath = getIntent().getStringExtra("videoPath");
        videoView.setVideoURI(Uri.parse(videoPath));

        videoView.setOnPreparedListener(mediaPlayer -> {
            Toast.makeText(getBaseContext(), "El video esta preparado", Toast.LENGTH_LONG).show();
            videoView.start();
        });

        videoView.setOnCompletionListener(mediaPlayer -> {
            Toast.makeText(getBaseContext(), "El video ha acabado", Toast.LENGTH_LONG).show();
            mediaController.show(200000);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupVideoView(); // Permission granted; proceed with video setup
            } else {
                Toast.makeText(this, "Permission denied to access video files", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        mediaController.show();
        return false;
    }


}