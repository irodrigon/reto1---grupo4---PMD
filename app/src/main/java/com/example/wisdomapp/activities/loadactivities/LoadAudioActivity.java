package com.example.wisdomapp.activities.loadactivities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wisdomapp.R;
import com.example.wisdomapp.activities.libraryactivities.AudioLibraryActivity;
import com.example.wisdomapp.database.AudioDataBaseHelper;

import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoadAudioActivity extends AppCompatActivity {

    private static final int REQUEST_MICROPHONE_PERMISSIONS = 1;
    private EditText editTextDescription;
    private Button buttonVolverAudioLibrary;
    private Button buttonRecord;
    private Button buttonStopRecording;
    private AudioDataBaseHelper helper;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load_audio);

        helper = new AudioDataBaseHelper(this);

        editTextDescription = findViewById(R.id.editTextDescription);
        buttonVolverAudioLibrary = findViewById(R.id.buttonVolverAudioLibrary);
        buttonRecord = findViewById(R.id.buttonRecordAudio);
        buttonStopRecording = findViewById(R.id.buttonRecordAudio2);
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

        buttonVolverAudioLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoadAudioActivity.this, AudioLibraryActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestMicrophonePermission()) {
                    String audioTitle = editTextDescription.getText().toString().trim();
                    if (audioTitle.isEmpty()) {
                        Toast.makeText(LoadAudioActivity.this, "Por favor introduce un titulo", Toast.LENGTH_SHORT).show();
                    }else{
                        startRecording(audioTitle);
                    }

                }
            }
        });

        buttonStopRecording.setOnClickListener(view -> {
            stopRecording();
        });

    }

    private boolean checkAndRequestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void startRecording(String title) {
        File audioFile = new File(getExternalFilesDir(null), title + "_" + System.currentTimeMillis() + ".3gp");
        audioFilePath = audioFile.getAbsolutePath();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioFilePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {

            Toast.makeText(this, "Recording failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {

        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            storeAudioInDatabase(editTextDescription.getText().toString().trim(), audioFilePath, timestamp);
            Toast.makeText(this, "Recording saved", Toast.LENGTH_SHORT).show();

        }

    }

    private void storeAudioInDatabase(String title, String audioPath, String timestamp) {
        boolean isInserted = helper.insertAudio(title, audioPath, timestamp);

        if (isInserted) {
            Toast.makeText(this, "El audio ha sido guardado correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se ha podido guardar el audio.", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Intent> AudioCaptureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri audioUri = result.getData().getData();
                    if (audioUri != null) {
                        String videoTitle = editTextDescription.getText().toString().trim();
                        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        storeAudioInDatabase(videoTitle, audioUri.toString(), timestamp);
                    } else {
                        Toast.makeText(this, "Fallo en la grabacion del audio.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

}