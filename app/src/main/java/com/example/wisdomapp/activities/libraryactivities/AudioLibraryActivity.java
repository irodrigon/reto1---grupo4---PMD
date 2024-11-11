package com.example.wisdomapp.activities.libraryactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wisdomapp.items.Audio;
import com.example.wisdomapp.adapters.AudioAdapter;
import com.example.wisdomapp.database.AudioDataBaseHelper;
import com.example.wisdomapp.activities.loadactivities.LoadAudioActivity;
import com.example.wisdomapp.main.MainActivity;
import com.example.wisdomapp.R;

import java.util.List;

public class AudioLibraryActivity extends AppCompatActivity {


    private AudioDataBaseHelper helper;
    private ImageButton videosLibraryBackToMainButton;
    private ImageView loadAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_audio_library);

        loadAudio =(ImageView) findViewById(R.id.buttonloadaudio);

        helper = new AudioDataBaseHelper(this);

        videosLibraryBackToMainButton = (ImageButton) findViewById(R.id.audioslibrarybacktomainbutton);

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

        videosLibraryBackToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AudioLibraryActivity.this, MainActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        loadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AudioLibraryActivity.this, LoadAudioActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        displayAudios();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayAudios() {
        List<Audio> audioList= helper.getAudioList();

        AudioAdapter audioAdapter = new AudioAdapter(this,audioList);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAudios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(audioAdapter);
    }

}