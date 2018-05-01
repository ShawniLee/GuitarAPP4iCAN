package ex.guitartest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import ex.guitartest.teacher.TeacherHomeActivity;

public class AlarmService extends Service {
    private static final int NOTIFICATION_ID = 1000;
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent2 = new Intent(AlarmService.this, TeacherHomeActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent2, 0);
                Notification notify = new NotificationCompat.Builder(getApplication(),"default")
                        .setSmallIcon(R.drawable.logo)
                       // .setTicker("今天的练习时间到啦！")
                        //.setContentTitle("练习提醒")
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText("本周还有2个小时的练习任务哦，快去练习吧~~"))
                        .setContentTitle("动态提醒")
                        .setContentTitle("老师发布了新动态，快来看看吧~~")
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.mipmap.nolificationpicture)))
                        .setFullScreenIntent(pendingIntent,false)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                manager.notify(NOTIFICATION_ID, notify);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
