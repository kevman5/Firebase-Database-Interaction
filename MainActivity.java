package com.example.project_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference reff;
    Vehicle vehicle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"Firebase Connected", Toast.LENGTH_LONG).show();
    }
    public void btndelrec(View V)
    {

        EditText ed_key;
        String key;

        ed_key = (EditText)findViewById(R.id.ed_search);
        key = ed_key.getText().toString();

        reff = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(key);
        reff.removeValue();

        Toast.makeText(MainActivity.this, "Removed Record",Toast.LENGTH_LONG).show();

    }
    public void btnmultisearch(View v)
    {

        reff = FirebaseDatabase.getInstance().getReference().child("Vehicle");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double average, sum, agen;
                int count = 0;
                String year;
                sum = 0;
                if (snapshot .hasChildren())
                {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        year = dataSnapshot1.child("year").getValue().toString();
                        agen = Double.parseDouble(year);
                        sum = sum + agen;
                        count = count + 1;
                    }

                }
                average = sum / count;
                Toast.makeText(MainActivity.this,String.valueOf(average),Toast.LENGTH_LONG).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void btnrecordsearch(View v)
    {
        EditText ed_year, ed_make, ed_model, ed_price, ed_key;
        String key;

        ed_year = (EditText) findViewById(R.id.ed_year);
        ed_make = (EditText) findViewById(R.id.ed_make);
        ed_model = (EditText) findViewById(R.id.ed_model);
        ed_price = (EditText) findViewById(R.id.ed_price);
        ed_key = (EditText)findViewById(R.id.ed_search);
        key = ed_key.getText().toString();
        reff = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(key);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String make = snapshot.child("make").getValue().toString();
                    String model = snapshot.child("model").getValue().toString();
                    String year = snapshot.child("year").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    ed_make.setText(make);
                    ed_model.setText(model);
                    ed_year.setText(year);
                    ed_price.setText(price);
                }
                else
                    Toast.makeText(MainActivity.this, "Record Not Found", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void btnclicksubmit(View v)
    {
        EditText ed_year, ed_make, ed_model, ed_price;
        String make, model, key;
        int year;
        double price;

        ed_year = (EditText) findViewById(R.id.ed_year);
        ed_make = (EditText) findViewById(R.id.ed_make);
        ed_model = (EditText) findViewById(R.id.ed_model);
        ed_price = (EditText) findViewById(R.id.ed_price);

        year = Integer.parseInt(ed_year.getText().toString());
        make = ed_make.getText().toString();
        model = ed_model.getText().toString();
        price = Double.parseDouble(ed_price.getText().toString());

        vehicle = new Vehicle();
        vehicle.setYear(year);
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setPrice(Double.parseDouble(String.valueOf(price)));

        reff = FirebaseDatabase.getInstance().getReference().child("Vehicle");
        reff.push().setValue(vehicle);
        key = year + make + model;
        reff.child(key).setValue(vehicle);
        Toast.makeText(MainActivity.this, "Inserted Record",Toast.LENGTH_LONG).show();
    }
}