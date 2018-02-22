package com.example.cslee.kamponghubso.viewholder;

/**
 * Created by CSLee on 23/12/2017.
 */
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.models.Advert;
import com.example.cslee.kamponghubso.models.Shop;
import com.example.cslee.kamponghubso.utilities.Calculations;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

public class AdListHolder extends RecyclerView.ViewHolder{
    private static final String TAG = AdListHolder.class.getSimpleName();

    public ImageView adImage;
    public TextView shopName;
    public TextView adDate;

    public AdListHolder(View itemView) {
        super(itemView);
        adImage = (ImageView)itemView.findViewById(R.id.adImage);
        shopName=(TextView)itemView.findViewById(R.id.shopName);
        adDate=(TextView)itemView.findViewById(R.id.adDate);
    }
    public void bindToList(Advert ad) {

        setImage(ad.getAdImage());
      shopName.setText(ad.getShopName());
        adDate.setText(ad.getAdDate());

    }

    public void setImage(String adImageString)
    {
        //Get Bitmap from base64
        Bitmap bitmap = Calculations.base64ToBitmap(adImageString);
        adImage.setImageBitmap(bitmap);

    }


}
