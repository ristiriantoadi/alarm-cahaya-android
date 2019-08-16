package com.example.aplikasialarmmatahari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BeriRating extends AppCompatActivity {

    Button kirimRating;
    EditText ratingPenilaian;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beri_rating);

        ratingPenilaian = findViewById(R.id.penilaian);

        kirimRating = findViewById(R.id.button_kirim_penilaian);
        kirimRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("data/total_data");
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String totalData = dataSnapshot.getValue().toString();
//                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("totalData");
//                        reference1.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String totalData = dataSnapshot.getValue().toString();
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("data/"+totalData);
                        String penilaian  = ratingPenilaian.getText().toString();
                        reference1.child("rating").setValue(penilaian);
                        Toast.makeText(getApplicationContext(),"Penilaian terkirim",Toast.LENGTH_SHORT);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
