package com.example.sistema_ventasfb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Se genera un objeto para conectarse a la BD de Firebase- Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idAutomatic;
    String mTotalcomsio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar los Id del archivo de activity_main.xml

        EditText idseller = findViewById(R.id.etidseller);
        EditText fullname = findViewById(R.id.etfullname);
        EditText email = findViewById(R.id.etemail);
        EditText password = findViewById(R.id.etpassword);
        TextView totalcomision = findViewById(R.id.tvtotalcomision);
        ImageButton btnsave = findViewById(R.id.btnsave);
        ImageButton btnsearch = findViewById(R.id.btnsearch);
        ImageButton btnedit = findViewById(R.id.btnedit);
        ImageButton btndelete = findViewById(R.id.btndelete);
        ImageButton btnsales = findViewById(R.id.btnsales);
        ImageButton btnlist = findViewById(R.id.btnlist);


        //Eventos

        btnsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("seller")
                        .whereEqualTo("idseller", idseller.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    if(!task.getResult().isEmpty()){
                                        for (QueryDocumentSnapshot document: task.getResult()){
                                            mTotalcomsio = String.valueOf(document.getDouble("totalcomision"));

                                        }

                                        //ir a ventas con el parametro de idseller
                                        Intent iSales = new Intent(getApplicationContext(), sales.class);
                                        //pasar el parametro de la identificación del vendedor
                                        iSales.putExtra("eidseller", idseller.getText().toString());
                                        iSales.putExtra("etotalcomision", mTotalcomsio);
                                        startActivity(iSales);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Id vendedor no existe!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });





        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalcomision.getText().toString().equals("0.0")){
                    db.collection("seller").document(idAutomatic)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "Vendedor eliminado", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(MainActivity.this, "Vendedor con registro de ventas!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> mseller = new HashMap<>();
                mseller.put("idseller", idseller.getText().toString());
                mseller.put("fullname", fullname.getText().toString());
                mseller.put("email",email.getText().toString());
                mseller.put("password", password.getText().toString());
                mseller.put("totalcomision", 0);


                db.collection("seller")
                        .whereEqualTo("idseller", idseller.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if(!task.getResult().isEmpty()){
                                        // la instantanea tiene informacion del documento
                                        db.collection("seller").document(idAutomatic)
                                                .set(mseller)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unset) {
                                                        Toast.makeText(getApplicationContext(), "Datos Actualizados correctamente", Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Error al guardar los datos...", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        Toast.makeText(getApplicationContext(), "Vendedor actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Este vendedor no existe", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });

            }
        });







        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bucar por Id Seller y recuperar todos los datos
                db.collection("seller")
                        .whereEqualTo("idseller", idseller.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if(!task.getResult().isEmpty()){
                                        // la instantanea tiene informacion del documento
                                        for (QueryDocumentSnapshot document: task.getResult()){
                                            idAutomatic= document.getId();
                                            //Mostrar la informacion en cada uno de los objetos referenciados
                                            fullname.setText(document.getString("fullname"));
                                            email.setText(document.getString("email"));
                                            totalcomision.setText(String.valueOf(document.getDouble("totalcomision"))) ;
                                        }
                                    }
                                    else{
                                        //Si no encuentra el Id Seller del vendedor
                                        Toast.makeText(getApplicationContext(), "Id Vendedor no Existe", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validacion de que los datos estén bien diligenciados
                String midseller = idseller.getText().toString();
                String mfullname = fullname.getText().toString();
                String memail = email.getText().toString();
                String mpassword = password.getText().toString();

                if (!midseller.isEmpty() && !mfullname.isEmpty() && !memail.isEmpty() && !mpassword.isEmpty()){
                    //crear una tabla temporal con los mismos campos de la coleccion seller
                    Map<String, Object> mseller = new HashMap<>();
                    mseller.put("idseller", midseller);
                    mseller.put("fullname", mfullname);
                    mseller.put("email",memail);
                    mseller.put("password", mpassword);
                    mseller.put("totalcomision", 0);


                    db.collection("seller")
                            .whereEqualTo("idseller", idseller.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        if(!task.getResult().isEmpty()){
                                            // la instantanea tiene informacion del documento
                                            Toast.makeText(getApplicationContext(), "Ese vendedor ya existe en la Base de datos", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            //Si no encuentra el Id Seller del vendedor
                                            //agregar el documento a la coleccion (tabla) seller a traves de la tabla temporal mseller
                                            db.collection("seller")
                                                    .add(mseller)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), "Datos Ingresados correctamente", Toast.LENGTH_SHORT).show();
                                                            idseller.setText("");
                                                            fullname.setText("");
                                                            email.setText("");
                                                            password.setText("");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Error al guardar los datos...", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos..", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}