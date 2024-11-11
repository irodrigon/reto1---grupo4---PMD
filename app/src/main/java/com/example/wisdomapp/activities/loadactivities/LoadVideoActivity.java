package com.example.wisdomapp.activities.loadactivities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.Manifest;

import com.example.wisdomapp.R;
import com.example.wisdomapp.activities.libraryactivities.VideoLibraryActivity;
import com.example.wisdomapp.database.DataBaseHelper;

public class LoadVideoActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private DataBaseHelper helper;
    private EditText titleEditText;
    private static final int REQUEST_CAMERA_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load_video);


        helper = new DataBaseHelper(this);
        Button btVideo = findViewById(R.id.btGrabar);
        Button btVolver = findViewById(R.id.btIrApuntes);
        titleEditText = findViewById(R.id.textVideo);

        btVideo.setOnClickListener(view -> {
            if (checkAndRequestCameraPermission()) {
                String videoTitle = titleEditText.getText().toString().trim();
                if (videoTitle.isEmpty()) {
                    Toast.makeText(this, "Por favor introduce un titulo", Toast.LENGTH_SHORT).show();

                }else{
                    recordVideo();
                }

            }
        });

        btVolver.setOnClickListener(view -> {
            Intent intent = new Intent(LoadVideoActivity.this, VideoLibraryActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    public void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoCaptureLauncher.launch(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recordVideo();
            } else {
                Toast.makeText(this, "Se requieren los permisos de camara para poder grabar el vídeo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void storeVideoInDatabase(String title, String videoPath, String timestamp) {
        boolean isInserted = helper.insertVideo(title, videoPath, timestamp);

        if (isInserted) {
            Toast.makeText(this, "El video ha sido guardado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se ha podido guardar el video", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Intent> videoCaptureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri videoUri = result.getData().getData();
                    if (videoUri != null) {
                        String videoTitle = titleEditText.getText().toString().trim();
                        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        storeVideoInDatabase(videoTitle, videoUri.toString(), timestamp);
                    } else {
                        Toast.makeText(this, "Fallo en la captura del vídeo.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

}

