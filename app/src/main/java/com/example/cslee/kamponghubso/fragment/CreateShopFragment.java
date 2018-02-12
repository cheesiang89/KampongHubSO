package com.example.cslee.kamponghubso.fragment;


import android.app.Activity;
import android.app.Dialog;
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
import java.util.Iterator;
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
    private TextView shopBlk;
    private TextView shopUnit;
    private TextView shopBuilding;
    private TextView shopStreet;
    private TextView shopPostal;
    private TextView shopName;
    private TextView shopDescription;
    private TextView phoneNumber;
    private Button btnCreate;
    private Button btnUploadImage;
    private Button btnDeleteImage;
    private ImageView createPicture;
    private Bitmap selectedPicture;

    private ProgressDialog dialog;

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
        shopBlk = (EditText) view.findViewById(R.id.shopBlk);
        shopUnit = (EditText) view.findViewById(R.id.shopUnit);
        shopBuilding = (EditText) view.findViewById(R.id.shopBuilding);
        shopStreet = (EditText) view.findViewById(R.id.shopStreet);


        shopPostal = (EditText) view.findViewById(R.id.shopPostal);
        shopName = (EditText) view.findViewById(R.id.shopName);
        shopDescription=(EditText) view.findViewById(R.id.shopDescription);
        phoneNumber=(EditText) view.findViewById(R.id.phoneNumber);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        //Set text change listeners to get rid of required field error
        addTextListeners();

        //For image
        btnUploadImage=(Button)view.findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(this);
        btnDeleteImage=(Button)view.findViewById(R.id.btnDeleteImage);
        btnDeleteImage.setOnClickListener(this);
        btnDeleteImage.setEnabled(false);
        btnDeleteImage.setVisibility(View.GONE);

        createPicture=(ImageView)view.findViewById(R.id.createPicture);
        createPicture.setVisibility(View.GONE);

        btnCreate = (Button)view.findViewById(R.id.btnCreateShop);
        btnCreate.setOnClickListener(this);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Shop");
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
        }else if (id==R.id.btnDeleteImage){
            // Select picture and populate ImageView after
            deletePicture();
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
    private void deletePicture() {
        if (hasImage(createPicture)) {
            //Delete image in "createPicture"
           createPicture.setImageDrawable(null);
            createPicture.setVisibility(View.GONE);
            btnDeleteImage.setVisibility(View.GONE);
        }
    }
    private void addTextListeners(){
        //Clear error messages on text change of required fields
        shopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        startTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startTime.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        endTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                endTime.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shopBlk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopBlk.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shopUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopUnit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shopStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopStreet.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shopPostal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopPostal.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shopDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopDescription.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        shopBlk.setText("");
        shopBlk.clearFocus();
        shopUnit.setText("");
        shopUnit.clearFocus();
        shopBuilding.setText("");
        shopBuilding.clearFocus();
        shopStreet.setText("");
        shopStreet.clearFocus();
        shopPostal.setText("");
        shopPostal.clearFocus();
        shopName.setText("");
        shopName.clearFocus();
        shopDescription.setText("");
        shopDescription.clearFocus();
        phoneNumber.setText("");
        phoneNumber.clearFocus();
        createPicture.setImageDrawable(null);
        createPicture.setVisibility(View.GONE);
        btnDeleteImage.setVisibility(View.GONE);
    }

    //Create shop
    private void createShop() {
      final String shopTitle = shopName.getText().toString().trim();
       final String blk = shopBlk.getText().toString().trim();
        final String unit = shopUnit.getText().toString().trim();
        final String building = shopBuilding.getText().toString().trim();
        final String street = shopStreet.getText().toString().trim();

        final String postal = shopPostal.getText().toString().trim();
        //Address is combination of 4 fields + Postal (for easy retrieval in User App
        final String address = blk+","+unit+","+building+","+street+",S"+postal;

       final String sTime = startTime.getText().toString().trim();
        final String eTime = endTime.getText().toString().trim();
        final String description = shopDescription.getText().toString().trim();
        final String phone = phoneNumber.getText().toString().trim();
        //Check fields
        Map<Object,String> requiredControls = new HashMap<>();
        requiredControls.put(shopName,shopTitle);
        requiredControls.put(shopBlk,blk);
        requiredControls.put(shopUnit,unit);
        requiredControls.put(shopStreet,street);
        requiredControls.put(startTime,sTime);
        requiredControls.put(endTime,eTime);
         //Postal and phone need extra check
        requiredControls.put(shopPostal,postal);
        requiredControls.put(phoneNumber,phone);
        //if got error
        if(validateFields(requiredControls)){
            return;
        };

        // Set default image if no picture selected
        if (!hasImage(createPicture)) {
            selectedPicture= BitmapFactory.decodeResource(getResources(), R.drawable.no_image);

        }
        final String shopImage = Calculations.bitmapToBase64(selectedPicture);

        // Disable "Create" button so only after all info gathered then Create Shop
        setEditingEnabled(false);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Creating shop...");
        dialog.show();
       /* Toast.makeText(getActivity(), "Creating Shop...", Toast.LENGTH_SHORT).show();*/
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
                            try {
                                Shop shop = new Shop(userId,
                                        shopTitle, shopImage,
                                        address, postal,
                                        sTime, eTime,
                                        description, phone,
                                        getContext());
                                createEntries(shop);
                             /*   Toast.makeText(getActivity(),
                                        "Shop Created",
                                        Toast.LENGTH_SHORT).show();*/
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                clearScreen();
                                // Go back ShopList
                                Fragment newFragment= new ShopListFragment();
                                ((NavigationActivity)getActivity()).goFragment(newFragment,R.id.screen_area);
                            }catch(IndexOutOfBoundsException iobe){
                                //Check postal code validity here only
                                Log.e(TAG, "on Create Shop: "+iobe.getMessage() );
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                shopPostal.setError("Postal code invalid");
                                // Back to the screen
                                setEditingEnabled(true);
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
        // [END single_value_read]
    }
    private boolean validateFields(Map<Object,String> controls){
       boolean gotErrors = false;
        // Check all required fields filled
        Iterator it = controls.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (TextUtils.isEmpty(pair.getValue().toString())) {
                ((TextView) pair.getKey()).setError(REQUIRED);
                gotErrors=true;
            }
            //Extra check for Phonenumber and Postal (partial check: only 1st 2 digits valid, back 4 follows unknown logic)
            if((pair.getKey()).equals(phoneNumber)&& !TextUtils.isEmpty(pair.getValue().toString())){
                if(!Calculations.checkTelephoneValid(pair.getValue().toString())){
                    ((TextView) pair.getKey()).setError("Phone number invalid");
                    gotErrors=true;
                }
            }

            if((pair.getKey()).equals(shopPostal)&& !TextUtils.isEmpty(pair.getValue().toString())){
                if(!Calculations.checkPostalValid(pair.getValue().toString())){
                    ((TextView) pair.getKey()).setError("Postal Code invalid");
                    gotErrors=true;
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return gotErrors;
    }


    private void setEditingEnabled(boolean enabled) {
        startTime.setEnabled(enabled);
        endTime.setEnabled(enabled);
        shopBlk.setEnabled(enabled);
        shopUnit.setEnabled(enabled);
        shopBuilding.setEnabled(enabled);
        shopStreet.setEnabled(enabled);
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
