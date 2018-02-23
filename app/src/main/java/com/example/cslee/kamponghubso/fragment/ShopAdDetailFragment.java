package com.example.cslee.kamponghubso.fragment;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslee.kamponghubso.NavigationActivity;
import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.models.Advert;
import com.example.cslee.kamponghubso.utilities.Calculations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopAdDetailFragment extends Fragment {

    public static final String SHOP_AD_KEY = "shop_ad_key";
    private static final String TAG = ShopAdDetailFragment.class.getSimpleName();

    //Layout
    private TextView shopName;
    private ImageView adImage;
    private TextView adDesc;

    private Fragment fragment;
    private ProgressDialog dialog;


    //Firebase variables
    private DatabaseReference mAdReference;
    private String mAdKey;

    //Model
    Advert advert;

    public ShopAdDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_ad_detail, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ad Detail");
        //Set progress dialog
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading data.");
        dialog.show();

        // Get ad key from intent
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mAdKey = bundle.getString(SHOP_AD_KEY, null);
            if (mAdKey == null) {
                throw new IllegalArgumentException("Must pass SHOP_AD_KEY");
            }

        }
        final String userId = ((NavigationActivity) getActivity()).getUid();

        // Initialize Database
        mAdReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("ads").child(mAdKey);

        // Initialize Views
        adImage = view.findViewById(R.id.adImage);
        shopName = view.findViewById(R.id.shopName);
        shopName.setSelected(true);
        adDesc = view.findViewById(R.id.adDesc);


        //Get data
        // Attach a listener to read the data at ads reference
        mAdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                advert = dataSnapshot.getValue(Advert.class);
                String base64String = advert.getAdImage();
                try {
                    Bitmap b = Calculations.base64ToBitmap(base64String, 1000, 1000);
                    adImage.setImageBitmap(b);
                }catch(Exception e) {
                    Log.e(TAG,"Error: "+e.getMessage());
                }
                shopName.setText(advert.getShopName());
                adDesc.setText(advert.getAdDescription());
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Ad failed, log a message
                Log.w(TAG, "loadAd:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(getContext(), "Failed to load ad details.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        });

        return view;
    }

}
