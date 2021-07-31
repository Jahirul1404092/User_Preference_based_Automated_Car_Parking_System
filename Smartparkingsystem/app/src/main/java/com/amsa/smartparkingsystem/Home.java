package com.amsa.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Math.abs;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Home extends AppCompatActivity {

    EditText slotet;
    TextView sltv,keytv,dst;
    Button parkbt,unparkbt, okbt;
//    static String slot="";
    static String valuePrev="";
    static String phone="";
    static String gari="";
    static String type="";
    static boolean parkstate=false;
    static boolean parked=false;
    static String slott="0";
    static long takaperhour=30;
    static int iii=0;
    static String value="";
    private MediaPlayer player;
    Vibrator vibrator;
    static long emergencyperkingtime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        Random rand = new Random();
//        int rand_int = ThreadLocalRandom.current().nextInt();
        int rand_int = rand.nextInt(10000);
        final int rand_int1=abs(rand_int);

        slotet=(EditText) findViewById(R.id.slotet);
        sltv=(TextView) findViewById(R.id.slottv);
        keytv=(TextView) findViewById(R.id.keytv);
        dst=(TextView) findViewById(R.id.dstv);
        parkbt=(Button) findViewById(R.id.parkbt);
        unparkbt=(Button) findViewById(R.id.unparkbt);
        okbt=(Button) findViewById(R.id.okbt);



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("key");
//        myRef.setValue(rand_int1+"");
//        keytv.setText(rand_int1+"");
        phone= getIntent().getExtras().getString("phone").toString();
        gari= getIntent().getExtras().getString("gari").toString();
        type= getIntent().getExtras().getString("type","nonemergency").toString();

        ActionBar actionBar;
        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(phone);

////        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference slotRef = database.getReference("User").child(phone).child("slot");
        // Read from the database
        slotRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                slott = dataSnapshot.getValue(String.class);
//                Toast.makeText(Home.this,slott+"",Toast.LENGTH_LONG).show();

                if(slott.contains("0")==true){
                    parkstate=false;
                    unparkbt.setVisibility(View.GONE);
                    parkbt.setVisibility(View.VISIBLE);
                    slotet.setEnabled(true);
                    slotet.setText("");
                    slotet.setHint("Enter a slot");
                    okbt.setVisibility(View.VISIBLE);
                }else {
                    parkstate=true;
//                    parked=true;
                    parkbt.setVisibility(View.GONE);
                    unparkbt.setVisibility(View.VISIBLE);
                    slotet.setEnabled(false);
                    slotet.setText("Parked at "+slott);
                    okbt.setVisibility(View.GONE);

                    if(type.equals("emergency")==true){
                        slotet.setHint("At Emergency slot is parked press Ok");
                    }

                }
                DatabaseReference sltref = database.getReference("slot");
                sltref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
//                        Toast.makeText(Home.this,slott+" "+value,Toast.LENGTH_LONG).show();
                        if(slott.contains("0")==false){
                            long currentTime=Calendar.getInstance().getTimeInMillis();
                            if(value.charAt(Integer.parseInt(slott)-1)=='0' && (currentTime-emergencyperkingtime>30000)){
                                Toast.makeText(Home.this,"Alarm",Toast.LENGTH_LONG).show();

                                player = MediaPlayer.create(Home.this, Settings.System.DEFAULT_ALARM_ALERT_URI);
                                //setting loop play to true
                                //this will make the ringtone continuously playing
                                player.setLooping(false);
                                player.start();

                                if (vibrator.hasVibrator()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            vibrator.vibrate(VibrationEffect.createOneShot(60000, VibrationEffect.DEFAULT_AMPLITUDE));
                                        long[] pattern = {0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000,0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000};//30min
                                        vibrator.vibrate(pattern, 1);
                                    } else {
                                        long[] pattern = {0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000,0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000, 0, 15000};//30min
                                        vibrator.vibrate(pattern, 1);
                                        // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array

                                    }
                                }
                                findViewById(R.id.alerm).setVisibility(View.VISIBLE);

                                findViewById(R.id.alerm).setBackgroundColor(Color.RED);
//                                for(int iii=0;iii<10;iii++) {
//                                    try
//                                    {
//                                        Thread.sleep(2000);
//                                    }
//                                    catch(InterruptedException ex)
//                                    {
//                                        Thread.currentThread().interrupt();
//                                    }
//                                    if (iii == 2) {
//                                        findViewById(R.id.alerm).setBackgroundColor(Color.YELLOW);
//                                    } else if (iii == 4) {
//                                        findViewById(R.id.alerm).setBackgroundColor(Color.RED);
//                                    } else if (iii == 6) {
//                                        findViewById(R.id.alerm).setBackgroundColor(Color.GREEN);
//                                    } else if (iii == 8) {
//                                        findViewById(R.id.alerm).setBackgroundColor(Color.BLUE);
//                                    }
//
//                                }
                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                findViewById(R.id.alerm).setVisibility(View.GONE);
                                                                vibrator.cancel();
                                                                player.stop();

                                                            }
                                                        },
                                            30000);
