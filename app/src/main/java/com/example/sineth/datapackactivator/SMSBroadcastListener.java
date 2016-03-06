package com.example.sineth.datapackactivator;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Sineth on 11/9/2015.
 */
public class SMSBroadcastListener extends BroadcastReceiver {
    static String message;
    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        //Bundle is a simple map
        if (bundle != null) {

            final Object[] pdusObj = (Object[]) bundle.get("pdus");
            //Key pdus is dedicated to sms
            for (int i = 0; i < pdusObj.length; i++) {

                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                message = currentMessage.getDisplayMessageBody();
                if(phoneNumber.equals("+94712755777")){
                    if (message.startsWith("You have DATA")){
                        Pattern p = Pattern.compile("-\\d+(\\.)?(\\d+)?");
                        Matcher m = p.matcher(message);
                        while (m.find()) {
                            notificationService(m.group().substring(1),"Data Balance",context,0);
                            break;
                        }

                    }else if(message.startsWith("M2M SMS")){
                        Pattern p = Pattern.compile("-\\d+(\\.)?(\\d+)?");
                        Matcher m = p.matcher(message);
                        notificationService(m.group().substring(1), "SMS Balance", context, 1);
                        notificationService(m.group().substring(1),"Data Balance",context,0);
                    }
                    abortBroadcast();
                }
            }
        }
    }

    private void notificationService(String content, String title,Context context,int type){
        String word= type == 1 ? "SMS" : "MB";
        NotificationCompat.Builder notification= new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_account)
                .setContentTitle(title).setStyle(new NotificationCompat.BigTextStyle())
                .setContentText("Machan,We have "+content+" "+word+" Left!! ");
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(type,notification.build());
    }



}
