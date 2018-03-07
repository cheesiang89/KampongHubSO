package com.example.cslee.kamponghubso.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.adapter.ChatListingAdapter;
import com.example.cslee.kamponghubso.models.ChatMessage;
import com.example.cslee.kamponghubso.models.MyApplication;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

//Uses in class FirebaseRecycleAdapter

public class ChatListingFragment extends Fragment {

    public ChatListingFragment() {
        // Required empty public constructor
    }

    //This constant is for easy referencing for Log purposes
    private static final String TAG = ChatListingFragment.class.getSimpleName();

    //Layout
    private RecyclerView chatListView;
    private LinearLayoutManager layoutManager;
    private ChatListingAdapter chatAdapter;
    private ProgressDialog dialog;
    private MyApplication myApp;
    private List<ChatMessage> latestMsgList;


    //Firebase variables
    private DatabaseReference mDatabase, latestChatDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat_listing, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Inbox");
        myApp = (MyApplication) getActivity().getApplicationContext();

        //gets the receiver branch of latestChatMsg, this is impt to get the reference for both sender and receiver in chatMSG
        mDatabase = FirebaseDatabase.getInstance().getReference(); //this gets a reference of the root database

        chatListView = rootView.findViewById(R.id.chatListRecyclerView);
        chatListView.setHasFixedSize(true);
        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Loading.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        // Set up Layout Manager, reverse layout
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatListView.setLayoutManager(layoutManager);
        latestMsgList = new ArrayList<>();

        // Set up FirebaseRecyclerAdapter with the Query
        latestChatDB = mDatabase.child("latestChatList").child("receiver").child(myApp.getUID());
        getShopID(latestChatDB);
        getShopName(latestChatDB);
        Query chatQuery = latestChatDB.limitToFirst(100);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(chatQuery, ChatMessage.class)
                .build();

        //FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChatMessage>().setSnapshotArray(listings).build();
        //Configure adapter
        chatAdapter = new ChatListingAdapter(options,this, latestMsgList) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        };
        //Set divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(chatListView.getContext(),
                layoutManager.getOrientation());
        chatListView.addItemDecoration(dividerItemDecoration);

        //Set adapter
        chatListView.setAdapter(chatAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        if (chatAdapter != null) {
            chatAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (chatAdapter != null) {
            chatAdapter.stopListening();
        }
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // public abstract Query getQuery(DatabaseReference databaseReference);

    //Method to get the id of the user sending to the shop
    public void getShopID(DatabaseReference databaseReference) {
        databaseReference.child("last").child("sender").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String shopUID = dataSnapshot.getValue(String.class);
                    myApp.setShopID(shopUID);
                    //myApp.setShopID(dataSnapshot.getValue(String.class));
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getShopName(DatabaseReference databaseReference) {
        databaseReference.child("last").child("senderName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String shopName = dataSnapshot.getValue(String.class);
                myApp.setShopName(shopName);
                //myApp.setShopID(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
