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
import com.example.cslee.kamponghubso.models.Shop;
import com.example.cslee.kamponghubso.utilities.Calculations;
import com.squareup.picasso.Picasso;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

public class ShopListHolder extends RecyclerView.ViewHolder{
    private static final String TAG = ShopListHolder.class.getSimpleName();
    public TextView shopName;
    public TextView shopIsOpen;
    public TextView shopTime;
    public TextView shopDistance;
    public ImageView shopImage;


    public ShopListHolder(View itemView) {
        super(itemView);
        shopImage = (ImageView)itemView.findViewById(R.id.shopImage);
        shopName = (TextView)itemView.findViewById(R.id.shopName);
        shopIsOpen = (TextView)itemView.findViewById(R.id.isShopOpen);
        shopTime = (TextView)itemView.findViewById(R.id.shopTime);
        shopDistance = (TextView)itemView.findViewById(R.id.shopDistance);

    }
    public void bindToList(Shop shop,View.OnClickListener clickListener) {
        setShopName(shop.getShopName());
        setImage(shop.getShopImage());
        setShopOpen(shop.getTimeStart(),shop.getTimeEnd(),"1200");
        setTime(shop.getTimeStart(),shop.getTimeEnd());
        setDistance("A","B");

    }
    public void setShopName(String title)
    {
        shopName.setText(title);
    }

    public void setImage(String shopImageString)
    {
        //Get Bitmap from base64
        Bitmap bitmap = Calculations.base64ToBitmap(shopImageString);
        shopImage.setImageBitmap(bitmap);

/*        //OLD CODE
        Picasso.with(itemView.getContext())
                .load(imageUrl)
                .into(shopImage);*/
    }
    public void setShopOpen(String timeStart, String timeEnd, String currentTime){
        //TODO: Calculate is shop open
        shopIsOpen.setText(Calculations.calcShopOpen(timeStart,timeEnd,currentTime));

    }
    public void setTime(String timeStart, String timeEnd){
        shopTime.setText(Calculations.calcTime(timeStart,timeEnd));
    }
    public void setDistance(String currentDistance, String shopCoordinates){
        //TODO: Need to calculate distance based on current coordinates and shop coordinates
        shopDistance.setText(Calculations.calcDistance(currentDistance,shopCoordinates));
    }
}
