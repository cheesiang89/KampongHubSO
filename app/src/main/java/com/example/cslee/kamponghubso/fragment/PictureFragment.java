package com.example.cslee.kamponghubso.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.utilities.Calculations;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends Fragment {
    public static final String PICTURE_KEY = "picture_key";
    private static final String TAG = PictureFragment.class.getSimpleName();

    //Layout
    private PhotoView adImage;
    private Bitmap bitmap;
    private String base64String;

    public PictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_picture, container, false);
        adImage = view.findViewById(R.id.adImageView);
        // Get ad key from intent
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            base64String = bundle.getString(PICTURE_KEY, null);
            if (base64String == null) {
                throw new IllegalArgumentException("Must pass picture");
            }
        bitmap = Calculations.base64ToBitmap(base64String,500,300);
            adImage.setImageBitmap(bitmap);
        }else {
            adImage.setImageResource(R.drawable.no_image);
        }
        return view;
    }

}
