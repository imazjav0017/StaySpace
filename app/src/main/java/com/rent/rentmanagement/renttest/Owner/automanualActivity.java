package com.rent.rentmanagement.renttest.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.rent.rentmanagement.renttest.R;

public class automanualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_automanual);
        LinearLayout l1 = (LinearLayout)findViewById(R.id.l1);
        LinearLayout l2 = (LinearLayout)findViewById(R.id.l2);
        Intent i1=getIntent();
        final String buildingId=i1.getStringExtra("buildingId");
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(automanualActivity.this,BuildActivity.class);
                intent.putExtra("buildingId",buildingId);
                startActivity(intent);
                finish();
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(automanualActivity.this,manualActivity.class);
                i.putExtra("buildingId",buildingId);
                startActivity(i);
                finish();
            }
        });
    }
}
