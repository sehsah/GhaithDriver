package com.mahmoudsehsah.ghaithDriver.Server;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.acitivities.HomeActivity;
import com.mahmoudsehsah.ghaithDriver.acitivities.MessageActivity;
import com.mahmoudsehsah.ghaithDriver.acitivities.NotifcationActivity;
import com.mahmoudsehsah.ghaithDriver.models.ChatList;
import com.mahmoudsehsah.ghaithDriver.models.FinishTrip;
import com.mahmoudsehsah.ghaithDriver.models.NewTripe;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mahmoud on 3/19/2018.
 */

public class  MyFirebaseMessagingService extends FirebaseMessagingService {
    public static int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
     //   sendNotification(remoteMessage.getData());
        if (remoteMessage.getNotification() != null){
            sendNotification(remoteMessage.getData());

        }

        if (remoteMessage.getData().size() > 0){

            int typeNoti = Integer.parseInt(remoteMessage.getData().get("typeNoti"));

            if (typeNoti == 1) {

                sendNotification(remoteMessage.getData());
            }else if(typeNoti == 5){
                Intent intent = new Intent("CancelTrip");
                sendBroadcast(intent);

            }else if(typeNoti == 4){
                String id_user = remoteMessage.getData().get("id_user");
                String id_trip = remoteMessage.getData().get("id_trip");
                String price = remoteMessage.getData().get("price");
                int id_driver = Integer.parseInt(remoteMessage.getData().get("lat"));
                FinishTrip finish_trip = new FinishTrip();
                finish_trip.setId_user(id_user);
                finish_trip.setId_driver(String.valueOf(id_driver));
                finish_trip.setId_trip(id_trip);
                finish_trip.setPrice(price);
                if (isAppIsInBackground(this)) {
                    // app is in background show notification to user
                    sendNotificationFinishTrip(finish_trip);
                    Log.e("background","true");
                } else {
                    // app is forground and user see it now send broadcast to update chat
                    // you can send broadcast to do anything if you want !
                    Intent intent = new Intent("ShowBillActivity");
                    intent.putExtra("bill", finish_trip);
                    sendBroadcast(intent);
                    Log.e("recevied", "true");
                }
            }else if(typeNoti == 3){
                Log.e("trip id_user ", remoteMessage.getData().get("id_user"));
                Log.e("trip lat ", remoteMessage.getData().get("lat"));
                Log.e("trip lng ", remoteMessage.getData().get("lng"));
                Log.e("trip pickup_location ", remoteMessage.getData().get("pickup_location"));
                Log.e("trip drop_location ", remoteMessage.getData().get("drop_location"));
                Log.e("trip pricee ", remoteMessage.getData().get("price"));
                Log.e("trip lat_user ", remoteMessage.getData().get("lat_user"));
                Log.e("trip lng_user ", remoteMessage.getData().get("lng_user"));
                Log.e("id  ", remoteMessage.getData().get("id"));

                String id_user = remoteMessage.getData().get("id_user");
                String places = remoteMessage.getData().get("places");
                String time = remoteMessage.getData().get("time");
                double lat = Double.parseDouble(remoteMessage.getData().get("lat"));
                double lng = Double.parseDouble(remoteMessage.getData().get("lng"));
                String pickup_location = remoteMessage.getData().get("pickup_location");
                String drop_location = remoteMessage.getData().get("drop_location");
                String price = remoteMessage.getData().get("price");
                double lat_user = Double.parseDouble(remoteMessage.getData().get("lat_user"));
                double lng_user = Double.parseDouble(remoteMessage.getData().get("lng_user"));
                int id = Integer.parseInt(remoteMessage.getData().get("id"));

                NewTripe tripe = new NewTripe();
                tripe.setPickup_location(pickup_location);
                tripe.setId_user(String.valueOf(id_user));
                tripe.setLat_user(Double.parseDouble(String.valueOf(lat_user)));
                tripe.setLng_user(Double.parseDouble(String.valueOf(lng_user)));
                tripe.setDrop_location(drop_location);
                tripe.setPlaces(String.valueOf(places));
                tripe.setLng(Double.parseDouble(String.valueOf(lng)));
                tripe.setLat(Double.parseDouble(String.valueOf(lat)));
                tripe.setPrice(String.valueOf(price));
                tripe.setId(String.valueOf(id));
                tripe.setCreatedAt(time);

                ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                Log.d("TEST", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());

                if (isAppIsInBackground(this) &&  !taskInfo.get(0).topActivity.getClassName().equals(HomeActivity.class.getName())) {
                    // app is in background show notification to user
                    sendNotificationNewTrip(tripe);
                    Log.e("background","true");
                } else {
                    // app is forground and user see it now send broadcast to update chat
                    // you can send broadcast to do anything if you want !
                    Intent intent = new Intent("ShowTripActivity");
                    intent.putExtra("tripe", tripe);
                    sendBroadcast(intent);
                    Log.e("recevied", "true");
                }

            } else {

                Log.e("message content ", remoteMessage.getData().get("message"));
                Log.e("message id_order ", remoteMessage.getData().get("id_order"));
                Log.e("message client_id ", remoteMessage.getData().get("client_id"));

                String messageContent = remoteMessage.getData().get("message");
                int userId = Integer.parseInt(remoteMessage.getData().get("client_id"));
                int driver_id = Integer.parseInt(remoteMessage.getData().get("driver_id"));
                int send = Integer.parseInt(remoteMessage.getData().get("send"));
                int type = Integer.parseInt(remoteMessage.getData().get("type"));
                int id_order = Integer.parseInt(remoteMessage.getData().get("id_order"));
                String timestamp = remoteMessage.getData().get("timestamp");

                // Create new message and assign value to it
                ChatList message = new ChatList();
                message.setMessage(messageContent);
                message.setClientId(String.valueOf(userId));
                message.setCreatedAt(timestamp);
                message.setDriverId(String.valueOf(driver_id));
                message.setSend(send);
                message.setid_order(String.valueOf(id_order));
                message.setType(String.valueOf(type));

                SessionManager sessionManager = new SessionManager(MyFirebaseMessagingService.this);
                HashMap<String, String> user = sessionManager.getUserDetails();
                int uid = Integer.parseInt(user.get(SessionManager.USER_ID));

//                ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//                Log.d("TEST", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());


                // check if the sender of message is current user or not
                //if (!(userId == uid)) {
                // check if app in background or not
                if (isAppIsInBackground(this)) {
                    // app is in background show notification to user
                    sendNotificationMessage(message);
                } else {
                    // app is forground and user see it now send broadcast to update chat
                    // you can send broadcast to do anything if you want !
                    Intent intent = new Intent("UpdateChatActivity");
                    intent.putExtra("message", message);
                    intent.putExtra("driver_id", driver_id);
                    intent.putExtra("id_order",id_order);
                    sendBroadcast(intent);
                    Log.e("recevied", "true");

                }
            }
            //}
        }
    }

    private  void  sendNotificationFinishTrip(FinishTrip finishTrip){
        Intent intent = new Intent(this, HomeActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("تم انتهاء رحلتك")
                .setContentText("")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private  void  sendNotificationNewTrip(NewTripe tripe){
        Intent intent = new Intent(this, HomeActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("رحله جديدة في انتظارك")
                .setContentText("")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private  void sendNotificationMessage(ChatList message)
    {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("message", message.getMessage());
        intent.putExtra("client_id", message.getClientId());
        intent.putExtra("id_order",message.getid_order());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("رساله من العميل")
                .setContentText(message.getMessage())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotification(Map<String, String> data) {

        int num = ++NOTIFICATION_ID;
        Bundle msg = new Bundle();
        for (String key : data.keySet()) {
            Log.e(key, data.get(key));
            msg.putString(key, data.get(key));
        }
        Intent intent = new Intent(this, NotifcationActivity.class);
        if (msg.containsKey("action")) {
            intent.putExtra("action", msg.getString("action"));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, num /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Log.e("title ",msg.getString("title"));
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(msg.getString("title"))
                .setContentText(msg.getString("msg"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(num, notificationBuilder.build());
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses)
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
