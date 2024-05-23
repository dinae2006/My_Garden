package com.example.my_garden;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameET;
    private EditText passwordET;
    private Button loginBtn;
    private TextView createAccountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        setIds();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameET.getText().toString();
                String password=passwordET.getText().toString();
                if(checkData(username,password)){
                    databaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if( task.isSuccessful() ){
                                Account account = task.getResult().getValue(Account.class);
                                if( account !=null && account.getPassword().equals(password)){
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    SharedPreferences sharedPreferences=getSharedPreferences("file",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString("check","ok");
                                    editor.commit();
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this,"Error Password",Toast.LENGTH_LONG).show();

                                }
                            }else{
                                Toast.makeText(LoginActivity.this,"User Name Not Found",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }



            }
        });

        createAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivityForResult(intent,5);
            }
        });


    }
    public void setIds(){
        usernameET=findViewById(R.id.username_ET);
        passwordET=findViewById(R.id.password_ET);
        loginBtn=findViewById(R.id.Login_Btn);
        createAccountTV=findViewById(R.id.CreateAccount_TV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                // The result was successful
                usernameET.setText(data.getStringExtra("username"));
                passwordET.setText(data.getStringExtra("password"));
                // Handle the result here
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation
                // Handle cancellation here
            }
        }
    }

    //function to check if the user put the right data
    public boolean checkData(String userName,String password){
        boolean flag=true;
        if(userName.isEmpty()) {
            usernameET.setError("put your userName");
            flag=false;
        }
        if(password.isEmpty()) {
            passwordET.setError("put your password");
            flag=false;
        }
        return flag;
    }

}