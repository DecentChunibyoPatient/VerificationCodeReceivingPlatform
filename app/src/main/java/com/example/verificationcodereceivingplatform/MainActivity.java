package com.example.verificationcodereceivingplatform;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;


import com.example.verificationcodereceivingplatform.tool.AppUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Ma60 ma60=new Ma60("1975614467");
    WeixinBroadcastReceiver weixinBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtils.startService(getApplicationContext(),"android.intent.action.START_PCM_PLAY_SERVICE");

/*        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //weixinBroadcastReceiver.startActivity("com.tencent.mm.plugin.webview.ui.tools.WebViewUI");
                        weixinBroadcastReceiver.startWebViewUI("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx646754dde6879801&redirect_uri=http%3a%2f%2fwechat.9688game.com%2fBindAccount.aspx&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect");
                        doStartApplicationWithPackageName("com.tencent.mm");
                    }
                });
            }
        }.start();*/

        new Thread(){
            @Override
            public void run() {
                try {
                    String number="13250740368";
                    Game9688 game9688=new Game9688();
                    game9688.CheckName(number);
                    game9688.GetSecurityCode(number);
                    game9688.VerifyCode(number,"0000");
                    game9688.SpreadLink(number,"asdasdasdxx","123456a","0000");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
      /*  ma60.login("1975614467", "123456a", new Ma60.LoginListener() {
            @Override
            public void ok(String string) {
                try {
                    ma60.setDocksns( ma60.finddocks());
                    String[] dockcode=ma60.findDockcode("开开斗地主").split("[&]");
                    System.out.println(dockcode[0]+" "+dockcode[5]+" "+dockcode[7]);
                    ma60.setDockcode(dockcode[0]);
                    ma60.setSmsListener(new Ma60.SmsListener() {
                        @Override
                        public void ok(String number) {

                        }

                        @Override
                        public void err(String number) {

                        }

                        @Override
                        public void timeOut(String number) {

                        }

                        @Override
                        public void porgress(String number, long porgress, long max) {

                        }
                    });
                    String string1=ma60.gettelnum("","");
                    JSONObject jsonObject=new JSONObject(string1).getJSONObject("Return");
                    ma60.addSms(jsonObject.getString("Telnum"));
                    *//*Thread.sleep(3000);
                    ma60.freetelnumall();*//*
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(String string) {

            }
        });*/
    }
    void findView(ArrayList<View> arrayList, Class c, ViewGroup viewGroup){
        if (viewGroup.getChildCount()>0){
            for (int i=0;i<viewGroup.getChildCount();i++){
                View view=viewGroup.getChildAt(i);
                if (view.getClass().equals(c)){
                    arrayList.add(view);
                }
                try {
                    findView(arrayList,c, (ViewGroup) view);
                }catch (ClassCastException e){

                }
            }
        }
    }


}
