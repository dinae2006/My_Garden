package com.example.my_garden;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartUpScreen extends Activity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        Thread thread =new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        SharedPreferences sharedPreferences=getSharedPreferences("file",MODE_PRIVATE);
        String st=sharedPreferences.getString("check",null);

        Intent intent;
        if(st!=null){
            intent=new Intent(StartUpScreen.this, MainActivity.class);
        }else {
            intent=new Intent(StartUpScreen.this,LoginActivity.class);
        }
        startActivity(intent);
    }
}