package com.ashik.justice.developer.studyassistants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp2_Activty extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="note" ;
    private int selectedR;
    private TextInputEditText userInstitution,userDept,userDesig,userSession,userReg,userPhone;
    private Button buttonReg;
    private String url="";
    private String type="";
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up2 );

        TextInputLayout txtD = findViewById( R.id.txtDes );
        TextInputLayout txtS = findViewById( R.id.txtSession );
        TextInputLayout txtR = findViewById( R.id.txtReg );

        //firbase auth instance
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        //typecast for textInputEditText

        userInstitution=(TextInputEditText)findViewById(R.id.user_institution);
        userDept=(TextInputEditText)findViewById(R.id.user_dept);
        userDesig=(TextInputEditText)findViewById(R.id.user_desig);
        userSession=(TextInputEditText)findViewById(R.id.user_session);
        userReg=(TextInputEditText)findViewById(R.id.user_reg);
        userPhone=(TextInputEditText)findViewById(R.id.user_phone);

        //typecast for registration button

        buttonReg=(Button)findViewById(R.id.button_reg);


        buttonReg.setOnClickListener(this);




        Intent receivedIntent = getIntent();

        selectedR = receivedIntent.getIntExtra("rbId",-1);

        if (selectedR == 1) {
            txtD.setVisibility( View.GONE );
            txtS.setVisibility( View.VISIBLE );
            txtR.setVisibility( View.VISIBLE );
            type="Admin";
        }
        if (selectedR == 2) {
            txtD.setVisibility( View.GONE );
            txtS.setVisibility( View.VISIBLE );
            txtR.setVisibility( View.VISIBLE );
            type="Student";
        }
        if (selectedR == 3) {
            txtD.setVisibility( View.VISIBLE );
            txtS.setVisibility( View.GONE );
            txtR.setVisibility( View.GONE );
            type="Teacher";
        }
    }
    private void signUpForStudentOrAdmin(){

        final String institution=userInstitution.getText().toString().trim();
        final String dept=userDept.getText().toString().trim();
        final String session=userSession.getText().toString().trim();
        final String reg=userReg.getText().toString().trim();
        final String phone=userPhone.getText().toString().trim();
        if (TextUtils.isEmpty(institution)){
            userInstitution.setError("Enter institution");
            userInstitution.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(dept)){
            userDept.setError("Enter dept");
            userDept.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(session)){
            userSession.setError("Enter session");
            userSession.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(reg)){
            userReg.setError("Enter reg");
            userReg.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(phone)){
            userPhone.setError("Enter phone");
            userPhone.setFocusable(true);
            return;
        }
        SharedPrefManager.getIntance(this).userImportantData(institution,dept,session,type);
        progressDialog.setMessage("registering"+type+"......");
        progressDialog.show();
       mAuth.createUserWithEmailAndPassword(SharedPrefManager.getIntance(this).getEmail(),
               SharedPrefManager.getIntance(this).getPassword())
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       if (task.isSuccessful()){
                           final String current_uid=mAuth.getCurrentUser().getUid();
                           if (type.equals("Admin")){
                               url=Constants.admin_url;
                           }else{
                               url=Constants.student_request_url;
                           }

                           StringRequest request=new StringRequest(Request.Method.POST, url,
                                   new Response.Listener<String>() {
                                       @Override
                                       public void onResponse(String response) {
                                           progressDialog.dismiss();
                                           startActivity(new Intent(SignUp2_Activty.this,UploadPhoto.class));
                                           finish();
                                           try {
                                               JSONObject obj=new JSONObject(response);
                                               Toast.makeText(SignUp2_Activty.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

                                           }
                                           catch (JSONException e) {
                                               e.printStackTrace();
                                           }
                                       }
                                   }, new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   progressDialog.dismiss();
                                   Toast.makeText(SignUp2_Activty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                               }
                           }){
                               protected Map<String, String> getParams() throws AuthFailureError {
                                   Map<String, String> param=new HashMap<>();
                                   param.put("fire_id",current_uid);
                                   param.put("username",SharedPrefManager.getIntance(getApplicationContext()).getUserName());
                                   param.put("institution",institution);
                                   param.put("dept",dept);
                                   param.put("session",session);
                                   param.put("reg",reg);
                                   param.put("email",SharedPrefManager.getIntance(getApplicationContext()).getEmail());
                                   param.put("password",SharedPrefManager.getIntance(getApplicationContext()).getPassword());
                                   param.put("phone",phone);
                                   param.put("image","default");
                                   param.put("token",SharedPrefManager.getIntance(getApplicationContext()).getDeviceToken());
                                   param.put("type",type);
                                   return param;
                               }
                           };
                           MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                       }else {
                           // If sign in fails, display a message to the user.
                           Log.w(TAG, "createUserWithEmail:failure", task.getException());
                           Toast.makeText(SignUp2_Activty.this, "Authentication failed.",
                                   Toast.LENGTH_SHORT).show();

                       }

                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progressDialog.dismiss();

           }
       });

    }

    @Override
    public void onClick(View view) {
        signUpForStudentOrAdmin();
    }
}
