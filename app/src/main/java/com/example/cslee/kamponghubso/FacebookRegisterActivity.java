package com.example.cslee.kamponghubso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cslee.kamponghubso.models.MyApplication;
import com.example.cslee.kamponghubso.models.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class FacebookRegisterActivity extends AppCompatActivity {
    private EditText username, zipcode, phone;
    private Button registerBtn, cancelBtn;
    private ProgressDialog progressDialog;
    private User userDetails;
    private MyApplication myApp;
    private DatabaseReference rootDB, userDB;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_facebook_register);
        username = findViewById(R.id.rr_user);
        zipcode = findViewById(R.id.rr_postal);
        phone = findViewById(R.id.rr_phone);
        registerBtn = findViewById(R.id.fb_register);
        cancelBtn = findViewById(R.id.fb_cancel);
        myApp = (MyApplication) getApplicationContext();
        getSupportActionBar().setTitle("Register Account for Facebook Users");
        rootDB = FirebaseDatabase.getInstance().getReference();
        userDB = rootDB.child("users");
        progressDialog = new ProgressDialog(this);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //if the email and password are not empty, display progress dialog
                progressDialog.setMessage("Registering...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                registerUser2();
                progressDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }

                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        AccessToken.setCurrentAccessToken(null);
                    }
                }).executeAsync();
                Intent i = new Intent(FacebookRegisterActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    protected void registerUser2() {
        //method to register the user email and password into firebase auth table
        try {

            String emailText = myApp.getEmail();
            String userNameText = username.getText().toString().trim();
            String postalText = zipcode.getText().toString().trim();
            String phoneText = phone.getText().toString().trim();

            if (TextUtils.isEmpty(postalText)) {
                Toast.makeText(this, "Zipcode is a required field", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(userNameText)) {
                Toast.makeText(this, "User name cannot be left blank", Toast.LENGTH_LONG).show();
                return;
            }

            if (phoneText.isEmpty()) { //phone no. is optional
                int postal = Integer.parseInt(zipcode.getText().toString().trim());
                userDetails = new User(emailText, userNameText, postal);
                //userDB.push().setValue(userDetails); //pushes the data into firebase user table
            } else {
                int phoneNo = Integer.parseInt(phone.getText().toString().trim()); //phone no. optional
                int postal = Integer.parseInt(zipcode.getText().toString().trim());
                userDetails = new User(emailText, userNameText, postal, phoneNo);
                //userDB.push().setValue(userDetails); //pushes the data into firebase user table
            }

            firebaseAuth.createUserWithEmailAndPassword(emailText, postalText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                userDB.child(uid).setValue(userDetails);
                                Intent i = new Intent(FacebookRegisterActivity.this, NavigationActivity.class);
                                startActivity(i);
                                Toast.makeText(FacebookRegisterActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            } else {
                                //display some message here
                                Toast.makeText(FacebookRegisterActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (NullPointerException e) {
            Toast.makeText(this, "Please enter all details correctly", Toast.LENGTH_LONG).show();
        }
    }
}
