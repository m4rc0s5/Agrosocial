package com.marcos.longhini.agrosocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //verifica se o login é automatico
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean auto = sharedPreferences.getBoolean("key_login", false);
        if (auto == true) {
            Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
            startActivity(intent);
        }
    }


    public void Login(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void Cadastro(View view) {
        Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entrada_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            alerta();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void alerta() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View customLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_dialog, null);
        EditText passwordEditText = customLayout.findViewById(R.id.password_edit_text);
        Button positiveButton = customLayout.findViewById(R.id.yes_button);
        Button negativeButton = customLayout.findViewById(R.id.no_button);
        passwordEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        builder.setTitle("");
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = passwordEditText.getText().toString(); // Get the entered password
                if (enteredPassword.equals("marcos467")) { // Replace "your_password" with the actual password
                    Intent intent = new Intent(MainActivity.this, CadastroAreaActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    Ferramentas.mensagem_Tela(MainActivity.this, "Senha não confere!");
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}