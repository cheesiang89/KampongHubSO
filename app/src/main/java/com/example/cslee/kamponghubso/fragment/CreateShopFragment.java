package com.example.cslee.kamponghubso.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslee.kamponghubso.NavigationActivity;
import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.models.Shop;
import com.example.cslee.kamponghubso.models.User;
import com.example.cslee.kamponghubso.utilities.Calculations;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Fragment is to allow shop owner to create shop
 * Details include:
 * ShopName, ShopImage, ShopAddress, ShopPostal
 * Calculated: shopLatitude, shopLongtitude, zone
 */
public class CreateShopFragment extends Fragment implements View.OnClickListener, TimePickerFragment.TimePickerDialogFragmentEvents {
    private TextView startTime;
    private TextView endTime;
    private TextView shopAddress;
    private TextView shopPostal;
    private TextView shopName;
    private Button btnCreate;
    private Button btnUploadImage;
    private ImageView createPicture;
    private Bitmap selectedPicture;

    public static final int PICK_IMAGE = 1;
    private static final String REQUIRED = "Required";
    private static final String TAG = "CreateShop";

    //Firebase ref
    private DatabaseReference mDatabase;

    public CreateShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_shop, container, false);
        startTime = (TextView) view.findViewById(R.id.startTime);
        endTime = (TextView) view.findViewById(R.id.endTime);
        shopAddress = (TextView) view.findViewById(R.id.shopAddress);
        shopPostal = (TextView) view.findViewById(R.id.shopPostal);
        shopName = (TextView) view.findViewById(R.id.shopName);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        //For image
        btnUploadImage=(Button)view.findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(this);

        createPicture=(ImageView)view.findViewById(R.id.createPicture);

        btnCreate = (Button)view.findViewById(R.id.btnCreateShop);
        btnCreate.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.startTime) {
            TimePickerFragment startTimeFragment = new TimePickerFragment();
            startTimeFragment.setFlag(TimePickerFragment.FLAG_START_TIME);
            startTimeFragment.setTimePickerDialogFragmentEvents(CreateShopFragment.this); //Changed
            startTimeFragment.show(getActivity().getFragmentManager(), "Time Picker");
        } else if (id == R.id.endTime) {
            TimePickerFragment endTimeFragment = new TimePickerFragment();
            endTimeFragment.setFlag(TimePickerFragment.FLAG_END_TIME);
            endTimeFragment.setTimePickerDialogFragmentEvents(CreateShopFragment.this); //Changed
            endTimeFragment.show(getActivity().getFragmentManager(), "Time Picker");
        }else if (id==R.id.btnUploadImage){
            // Select picture and populate ImageView after
            getPicture();
        }else if(id==R.id.btnCreateShop){
            // Create Shop
            createShop();
        }
    }

    @Override
    public void onTimeSelected(String time, int flag) {
        if (flag == 0) {
            startTime.setText(time);
        } else if (flag == 1) {
            endTime.setText(time);
        }
    }
    public void getPicture() {
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

    //Check ImageView has image
    private boolean hasImage(ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return hasImage;
    }

    //Clear screen after shop created
    private void clearScreen(){
        startTime.setText("");
        startTime.clearFocus();
        endTime.setText("");
        endTime.clearFocus();
        shopAddress.setText("");
        shopAddress.clearFocus();
        shopPostal.setText("");
        shopPostal.clearFocus();
        shopName.setText("");
        shopName.clearFocus();

        createPicture.setImageDrawable(null);
    }

    //Create shop
    private void createShop() {
      final String shopTitle = shopName.getText().toString().trim();
       final String address = shopAddress.getText().toString().trim();
        final String postal = shopPostal.getText().toString().trim();
       final String sTime = startTime.getText().toString().trim();
        final String eTime = endTime.getText().toString().trim();

        // Check all required fields filled
        if (TextUtils.isEmpty(shopTitle)) {
            shopName.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            shopAddress.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(postal)) {
            shopPostal.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(sTime)) {
            startTime.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(eTime)) {
            endTime.setError(REQUIRED);
            return;
        }

        // Set default image if no picture selected
        if (!hasImage(createPicture)) {
            selectedPicture= BitmapFactory.decodeResource(getResources(), R.drawable.ic_store_black_24dp);

        }
        final String shopImage = Calculations.bitmapToBase64(selectedPicture);

        // Disable "Create" button so only after all info gathered then Create Shop
        setEditingEnabled(false);
        Toast.makeText(getActivity(), "Creating Shop...", Toast.LENGTH_SHORT).show();
        // [START single_value_read]
        final String userId = ((NavigationActivity)getActivity()).getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
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
                            Shop shop = new Shop(userId, shopTitle, shopImage,
                                    address, postal, sTime, eTime);

                            createEntries(shop);
                            Toast.makeText(getActivity(),
                                    "Shop Created",
                                    Toast.LENGTH_SHORT).show();
                            clearScreen();
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);

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
        // [END single_value_read]
    }
    private void setEditingEnabled(boolean enabled) {
        startTime.setEnabled(enabled);
        endTime.setEnabled(enabled);
        shopAddress.setEnabled(enabled);
        shopPostal.setEnabled(enabled);
        shopName.setEnabled(enabled);

        createPicture.setEnabled(enabled);
        if (enabled) {
            btnCreate.setVisibility(View.VISIBLE);
        } else {
            btnCreate.setVisibility(View.GONE);
        }
    }
    private void createEntries(Shop shop) {
        // Create shop at /shops/ and /user/shops simultaneously
        String shopKey = mDatabase.child("shops").push().getKey();

        //To create new shop. If need to update multiple places, have more entries in "ChildUpdates"
        Map<String, Object> shopValues = shop.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/shops/"+ shopKey, shopValues);
        childUpdates.put("/users/" + shop.getShopOwnerUid()+"/"+"shops/"+shopKey, shopValues);

        mDatabase.updateChildren(childUpdates);

        //Go to RetrievalFragment to show updates
       /* Fragment fragment = new RetrieveTestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RetrieveTestFragment.EXTRA_SHOP_KEY, shopKey);
        fragment.setArguments(bundle);
        ((NavigationActivity)getActivity()).goFragment(fragment,R.id.screen_area);*/

    }

}
