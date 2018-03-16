package com.example.cslee.kamponghubso;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import com.example.cslee.kamponghubso.models.MyApplication;

import org.json.JSONObject;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private EditText emailText, passwordText;
    private Button loginBtn, registerBtn;
    private String FBEmail, email, password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private MyApplication myApp;
    private DatabaseReference userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        myApp = (MyApplication)getApplicationContext();
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        registerBtn = (Button) findViewById(R.id.register);
        loginBtn = (Button) findViewById(R.id.login);
        callbackManager = CallbackManager.Factory.create();
        LoginButton FBloginBtn = (LoginButton) findViewById(R.id.fb_login);
        FBloginBtn.setReadPermissions("email", "public_profile");
        getLoginDetails(FBloginBtn);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        userDB = FirebaseDatabase.getInstance().getReference().child("users");


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loginRequest();
            }
        });

    }

    //method to login user using email and password
    protected void loginRequest() {
        email = emailText.getText().toString().trim();
        password  = passwordText.getText().toString().trim();
        //For test
        if(TextUtils.isEmpty(email) && (TextUtils.isEmpty(password)))
        {
            email = "shop@shop.com";
            password ="password";
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter a valid email address",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Hold on...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String userUid = firebaseAuth.getCurrentUser().getUid();
                        progressDialog.dismiss();
                        //if the task is successful
                        if (task.isSuccessful()) {
                            //start the profile activity
                            myApp.setUID(userUid);
                            finish();
                            Intent i = new Intent(MainActivity.this, NavigationActivity.class);
                            i.putExtra("email", email);
                            startActivity(i);
                        } else {
                            //display some message here
                            Toast.makeText(MainActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    //Register a callback function with the facebook loginButton to respond to the login result
    protected void getLoginDetails(LoginButton login_button) {
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(intent);
                AccessToken AT = AccessToken.getCurrentAccessToken();
                Log.d("access token", AT.toString());
                handleFacebookAccessToken(login_result.getAccessToken());
                GraphRequest.newMeRequest(
                        login_result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                try {
                                    if (response.getError() != null) {
                                        // handle error
                                    } else {
                                        // get id of user
                                        FBEmail = me.getString("id")+"@facebook.com";
                                        myApp.setEmail(FBEmail);
                                    }
                                } catch (JSONException e) {
                                }
                            }
                        }).executeAsync();

                //TODO how to check the email exists in the user table only for first time users then go FB Register
                if(!checkIfEmailExistInFirebase(FBEmail)) {
                    Intent i = new Intent(MainActivity.this, FacebookRegisterActivity.class);
                    startActivity(i);

                }else {
                    Intent i = new Intent(MainActivity.this, NavigationActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancel() {
                // code for cancellation
            }

            @Override
            public void onError(FacebookException exception) {
                //  code to handle error
                Log.d("facebook error", exception.toString());
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();


                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume(); //logs install and app activate app event
        AppEventsLogger.activateApp(getApplication());
    }

    @Override
    protected void onPause() {
        super.onPause(); //logs app deactivate app event
    }

    //passes the activity result back to the facebook SDK
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("data",data.toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    //method to check if FB email is not the first time login
    public boolean checkIfEmailExistInFirebase(String emailAddress)
    {
        final String emailAdd = emailAddress;
        final boolean[] emailExist = new boolean[1];
        Query getUser = userDB.orderByChild("email").equalTo(emailAdd);
        getUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if(singleSnapshot.exists()) {
                        emailExist[0] = true;
                    }else
                    { emailExist[0] = false;}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return emailExist[0];
    }
}
