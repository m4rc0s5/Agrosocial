package com.marcos.longhini.agrosocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    }

    public void eventoclick(View view) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
        String strDate = sdf.format(c);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("data", strDate );
        mapa.put("mensagem","helloword");
        mapa.put("topicos", "Agricultura");
        db.collection("marcos").document().set(mapa).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "enviada", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}