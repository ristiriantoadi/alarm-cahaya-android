package com.example.aplikasialarmmatahari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MatikanAlarm extends AppCompatActivity {

    Button matikanAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matikan_alarm);

        matikanAlarm = findViewById(R.id.button_matikan_alarm);
        matikanAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendMatikanAlarmRequest();
                    Intent intent = new Intent(getApplicationContext(),BeriRating.class);
                    startActivity(intent);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendMatikanAlarmRequest() throws IOException{
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.43.29/matikan_alarm";

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

