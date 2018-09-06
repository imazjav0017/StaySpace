 package com.rent.rentmanagement.renttest.Owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rent.rentmanagement.renttest.R;

 public class UpdateOwnerProfileActivity extends AppCompatActivity {
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         if(item.getItemId()==android.R.id.home)
         {
             onBackPressed();
             return true;
         }
         return false;
     }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_update_owner_profile);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Owner Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
