package com.marcos.longhini.agrosocial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class NovaMsgActivity extends AppCompatActivity {
private Spinner spinner;
private EditText txtMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_msg);
        txtMsg = findViewById(R.id.txtMensagem);
        spinner = findViewById(R.id.spArea);
        //popular o spinner
    }

    public void enviarClick(View view) {
        //mandar para o Firebase
        String area = spinner.getSelectedItem().toString();
        String mensagem = txtMsg.getText().toString();

    }
}