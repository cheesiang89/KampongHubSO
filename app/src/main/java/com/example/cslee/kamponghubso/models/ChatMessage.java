package com.example.cslee.kamponghubso.models;

import java.util.Date;
import android.graphics.Bitmap;

/**
 * Created by TTH on 13/12/2017.
 */

public class ChatMessage {
    private String msg;
    private String email;
    private String createdAtTime;
    private String createdAtDate;
    private boolean myMsg; //true is user while false is shopowner
    private String image;
    private String sender; //sender is the user of the app
    private String senderName; //senderName is the name of the user sending the msg
    private String receiver; //receiver is the shopowner of the app
    private String receiverName; //receiverName is the name of person replying to user


    public ChatMessage () {

    }

    public ChatMessage (String sender, String senderName, String receiver, String receiverName, Boolean myMsg, String createdAtDate, String createdAtTime, String image) {
        this.sender = sender;
        this.senderName = senderName;
        this.receiver = receiver;
        this.receiverName = receiverName;
        this.image = image;
        this.createdAtDate = createdAtDate;
        this.createdAtTime = createdAtTime;
        this.myMsg = myMsg;
    }

    public ChatMessage(String sender, String senderName, String receiver, String receiverName, String msg, String createdAtDate, String createdAtTime, Boolean myMsg) {
        this.sender = sender;
        this.senderName = senderName;
        this.receiver = receiver;
        this.receiverName = receiverName;
        this.msg = msg;
        this.createdAtDate = createdAtDate;
        this.createdAtTime = createdAtTime;
        this.myMsg = myMsg;
    }

    public String getSender() { return sender;}

    public void setSender(String sender) { this.sender = sender; }

    public String getSenderName() { return senderName;}

    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getReceiver() { return receiver;}

    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getReceiverName() { return receiverName;}

    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getMsg() {return msg;}

    public void setMsg(String msg) {this.msg = msg;
    }

    public String getDate() {return createdAtDate; }

    public void setDate(String date) {this.createdAtDate = date; }

    public String getTime() {return createdAtTime;
    }

    public void setTime(String time) {this.createdAtTime = time;
    }

    public boolean getMsgType() {return myMsg;}

    public void setMsgType(boolean msgType) {this.myMsg = msgType;}


    public String getImage() {return image;}

    public void setImage(String imageSelected) {this.image = imageSelected;
    }

    public String getEmail() {return email;
    }
    public void setEmail(String email) {this.email= email;
    }
}
