package com.marcos.longhini.agrosocial;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
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
        EditText nome = findViewById(R.id.Nomebarra);
        EditText email = findViewById(R.id.emailbarra);
        EditText senha = findViewById(R.id.Senhabarra);
        EditText telefone = findViewById(R.id.tel_barra);

        String nomeStr = nome.getText().toString();
        String emailStr = email.getText().toString();
        String SenhaStr = senha.getText().toString();
        String TelefoneStr = telefone.getText().toString();

    if (nomeStr.isEmpty() || emailStr.isEmpty() || SenhaStr.isEmpty()|| TelefoneStr.isEmpty() ){
        Ferramentas.mensagem_Tela(CadastroActivity.this,"Não é permitido campos em branco!");

        return;

    }
// Get the SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

// Get the editor to edit and save data
        SharedPreferences.Editor editor = sharedPreferences.edit();

// Put the key-value pairs
        editor.putString("key_name", nomeStr);
        editor.putString("key_email",emailStr);
        editor.putString("key_senha",SenhaStr);
        editor.putString("key_telefone",TelefoneStr);

// Apply the changes
        editor.apply();
        Ferramentas.mensagem_Tela(CadastroActivity.this,"Cadastro realizado com sucesso");
        onBackPressed();
    }
    public void tirarfoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            launcher.launch(intent);
        }
    }
}