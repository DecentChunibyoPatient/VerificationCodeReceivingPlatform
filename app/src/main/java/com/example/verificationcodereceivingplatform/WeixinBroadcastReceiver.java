package com.example.verificationcodereceivingplatform;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class WeixinBroadcastReceiver extends BroadcastReceiver {
     Context context;
     String Action="com.tencent.mm";
    WeixinListeren weixinListeren;
    boolean isRun=false;
     public WeixinBroadcastReceiver(Context context, WeixinListeren weixinListeren){
         this.context=context;
         this.weixinListeren=weixinListeren;
         context.registerReceiver(this, new IntentFilter(context.getPackageName()));//注册广播
     }
    @Override
    public void onReceive(Context context, Intent intent) {
        isRun=true;
         String loadUrl=intent.getStringExtra("loadUrl");
        if (loadUrl!=null)
            weixinListeren.loadUrl( context,  intent,loadUrl);
    }

      public void startWebViewUI(String rawUrl){
        Intent intent=new Intent();
        intent.setAction(Action);
        intent.putExtra("name","startWebViewUI");
        intent.putExtra("rawUrl",rawUrl);
        intent.putExtra("Action",context.getPackageName());
        context.sendBroadcast(intent);
    }
    void startActivity(String loadClass){
        Intent intent=new Intent();
        intent.setAction(Action);
        intent.putExtra("name","startActivity");
        intent.putExtra("loadClass",loadClass);
        intent.putExtra("Action",context.getPackageName());
        context.sendBroadcast(intent);
    }
    public void startAPP(String PackageName){
        Intent intent=new Intent();
        intent.setAction(Action);
        intent.putExtra("name","startAPP");
        intent.putExtra("Action",PackageName);
        context.sendBroadcast(intent);
    }
    public void isRun(){
        Intent intent=new Intent();
        intent.setAction(Action);
        intent.putExtra("name","isRun");
        intent.putExtra("Action",context.getPackageName());
        context.sendBroadcast(intent);
    }
    public interface WeixinListeren{
         void loadUrl(Context context, Intent intent, String url);
    }
}
