package com.mansa.StaySpace.Tenants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class TenantIdPhotoActivity extends AppCompatActivity {
    ImageView idImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_id_photo);
        idImage=(ImageView)findViewById(R.id.idImage);
        final PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(idImage);
        String idProofPicsS= LoginActivity.sharedPreferences.getString("idProofPics",null);
        if(idProofPicsS!=null)
        {

            Log.i("idProof","NotNull");
            JSONArray idProofPics= null;
            try {
                idProofPics = new JSONArray(idProofPicsS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(idProofPics.length()>0)
            {
                Log.i("idProof","setting");
                String idUrl= null;
                try {
                    idUrl = idProofPics.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Picasso.with(TenantIdPhotoActivity.this).load(idUrl).fit().into(idImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        photoViewAttacher.update();
                        Log.i("idProof","set");
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }
        else
        {
            Log.i("idProof","null");
        }
    }
}
