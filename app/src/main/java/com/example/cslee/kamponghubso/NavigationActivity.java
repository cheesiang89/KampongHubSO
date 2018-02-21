package com.example.cslee.kamponghubso;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cslee.kamponghubso.fragment.CreateAdFragment;
import com.example.cslee.kamponghubso.fragment.ShopAdFragment;
import com.example.cslee.kamponghubso.fragment.test.EditTestFragment;
import com.example.cslee.kamponghubso.fragment.ShopListFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class NavigationActivity extends AppCompatActivity {

    //private TextView mTextMessage;
   // private ImageButton chatBtn;
   private String UserEmail;
   private Fragment fragment;

   //Firebase variable
    private FirebaseAuth mAuth=FirebaseAuth.getInstance(); // <== To prevent null error in getUid();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        //chatBtn = (ImageButton)(findViewById(R.id.chatButton));
       // mTextMessage = (TextView)findViewById(R.id.message);
         Bundle extras = getIntent().getExtras();
        if(extras == null)
        {UserEmail = null;}
        else
        {UserEmail = extras.getString("email");}
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

       /* chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();

                Intent i = new Intent(NavigationActivity.this,Chat.class);
                i.putExtra("email", UserEmail);
                startActivity(i);
            }
        });*/

        //Populate Content Area with Fragment
    /*    fragment = new ShopListingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.screen_area,fragment);
        fragmentTransaction.commit();*/
        getSupportActionBar().setTitle("Main");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void logOut() {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
        //noinspection SimplifiableIfStatement


            case R.id.menu_search:
                Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_createAd:
                fragment= new CreateAdFragment();
                goFragment(fragment,R.id.screen_area);
                return true;

            case R.id.menu_logout:
                logOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;

            //return super.onOptionsItemSelected(item);
        }
        return false;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.navigation_shop:
                    //mTextMessage.setText(R.string.title_chat);
                    Toast.makeText(NavigationActivity.this, "My Shops", Toast.LENGTH_SHORT).show();
                    fragment= new ShopListFragment();
                    goFragment(fragment,R.id.screen_area);
                    getSupportActionBar().setTitle("Shop List");
                    return true;

                case R.id.navigation_chat:
                    //mTextMessage.setText(R.string.title_chat);
                    Toast.makeText(NavigationActivity.this, "Chat", Toast.LENGTH_SHORT).show();

                    //TODO: Chat

                    return true;
                case R.id.navigation_advert:
                   // mTextMessage.setText(R.string.title_bookmark);
                    Toast.makeText(NavigationActivity.this, "My Ads", Toast.LENGTH_SHORT).show();
                   fragment = new ShopAdFragment();
                    goFragment(fragment,R.id.screen_area);
                    return true;

            }
            return false;
        }
    };
    public void goFragment(Fragment fragment, int toReplace){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //For replace: refers to the FrameLayout in "content_main"
        ft.replace(toReplace,fragment)
                .addToBackStack(null)
                .commit();
    }
    //Get Firebase Uid
    public String getUid() {
        return mAuth.getCurrentUser().getUid();
    }

}
