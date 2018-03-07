package com.example.cslee.kamponghubso.viewholder;

/**
 * Created by TTH on 25/02/2018.
 */
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.models.ChatMessage;
import com.example.cslee.kamponghubso.utilities.Calculations;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

public class ChatListHolder extends RecyclerView.ViewHolder{
    private static final String TAG = ChatListHolder.class.getSimpleName();
    public ImageView shopProfile;
    public TextView shopName;
    public TextView chatDateTime;
    public TextView msgContents;



    public ChatListHolder(View itemView) {
        super(itemView);
        //shopProfile = (CircleImageView) itemView.findViewById(R.id.list_shop_profile);
        shopName = (TextView)itemView.findViewById(R.id.list_shop_name);
        shopName.setSelected(true);
        chatDateTime = (TextView)itemView.findViewById(R.id.list_date_time);
        msgContents = (TextView)itemView.findViewById(R.id.list_msg_content);

    }
    public void bindToList(ChatMessage chatMsg, View.OnClickListener clickListener) {
        //setProfile(chatMsg.getImage());
        setShopName(chatMsg.getSenderName());
        setDateTime(chatMsg.getDate(), chatMsg.getTime());
        setMsgContent(chatMsg.getMsg()); //gets the latest chat msg
        /*setShopName("ABC 123");
        setDateTime("Sun, 17 Mar 2018 10:00PM");
        setMsgContent("A Polish expedition hoping to complete the first winter ascent of the world's second-highest mountain, K2, says one of its climbers appears to have launched an unauthorised solo attempt."); //gets the latest chat msg
    */
    }


    /*private void setProfile(String shopImageString)
    {
        //Get Bitmap from base64
        Bitmap bitmap = Calculations.base64ToBitmap(shopImageString);
        shopProfile.setImageBitmap(bitmap);
    }*/


    public void setShopName(String title)
    {
        shopName.setText(title);
    }

    //public void setDateTime(String date){chatDateTime.setText(date);}

    public void setDateTime(String date, String time){

        //SimpleDateFormat sdf = new SimpleDateFormat("EEE, ddMMyyyy hh:mm a");
        String dateTime = date+" "+time;
        //String strTime = sdf.format(dateTime);
        chatDateTime.setText(dateTime);
    }

    public void setMsgContent(String msg){
        //TODO: Detect that if it is an image, show something else
        msgContents.setText(msg);
    }
}
