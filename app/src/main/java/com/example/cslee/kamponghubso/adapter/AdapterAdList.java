package com.example.cslee.kamponghubso.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cslee.kamponghubso.NavigationActivity;
import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.fragment.EditShopFragment;
import com.example.cslee.kamponghubso.fragment.ShopAdDetailFragment;
import com.example.cslee.kamponghubso.models.Advert;
import com.example.cslee.kamponghubso.models.Shop;
import com.example.cslee.kamponghubso.viewholder.AdListHolder;
import com.example.cslee.kamponghubso.viewholder.ShopListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by CSLee on 8/1/2018.
 */

public class AdapterAdList extends FirebaseRecyclerAdapter<Advert, AdListHolder> {
   private Fragment fragment;

    public AdapterAdList(@NonNull FirebaseRecyclerOptions<Advert> options) {
        super(options);
    }
    public AdapterAdList(@NonNull FirebaseRecyclerOptions<Advert> options, Fragment fragment) {
        super(options);
        this.fragment=fragment;
         }
    @Override
    public AdListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new AdListHolder(inflater.inflate(R.layout.ad_list_item, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder(AdListHolder viewHolder, int position, final Advert model) {
        //TODO: Method to be added later: To show store details
        final DatabaseReference adRef = getRef(position);

        // Set click listener for the ad view
        final String adKey = adRef.getKey();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch ShopDetailActivity
                Fragment newFragment= new ShopAdDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ShopAdDetailFragment.SHOP_AD_KEY, adKey);
                newFragment.setArguments(bundle);
                ((NavigationActivity)fragment.getActivity()).goFragment(newFragment,R.id.screen_area);
            }
        });
        viewHolder.bindToList(model);
    }
  }
