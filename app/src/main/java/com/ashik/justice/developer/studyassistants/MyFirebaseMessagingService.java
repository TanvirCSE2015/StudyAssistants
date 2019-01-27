package com.ashik.justice.developer.studyassistants;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                JSONObject data = json.getJSONObject("data");
             /*
              }*/
                sendPushNotification(json);
            } catch (JSONException e) {
                Log.e(TAG, "Exception" + e.getMessage());
            }

        }
    }

    private void sendPushNotification(JSONObject json) {

        String type = "";

        Log.e(TAG, "Notification JSON: " + json.toString());
        try {

            //getting the json data
            JSONObject data = json.getJSONObject("data");


            //parsing json data

            String title = data.getString("user_name");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            type = data.getString("notificationType");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creting intent
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            if (imageUrl.equals("default")) {
                mNotificationManager.showSmallNotification(title, message, intent, type);
            } else {
                mNotificationManager.showBoigNotification(title, message, imageUrl, intent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        if (type.equals("Request")) {
            int count = SharedPrefManager.getIntance(getApplicationContext()).getRequestBadgeCouont();
            count = count + 1;
            SharedPrefManager.getIntance(getApplicationContext()).storeRequestBadgeCount(count);
            Intent intent = new Intent();
            intent.putExtra("count", count);
            intent.putExtra("type", type);
            intent.setAction("com.ashik.justice.developer.studyassistants");
            sendBroadcast(intent);
        }
    }
}
