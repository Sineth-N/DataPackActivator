package com.example.sineth.datapackactivator.ScratchCard;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sineth.datapackactivator.DatabaseAccess.DBHelper;
import com.example.sineth.datapackactivator.DatabaseAccess.SMSWriterSQLite;
import com.example.sineth.datapackactivator.R;

import java.util.ArrayList;
import java.util.List;

public class ScratchCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);
        setSupportActionBar((Toolbar) findViewById(R.id.app_bar_scratch_card));
        final android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ScratchCardActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ContactAdapter(getData()));
    }

    private List<CardInfo> getData() {
        return new SMSWriterSQLite(this).getData(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(android.R.menu.menu_scratch_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }if(id==R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