//                                }

                            }

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        // Read from the database
        DatabaseReference myRef2 = database.getReference("slot");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
//                Log.d("log", "Value is: " + value);
//                Toast.makeText(Home.this,value+"",Toast.LENGTH_SHORT).show();
                int len=value.length();
                String slot="";
                for (int i=0;i<len;i++) {
                    if(valuePrev.length()==len){
                        if (valuePrev.charAt(i) != value.charAt(i) && value.charAt(i)=='1') {
                            //////////here ypu can send push notification
                            final int k = i + 1;
                            keytv.setText("At slot " + k + " is parked");
                            dst.setVisibility(View.GONE);
//                            parkbt.setEnabled(false);
                            parkbt.setVisibility(View.GONE);


//                            Toast.makeText(Home.this,timee+" "+gari,Toast.LENGTH_LONG).show();

//                            if(parked==false){
                            final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
                            userRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Adapter adapter = dataSnapshot1.getValue(Adapter.class);
                                        final String phoneNo = adapter.getPhone();
                                        if (phoneNo.equals(phone) == true) {

                                            Calendar time = Calendar.getInstance();
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                                            String timee = simpleDateFormat.format(time.getTime());
                                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(phone);
                                            final HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("phone", phone);
                                            hashMap.put("gari", gari);
                                            hashMap.put("slot", String.valueOf(k));
                                            hashMap.put("time", timee);

                                            okbt.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    databaseReference.setValue(hashMap);
                                                    okbt.setVisibility(View.GONE);
                                                    DatabaseReference myRef = database.getReference("slt"+String.valueOf(k));
                                                    myRef.setValue("c");
                                                    DatabaseReference myRf = database.getReference("key");
                                                    myRf.setValue("3432:9");
                                                }
                                            });


//                                            okbt.setVisibility(View.VISIBLE);

//

//                                            Intent intent=new Intent(Home.this,MainActivity.class);
//                                            startActivity(intent);
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
//                          }
                        }
                    }
                    if(value.charAt(i)=='0'){
                        int j=i+1;
                        if (j==2){slot+="Slot 2 is free     ,    30Tk/h\n";}
                        else if (j==3){slot+="Slot 3 is free     ,    25Tk/h\n";}
                        else if (j==4){slot+="Slot 4 is free     ,    20Tk/h\n";}
//                        else if (j==5){slot+="Area B is free";}

                    }
                }
                valuePrev=value;
