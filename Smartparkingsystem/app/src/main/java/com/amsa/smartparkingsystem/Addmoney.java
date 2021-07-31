package com.amsa.smartparkingsystem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Addmoney extends AppCompatActivity {

    static String phone="";
    static String gari="";
    static String type="";
    TextView blntv;
    static String balance="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);
        blntv=(TextView)findViewById(R.id.blntv);

        phone= getIntent().getExtras().getString("phone").toString();
        gari= getIntent().getExtras().getString("gari").toString();

        ActionBar actionBar;
        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(phone);

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        balance = prefs.getString("balance", "Null");//"No name defined" is the default value.
        blntv.setText(balance);

        findViewById(R.id.gotoparkingtypebt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Addmoney.this,Parkingtype.class);
                intent.putExtra("phone",phone);
                intent.putExtra("gari",gari);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.bkashimgid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tk=0;
                try{
                    tk=Integer.parseInt(balance)+50;
                    SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                    editor.putString("phone", phone);
                    editor.putString("gari", gari);
                    editor.putString("balance",String.valueOf(tk));
                    editor.apply();
                    Toast.makeText(Addmoney.this,"50 taka added!!",Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }

            }
        });

        findViewById(R.id.nogodimgid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tk=0;
                try{
                    tk=Integer.parseInt(balance)+50;
                    SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                    editor.putString("phone", phone);
                    editor.putString("gari", gari);
                    editor.putString("balance",String.valueOf(tk));
                    editor.apply();
                    Toast.makeText(Addmoney.this,"50 taka added!!",Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }
            }
        });

        findViewById(R.id.nogodimgid).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                editor.putString("phone", phone);
                editor.putString("gari", gari);
                editor.putString("balance","0");
                editor.apply();
                Toast.makeText(Addmoney.this,"0 taka!!",Toast.LENGTH_LONG).show();
                return true;
            }
        });
        findViewById(R.id.bkashimgid).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                editor.putString("phone", phone);
                editor.putString("gari", gari);
                editor.putString("balance","0");
                editor.apply();
                Toast.makeText(Addmoney.this,"0 taka!!",Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }
}