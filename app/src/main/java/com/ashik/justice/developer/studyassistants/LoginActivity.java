package com.ashik.justice.developer.studyassistants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText emailLog,passwordLog;
    private Button login;
    private FirebaseAuth mAuth;
    private String url="";
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        mAuth = FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
        emailLog=(TextInputEditText)findViewById(R.id.log_email);
        passwordLog=(TextInputEditText)findViewById(R.id.log_password);

        login=(Button)findViewById(R.id.log_in);

        login.setOnClickListener( this);
        checkUserLoginOrnot();
    }

    public void GoToSignUp(View view) {
        startActivity( new Intent( LoginActivity.this, SignUp1_Activity.class ) );
    }

    @Override
    public void onClick(View view) {
        signIn();

    }
    private void signIn(){

        String email=emailLog.getText().toString();
        String password=passwordLog.getText().toString();
       dialog.setMessage("log in....");
       dialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            url=Constants.user_information;

                            dialog.dismiss();

                            StringRequest request=new StringRequest(Request.Method.POST, Constants.user_information,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            String username="";
                                            String institution="";
                                            String dept="";
                                            String session="";
                                            String email="";
                                            String password="";
                                            String type="";



                                            try {
                                                JSONObject object=new JSONObject(response);
                                                JSONArray array=object.getJSONArray("users");
                                                //clear the shared pref
                                                SharedPrefManager.getIntance(getApplicationContext()).clearSharedPref();
                                                for (int i=0;i<array.length();i++){
                                                  JSONObject ob=array.getJSONObject(i);
                                                  username=ob.getString("username");
                                                    institution=ob.getString("institution");
                                                    dept=ob.getString("dept");
                                                    session=ob.getString("session");
                                                    email=ob.getString("email");
                                                    password=ob.getString("password");
                                                    type=ob.getString("type");

                                                }
                                                SharedPrefManager.getIntance(getApplicationContext()).saveSignUpFirstData(
                                                        username,email,password
                                                );
                                                SharedPrefManager.getIntance(getApplicationContext()).userImportantData(
                                                        institution,dept,session,type
                                                );

                                                Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }){

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params=new HashMap<>();
                                    params.put("fire_id",mAuth.getCurrentUser().getUid());
                                    return params;
                                }
                            };
                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                          //  Log.w(TAG, "signInWithEmail:failure", task.getException());
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });


    }
    public void checkUserLoginOrnot(){
        if (!SharedPrefManager.getIntance(getApplicationContext()).getEmail().equals("") &&
                !SharedPrefManager.getIntance(this).getPassword().equals("")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }
}
