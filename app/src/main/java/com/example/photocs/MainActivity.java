package com.example.photocs;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnCamera;
    Button sendImage;
    String pathtofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.btnCamera);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPicture();
            }
        });

        sendImage = findViewById(R.id.sendImage);
        sendImage.setVisibility(View.INVISIBLE);

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendserver();
            }
        });
        imageView = findViewById(R.id.Image);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bitmap = BitmapFactory.decodeFile(pathtofile);
                imageView.setImageBitmap(bitmap);
                sendImage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void dispatchPicture() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepic.resolveActivity(getPackageManager()) != null) {
            File photofile = null;
            try {
                photofile = createPhotoFile();
                if (photofile != null) {
                    pathtofile = photofile.getAbsolutePath();
                    Uri photoUri = FileProvider.getUriForFile(MainActivity.this, "com.example.photocs", photofile);
                    takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takepic, 1);
                }
            } catch (Exception e) {
            }
        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storagedir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storagedir);
        } catch (IOException e) {
            Log.d("my Log", "Excep : " + e.toString());
        }
        return image;
    }

    private void sendserver() {

    }
}
