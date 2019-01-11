package com.example.rai.kmn_mp_beta;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

public class MyNotification extends Notification {
    private Context ctx;
    private NotificationManager mNotificationManager;

    @SuppressLint("NewApi")
    public MyNotification(Context ctx, String Songname, Bitmap bm){
        super();
        this.ctx=ctx;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
        CharSequence tickerText = "Shortcuts";
        long when = System.currentTimeMillis();
        Notification.Builder builder = new Notification.Builder(ctx);
        @SuppressWarnings("deprecation")
        Notification notification=builder.getNotification();
        notification.when=when;
        notification.tickerText=tickerText;
        notification.icon=R.drawable.ic_launcher;


        RemoteViews contentView=new RemoteViews(ctx.getPackageName(), R.layout.notification);
        contentView.setTextViewText(R.id.noti_namesong,Songname);
        contentView.setImageViewBitmap(R.id.noti_image,bm);
        //set the button listeners
        setListeners(contentView);

        notification.contentView = contentView;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        CharSequence contentTitle = "From Shortcuts";
        mNotificationManager.notify(0, notification);
    }
    public void setListeners(RemoteViews view){
        //radio listener
        Intent play = new Intent(ctx, HelperActivity.class);
        play.putExtra("DO", "play");
        PendingIntent pplay = PendingIntent.getActivity(ctx, 0, play, 0);
        view.setOnClickPendingIntent(R.id.noti_bt_play, pplay  );

        Intent next = new Intent(ctx, HelperActivity.class);
        next.putExtra("DO", "next");
        PendingIntent pnext = PendingIntent.getActivity(ctx, 1, next, 0);
        view.setOnClickPendingIntent(R.id.noti_bt_next, pnext);


        //app listener
        Intent app = new Intent(ctx, HelperActivity.class);
        app.putExtra("DO", "app");
        PendingIntent pApp = PendingIntent.getActivity(ctx, 4, app, 0);
        view.setOnClickPendingIntent(R.id.noti_btn_cancel, pApp);
    }

}
