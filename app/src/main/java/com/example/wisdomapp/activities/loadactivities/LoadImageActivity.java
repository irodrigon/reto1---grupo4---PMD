package com.example.wisdomapp.activities.loadactivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wisdomapp.R;
import com.example.wisdomapp.activities.libraryactivities.ImageLibraryActivity;
import com.example.wisdomapp.activities.libraryactivities.VideoLibraryActivity;
import com.example.wisdomapp.database.ImageDataBaseHelper;
import com.example.wisdomapp.database.VideoDataBaseHelper;
import com.example.wisdomapp.items.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoadImageActivity extends AppCompatActivity {

    private static final int REQUEST_PHOTO_CAPTURE = 1;
    private ImageDataBaseHelper helper;
    private EditText titleEditText;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load_image);

        helper = new ImageDataBaseHelper(this);
        Button btnSacarFoto = findViewById(R.id.btnSacarFoto);
        Button btnVolver = findViewById(R.id.btIrApuntes2);
        titleEditText = findViewById(R.id.textImage);

        btnSacarFoto.setOnClickListener(view -> {
            if (checkAndRequestCameraPermission()) {
                String videoTitle = titleEditText.getText().toString().trim();
                if (videoTitle.isEmpty()) {
                    Toast.makeText(this, "Por favor introduce un titulo", Toast.LENGTH_SHORT).show();

                }else{
                    takePicture();
                }

            }
        });

        btnVolver.setOnClickListener(view -> {
            Intent intent = new Intent(LoadImageActivity.this, ImageLibraryActivity.class);
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

    public void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageCaptureLauncher.launch(intent);
    }

    private void storeImageInDatabase(String title, String imagePath, String timestamp) {
        boolean isInserted = helper.insertImage(title, imagePath, timestamp);

        if (isInserted) {
            Toast.makeText(this, "La imagen ha sido guardada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se ha podido guardar la imagen", Toast.LENGTH_SHORT).show();
        }
    }



    private final ActivityResultLauncher<Intent> imageCaptureLauncher = registerForActivityResult(

            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        if (imageBitmap != null) {
                            String imagePath = saveImageToFile(imageBitmap); // Implement this method
                            String imageTitle = titleEditText.getText().toString().trim();
                            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                            storeImageInDatabase(imageTitle, imagePath, timestamp);
                        } else {
                            Toast.makeText(LoadImageActivity.this, "Fallo en la captura de la imagen.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private String saveImageToFile(Bitmap bitmap) {

        String filename = "image_" + System.currentTimeMillis() + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, filename);

        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar el archivo.", Toast.LENGTH_SHORT).show();
            return null;
        }

    }



}
