package com.ashik.justice.developer.studyassistants;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static Context mcntx;
    private static SharedPrefManager mInstance;

    private static final String SHARED_PREF_NAME="storeUserData";
    private static final String DEVICE_TOKEN="token";
    private static final String USER_NAME="username";
    private static final String USER_EMAIL="email";
    private static final String USER_PASSWORD="password";
    private static final String USER_INS="institution";
    private static final String USER_DEPT="dept";
    private static final String USER_SESSION="session";
    private static final String USER_TYPE="type";
    private static final String Notification_badge="requestBadge";



    public SharedPrefManager(Context context) {

        mcntx=context;
    }
    public static synchronized SharedPrefManager getIntance(Context context){

        if (mInstance==null){
            mInstance=new SharedPrefManager(context);

        }
        return mInstance;
    }

    //save device token to sharedPreferrence

    public boolean saveDeviceToken(String token){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(DEVICE_TOKEN,token);
        editor.commit();
        editor.apply();
        return true;
    }
    //get device token from sharedprefference

    public String getDeviceToken(){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return preferences.getString(DEVICE_TOKEN,"");

    }
    //save user signup first information

    public void saveSignUpFirstData(String name,String email,String password){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_NAME,name);
        editor.putString(USER_EMAIL,email);
        editor.putString(USER_PASSWORD,password);
        editor.commit();
        editor.apply();
    }

    public String getUserName(){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return preferences.getString(USER_NAME,"");
    }
    public String getEmail(){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return preferences.getString(USER_EMAIL,"");

    }
    public String getPassword(){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return preferences.getString(USER_PASSWORD,"");
    }
    public void userImportantData(String institution,String dept,String session,String type){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_INS,institution);
        editor.putString(USER_DEPT,dept);
        editor.putString(USER_SESSION,session);
        editor.putString(USER_TYPE,type);
        editor.commit();
        editor.apply();
    }
    public void clearSharedPref(){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.commit();
    }
    public String getStudentInstitution(){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return preferences.getString(USER_INS,"");
    }
    public String getStudentDept(){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return preferences.getString(USER_DEPT,"");


    }
    public String getStudentSession(){
        SharedPreferences preferences=mcntx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
      return  preferences.getString(USER_SESSION,"");
    }

    public void storeRequestBadgeCount(int count){
        SharedPreferences sharedPreferences=mcntx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(Notification_badge,count);
        editor.commit();
        editor.apply();

    }
    public int getRequestBadgeCouont(){
        SharedPreferences sharedPreferences=mcntx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Notification_badge,0);
    }
    public void clearRequestBadge(){
        SharedPreferences sharedPreferences=mcntx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove(Notification_badge);
        editor.apply();
    }
}
