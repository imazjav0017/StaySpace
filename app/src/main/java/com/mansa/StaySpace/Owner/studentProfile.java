package com.mansa.StaySpace.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mansa.StaySpace.Adapters.IdProofAdapter;
import com.mansa.StaySpace.Adapters.TotalTenantsAdapter;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class studentProfile extends AppCompatActivity {
String _id,name,phNo,roomNo,adhaarNo,roomId;
    boolean from,isTenant;
    EditText sRoomNo,sPhNo,sAadharNo,sName;
    Button edit,delete;
    String response;
    ProgressDialog deleteStudentDialog,editStudentDialog;
    RecyclerView idProofsRv;
    String idPicsUrls;
    List<String> idPics;
    IdProofAdapter adapter;
    public void viewPhotos(View view) {
        String idPicsUrls=getIntent().getStringExtra("idProofPics");
        if(idPicsUrls!=null)
        {

        }
    }

    public class EditStudentsTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("data", params[1]);
                int resp = connection.getResponseCode();
                Log.i("editStudentsResp",String.valueOf(resp));
                if(resp==200)
                {
                    response=getResponse(connection);
                    return response;
                }
                else
                {
                    response=getResponse(connection);
                    Log.i("EDIT",response);
                    return response;
                }

            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            editStudentDialog.dismiss();
            if (s != null) {
                enable(edit);
                if(s.equals("ok"))
                Toast.makeText(getApplicationContext(),"Saved Changes",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(studentProfile.this, "Bad Request!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                enable(edit);
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }
    }
    public String  getResponse(HttpURLConnection connection)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        }catch(Exception e)
        {
            return e.getMessage();
        }
    }
    void setTokenJson(String mode)
    {
        try {
        JSONObject token = new JSONObject();
        token.put("auth", LoginActivity.sharedPreferences.getString("token", null));
        token.put("studentId", _id);

        if(mode.equals("e"))
        {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
                    if(roomNo.equals("")||name.equals("")||adhaarNo.equals("")||phNo.equals(""))
                    {
                        enable(edit);
                        Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        token.put("name",name);
                        token.put("adharNo",adhaarNo);
                        token.put("mobileNo",phNo);
                        token.put("roomId",roomId);
                        EditStudentsTask task=new EditStudentsTask();
                        task.execute(LoginActivity.MAINURL+"/rooms/editStudents", token.toString());
                        editStudentDialog=new ProgressDialog(studentProfile.this);
                        editStudentDialog.setMax(100);
                        editStudentDialog.setMessage("Edit "+name);
                        editStudentDialog.setTitle("Editing...");
                        editStudentDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        editStudentDialog.show();
                    }
                }

        }
        else
        {

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void enable(Button b)
    {
        b.setClickable(true);
    }
    public void editStudent(View v)
    {
        Log.i("e","c");
        edit.setClickable(false);
        name=sName.getText().toString();
        phNo=sPhNo.getText().toString();
        adhaarNo=sAadharNo.getText().toString();
        setTokenJson("e");
    }
    public void deleteStudent(View v)  {
        try {
            deleteStudent();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void deleteStudent() throws JSONException {
        String auth = LoginActivity.sharedPreferences.getString("token", null);
        JSONObject data=new JSONObject();
        data.put("auth",auth);
        data.put("roomId",roomId);
        if(isTenant)
            data.put("tenantId",_id);
        else
            data.put("studentId",_id);
        DeleteStudentTask task=new DeleteStudentTask();
        task.execute(LoginActivity.MAINURL+"/rooms/deleteStudents",data.toString());
        deleteStudentDialog=new ProgressDialog(studentProfile.this);
        deleteStudentDialog.setMax(100);
        deleteStudentDialog.setMessage("Deleting "+name);
        deleteStudentDialog.setTitle("Delete Tenant");
        deleteStudentDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        deleteStudentDialog.show();
    }
    class DeleteStudentTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("DELETESTUDENTDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("DELETESTUDENTRESP",String.valueOf(resp));
                if(resp==200)
                {
                    String response=getResponse(connection);
                    return response;
                }
                else if(resp==422)
                {
                    return "422";
                }

            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            deleteStudentDialog.dismiss();
            if (s != null) {
                if(s.equals("422"))
                {
                    Toast.makeText(studentProfile.this, "Please Collect rent payment to checkout!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(studentProfile.this, "Tenant Checked out!", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    i.putExtra("deletedStudent",true);
                    startActivity(i);
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
            }
        }

        public String  getResponse(HttpURLConnection connection)
        {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            }catch(Exception e)
            {
                return e.getMessage();
            }
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_tenant_profile);
        Intent i=getIntent();
        name=i.getStringExtra("name");
        _id=i.getStringExtra("id");
        phNo=i.getStringExtra("phNo");
        roomNo=i.getStringExtra("roomNo");
        roomId=i.getStringExtra("roomId");
        from=i.getBooleanExtra("total",false);
        adhaarNo=i.getStringExtra("aadharNo");
        isTenant=i.getBooleanExtra("isTenant",false);
        idPicsUrls=getIntent().getStringExtra("idProofPics");
        Log.i("aadharNo",adhaarNo);
        sName=(EditText) findViewById(R.id.studentNameField);
        sRoomNo=(EditText)findViewById(R.id.studentRoomNoField);
        sPhNo=(EditText)findViewById(R.id.studentPhNoField);
        sAadharNo=(EditText)findViewById(R.id.studentAadharNoField);
        edit=(Button)findViewById(R.id.editStudent);
        delete=(Button)findViewById(R.id.deleteStudent);
        sName.setText(name);
        sRoomNo.setText(roomNo);
        sPhNo.setText(phNo);
        sAadharNo.setText(adhaarNo);
        sRoomNo.setEnabled(false);
        if(idPicsUrls!=null){
            setIdPhotos();
        }
    }
    void setIdPhotos()
    {
        idProofsRv=(RecyclerView)findViewById(R.id.tenantIdProofsRv);
        String[]ids=idPicsUrls.split(",");
        idPics= Arrays.asList(ids);
        adapter=new IdProofAdapter(idPics);
        LinearLayoutManager lm2=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        idProofsRv.setLayoutManager(lm2);
        idProofsRv.setHasFixedSize(true);
        idProofsRv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
