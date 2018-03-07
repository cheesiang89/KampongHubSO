package com.example.cslee.kamponghubso.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.cslee.kamponghubso.models.ChatMessage;
import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.models.MyApplication;
import com.example.cslee.kamponghubso.utilities.Calculations;

import java.util.List;
/**
 * Created by TTH on 25/12/2017.
 */

public class MessageAdapter extends ArrayAdapter<ChatMessage> {
    private Activity activity;
    private List<ChatMessage> messages;
    private ChatMessage chatMessage;
    private MyApplication myApp;

    public MessageAdapter(Activity context, int resource, List<ChatMessage> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
        myApp = (MyApplication) activity.getApplicationContext();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutResource = 0; // determined by view type
        chatMessage = getItem(position);
        int viewType = getItemViewType(position);

        if (!chatMessage.getMsgType() ) { //if false, will put as my messsages
            layoutResource = R.layout.message_sent;
        }
        else {
            layoutResource = R.layout.received_message;
        }

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        //set message content
        if(chatMessage.getImage() != null)
        {
            holder.image.setImageBitmap(Calculations.base64ToBitmap(chatMessage.getImage(),1000,600));
            holder.imageTime.setText(chatMessage.getDate() + ", " + chatMessage.getTime());
            holder.msg.setVisibility(View.INVISIBLE);
        }else {
            holder.msg.setText(chatMessage.getMsg());
            holder.msgTime.setText(chatMessage.getDate() + ", " + chatMessage.getTime());
            if (chatMessage.getMsgType()) {
                holder.name.setText(chatMessage.getSenderName());
            }
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        if(messages.get(position).getMsg() == null )
        {
           return 0;
        }else if(messages.get(position).getMsgType() == true)
        {return 1;}
        else
        {return 2;}
    }

    private class ViewHolder {
        private TextView name;
        private TextView msg;
        private TextView msgTime;
        private ImageView image;
        private TextView imageTime;


        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.text_message_name);
            msg = (TextView) v.findViewById(R.id.text_message_body);
            msgTime = (TextView) v.findViewById(R.id.text_message_time);
            image = (ImageView) v.findViewById(R.id.image_message_body);
            imageTime = (TextView) v.findViewById(R.id.image_message_time);
        }
    }

}
