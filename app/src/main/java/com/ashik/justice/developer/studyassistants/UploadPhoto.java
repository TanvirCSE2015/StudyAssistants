package com.ashik.justice.developer.studyassistants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class UploadPhoto extends AppCompatActivity implements View.OnClickListener {
private CircleImageView circleImageView;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
private Button btnUpload,btnSkip;
private byte[] thumb_bite;
private static final int GALLERY_PICK=1;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        circleImageView=(CircleImageView)findViewById(R.id.circle_image);
        btnUpload=(Button)findViewById(R.id.upload);
        btnSkip=(Button)findViewById(R.id.skip);
        //set click listner
        circleImageView.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.circle_image:
                createPhotoChoser();
                break;
            case R.id.upload:
                uploadphoto();
                break;
            case R.id.skip:
                break;
        }
    }
    private void createPhotoChoser(){
        Intent galleryIntent=new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_PICK && resultCode==RESULT_OK){
            Uri uri=data.getData();
            CropImage.activity(uri).setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

               File thumbni_file=new File(resultUri.getPath());
                try {
                    Bitmap compressedImage = new Compressor(this)
                             .setMaxWidth(200)
                             .setMaxHeight(200)
                             .setQuality(75)
                             .compressToBitmap(thumbni_file);
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    compressedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                   thumb_bite=baos.toByteArray();

                    Bitmap bitmap=BitmapFactory.decodeByteArray(thumb_bite,0,thumb_bite.length);
                    circleImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void uploadphoto(){
        progressDialog.setMessage("Uploading image.....");
        progressDialog.show();


        final String user_id=mAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("profile_pics").child(user_id+".jpg");

        UploadTask uploadTask=mStorageRef.putBytes(thumb_bite);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    final String download_url=task.getResult().getDownloadUrl().toString();
                    StringRequest request=new StringRequest(Request.Method.POST, Constants.user_uploading_image,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject object=new JSONObject(response);

                                        Toast.makeText(UploadPhoto.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    startActivity(new Intent(UploadPhoto.this,Home.class));
                                    finish();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadPhoto.this, "faiure", Toast.LENGTH_SHORT).show();

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params=new HashMap<>();
                            params.put("fire_id",user_id);
                            params.put("image",download_url);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


                }

            }
        });

    }
}
