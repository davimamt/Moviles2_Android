package com.example.sistema_ventasfb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Se genera un objeto para conectarse a la BD de Firebase- Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                    //agregar el documento a la coleccion (tabla) seller a traves de la tabla temporal mseller
                    db.collection("seller")
                            .add(mseller)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Datos Ingresados correctamente", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error al guardar los datos...", Toast.LENGTH_SHORT).show();
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