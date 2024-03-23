package com.marcos.longhini.agrosocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private List<String> listDataHeader;
    //private HashMap<String, List<String>> listDataChild;
    private Map<String, List<Timeline>> listDataChild;// = new ArrayList<>();
    String headerName = "";
    FirebaseDatabase database;
    FirebaseFirestore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        database = FirebaseDatabase.getInstance();
        store = FirebaseFirestore.getInstance();

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

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if (listDataHeader != null) {
                    headerName = listDataHeader.get(i);
                    //Ferramentas.mensagem_Tela(TimelineActivity.this, headerName);
                }
                return false;
            }
        });

    }

    private void prepareListAreas() {
        listDataHeader = new ArrayList<>();

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

        for (String collectionPath : listDataHeader) {
            store.collection(collectionPath)
                    .orderBy("data", Query.Direction.DESCENDING)
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
                                        String doc = document.getId();
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
                                        Timeline childItem = new Timeline(doc, nome, mensagem, dt, imagem);
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






    class CustomListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private Map<String, List<Timeline>> _listDataChild;

        public CustomListAdapter(Context context, List<String> listDataHeader, Map<String, List<Timeline>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            Timeline childItem = (Timeline) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_subgroup, null);
            }

            TextView txtChildName = convertView.findViewById(R.id.txtNome);
            TextView txtChildDt = convertView.findViewById(R.id.txtDt);
            TextView txtChildMessage = convertView.findViewById(R.id.txtMensagem);
            //ImageView imgChildImage = convertView.findViewById(R.id.imgFoto);
            TextView txtChildDoc = convertView.findViewById(R.id.txtDocument);

            txtChildDoc.setText(childItem.getDoc());
            txtChildName.setText(childItem.getNome());
            txtChildDt.setText(childItem.getDt());
            txtChildMessage.setText(childItem.getMensagem());
            //String img64 = childItem.getImagem64();
            //if (!img64.equals("null")) {
            //    Bitmap bmp = Ferramentas.decodeFromBase64(img64);
            //    imgChildImage.setImageBitmap(bmp);
            //}

            if (childPosition % 2 == 0) {
                convertView.setBackgroundColor(Color.parseColor("#EEFFEE"));
            }

            String usuario = txtChildName.getText().toString();
            String documentId = txtChildDoc.getText().toString();
            ImageButton imgDel = convertView.findViewById(R.id.btnDelete);
            ImageButton imgReply = convertView.findViewById(R.id.btnReply);
            SharedPreferences sharedPreferences = _context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String nome = sharedPreferences.getString("key_nome", "anônimo");

            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nome.equals(usuario)) {
                        //Ferramentas.mensagem_Tela(_context, headerName +"\n"+documentId);
                        store.collection(headerName)
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Ferramentas.mensagem_Tela(_context, "Mensagem excluída!");
                                        prepareListAreas();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                    else {
                        Ferramentas.mensagem_Tela(_context, "Somente mensagens de sua autoria podem ser excluídas!");
                    }
                }
            });
            imgReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Ferramentas.mensagem_Tela(_context, headerName);
                    Intent intent = new Intent(TimelineActivity.this, ReplyMsgActivity.class);
                    intent.putExtra("header",headerName);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return _listDataChild.get(_listDataHeader.get(groupPosition)).size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_group, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.txtArea);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


}