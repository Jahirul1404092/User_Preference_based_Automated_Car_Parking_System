package com.amsa.smartparkingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Parkingtype extends AppCompatActivity {

    static String phone="";
    static String gari="";
    Button emerbt,nonemerkbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkingtype);
        emerbt=(Button) findViewById(R.id.emergencybt);
        nonemerkbt=(Button) findViewById(R.id.nonemergencykbt);

        phone= getIntent().getExtras().getString("phone").toString();
        gari= getIntent().getExtras().getString("gari").toString();

        ActionBar actionBar;
        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(phone);

        DatabaseReference sltref = FirebaseDatabase.getInstance().getReference("slot");
        sltref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value=dataSnapshot.getValue(String.class);
                if(value.charAt(0)=='0'){
                    emerbt.setVisibility(View.VISIBLE);
                }else if(value.charAt(0)=='1'){
                    emerbt.setVisibility(View.GONE);
                }

                if((value.charAt(1)=='1' && value.charAt(2)=='1') && (value.charAt(3)=='1')){
                    nonemerkbt.setText("   Go to Area B   ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        emerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("key");
                myRef.setValue(":1");
                Intent intent = new Intent(Parkingtype.this, Home.class);
                intent.putExtra("phone", phone);
                intent.putExtra("gari", gari);
                intent.putExtra("type","emergency");
                startActivity(intent);
            }
        });
        nonemerkbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Parkingtype.this, Home.class);
                intent.putExtra("phone", phone);
                intent.putExtra("gari", gari);
                intent.putExtra("type","nonemergency");
                startActivity(intent);
            }
        });

        final DatabaseReference slotRef = FirebaseDatabase.getInstance().getReference("User").child(phone).child("slot");
        slotRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String slt = dataSnapshot.getValue(String.class);
//                Toast.makeText(Home.this,slott+"",Toast.LENGTH_LONG).show();

                if(slt.contains("1")==true){
                    Intent intent = new Intent(Parkingtype.this, Home.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("gari", gari);
                    intent.putExtra("type","emergency");
                    startActivity(intent);
                }else if(slt.contains("2")==true || slt.contains("3")==true || slt.contains("4")==true || slt.contains("5")==true || slt.contains("6")==true){
                    Intent intent = new Intent(Parkingtype.this, Home.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("gari", gari);
                    intent.putExtra("type","nonemergency");
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.balance_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
//            case R.id.addbln:
//                //see all
//                Intent intent = new Intent(Parkingtype.this, Addmoney.class);
//                intent.putExtra("phone", phone);
//                intent.putExtra("gari", gari);
////                intent.putExtra("type","nonemergency");
//                startActivity(intent);
//                return true;

            case R.id.checkbln:
                SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
                final String balance = prefs.getString("balance", "Null");//"No name defined" is the default value.

                AlertDialog.Builder builder=new AlertDialog.Builder(Parkingtype.this);
                builder.setTitle("You have "+balance+" Tk")
                        .setPositiveButton("Add money", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ////
                                Intent intent = new Intent(Parkingtype.this, Addmoney.class);
                                intent.putExtra("phone", phone);
                                intent.putExtra("gari", gari);
                                startActivity(intent);

                            }
                        })
                        .setCancelable(true);
                AlertDialog alert=builder.create();
                alert.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}