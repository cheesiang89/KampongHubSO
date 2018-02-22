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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAdFragment extends Fragment implements View.OnClickListener {



    private Button btnUploadImage;
    private Button btnDeleteImage;
    private ImageView createPicture;
    private Bitmap selectedPicture;
    private Spinner spinnerName;
    private TextView lblShopNameRequired;
    private TextView lblImageRequired;
    private Button btnCreateAdvert;
    private TextView adDescription;

    public static final int PICK_IMAGE = 1;
    private static final String TAG = "CreateAd";
    private static final String REQUIRED = "Required";

    private boolean gotError = false;

    private List<String> names;
    private List<String> nameID ;
    private String chosenNameID="";
    private String chosenName="";
    private String userId="";
    private String shopZone="";


    private ProgressDialog dialog;

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
        //For description
        adDescription=(EditText) view.findViewById(R.id.adDescription);
        //For image
        btnUploadImage = (Button) view.findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(this);
        btnDeleteImage = (Button) view.findViewById(R.id.btnDeleteImage);
        btnDeleteImage.setOnClickListener(this);
        btnDeleteImage.setEnabled(false);
        btnDeleteImage.setVisibility(View.GONE);

        createPicture = (ImageView) view.findViewById(R.id.createPicture);
        createPicture.setVisibility(View.GONE);

        btnCreateAdvert = (Button) view.findViewById(R.id.btnCreateAdvert);
        btnCreateAdvert.setOnClickListener(this);

        //Create default empty value for spinner
         names= new ArrayList<String>();
        nameID = new ArrayList<String>();
        names.add("");
        nameID.add("");
       spinnerName = (Spinner) view.findViewById(R.id.spinnerName);
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                chosenNameID = nameID.get(pos);
                chosenName=spinnerName.getSelectedItem().toString();
                //Reset "Required" error label
                lblShopNameRequired.setVisibility(View.GONE);
                gotError=false;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //For error message
        lblShopNameRequired=(TextView)view.findViewById(R.id.lblShopNameRequired);
        lblShopNameRequired.setVisibility(View.GONE);
        lblImageRequired=(TextView)view.findViewById(R.id.lblImageRequired);
        lblImageRequired.setVisibility(View.GONE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create Ad");

        mDatabase.child("users").child(userId).child("shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array


                for (DataSnapshot shopSnapshot: dataSnapshot.getChildren()) {
                    String shopName = shopSnapshot.child("shopName").getValue(String.class);
                    String shopID = shopSnapshot.getRef().getKey().toString();
                    shopZone=shopSnapshot.child("shopZone").getValue(String.class);
                    names.add(shopName);
                    nameID.add(shopID);
                }


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
        } else if (id == R.id.btnCreateAdvert) {
            // Create Shop
            createAd();
        }
    }

    private void createAd() {
        // Set default image if no picture selected
      /*  if (!hasImage(createPicture)) {
            selectedPicture= BitmapFactory.decodeResource(getResources(), R.drawable.no_image);

        }*/
        //Ensure required fields filled
        //1.ImageView
        if (!hasImage(createPicture)) {
            lblImageRequired.setVisibility(View.VISIBLE);
            gotError=true;
        }
        //2. Spinner
        if (chosenNameID == null || chosenNameID.isEmpty()) {
           lblShopNameRequired.setVisibility(View.VISIBLE);
            gotError=true;
        }
        if(gotError) return;

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
                        Date currentTime = Calendar.getInstance().getTime();
                        final String adDesc = adDescription.getText().toString().trim();
                        //Create advert
                        Advert advert = new Advert();
                        advert.setAdImage(adImage);
                        advert.setShopId(chosenNameID);
                        advert.setShopName(chosenName);
                        advert.setAdDate(currentTime.toString());
                        advert.setAdDescription(adDesc);
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
                //Reset "Required" error label
                lblImageRequired.setVisibility(View.GONE);
                gotError=false;
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

        if (enabled) {
            btnCreateAdvert.setVisibility(View.VISIBLE);
        } else {
            btnCreateAdvert.setVisibility(View.GONE);
        }
    }

    private void createEntries(Advert advert) {
        // Create advert at:
        // 1./user/userID/ads/adID (for shopOwner)
        // 2. /user/userID/shops/shopID/adID (for consistency)
        // 3. /shops/shopID/ads--adID (user will retrieve ads after seeing shop details (i.e. get shopID)) simultaneously
       if(chosenNameID!=null && !chosenNameID.isEmpty()) {
           if (userId!=null && !userId.isEmpty()) {
               if(shopZone!=null && !shopZone.isEmpty()) {
                   String adKey = mDatabase.child("ads").push().getKey();

                   //To create new advert. If need to update multiple places, have more entries in "ChildUpdates"
                   Map<String, Object> advertValues = advert.toMap();
                   Map<String, Object> childUpdates = new HashMap<>();

                   childUpdates.put("/users/" + userId + "/" + "ads/" + adKey, advertValues);
                   childUpdates.put("/users/" + userId + "/" + "shops/" + chosenNameID + "/" + "ads/" + adKey, advertValues);
                   childUpdates.put("/shops/" +shopZone+"/"+ chosenNameID + "/" + "ads/" + adKey, advertValues);

                   mDatabase.updateChildren(childUpdates);
               }
           }
       }
    }
}
