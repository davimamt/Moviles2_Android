package com.example.sistema_ventasfb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class sales extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idAutomatic;
    double mTotalcomision = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        EditText idsellers = findViewById(R.id.etidsellersale);
        EditText idsale = findViewById(R.id.etidsale);
        EditText datesale = findViewById(R.id.etdatesale);
        EditText salevalue = findViewById(R.id.etsalevalue);
        ImageButton btnsavesale = findViewById(R.id.btnsavesale);
        ImageButton btnback = findViewById(R.id.btnback);


        // Recibir la identificación enviada desde la actividad MainActivity
        idsellers.setText(getIntent().getStringExtra("eidseller"));
        Toast.makeText(getApplicationContext(), "Total Comision: $"+getIntent().getStringExtra("etotalcomision"), Toast.LENGTH_SHORT).show();

        //Eventos
        btnsavesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar que todos lo datos estén diligenciados
                String xidsale = idsale.getText().toString();
                String xdatesale = datesale.getText().toString();
                String xsalevalue = salevalue.getText().toString();
                if (!xidsale.isEmpty() && !xdatesale.isEmpty() && !xsalevalue.isEmpty()){
                    // Guardar la venta
                    Map<String, Object> cSales = new HashMap<>();
                    cSales.put("idsale", idsale.getText().toString());
                    cSales.put("idseller", idsellers.getText().toString());
                    cSales.put("datesale", datesale.getText().toString());
                    cSales.put("salevalue", salevalue.getText().toString());
                    db.collection("sales")
                            .add(cSales)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    //Buscar el IdSeller para que retorne el total de la comision actual para acumular
                                    //el total de la comision con la base


                                    Toast.makeText(getApplicationContext(), "Venta guardada correctamente...", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "No se guardó la venta ...", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });





    }
}