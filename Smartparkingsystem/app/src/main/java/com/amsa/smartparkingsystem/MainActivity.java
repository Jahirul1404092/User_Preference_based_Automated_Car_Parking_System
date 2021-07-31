package com.amsa.smartparkingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText phoneet,gariet;
    Button update_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneet=(EditText) findViewById(R.id.phone_et);
        gariet=(EditText) findViewById(R.id.garirno_et);
        update_bt=(Button) findViewById(R.id.bt_update);

        update_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneet.getText().toString();
                final String gari = gariet.getText().toString();
                if (phone.isEmpty() || gari.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill all filed", Toast.LENGTH_LONG).show();
                } else {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("phone", phone);
                    hashMap.put("gari", gari);
                    hashMap.put("slot", "0");
                    hashMap.put("time", "0");
                    final String msgId = String.valueOf(Calendar.getInstance().getTimeInMillis());

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(phone);
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {
                                //verification successful we will start the profile activity
                                phoneet.setText("");
                                gariet.setText("");
                                phoneet.setHint(phone);
                                gariet.setHint(gari);
                                SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                                editor.putString("phone", phone);
                                editor.putString("gari", gari);
                                editor.putString("balance","500");
                                editor.apply();
                                Toast.makeText(MainActivity.this, "Updated!!", Toast.LENGTH_LONG).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Intent intent = new Intent(MainActivity.this, Parkingtype.class);
                                                            intent.putExtra("phone", phone);
                                                            intent.putExtra("gari", gari);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    },
                                        5000);
                            } else {
                                Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String phone = prefs.getString("phone", "Null");//"No name defined" is the default value.
        final String gari = prefs.getString("gari", "Null"); //0 is the default value.

        if(phone.equals("Null")==false){
            phoneet.setHint(phone);
            gariet.setHint(gari);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent=new Intent(MainActivity.this,Parkingtype.class);
                                        intent.putExtra("phone",phone);
                                        intent.putExtra("gari",gari);
                                        startActivity(intent);
                                        finish();
                                    }
                                },
                    5000);
        }
        
    }


    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }
}
