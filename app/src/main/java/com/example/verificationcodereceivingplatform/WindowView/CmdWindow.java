package com.example.verificationcodereceivingplatform.WindowView;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.verificationcodereceivingplatform.Game9688;
import com.example.verificationcodereceivingplatform.R;
import com.example.verificationcodereceivingplatform.WeixinBroadcastReceiver;
import com.example.verificationcodereceivingplatform.tool.AppUtils;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by ibm on 2017/4/9.
 */

public class CmdWindow extends WindowManagerService {
    static String TAG="CmdWindow";
    static public ImageButton button;
    static public View buttonbar;
    static public TextView seep;
    RelativeLayout relativeLayout;
    WeixinBroadcastReceiver weixinBroadcastReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setContentView(R.layout.button);


        weixinBroadcastReceiver=new WeixinBroadcastReceiver(getApplicationContext(), new WeixinBroadcastReceiver.WeixinListeren() {
            @Override
            public void loadUrl(final Context context, Intent intent, String url) {
                System.out.println(" loadUrl="+url);
                //weixinBroadcastReceiver.startAPP(getPackageName());
                HashMap<String, String> hashMap=new HashMap<>();
                String[]strings=url.split("[?]")[1].split("[&]");
                for (String string:strings){
                    String[]strings1=string.split("[=]");
                    hashMap.put(strings1[0],strings1[1]);
                }

                final String code=hashMap.get("code");
                if (code!=null)
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Game9688 game9688=new Game9688();
                                game9688.BindAccount(code);
                                game9688.BindUser("13250740368","123456a",code);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
            }
        });

        relativeLayout=findViewById(R.id.RelativeLayout);
        button= findViewById(R.id.imageButton);
        seep=findViewById(R.id.seep);
        seep.setClickable(false);

        button=findViewById(R.id.imageButton);
        button.setClickable(false);
        findViewById(R.id.imageButton).setOnTouchListener(new TouchListener(new TouchListener.Touch() {
            @Override
            public void open() {
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(70,0,0,0);
              openChildView(buttonbar,layoutParams);
            }

            @Override
            public void close() {
               closeChildView();
            }

            @Override
            public void move(View v, MotionEvent event) {
                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                WindowManager.LayoutParams layoutParams=getLayoutParams();
                layoutParams.x = (int) (event.getRawX() - v.getMeasuredWidth()/2);
                //25为状态栏的高度
                layoutParams.y = (int) event.getRawY() - v.getMeasuredHeight()/2 - 25;
                //刷新
                getWindowManager().updateViewLayout(getRootView(), layoutParams);
            }
        }));
        buttonbar=ChildView.buttonbarinit(getApplicationContext(), new ChildView.CmdInterface() {
            @Override
            public void test() {
           /*  if (AppUtils.isAppRunning(getApplicationContext(),"com.tencent.mm")){
                 System.out.println("正在运行");

             }else {
                 System.out.println("关闭");
                 AppUtils.doStartApplicationWithPackageName(getApplicationContext(),"com.tencent.mm");
             }*/
                start();
            }
        });
    }
    public void start(){
        weixinBroadcastReceiver.startWebViewUI("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx646754dde6879801&redirect_uri=http%3a%2f%2fwechat.9688game.com%2fBindAccount.aspx&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect");
        AppUtils.doStartApplicationWithPackageName(getApplicationContext(),"com.tencent.mm");
    }


}
