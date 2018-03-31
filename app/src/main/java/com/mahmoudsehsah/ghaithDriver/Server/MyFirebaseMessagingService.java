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
import com.mahmoudsehsah.ghaithDriver.acitivities.ChatActivity;
import com.mahmoudsehsah.ghaithDriver.acitivities.MessageActivity;
import com.mahmoudsehsah.ghaithDriver.acitivities.NotifcationActivity;
import com.mahmoudsehsah.ghaithDriver.acitivities.OrdersActivity;
import com.mahmoudsehsah.ghaithDriver.models.ChatList;
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

            } else {


                Log.e("message content ", remoteMessage.getData().get("message"));

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

                // check if the sender of message is current user or not
                //if (!(userId == uid)) {
                // check if app in background or not
                if (isAppIsInBackground(this)) {
                    // app is in background show notification to user
                    sendNotificationMessage(message);
                    Log.e("recevied ", " InBackground");
                } else {
                    // app is forground and user see it now send broadcast to update chat
                    // you can send broadcast to do anything if you want !
                    Intent intent = new Intent("UpdateChatActivity");
                    intent.putExtra("message", message);
                    sendBroadcast(intent);
                    Log.e("recevied", "true");

                }
            }
            //}
        }
    }

    private  void sendNotificationMessage(ChatList message)
    {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("message", String.valueOf(message));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("رساله من المندوب")
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