//                slot+=" is free";
                sltv.setText(slot);
                if(type.equals("emergency")==true){
                    sltv.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("log", "Failed to read value.", error.toException());
            }
        });

        if(type.equals("emergency")==true){
            slotet.setHint("go to Emergency slot");
            final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Adapter adapter = dataSnapshot1.getValue(Adapter.class);
                        final String phoneNo = adapter.getPhone();
                        if (phoneNo.equals(phone) == true) {

                            Calendar time = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                            String timee = simpleDateFormat.format(time.getTime());
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(phone);
                            final HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("phone", phone);
                            hashMap.put("gari", gari);
                            hashMap.put("slot", "1");
                            hashMap.put("time", timee);

//                            databaseReference.setValue(hashMap);
//                            DatabaseReference myRef = database.getReference("slt1");
//                            myRef.setValue("c");
//                            okbt.setVisibility(View.VISIBLE);
                            okbt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    databaseReference.setValue(hashMap);
                                    okbt.setVisibility(View.GONE);
                                    DatabaseReference myRef = database.getReference("slt1");
                                    myRef.setValue("c");

                                    DatabaseReference Rf = FirebaseDatabase.getInstance().getReference("key");
                                    Rf.setValue("3432:9");
                                    emergencyperkingtime=Calendar.getInstance().getTimeInMillis();
                                }
                            });
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        parkbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetOn()) {
                    String str = slotet.getText().toString();
                    if (str.equals("1") || str.equals("2") || str.equals("3") || str.equals("4") || str.equals("5") || str.equals("6")) {
    //                    dst.setText()

                        Random rand = new Random();
                        int rand_int = rand.nextInt(10000);
                        int rand_int1=abs(rand_int);
                        DatabaseReference myRef = database.getReference("key");
                        myRef.setValue(rand_int1+":"+str);

                        keytv.setVisibility(View.VISIBLE);
                        keytv.setText(rand_int1 + "");
                        dst.setVisibility(View.VISIBLE);
                        dst.setText("To park at " + str + " enter this code to Arduino");
                        slotet.setVisibility(View.INVISIBLE);
                    } else {
                        slotet.setText("");
                        dst.setVisibility(View.VISIBLE);
                        dst.setText("Enter a valid slot number");
                    }
                }else{
                    dst.setVisibility(View.VISIBLE);
                    dst.setText("No internet connection");
                }
            }
        });
        unparkbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(parked==true){

                DatabaseReference timeRef = database.getReference("User").child(phone).child("time");
                // Read from the database
                timeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String pretime = dataSnapshot.getValue(String.class);

                        Calendar time = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                        String nowtime = simpleDateFormat.format(time.getTime());
                        long hour = 0;
                        try {
                            hour = dateDifference(simpleDateFormat.parse(pretime), simpleDateFormat.parse(nowtime));
                        } catch (ParseException e) {
//                            Toast.makeText(Home.this, "Date error", Toast.LENGTH_LONG).show();
                        }
                        long taka = 0;
//                        taka=taka=hour * Integer.parseInt(slott) * takaperhour;
                        if(type.equals("emergency")==true){
                            taka=hour * 40;
                        }else if(Integer.parseInt(slott)==2){
                            taka=hour * 30;
                        }else if(Integer.parseInt(slott)==3){
                            taka=hour * 25;
                        }else if(Integer.parseInt(slott)==4){
                            taka=hour * 20;
                        }else if(Integer.parseInt(slott)==5){
                            taka=hour * 20;
                        }else if(Integer.parseInt(slott)==6){
                            taka=hour * 20;
                        }

                        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
                        String balanc = prefs.getString("balance", "Null");//"No name defined" is the default value.
                        long tk=0;
                        try{
                            if(Integer.parseInt(balanc)-taka>=0){
                                tk=Integer.parseInt(balanc)-taka;
                            }else{
                                tk=Integer.parseInt(balanc);
                            }

                            SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                            editor.putString("phone", phone);
                            editor.putString("gari", gari);
                            editor.putString("balance",String.valueOf(tk));
                            editor.apply();
                            Toast.makeText(Home.this,taka+" taka deducted!!",Toast.LENGTH_LONG).show();
                        }catch (Exception e){

                        }
                        
//                        Toast.makeText(Home.this,"slt"+slott+" ",Toast.LENGTH_LONG).show();
//                        Toast.makeText(Home.this, taka + "", Toast.LENGTH_LONG).show();
                        parked=false;

                        DatabaseReference slotRef = FirebaseDatabase.getInstance().getReference("User").child(phone).child("slot");
                        slotRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String slottt = dataSnapshot.getValue(String.class);
                                String slotttt="3432,"+slott+":9";
                                System.out.println("grrrrrrrrrrdrge"+slotttt);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("slt"+slott);
                                myRef.setValue("o");
                                DatabaseReference myRf = database.getReference("key");
                                if(slott.equals("0")==false){
                                    myRf.setValue("3432,"+slott+":9");
                                }
//                                else{
//                                    myRf.setValue("3432:9");
//                                }
//                                Toast.makeText(Home.this,"slt"+slott+" ",Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });
//                        Toast.makeText(Home.this,"slt"+slott+" ",Toast.LENGTH_LONG).show();

                        ///////////////////////
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("phone", phone);
                        hashMap.put("gari", gari);
                        hashMap.put("slot", "0");
                        hashMap.put("time", "0");
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(phone);
//                        System.out.println("loggg  unparked ");
                        databaseReference.setValue(hashMap);

                        Intent intent=new Intent(Home.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
            }
//            }
        });



    }


    /*private Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.developer:
                //see all
//                Intent intent=new Intent(Home.this,About.class);
//                startActivity(intent);
                Toast.makeText(Home.this,"Amsa: alensamsa3@gmail.com\n01521579898",Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
    public boolean isInternetOn() {

        final ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
//            Toast.makeText(Home.this, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public long dateDifference(Date startDate, Date endDate) {
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
        if(elapsedMinutes>=0){
            elapsedHours+=1;
        }
        return elapsedHours;
    }
}
