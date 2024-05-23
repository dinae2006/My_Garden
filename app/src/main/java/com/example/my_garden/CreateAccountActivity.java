package com.example.my_garden;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText userNameET;
    private EditText emailET;
    private EditText pwdET;
    private Button regBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        setIds();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName =firstNameET.getText().toString();
                String lastName =lastNameET.getText().toString();
                String userName =userNameET.getText().toString();
                String email =emailET.getText().toString();
                String pwd =pwdET.getText().toString();

                if( checkData(firstName,lastName,userName,pwd,email)) {

                    databaseReference.child(userName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().exists()) {
                                Toast.makeText(CreateAccountActivity.this, "User Name Exists", Toast.LENGTH_LONG).show();
                            } else {
                                Account account = new Account(firstName, lastName, email, userName, pwd);
                                databaseReference.child(userName).setValue(account);
                                Intent data = new Intent();
                                data.putExtra("username", userName);
                                data.putExtra("password", pwd);
                                setResult(RESULT_OK, data);
                                finish();
                            }

                        }
                    });
                }
            }
        });
    }

    public void setIds(){
        firstNameET=findViewById(R.id.first_nameET);
        lastNameET=findViewById(R.id.last_nameET);
        userNameET=findViewById(R.id.user_nameET);
        pwdET=findViewById(R.id.passwordET);
        emailET=findViewById(R.id.emailET);
        regBtn = findViewById(R.id.btn_reg);
    }

    public boolean checkData(String firstname,String lastname,String userName,String password,String email){
        boolean flag=true;
        if(firstname.isEmpty()) {
            firstNameET.setError("put your userName");
            flag=false;
        }
        if(lastname.isEmpty()) {
            lastNameET.setError("put your userName");
            flag=false;
        }
        if(userName.isEmpty()) {
            userNameET.setError("put your userName");
            flag=false;
        }
        if(password.isEmpty()) {
            pwdET.setError("put your password");
            flag=false;
        }
        else if(password.length()<=8) {
            pwdET.setError("you need more then 8 input for strongest password");
            flag=false;
        }
        if(!email.endsWith("@gmail.com")){
            emailET.setError("put your email");
            flag=false;
        }
        return flag;
    }
}