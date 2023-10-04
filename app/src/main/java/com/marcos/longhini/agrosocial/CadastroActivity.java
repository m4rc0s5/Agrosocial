package com.marcos.longhini.agrosocial;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class CadastroActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> launcher;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        imageView = findViewById(R.id.imageView);

         launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result here
                        Intent data = result.getData();
                        // Process the data as needed
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
                    }
                }
        );

    }
    public void salvar1click(View view) {

    }
    public void tirarfoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            launcher.launch(intent);
        }
    }
}