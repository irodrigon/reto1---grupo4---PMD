package com.example.wisdomapp.activities.libraryactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wisdomapp.activities.loadactivities.LoadVideoActivity;
import com.example.wisdomapp.main.MainActivity;
import com.example.wisdomapp.R;
import com.example.wisdomapp.items.Video;
import com.example.wisdomapp.adapters.VideoAdapter;
import com.example.wisdomapp.database.VideoDataBaseHelper;

import java.util.List;

public class VideoLibraryActivity extends AppCompatActivity {

    private VideoDataBaseHelper helper;
    private ListView videoListView;
    private ImageButton videosLibraryBackToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_library);

        videosLibraryBackToMainButton = (ImageButton) findViewById(R.id.videoslibrarybacktomainbutton);

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
                Intent intent = new Intent(VideoLibraryActivity.this, MainActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        helper = new VideoDataBaseHelper(this);
        videoListView = findViewById(R.id.videoListView);

        displayVideos();

        ImageView imgViewIrAGrabar = findViewById(R.id.imgViewIrAGrabar);
        imgViewIrAGrabar.setOnClickListener(view -> {
            Intent intent = new Intent(VideoLibraryActivity.this, LoadVideoActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayVideos() {
        List<Video> videoList = helper.getVideoList();

        VideoAdapter adapter = new VideoAdapter(this,videoList);
        videoListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

}