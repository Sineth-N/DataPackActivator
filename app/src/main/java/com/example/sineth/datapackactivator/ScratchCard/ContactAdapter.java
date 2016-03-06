package com.example.sineth.datapackactivator.ScratchCard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sineth.datapackactivator.DatabaseAccess.SMSWriterSQLite;
import com.example.sineth.datapackactivator.R;
import com.example.sineth.datapackactivator.USSDCall;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

/**
 * Created by Sineth on 11/23/2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {


    private static List<CardInfo> contactList;

    public ContactAdapter(List<CardInfo> contactList) {
        this.contactList = contactList;

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        CardInfo ci = contactList.get(i);
        contactViewHolder.value.setText(ci.getValue());
        contactViewHolder.PIN.setText(ci.getPIN());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView value;
        protected TextView PIN;
        protected com.melnykov.fab.FloatingActionButton sendButton;
        int i = 0;

        public ContactViewHolder(View v) {
            super(v);
            value = (TextView) v.findViewById(R.id.valueView);
            PIN= (TextView) v.findViewById(R.id.tv_pin);
            sendButton = (FloatingActionButton) v.findViewById(R.id.rechargeButton);
            sendButton.setOnClickListener(this);

        }

        @Override
        public void onClick(final View v) {
            final String PIN = contactList.get(getPosition()).getPIN();
            new USSDCall().makeUSSDCall(v.getContext(), PIN);
            SMSWriterSQLite smsWriterSQLite = new SMSWriterSQLite(v.getContext());
            i = smsWriterSQLite.deleteFromDatabase(PIN);
            if (i != -1) {
                Context context=v.getContext();
                contactList = new SMSWriterSQLite(context).getData(context);
                notifyItemRemoved(getPosition());
            }
        }
    }
}

