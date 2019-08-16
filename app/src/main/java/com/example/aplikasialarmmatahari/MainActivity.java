package com.example.aplikasialarmmatahari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button settingAlarm;
    TextView countDownText;
    TextView timeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = findViewById(R.id.time_text);
        timeText.setVisibility(View.INVISIBLE);

        countDownText = findViewById(R.id.count_down_text);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("config_alarm");
        reference.child("jam").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String jam = dataSnapshot.getValue().toString();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("config_alarm");
                reference1.child("menit").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String menit = dataSnapshot.getValue().toString();
                        String waktu= jam+":"+menit;
                        countDownText.setText(waktu);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        settingAlarm = findViewById(R.id.atur_alarm_button);
        settingAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

//                reference.child("config_alarm").child("jam").setValue("14");
//                reference.child("config_alarm").child("menit").setValue("43");
                Intent intent = new Intent(MainActivity.this,SettingAlarm.class);
                startActivity(intent);
            }
        });



        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("status_alarm").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String statusAlarm = dataSnapshot.getValue().toString();
                if(statusAlarm.equals("1")){
                    Log.d("status_alarm",statusAlarm);
                    Intent intent = new Intent(getApplicationContext(),MatikanAlarm.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
