package com.marcos.longhini.agrosocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void entrar(View view) {
        EditText email = findViewById(R.id.emailbarra);
        EditText senha = findViewById(R.id.Senhabarra);

        String emailStr = email.getText().toString();
        String SenhaStr = senha.getText().toString();
        if (emailStr.isEmpty() || SenhaStr.isEmpty()) {
            Ferramentas.mensagem_Tela(LoginActivity.this, "Não é permitido campos em branco!");
            return;
        }
        //validar email e senha
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String emailsp = sharedPreferences.getString("key_email", "");
        String senhasp = sharedPreferences.getString("key_senha", "");
        if (emailsp.equals(emailStr) && senhasp.equals(SenhaStr)) {

            //Salvar lembrar credencial
            CheckBox lembrarLogin = findViewById(R.id.lembrarLogin);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("key_login", lembrarLogin.isChecked());
            editor.apply();

            //Abrir tela Timeline
            Intent intent = new Intent(LoginActivity.this, TimelineActivity.class);
            startActivity(intent);

        } else {
            Ferramentas.mensagem_Tela(LoginActivity.this, "Usuario ou senha incorretos!!");
        }
    }
}