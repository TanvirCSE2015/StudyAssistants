package com.ashik.justice.developer.studyassistants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowRequestDetails extends AppCompatActivity implements View.OnClickListener {
private CircleImageView request_image;
private TextView user_name,user_details;
private String fire_id="";
private Button accepted,decline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_request_details);

        request_image=(CircleImageView)findViewById(R.id.request_profile);
        user_name=(TextView)findViewById(R.id.req_name);
        user_details=(TextView)findViewById(R.id.req_details);

        accepted=(Button)findViewById(R.id.accepted);
        decline=(Button)findViewById(R.id.decline);

        String url=Constants.get_request_details;

        //get fire id
        Intent intent=getIntent();
        fire_id=intent.getStringExtra("fire_id");

        accepted.setOnClickListener(this);
        decline.setOnClickListener(this);

        StringRequest request=new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("requestDetails");
                    for (int i=0;i<array.length();i++){
                        JSONObject ob=array.getJSONObject(i);

                        String image=ob.getString("image");
                        if (!image.equals("default")){
                            Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.profile).into(request_image);
                        }

                        user_name.setText(ob.getString("username"));
                        user_details.setText("Institution: "+ob.getString("institution")+"\n"+"Dept"+ob.getString("dept")
                        +"\n"+"Session: "+ob.getString("session")+"\n"+"Reg: "+ob.getString("reg")+"\n"+"Email: "+ob.getString("email")
                        +"\n"+"Profession: "+ob.getString("type"));


                    }
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
                params.put("fire_id",fire_id);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    private void accepteStudentRequest(){

        StringRequest request=new StringRequest(Request.Method.POST, Constants.accepte_request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            Toast.makeText(ShowRequestDetails.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                Map<String, String> param=new HashMap<>();
                param.put("fire_id",getIntent().getStringExtra("fire_id"));
                return param;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void deleteStudentRequest(){
        StringRequest request=new StringRequest(Request.Method.POST, Constants.delete_request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            Toast.makeText(ShowRequestDetails.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                Map<String, String> param=new HashMap<>();
                param.put("fire_id",getIntent().getStringExtra("fire_id"));
                return param;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.accepted:
                accepteStudentRequest();
                break;
            case R.id.decline:
                deleteStudentRequest();
                break;
        }
    }


}
