package com.marcos.longhini.agrosocial;

import static androidx.core.app.ActivityCompat.finishAffinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> listDataHeader;
    //private HashMap<String, List<String>> listDataChild;
    private Map<String, List<Timeline>> listDataChild;// = new ArrayList<>();
    ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimelineActivity.this, NovaMsgActivity.class);
                startActivity(intent);
            }
        });

        expandableListView = findViewById(R.id.expListView);

        // Verifica conectividade
        if (Ferramentas.isNetworkAvailable(this)) {
            prepareListAreas();
        } else {
            // Mostra alerta sem internet
            showNoInternetAlert();
        }

    }


    private void prepareListAreas() {
        listDataHeader = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataListRef = database.getReference("area_agricola");
        dataListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listDataHeader.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String value = areaSnapshot.getValue(String.class);
                    Log.d("AREA", value);
                    listDataHeader.add(value);
                }
                prepareListData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is unsuccessful
            }
        });
    }

    private void prepareListData() {
        listDataChild = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String collectionPath : listDataHeader) {
            db.collection(collectionPath)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                List<Timeline> childItemList = new ArrayList<>();

                                for (DocumentSnapshot document : task.getResult()) {
                                    // Get the document data as a Map
                                    Map<String, Object> data = document.getData();

                                    if (data != null) {
                                        // Iterate through the fields in the document
                                        String nome = "";
                                        String dt = "";
                                        String mensagem = "";
                                        String imagem = "";
                                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                                            String fieldName = entry.getKey();
                                            Object fieldValue = entry.getValue();

                                            if (entry.getKey().equals("nome")) {
                                                nome = entry.getValue().toString();
                                            }
                                            if (entry.getKey().equals("data")) {
                                                dt = entry.getValue().toString();
                                            }
                                            if (entry.getKey().equals("mensagem")) {
                                                mensagem = entry.getValue().toString();
                                            }
                                            if (entry.getKey().equals("imagem")) {
                                                imagem = entry.getValue().toString();
                                            }
                                            // Log or process the field name and value
                                            Log.d("TAG", "Field Name: " + fieldName + ", Field Value: " + fieldValue);
                                        }
                                        Timeline childItem = new Timeline(nome, mensagem, dt, imagem);
                                        childItemList.add(childItem);
                                    }
                                }
                                listDataChild.put(collectionPath, childItemList);

                                ExpandableListAdapter listAdapter = new CustomListAdapter(TimelineActivity.this, listDataHeader, listDataChild);
                                expandableListView.setAdapter(listAdapter);

                            } else {
                                Log.e("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        prepareListAreas();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            prepareListAreas();
            return true;
        } else if (id == R.id.action_sync2) {
            prepareListAreas();
            return true;
        } else if (id == R.id.action_out) {
            logout();
            return true;
        } else if (id == R.id.action_exit) {
            finishAffinity();
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(TimelineActivity.this, SobreActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        final AlertDialog alert = new AlertDialog.Builder(TimelineActivity.this).create();
        //alert.setTitle("title");
        alert.setMessage("Tem certeza que gostaria de realizar o LOGOUT ?");
        alert.setIcon(R.drawable.ic_warning);
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("key_login", false);
                editor.apply();
                finishAffinity();
            }
        });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.show();

    }

    private void showNoInternetAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TimelineActivity.this);
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