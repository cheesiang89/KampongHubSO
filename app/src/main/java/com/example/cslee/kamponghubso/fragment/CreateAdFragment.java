package com.example.cslee.kamponghubso.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cslee.kamponghubso.NavigationActivity;
import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.models.Advert;
import com.example.cslee.kamponghubso.models.Shop;
import com.example.cslee.kamponghubso.models.User;
import com.example.cslee.kamponghubso.utilities.Calculations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAdFragment extends Fragment implements View.OnClickListener {


    private Button btnCreate;
    private Button btnUploadImage;
    private Button btnDeleteImage;
    private ImageView createPicture;
    private Bitmap selectedPicture;
    public static final int PICK_IMAGE = 1;

    private static final String TAG = "CreateAd";
    private ProgressDialog dialog;
    String userId;
    //Firebase ref
    private DatabaseReference mDatabase;

    public CreateAdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId= ((NavigationActivity)getActivity()).getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_ad, container, false);
        //For image
        btnUploadImage = (Button) view.findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(this);
        btnDeleteImage = (Button) view.findViewById(R.id.btnDeleteImage);
        btnDeleteImage.setOnClickListener(this);
        btnDeleteImage.setEnabled(false);
        btnDeleteImage.setVisibility(View.GONE);

        createPicture = (ImageView) view.findViewById(R.id.createPicture);
        createPicture.setVisibility(View.GONE);

        btnCreate = (Button) view.findViewById(R.id.btnCreateShop);
        btnCreate.setOnClickListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Ad");

        mDatabase.child("users").child(userId).child("shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> names = new ArrayList<String>();
                final List<String> nameID = new ArrayList<String>();

                for (DataSnapshot shopSnapshot: dataSnapshot.getChildren()) {
                    String shopName = shopSnapshot.child("shopName").getValue(String.class);
                    String shopID = shopSnapshot.getRef().getKey().toString();
                    names.add(shopName);
                    nameID.add(shopID);
                }

                Spinner spinnerName = (Spinner) view.findViewById(R.id.spinnerName);
                ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, names);
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerName.setAdapter(nameAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnUploadImage) {
            // Select picture and populate ImageView after
            getPicture();
        } else if (id == R.id.btnDeleteImage) {
            // Select picture and populate ImageView after
            deletePicture();
        } else if (id == R.id.btnCreateShop) {
            // Create Shop
            createAd();
        }
    }

    private void createAd() {
        // Set default image if no picture selected
        if (!hasImage(createPicture)) {
            selectedPicture= BitmapFactory.decodeResource(getResources(), R.drawable.no_image);

        }
        final String adImage = Calculations.bitmapToBase64(selectedPicture);
        // Disable "Create" button so only after all info gathered then Create Advert
        setEditingEnabled(false);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Saving Advert...");
        dialog.show();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                User user = dataSnapshot.getValue(User.class);

                // [START_EXCLUDE]
                if (user == null) {
                    // User is null, error out
                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                    Toast.makeText(getActivity(),
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Create New Shop
                    try {
                        Advert advert = new Advert();
                        advert.setAdImage(adImage);
                        createEntries(advert);
                             /*   Toast.makeText(getActivity(),
                                        "Shop Created",
                                        Toast.LENGTH_SHORT).show();*/
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        // Go back Listing of Ads
                        Fragment newFragment= new ShopAdFragment();
                        ((NavigationActivity)getActivity()).goFragment(newFragment,R.id.screen_area);
                    } catch(Exception ex){
                        //Check postal code validity here only
                        Log.e(TAG, "on Create Shop: "+ex.getMessage() );
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(getActivity(),
                                "Error. Check your entry",
                                Toast.LENGTH_LONG).show();
                        // Back to the screen
                        setEditingEnabled(true);
                    }
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                setEditingEnabled(true);
                // [END_EXCLUDE]
            }
        });
    }

    private void getPicture() {
        //Choose picture from Gallery
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(Intent.createChooser(chooserIntent, "Select Picture"), PICK_IMAGE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            //Populate "createPicture" with choosen image
            final Uri imageUri = data.getData();
            selectedPicture = getImageFromPhone(imageUri);
            if (selectedPicture!=null) {
                createPicture.setImageBitmap(selectedPicture);
                btnDeleteImage.setEnabled(true);
                btnDeleteImage.setVisibility(View.VISIBLE);
                createPicture.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
    private Bitmap getImageFromPhone(Uri imageUri) {
        Bitmap pic = null;
        try {
            final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
            pic= BitmapFactory.decodeStream(imageStream);
        } catch (Exception e) {
            Log.e("GALLERYERROR", e.getMessage());
        }
        return  pic;
    }
    private void deletePicture() {
        if (hasImage(createPicture)) {
            //Delete image in "createPicture"
            createPicture.setImageDrawable(null);
            createPicture.setVisibility(View.GONE);
            btnDeleteImage.setVisibility(View.GONE);
        }
    }
    //Check ImageView has image
    private boolean hasImage(ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return hasImage;
    }
    private void setEditingEnabled(boolean enabled) {

        createPicture.setEnabled(enabled);
        if (enabled) {
            btnCreate.setVisibility(View.VISIBLE);
        } else {
            btnCreate.setVisibility(View.GONE);
        }
    }
    private void createEntries(Advert advert) {
        // Create advert at:
        // 1. /ads/adID (user will retrieve ads after seeing shop details (i.e. get shopID)
        // 2. /user/userID/ads/adID
        // 3. /shops/shopID--adID (have a ref to the ad) simultaneously
        /*String zone = shop.getShopZone();
        String adKey = mDatabase.child("ads").push().getKey();

        //To create new shop. If need to update multiple places, have more entries in "ChildUpdates"
        Map<String, Object> shopValues = shop.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/shops/"+zone+"/"+ shopKey, shopValues);
        childUpdates.put("/users/" + shop.getShopOwnerUid()+"/"+"shops/"+shopKey, shopValues);

        mDatabase.updateChildren(childUpdates);*/
    }
}
