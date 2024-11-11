package com.example.wisdomapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.gridlayout.widget.GridLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wisdomapp.R;
import com.example.wisdomapp.activities.libraryactivities.AudioLibraryActivity;
import com.example.wisdomapp.activities.libraryactivities.ImageLibraryActivity;
import com.example.wisdomapp.activities.libraryactivities.VideoLibraryActivity;


public class MainActivity extends AppCompatActivity {

    private GridLayout mainButtonImages;
    private GridLayout mainButtonVideos;
    private GridLayout mainButtonAudios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mainButtonImages = (GridLayout) findViewById(R.id.mainButtonImages);
        mainButtonVideos = (GridLayout) findViewById(R.id.mainButtonVideos);
        mainButtonAudios = (GridLayout) findViewById(R.id.mainButtonAudios);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                        }
                    }
                }
        );

        mainButtonImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImageLibraryActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        mainButtonAudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AudioLibraryActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        mainButtonVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoLibraryActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}