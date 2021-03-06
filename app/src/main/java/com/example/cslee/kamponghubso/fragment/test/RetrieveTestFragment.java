package com.example.cslee.kamponghubso.fragment.test;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslee.kamponghubso.NavigationActivity;
import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.utilities.Calculations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.cslee.kamponghubso.models.Shop;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class RetrieveTestFragment extends Fragment {
    //Firebase variables
    private DatabaseReference mShopReference;
    private Query query;
    //UI
    private TextView shopName;
    private ImageView shopPicture;
    private ProgressDialog dialog;

    //Constant
    private static final String TAG = "RetrieveTest";
    String userId;

    public RetrieveTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = ((NavigationActivity) getActivity()).getUid();
        // [START create_database_reference]
        mShopReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("shops");
        query = mShopReference.orderByKey().limitToLast(1);
        // [END create_database_reference]
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_retrieve_test, container, false);
        shopName = (TextView) view.findViewById(R.id.shopName);
        shopPicture = (ImageView) view.findViewById(R.id.shopPicture);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading data.");
        dialog.show();

            //Get data
            // Attach a listener to read the data at shops reference
            /*mShopReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    printShops((Map<String, Object>) dataSnapshot.getValue());
                    //Shop shop = dataSnapshot.getValue(Shop.class);

                    *//*shopName.setText(shop.getShopName());
                    Bitmap b =Calculations.base64ToBitmap(shop.getShopImage());
                    shopPicture.setImageBitmap(b);*//*
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Shop failed, log a message
                    Log.w(TAG, "loadShop:onCancelled", databaseError.toException());
                    // [START_EXCLUDE]
                    Toast.makeText(getContext(), "Failed to load shop details.",
                            Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
            });*/
        query.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop shop = dataSnapshot.getValue(Shop.class);
                Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                Map.Entry<String, Object > next = map.entrySet().iterator().next();
                Map<String, String > custShop = (Map<String, String >) next.getValue();
                String name = custShop.get("shopName");
                String picture= custShop.get("shopImage");
                shopName.setText(name);
                Bitmap b =Calculations.base64ToBitmap(picture,1000,600);
                shopPicture.setImageBitmap(b);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //Loop thorugh all child nodes (NOT USED)
    private void printShops(Map<String, Object> shops) {

        String a;
        //iterate through each user, ignoring their UID
        //Only get one Value
        int i = 0;
        for (
                Map.Entry<String, Object> shop : shops.entrySet()) {

            //Get Shop map
            Map singleShop = (Map) shop.getValue();
            if (i == 0) {
                shopName.setText((String) singleShop.get("shopName"));
                Bitmap b = Calculations.base64ToBitmap((String) singleShop.get("shopImage"),1000,600);
                shopPicture.setImageBitmap(b);
            } else
                a = (String) singleShop.get("shopName");
            i++;
        }
        i = 0;

    }
}
