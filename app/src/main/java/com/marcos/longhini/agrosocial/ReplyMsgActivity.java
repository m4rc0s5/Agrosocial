package com.marcos.longhini.agrosocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplyMsgActivity extends AppCompatActivity {

    private TextView txtArea;
    private EditText txtMsg;
    private ArrayAdapter adapter;
    private List<String> dataList;
    private String nome, email, telefone, imagemBase64, headerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_msg);

        txtMsg = findViewById(R.id.txtMensagem);
        txtArea = findViewById(R.id.txtArea);

        Intent intent = getIntent();
        headerName = intent.getStringExtra("header");
        txtArea.setText(headerName);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        nome = sharedPreferences.getString("key_nome", "anônimo");
        email = sharedPreferences.getString("key_email", "anônimo");
        telefone = sharedPreferences.getString("key_telefone", "anônimo");
        imagemBase64 = sharedPreferences.getString("key_imagem", "null");

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter(this, R.layout.simple_list_item, dataList);

    }

    private void carregaSpinner() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataListRef = database.getReference("area_agricola");
        dataListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataList.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String value = areaSnapshot.getValue(String.class);
                    Log.d("AREA", value);
                    dataList.add(value);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is unsuccessful
            }
        });
    }

    public void enviarClick() {
        //mandar para o Firebase
        String mensagem = txtMsg.getText().toString();

        if (mensagem.isEmpty()) {
            Ferramentas.mensagem_Tela(ReplyMsgActivity.this, "Não é permitido campo em branco!");
            return;
        }

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
        String strDate = sdf.format(c);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> mapa = new HashMap<>();
        mapa.put("data", strDate );
        mapa.put("mensagem",mensagem);
        //mapa.put("area_agricola", area);
        mapa.put("nome", nome);
        mapa.put("email", email);
        mapa.put("telefone", telefone);
        mapa.put("imagem", imagemBase64);
        db.collection(headerName).document().set(mapa).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Ferramentas.mensagem_Tela(ReplyMsgActivity.this, "Mensagem enviada!");
                txtMsg.setText("");
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Ferramentas.mensagem_Tela(ReplyMsgActivity.this, "Erro " + e.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.msg_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_msg) {
            enviarClick();
            return true;
        } else if (id == R.id.action_msg2) {
            enviarClick();
            return true;
        } else if (id == R.id.action_back) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNoInternetAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReplyMsgActivity.this);
        builder.setTitle("Sem conexão com a internet :(");
        builder.setMessage("Por favor verifique sua conexão com a internet e tente novamente.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close the app or take any other action
                finishAffinity();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }
}