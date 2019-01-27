package com.ashik.justice.developer.studyassistants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION=234;
    public static final int ID_SMALL_NOTIFICATION=235;
    private Context mcntx;

    public MyNotificationManager(Context mcntx) {
        this.mcntx = mcntx;
    }

    //method for showing big notification

    public void showBoigNotification(String title,String message,String url,Intent intent) {
        PendingIntent resultIntent = PendingIntent.getActivity(
                mcntx, ID_BIG_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mcntx);


        Notification notification;
        notification=builder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mcntx.getResources(),R.mipmap.ic_launcher))
                 .setContentText(message)
                .build();
        notification.flags |=Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager=(NotificationManager)mcntx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION,notification);

    }
    //method for showing small notification
    public  void showSmallNotification(String title,String message,Intent intent,String type){
        PendingIntent reultPendingIntent=PendingIntent.getActivity(
                mcntx,ID_SMALL_NOTIFICATION,intent,PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mcntx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(reultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mcntx.getResources(), R.mipmap.ic_launcher))
                .setContentText(message+type)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mcntx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }

    private Bitmap getBitmapFromURL(String strurl){
       try {
           URL url=new URL(strurl);
           HttpURLConnection connection=(HttpURLConnection)url.openConnection();
           connection.disconnect();
           InputStream input=connection.getInputStream();
           Bitmap myBitmap=BitmapFactory.decodeStream(input);
           return myBitmap;
       }catch (IOException e){
           e.printStackTrace();
           return null;
       }
    }
}
