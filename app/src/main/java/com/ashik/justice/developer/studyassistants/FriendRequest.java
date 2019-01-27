package com.ashik.justice.developer.studyassistants;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FriendRequest extends Fragment {

    private List<ListRequest> reqiuestList;
    private RecyclerView requestRecycler;
    private String url;
    private Context context;
    private RecyclerView.Adapter adapter;
    private ProgressDialog progressDialog;

    public FriendRequest() {
       // context=getContext();
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_friend_request, container, false);
       context=container.getContext();
       requestRecycler=(RecyclerView)view.findViewById(R.id.req_recycle);
       requestRecycler.setHasFixedSize(true);
       requestRecycler.setLayoutManager(new LinearLayoutManager(context));
        loadStudentRequest();
        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
       return view;
    }

    private void loadStudentRequest(){
    url=Constants.retrive_student_request;
    reqiuestList =new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object=new JSONObject(response);
                            JSONArray array=object.getJSONArray("reqDetails");
                            for (int i=array.length()-1;i>=0;i--){
                           JSONObject details=array.getJSONObject(i);
                           String fire_id=details.getString("fire_id");
                           String image=details.getString("image");
                           String name=details.getString("username");
                           ListRequest list=new ListRequest(fire_id,image,name);
                           reqiuestList.add(list);

                            }
                            adapter=new RequestRecycleAdapter(reqiuestList,context);
                            requestRecycler.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "false", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> param=new HashMap<>();
                param.put("institution",SharedPrefManager.getIntance(context).getStudentInstitution());
                param.put("dept",SharedPrefManager.getIntance(context).getStudentDept());
                param.put("session",SharedPrefManager.getIntance(context).getStudentSession());
                return param;

            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


}
