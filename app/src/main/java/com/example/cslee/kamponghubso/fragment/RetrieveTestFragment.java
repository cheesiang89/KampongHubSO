package com.example.cslee.kamponghubso.fragment;


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
import com.google.firebase.database.ValueEventListener;
import com.example.cslee.kamponghubso.models.Shop;
/**
 * A simple {@link Fragment} subclass.
 */
public class RetrieveTestFragment extends Fragment {
    //Firebase variables
    private DatabaseReference mShopReference;

    //UI
    private TextView shopName;
    private ImageView shopPicture;
    private ProgressDialog dialog;

    //Constant
    private static final String TAG = "RetrieveTest";
    public static final String EXTRA_SHOP_KEY = "shop_key";
    private static String shopKey;

    public RetrieveTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get shop key from intent
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shopKey = bundle.getString(EXTRA_SHOP_KEY , null);
            if (shopKey == null) {
                mShopReference = FirebaseDatabase.getInstance().getReference()
                        .child("shops");
                throw new IllegalArgumentException("Must pass EXTRA_SHOP_KEY");
            }
            else{
                // [START create_database_reference]
                mShopReference = FirebaseDatabase.getInstance().getReference()
                        .child("shops").child(shopKey);
                // [END create_database_reference]

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_retrieve_test, container, false);
        shopName = (TextView)view.findViewById(R.id.shopName);
        shopPicture = (ImageView) view.findViewById(R.id.shopPicture);
         dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading data.");
        dialog.show();

        if (shopKey!=null) {
            //Get data
            // Attach a listener to read the data at shops reference
            mShopReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Shop shop = dataSnapshot.getValue(Shop.class);

                    shopName.setText(shop.getShopName());
                     shopPicture.setImageBitmap(Calculations.base64ToBitmap(shop.getShopImage()));
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
            });
        }else{
            dialog.dismiss();
        }
        return view;
    }


}
