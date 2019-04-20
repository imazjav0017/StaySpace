package com.mansa.StaySpace.Tenants.TenantFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Tenants.PhotoActivity;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.EditProfileActivity;
import com.mansa.StaySpace.Tenants.TenantIdPhotoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TenantProfileFragment extends android.support.v4.app.Fragment {
    static TextView name,email,phNo;
    Button updateProfile,idImage,addImage;
    View v;
    static Context context;
    static ImageView ad;
    public TenantProfileFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            setData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public TenantProfileFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.tenant_profile_fragment,container,false);
        name=(TextView)v.findViewById(R.id.tenantNameTextView);
        email=(TextView)v.findViewById(R.id.tenantEmailTextView);
        phNo=(TextView)v.findViewById(R.id.tenantNumberTextView);
        ad=(ImageView)v.findViewById(R.id.tenantSideAd);
        idImage=v.findViewById(R.id.idImageButton);
        addImage=v.findViewById(R.id.addImagebutton);
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAd();
            }
        });
        updateProfile=(Button)v.findViewById(R.id.tenantUpdateProfileBtn);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, EditProfileActivity.class);
                startActivity(i);
            }
        });
        String idProofPicsS=LoginActivity.sharedPreferences.getString("idProofPics",null);
        if(idProofPicsS!=null)
        {
            Log.i("idProof","NotNull");
            JSONArray idProofPics= null;
            try {
                idProofPics = new JSONArray(idProofPicsS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(idProofPics.length()>0) {
                idImage.setText("Click To View ID Photo");
            }
        }
        idImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idImage.getText().toString().equals("Click To View ID Photo"))
                {
                    startActivity(new Intent(context, TenantIdPhotoActivity.class));
                }
                else
                {
                    startActivity(new Intent(context, PhotoActivity.class));
                }
            }
        });
        if(idImage.getText().toString().equals("Click To View ID Photo"))
        {
            addImage.setVisibility(View.VISIBLE);
            addImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, PhotoActivity.class));
                }
            });
        }
        else
        {
            addImage.setVisibility(View.INVISIBLE);
        }

        return v;
    }
    public static void setData() throws JSONException {
        String tName,tEmail,tPhoneNO;
        String data= LoginActivity.sharedPreferences.getString("tenantDetail",null);
        if(data!=null && name!=null)
        {
            JSONObject object=new JSONObject(data);
            tEmail=object.getString("email");
            tPhoneNO=object.getString("mobileNo");
            JSONObject nameObject=object.getJSONObject("name");
            tName=nameObject.getString("firstName")+" "+nameObject.getString("lastName");
            name.setText(tName);
            email.setText(tEmail);
            phNo.setText(tPhoneNO);
        }
       /* String idProofPicsS=LoginActivity.sharedPreferences.getString("idProofPics",null);
        if(idProofPicsS!=null)
        {

            Log.i("idProof","NotNull");
            JSONArray idProofPics=new JSONArray(idProofPicsS);
            if(idProofPics.length()>0)
            {
                Log.i("idProof","setting");
                String idUrl=idProofPics.getString(0);
                Picasso.with(context).load(idUrl).fit().into(idImage, new Callback() {
                    @Override
                    public void onSuccess() {
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
        }*/
    }
    void showAd()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_show_ad, null, false);
        builder.setView(v);
        final AlertDialog resetDialog = builder.create();
        resetDialog.show();
    }
}
