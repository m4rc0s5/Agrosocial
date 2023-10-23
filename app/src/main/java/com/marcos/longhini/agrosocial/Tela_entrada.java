package com.marcos.longhini.agrosocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Tela_entrada extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_entrada);

    }

    public void Login(View view) {
        Intent intent = new Intent(Tela_entrada.this,Login.class);
        startActivity(intent);

    }

    public void Cadastro(View view) {
        Intent intent = new Intent(Tela_entrada.this,CadastroActivity.class);
        startActivity(intent);
    }


}