package com.example.cslee.kamponghubso.fragment.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.cslee.kamponghubso.NavigationActivity;
import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.models.Shop;
import com.example.cslee.kamponghubso.models.User;
import com.example.cslee.kamponghubso.utilities.Calculations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import 	android.graphics.drawable.Drawable;
import 	android.graphics.drawable.BitmapDrawable;

//Purpose of fragment:
// 1. Create a shop entry with Shopname + ShopImage
//2. Can upload picture, to be retrieved in other fragment

public class CreateTestFragment extends Fragment{
    private Button btnCreate;
    private Button btnUploadImage;
    private EditText createName;
    private ImageView createPicture;
    private TextView errorMessage;
    private Bitmap selectedPicture;
    //Firebase ref
    private DatabaseReference mDatabase;

    public static final int PICK_IMAGE = 1;
    private static final String TAG = "CreateShop";
    private static final String REQUIRED = "Required";

    public CreateTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // Inflate the layout for this fragment
                View view= inflater.inflate(R.layout.fragment_create_test, container, false);
                btnCreate = (Button)view.findViewById(R.id.btnCreateShop);
                btnUploadImage=(Button)view.findViewById(R.id.btnUploadImage);
        createPicture=(ImageView)view.findViewById(R.id.createPicture);
                createName = (EditText)view.findViewById(R.id.createName);
                errorMessage=(TextView)view.findViewById(R.id.errorMessage);
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Select picture and populate ImageView after
                getPicture();
            }
        });
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create Shop
                        createShop();
                    }
                });

                return view;
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
                errorMessage.setText(null);
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
    private void createShop() {
        final String shopTitle = createName.getText().toString().trim();
        final String shopImage = Calculations.bitmapToBase64(selectedPicture);

        // Title is required
        if (TextUtils.isEmpty(shopTitle)) {
            createName.setError(REQUIRED);
            return;
        }
        // Image is required
        if (!hasImage(createPicture)) {
           errorMessage.setText("Image"+REQUIRED);
            return;
        }
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
                            createEntries(userId,  shopTitle, shopImage);
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
        createName.setEnabled(enabled);
        createPicture.setEnabled(enabled);
        if (enabled) {
            btnCreate.setVisibility(View.VISIBLE);
        } else {
            btnCreate.setVisibility(View.GONE);
        }
    }


    private void createEntries(String shopOwnerUid, String shopName, String shopImage) {
     /*   // Create shop at /shops/ and /user/shops simultaneously
        String shopKey = mDatabase.child("shops").push().getKey();
        Shop shop = new Shop("1", "2", "3","4","5");
        //To create new shop. If need to update multiple places, have more entries in "ChildUpdates"
        Map<String, Object> shopValues = shop.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/shops/" + shopKey, shopValues);
        childUpdates.put("/users/" + shopOwnerUid+"/"+"shops/"+shopKey, shopValues);

        mDatabase.updateChildren(childUpdates);
*/
        //Go to RetrievalFragment to show updates
       /* Fragment fragment = new RetrieveTestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RetrieveTestFragment.EXTRA_SHOP_KEY, shopKey);
        fragment.setArguments(bundle);
        ((NavigationActivity)getActivity()).goFragment(fragment,R.id.screen_area);*/

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
        createName.setText("");
        createName.clearFocus();
        createPicture.setImageDrawable(null);
    }

}
