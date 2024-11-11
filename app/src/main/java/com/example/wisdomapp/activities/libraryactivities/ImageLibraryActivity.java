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
import com.example.wisdomapp.R;
import com.example.wisdomapp.activities.loadactivities.LoadImageActivity;
import com.example.wisdomapp.activities.loadactivities.LoadVideoActivity;
import com.example.wisdomapp.adapters.ImageAdapter;
import com.example.wisdomapp.database.ImageDataBaseHelper;
import com.example.wisdomapp.items.Image;
import com.example.wisdomapp.main.MainActivity;

import java.util.List;

public class ImageLibraryActivity extends AppCompatActivity {

    private ImageDataBaseHelper helper;
    private ImageButton imageLibraryBackToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_library);

        imageLibraryBackToMainButton = (ImageButton) findViewById(R.id.imagelibrarybacktomainbutton);

        helper = new ImageDataBaseHelper(this);

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

        imageLibraryBackToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageLibraryActivity.this, MainActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        ImageView imgViewloadImage= (ImageView) findViewById(R.id.buttonloadimage);
        imgViewloadImage.setOnClickListener(view -> {
            Intent intent = new Intent(ImageLibraryActivity.this, LoadImageActivity.class);
            startActivity(intent);
        });

        displayImages();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

        private void displayImages() {
            List<Image> imageList= helper.getImagesList();

            ImageAdapter ImageAdapter = new ImageAdapter(imageList);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(ImageAdapter);
        }

    }



