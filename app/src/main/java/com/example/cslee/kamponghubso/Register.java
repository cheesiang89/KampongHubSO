package com.example.cslee.kamponghubso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cslee.kamponghubso.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email,password, username, zipcode,phone;
    private Button signUpBtn;
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private DatabaseReference rootDB, userDB;
    private User userDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText) findViewById(R.id.r_email);
        username = (EditText) findViewById(R.id.r_user);
        password = (EditText) findViewById(R.id.r_password);
        zipcode = (EditText) findViewById(R.id.r_address);
        phone = (EditText) findViewById(R.id.r_phone);
        signUpBtn = (Button) findViewById(R.id.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        rootDB = FirebaseDatabase.getInstance().getReference();
        userDB = rootDB.child("users");
        progressDialog = new ProgressDialog(this);
        actionBar = getSupportActionBar();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerUser();
            }
        });
        actionBar.setDisplayHomeAsUpEnabled(true); //needed to set up the action bar to display the navigation back button to MainActivity
    }

    //method to create a navigation back button back to the MainActivity
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    //method to register the user email and password into firebase auth table
    protected void registerUser() {
        try {
        String emailText = email.getText().toString().trim();
        String userNameText = username.getText().toString().trim();
        String passwordText  = password.getText().toString().trim();
        String addText  = zipcode.getText().toString().trim();
        String phoneText  = phone.getText().toString().trim();

        if(TextUtils.isEmpty(emailText)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(passwordText)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(addText)){
            Toast.makeText(this,"Please enter zipcode",Toast.LENGTH_LONG).show();
            return;
        }

        if (phoneText.isEmpty()) { //phone no. is optional
            int addNo = Integer.parseInt(zipcode.getText().toString().trim());
            userDetails = new User(emailText, userNameText, addNo);
            userDB.push().setValue(userDetails); //pushes the data into firebase user table
        }else {
            int phoneNo = Integer.parseInt(phone.getText().toString().trim()); //phone no. optional
            int addNo = Integer.parseInt(zipcode.getText().toString().trim());
            userDetails = new User(emailText, userNameText, addNo, phoneNo);
            userDB.push().setValue(userDetails); //pushes the data into firebase user table
        }


        //if the email and password are not empty, display progress dialog
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Register.this, MainActivity.class);
                            startActivity(i);

                        }else{
                            //display some message here
                            Toast.makeText(Register.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                        }
                });
        }catch(NullPointerException e) {
            Toast.makeText(this, "Please enter all details correctly",Toast.LENGTH_LONG).show();
        }
    }

    //method to hide the keyboard after touch on listView
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
