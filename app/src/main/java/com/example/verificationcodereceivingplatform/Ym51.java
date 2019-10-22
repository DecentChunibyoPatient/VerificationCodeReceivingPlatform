package com.example.verificationcodereceivingplatform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Ym51 extends http {
    HashMap<String,SmsThread> smsThreads=new HashMap<>();
    private SmsListener smsListener;
    private String itemid;
    private String token;
    //http://api.fxhyd.cn/UserInterface.aspx?action=login&username=你的账号&password=你的密码
    private String www="http://api.fxhyd.cn";
    public String login(String username, String password) throws IOException {
        return GET(www+ String.format("/UserInterface.aspx?action=login&username=%s&password=%s", username, password));
    }
    //http://api.fxhyd.cn/UserInterface.aspx?action=getaccountinfo&token=TOKEN
    public String getaccountinfo() throws IOException {
        return GET(www+ String.format("/UserInterface.aspx?action=getaccountinfo&token=%s", token));
    }

    //http://api.fxhyd.cn/UserInterface.aspx?action=getmobile&token=TOKEN&itemid=项目编号&excludeno=排除号段
    public String getmobile(String excludeno) throws IOException {
        return GET(www+ String.format("/UserInterface.aspx?action=getmobile&token=%s&itemid=%s&excludeno=%s ", token, itemid, excludeno));
    }
    //http://api.fxhyd.cn/UserInterface.aspx?action=getsms&token=TOKEN&itemid=项目编号&mobile=手机号码&release=1
    public String getsms(String mobile) throws IOException {
        return GET(www+ String.format("/UserInterface.aspx?action=getsms&token=%s&itemid=%s&mobile=%s", token, itemid, mobile));
    }
    //http://api.fxhyd.cn/UserInterface.aspx?action=release&token=TOKEN&itemid=项目编号&mobile=手机号码
    public String release(String mobile) throws IOException {
        return GET(www+ String.format("/UserInterface.aspx?action=release&token=%s&itemid=%S&mobile=%s ", token,itemid,mobile));
    }
    //GET http://api.fxhyd.cn/appapi.aspx?actionid=itemseach&itemname=%E5%A4%A9%E5%A4%A9&token=001649926664b0b1b2c0e8927b10a985e3c311c15701 HTTP/1.1
    public String itemseach(String itemname) throws IOException {
        return GET(www+ String.format("/appapi.aspx?actionid=itemseach&itemname=%s&token=%s",itemname, token));
    }
    public void setItemid(String itemid){
        this.itemid=itemid;
    }
    public void logout(){
        for (String key:smsThreads.keySet()){
            smsThreads.get(key).Stop();
        }
        smsThreads.clear();
        smsThreads=null;
    }
    interface LoginListener{
        void ok(String string);
        void err(String string);
    }
    interface SmsListener{
        void ok(String string);
        void err(String string);
        void timeOut(String number);
        void porgress(String number, long porgress, long max);
    }
    public void addSms(String number){
        SmsThread smsThread=new SmsThread(number,smsListener,this);
        smsThreads.put(number,smsThread);
    }
    public void setSmsListener(SmsListener smsListener){
        this.smsListener=smsListener;
    }
    boolean loginuser(String username, String password, LoginListener loginListener) throws IOException, JSONException {

        String string =login(username,password);
        String[]strings=string.split("[|]");
        if (strings.length==2){
            if (strings[0].equals("success")){
                token=strings[1];
                return true;
            }
        }
        loginListener.err(string);
        return false;
    }
    public void login(final String username, final String password, final LoginListener loginListener){


        if (Thread.currentThread().getName().equals("main")){
            new Thread(){
                @Override
                public void run() {
                    try {
                        loginuser(username,password,loginListener) ;
                    } catch (IOException e) {
                        e.printStackTrace();
                        loginListener.err(null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loginListener.err(null);
                    }
                }
            }.start();
        }else {
            try {
                loginuser(username,password,loginListener) ;
            } catch (IOException e) {
                e.printStackTrace();
                loginListener.err(null);
            } catch (JSONException e) {
                e.printStackTrace();
                loginListener.err(null);
            }
        }
    }
    class SmsThread extends Thread {
        String number;
        long timeOut=120*1000;
        long startTime,endTime;
        long loopTime=3000;
        SmsListener smsListener;
        Ym51 ym51;
        public SmsThread(String number, SmsListener smsListener, Ym51 ym51){
            this.number=number;
            this.smsListener=smsListener;
            this.ym51=ym51;
            start();
        }
        @Override
        public void run() {
            startTime= System.currentTimeMillis();
            try {

                /*
                 * 不管循环里是否调用过线程阻塞的方法如sleep、join、wait，这里还是需要加上
                 * !Thread.currentThread().isInterrupted()条件，虽然抛出异常后退出了循环，显
                 * 得用阻塞的情况下是多余的，但如果调用了阻塞方法但没有阻塞时，这样会更安全、更及时。
                 */
                while (!Thread.currentThread().isInterrupted()&& endTime-startTime<timeOut) {
                    String string=ym51.getsms(number);
                    String[]strings=string.split("[|]");
                    if (strings.length==2){
                        if (strings[0].equals("success")){
                            smsListener.ok(strings[1]);
                            break;
                        }
                    }
                    JSONObject jsonObject=new JSONObject(string).getJSONObject("Return");
                    if (jsonObject.getString("Staus").equals("0")){
                        smsListener.ok(string);
                        return;
                    }

                    Thread.sleep(loopTime);
                    endTime= System.currentTimeMillis();
                    smsListener.porgress(number,endTime-startTime,timeOut);
                }
                smsListener.timeOut(number);
            } catch (InterruptedException e) {
                //线程在wait或sleep期间被中断了
                smsListener.err(number);
            } catch (JSONException e) {
                e.printStackTrace();
                smsListener.err(number);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //线程结束前做一些清理工作
                try {
                    ym51.release(number);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        void Stop(){
            if (isAlive()){
                interrupt();
                stop();
            }
        }
    }
}
