package com.mansa.StaySpace.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class EditProfileFragment extends Fragment {
    boolean isOwner;
    EditText firstName,lastName,email,mobileNo;
    Button save;
    ProgressBar progressBar;
    RadioGroup gendergroup;
    String gender=null,id;

    public EditProfileFragment() {
    }

    @SuppressLint("ValidFragment")
    public EditProfileFragment(boolean isOwner) {
        this.isOwner = isOwner;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_edit_profile,container,false);
        firstName=(EditText)v.findViewById(R.id.EditfirstNameInput);
        lastName=(EditText)v.findViewById(R.id.EditLastNameInput);
        progressBar=(ProgressBar)v.findViewById(R.id.editProgress);
        email=(EditText)v.findViewById(R.id.EditemailInput);
        save=(Button)v.findViewById(R.id.editSave);
        mobileNo=(EditText)v.findViewById(R.id.editMobileNo);
        gendergroup=(RadioGroup)v.findViewById(R.id.editGenderRadioGroup);
        gendergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.maleRadioBtn)
                {
                    gender="m";
                }
                else
                {
                    gender="f";
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        try {
            setData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
    void setData() throws JSONException {
        if(isOwner)
        {
            String ownerDetails= LoginActivity.sharedPreferences.getString("ownerDetails",null);
            if(ownerDetails!=null)
            {
                JSONObject ownerObject=new JSONObject(ownerDetails);
                 id=ownerObject.getString("_id");
                JSONObject name=ownerObject.getJSONObject("name");
                String fName=name.getString("firstName");
                String lName=name.getString("lastName");
                String emailId=ownerObject.getString("email");
                String mobNo=ownerObject.getString("mobileNo");
                firstName.setText(fName);
                lastName.setText(lName);
                email.setText(emailId);
                mobileNo.setText(mobNo);
            }

        }else
        {

        }
    }
    void saveChanges()
    {
        Log.i("saving","changes");
        if(gender==null)
        {
            Toast.makeText(getContext(), "Select Your Gender", Toast.LENGTH_SHORT).show();
        }
        else {
            String fName = firstName.getText().toString();
            String lName = lastName.getText().toString();
            String emailId = email.getText().toString();
            String mobNo = mobileNo.getText().toString();
            if (fName.equals("") || lName.equals("") || emailId.equals("") || mobNo.equals("")) {
                Toast.makeText(getContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ((Activity)getContext()).onBackPressed();
            }
        }

    }
}
