package com.mansa.StaySpace.Tenants;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Response;
import com.mansa.StaySpace.RetrofitInterface;
import com.mansa.StaySpace.Tenants.Services.GetTenantHomeService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PhotoActivity extends AppCompatActivity {
    ImageView img;
    Uri pictureUri=null;
    boolean containsPic=false;
    boolean isSent=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Stay Space");
        img=(ImageView)findViewById(R.id.ownerDp);
    }
    /*void getImage()
    {
        addPhoto(null);
    }*/

    public void sendImage(View view) {
        if(!isSent) {
            if (pictureUri != null) {
                uploadImage(pictureUri);
            }
        }
        else
            Toast.makeText(this, "Image Already Uploaded", Toast.LENGTH_SHORT).show();
    }
    public void addPhoto(View v)
    {
        if(checkPermission()) {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    /*"Capture photo from camera"*/};
            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    choosePhotoFromGallary();
                                    break;
                               /* case 1:
                                    takePhotoFromCamera();
                                    break;*/
                            }
                        }
                    });
            pictureDialog.show();
        }
    }
    public void choosePhotoFromGallary() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, 1);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }
    public boolean checkPermission()
    {
        if(Build.VERSION.SDK_INT<23)
        {
            return true;
        }
        else
        {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return false;
            }
            else
                return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                addPhoto(null);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
               // imageLoader.setVisibility(View.VISIBLE);
                //imageLoader.setMax(100);
                //imageLoader.setProgress(0);
                isSent=false;
                containsPic=true;
                Uri uri=data.getData();
                try {
                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    final Bitmap rotatedBitmap;
                    if (width > height){
                        rotatedBitmap = rotate(bitmap,90);
                        Uri tempUri=getImageUri(getApplicationContext(),rotatedBitmap);
                        pictureUri=tempUri;
                        Picasso.with(getApplicationContext()).load(tempUri).fit().into(img, new Callback() {
                            @Override
                            public void onSuccess() {
                                //imageLoader.setProgress(100);
                                //imageLoader.setVisibility(View.INVISIBLE);
                                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byteArray = stream.toByteArray();
                                Log.i("compressing","complete");*/
                            }

                            @Override
                            public void onError() {
                                //imageLoader.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else
                    {
                        pictureUri=uri;
                        Picasso.with(getApplicationContext()).load(uri).fit().into(img, new Callback() {
                            @Override
                            public void onSuccess() {
                               // imageLoader.setProgress(100);
                                //imageLoader.setVisibility(View.INVISIBLE);
                               /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byteArray = stream.toByteArray();
                                Log.i("compressing","complete");*/
                            }

                            @Override
                            public void onError() {
                                //imageLoader.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        else if(requestCode==2)
        {
            //imageLoader.setVisibility(View.VISIBLE);
            //imageLoader.setMax(100);
            //imageLoader.setProgress(0);
            containsPic=true;
            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
           int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap rotatedBitmap;
            if (width > height){
                rotatedBitmap = rotate(bitmap,90);
                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();*/
                Uri tempUri=getImageUri(getApplicationContext(),rotatedBitmap);
                pictureUri=tempUri;
                Picasso.with(getApplicationContext()).load(tempUri).fit().into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                       // imageLoader.setProgress(100);
                        //imageLoader.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        //imageLoader.setVisibility(View.INVISIBLE);
                    }
                });
            }
            else
            {
                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();*/
                img.setImageBitmap(bitmap);
                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                Picasso.with(getApplicationContext()).load(tempUri).fit().into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        //imageLoader.setProgress(100);
                        //imageLoader.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        //imageLoader.setVisibility(View.INVISIBLE);
                    }
                });
                pictureUri=tempUri;
            }


        }
   }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private void uploadImage(Uri picUri){
        final ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(PhotoActivity.this);
        progressDialog.setTitle("Upload");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginActivity.MAINURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String path=getRealPathFromURI(picUri);
        Log.i("path",path);
        File file=new File(path);
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        RequestBody userId=RequestBody.create(MultipartBody.FORM,LoginActivity.sharedPreferences.getString("token",""));
        Call<Response> call = retrofitInterface.uploadImage(userId,body);
        call.enqueue(new retrofit2.Callback<Response>() {

            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
               // Log.i("Retrofit",""+response.body());
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    isSent=true;
                    Log.i("Retrofit","Success");
                    Response responseBody = response.body();
                    Toast.makeText(PhotoActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Retrofit","Fail");
                    Log.i("Retrofit","code:"+response.code());
                   // ResponseBody errorBody = response.errorBody();

                    Gson gson = new Gson();

                   /* try {

                        Response errorResponse = gson.fromJson(errorBody.string(), Response.class);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
              //  mProgressBar.setVisibility(View.GONE);
                Toast.makeText(PhotoActivity.this, "Internet Error", Toast.LENGTH_SHORT).show();
                Log.d("Retrofit", "onFailure: "+t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startService(new Intent(getApplicationContext(), GetTenantHomeService.class));
    }
}
