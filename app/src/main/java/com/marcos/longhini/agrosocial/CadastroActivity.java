package com.marcos.longhini.agrosocial;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class CadastroActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> launcher;
    private ImageView imageView;
    private EditText nome, email,senha,telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        imageView = findViewById(R.id.imageView);
        nome = findViewById(R.id.Nomebarra);
        email = findViewById(R.id.emailbarra);
        senha = findViewById(R.id.Senhabarra);
        telefone = findViewById(R.id.tel_barra);

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


        String nomeStr = nome.getText().toString();
        String emailStr = email.getText().toString();
        String SenhaStr = senha.getText().toString();
        String TelefoneStr = telefone.getText().toString();

        if (nomeStr.isEmpty() || emailStr.isEmpty() || SenhaStr.isEmpty() || TelefoneStr.isEmpty()) {
            Ferramentas.mensagem_Tela(CadastroActivity.this, "Não são permitidos campos em branco!");
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("key_nome", nomeStr.trim());
        editor.putString("key_email", emailStr.trim());
        editor.putString("key_senha", SenhaStr.trim());
        editor.putString("key_telefone", TelefoneStr.trim());

        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof BitmapDrawable) {
            // Converte imagem Drawable para Bitmap
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            // Converte  Bitmap em string Base64
            String encodedImage = Ferramentas.encodeToBase64(bitmap);
            // Salva string Base64 nas SharedPreferences
            editor.putString("key_imagem", encodedImage);
        }

        editor.apply();
        Ferramentas.mensagem_Tela(CadastroActivity.this, "Cadastro realizado com sucesso!");
        onBackPressed();
    }

    public void tirarfoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            launcher.launch(intent);
        }
    }

}