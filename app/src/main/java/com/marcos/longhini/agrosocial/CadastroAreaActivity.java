package com.marcos.longhini.agrosocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroAreaActivity extends AppCompatActivity {

    private ListView listView;
    private EditText txtAA;
    private ArrayAdapter adapter;
    private List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_area);

        txtAA = findViewById(R.id.txtAA);
        listView = findViewById(R.id.listView);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter(this, R.layout.simple_list_item, dataList);
        listView.setAdapter(adapter);

        carregarAA();

    }

    private void carregarAA() {

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


    public void insereAreaClick(View view) {
        String area = txtAA.getText().toString();
        if (area.isEmpty()) {
            Ferramentas.mensagem_Tela(CadastroAreaActivity.this, "Campo em branco!");
        }
        else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dataListRef = database.getReference("area_agricola");

            String newEntryKey = dataListRef.push().getKey();
            Map<String, Object> dataToAdd = new HashMap<>();
            dataToAdd.put(newEntryKey, area);

            dataListRef.updateChildren(dataToAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Data inserted successfully
                        Ferramentas.mensagem_Tela(CadastroAreaActivity.this, "Área inserida com sucesso!");
                        adapter.notifyDataSetChanged();

                    } else {
                        // Handle the error
                        Ferramentas.mensagem_Tela(CadastroAreaActivity.this,"Erro: " + task.getException());
                    }
                }
            });

        }
    }
}