package com.example.aplikasialarmmatahari;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    TextView waktu;
    EditText inputKecerahan;
    EditText inputInterval;
    LinearLayout inputWaktu;


    Button simulasiButton,matikanSimulasiButton;
    Button saveButton;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    String jam = "0";
    String menit = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);

        waktu = findViewById(R.id.waktu_alarm);
        inputKecerahan = findViewById(R.id.edit_kecerahan);
        inputInterval = findViewById(R.id.edit_interval);

        simulasiButton = findViewById(R.id.button_simulasi);
        saveButton = findViewById(R.id.button_save);
        matikanSimulasiButton = findViewById(R.id.button_matikan_simulasi);
        matikanSimulasiButton.setVisibility(View.INVISIBLE);

        inputWaktu = findViewById(R.id.waktu_input);

//        reference.child("status_alarm").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String status = dataSnapshot.getValue().toString();
//                if(status == "1"){
//                    Intent intent = new Intent(getApplicationContext(),MatikanAlarm.class);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        matikanSimulasiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sendMatikanSimulasiRequest();
                    simulasiButton.setVisibility(View.VISIBLE);
                    matikanSimulasiButton.setVisibility(View.INVISIBLE);
                }catch(IOException e){

                }
            }
        });

        simulasiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendSimulationRequest();
                    matikanSimulasiButton.setVisibility(View.VISIBLE);
                    simulasiButton.setVisibility(View.INVISIBLE);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                //reference.child("config_alarm").child("jam").setValue();
                reference.child("config_alarm").child("jam").setValue(jam);
                reference.child("config_alarm").child("menit").setValue(menit);

                String kecerahan = inputKecerahan.getText().toString();
                String interval = inputInterval.getText().toString();

                reference.child("config_alarm").child("kecerahan").setValue(kecerahan);
                reference.child("config_alarm").child("interval").setValue(interval);

                try {
                    sendAlarmConfig();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });



        inputWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"123",Toast.LENGTH_SHORT).show();
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time_picker");
            }
        });


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        jam = ""+hourOfDay;
        menit = ""+minute;

        waktu.setText(jam+":"+menit+" WITA");
    }

    protected void sendSimulationRequest() throws IOException {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.43.29/simulation";

        OkHttpClient client = new OkHttpClient();

        String kecerahan = inputKecerahan.getText().toString();
        String interval = inputInterval.getText().toString();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("kecerahan",kecerahan)
                .addFormDataPart("simulasi","true")
                .addFormDataPart("interval",interval)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String message = "Failure: " + e.getMessage();
//                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
//                Log.d("Failure: ",message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateUI(body);
                        //textResponse.setText(message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Log.d("Failure",message);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String message = "Response: " + response.body().string();
                //Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                //Log.d("Success: ",message);

                //final String responseText = "Response: "+message;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateUI(body);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void sendAlarmConfig() throws IOException{
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.43.29/save";

        OkHttpClient client = new OkHttpClient();
//
//        String hour = jam;
//        String minute = menit;


        String kecerahan = inputKecerahan.getText().toString();
        String interval = inputInterval.getText().toString();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("jam",jam)
                .addFormDataPart("menit",menit)
                .addFormDataPart("kecerahan",kecerahan)
                .addFormDataPart("interval",interval)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String message = "Failure: " + e.getMessage();
//                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
//                Log.d("Failure: ",message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateUI(body);
                        //textResponse.setText(message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Log.d("Failure", message);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String message = "Response: " + response.body().string();
                //Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                //Log.d("Success: ",message);

                //final String responseText = "Response: "+message;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateUI(body);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void sendMatikanSimulasiRequest() throws IOException{
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.43.29/matikan_simulasi";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String message = "Failure: " + e.getMessage();
//                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
//                Log.d("Failure: ",message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateUI(body);
                        //textResponse.setText(message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Log.d("Failure", message);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String message = "Response: " + response.body().string();
                //Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                //Log.d("Success: ",message);

                //final String responseText = "Response: "+message;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateUI(body);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}
