package com.mansa.StaySpace.Owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class IdProofActivity extends AppCompatActivity {
    ImageView idImage;
    ProgressBar imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_proof);
        idImage=(ImageView)findViewById(R.id.idImageFull);
        final PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(idImage);
        imageLoader=(ProgressBar)findViewById(R.id.idImageLoader);
        String idUrl= getIntent().getStringExtra("url");
        if(idUrl!=null) {
            imageLoader.setVisibility(View.VISIBLE);
            imageLoader.setMax(100);
            imageLoader.setProgress(0);
            Picasso.with(IdProofActivity.this).load(idUrl).fit().into(idImage, new Callback() {
                @Override
                public void onSuccess() {
                    imageLoader.setProgress(100);
                    imageLoader.setVisibility(View.INVISIBLE);
                    photoViewAttacher.update();

                }

                @Override
                public void onError() {
                    imageLoader.setVisibility(View.INVISIBLE);
                }
            });
        }

    }
}
