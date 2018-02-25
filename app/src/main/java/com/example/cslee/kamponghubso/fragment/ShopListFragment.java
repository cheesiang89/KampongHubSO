package com.example.cslee.kamponghubso.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.adapter.AdapterShopList;
import com.example.cslee.kamponghubso.models.Shop;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopListFragment extends Fragment {

    //This constant is for easy referencing for Log purposes
    private static final String TAG = ShopListFragment.class.getSimpleName();

    //Layout
    private RecyclerView rvShopList;
    private LinearLayoutManager layoutManager;
    private AdapterShopList mFirebaseAdapter;
    private ProgressDialog dialog;
    private SearchView mSearchView;


    //Firebase variables
    private DatabaseReference mDatabase;
    private ChildEventListener mShopListener; //not used

    //Model
    Shop shop;

    //Empty constructor
    public ShopListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_shop_list, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Shops");
         // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        rvShopList = rootView.findViewById(R.id.shopListRecyclerView);
        mSearchView = (SearchView) rootView.findViewById(R.id.menu_search);
        rvShopList.setHasFixedSize(true);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading data.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvShopList.setLayoutManager(layoutManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query shopQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Shop>()
                .setQuery(shopQuery, Shop.class)
                .build();

        //Configure adapter
        mFirebaseAdapter = new AdapterShopList(options,this) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        };
        //Set divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvShopList.getContext(),
                layoutManager.getOrientation());
        rvShopList.addItemDecoration(dividerItemDecoration);

        //Set adapter
        rvShopList.setAdapter(mFirebaseAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirebaseAdapter != null) {
            mFirebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFirebaseAdapter != null) {
            mFirebaseAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    //TODO: Method necessary to tweak query in later stage (i.e. make this abstract class)
    // public abstract Query getQuery(DatabaseReference databaseReference);

    //Method can be placed in inherited class later on
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_store_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentStoreQuery = databaseReference.child("users").child(getUid()).child("shops")
                .limitToFirst(100);
        // [END recent_store_query]

        return recentStoreQuery;
    }



}
