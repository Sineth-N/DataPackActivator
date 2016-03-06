package com.example.sineth.datapackactivator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Sineth on 11/28/2015.
 */
public class USSDCall {
    public void makeUSSDCall(Context context, String number) {
        String s = Uri.encode("#");
        String ussd = "*102*" + number + s;
        context.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)));
    }
}
