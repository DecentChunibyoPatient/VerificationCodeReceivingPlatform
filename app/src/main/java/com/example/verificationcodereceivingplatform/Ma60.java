package com.example.verificationcodereceivingplatform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Ma60 extends http{
    private final String www="http://sms.60ma.net";

    private String UserKey;
    private String UserID;
    private String dockcode;
    private String developer;

    private SmsListener smsListener;

    HashMap<String, String> docksnames=new HashMap<>();
    HashMap<String, String> dockcodes=new HashMap<>();
    HashMap<String,SmsThread> smsThreads=new HashMap<>();


     Ma60(String developer){
        this.developer=developer;
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
    class SmsThread extends Thread {
         String number;
         long timeOut=120*1000;
         long startTime,endTime;
         long loopTime=3000;
         SmsListener smsListener;
         Ma60 ma60;
         public SmsThread(String number, SmsListener smsListener, Ma60 ma60){
             this.number=number;
             this.smsListener=smsListener;
             this.ma60=ma60;
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
                    String string=ma60.getsms(number,true);
                    JSONObject jsonObject=new JSONObject(string).getJSONObject("Return");
                    if (jsonObject.getString("Staus").equals("0")){
                        smsListener.ok(string);
                        break;
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
                    ma60.freetelnum(number);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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
    public void logout(){
         for (String key:smsThreads.keySet()){
             smsThreads.get(key).Stop();
         }

        docksnames.clear();
        dockcodes.clear();
        //smsThreads.clear();

        docksnames=null;
        dockcodes=null;
        //smsThreads=null;
    }
    public void login(String username, String password, final LoginListener loginListener){
        if (Thread.currentThread().getName().equals("main")){
            new Thread(){
                @Override
                public void run() {
                    try {
                        loginuser("1975614467","123456a",loginListener) ;
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
                loginuser("1975614467","123456a",loginListener) ;
            } catch (IOException e) {
                e.printStackTrace();
                loginListener.err(null);
            } catch (JSONException e) {
                e.printStackTrace();
                loginListener.err(null);
            }
        }
    }
    public void init(){

        login("1975614467", "123456a", new LoginListener() {
            @Override
            public void ok(String string) {
                try {
                    System.out.println("XXXX"+ Thread.currentThread().getName());
                    setDocksns( finddocks());
                    String[] dockcode=findDockcode("开开斗地主").split("[&]");
                    System.out.println(dockcode[0]+" "+dockcode[5]+" "+dockcode[7]);
                    setDockcode(dockcode[0]);
                    gettelnum("","");
                    Thread.sleep(3000);
                    freetelnumall();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(String string) {

            }
        });

    }
    public void setDockcode(String dockcode){
         this.dockcode=dockcode;
    }
    public void setDocksns(String string) throws JSONException {
         JSONObject jsonObject =new JSONObject(string).getJSONObject("Return");
         int Count=jsonObject.getInt("Count");
         for (;Count>0;Count--){
             String string1=jsonObject.getString("Info"+Count);

             //Info1=181C0686045015E&liushima&0&0.1&六十码官方对接-陌陌注册项目&陌陌注册&0&0.15
             String[]strings=string1.split("[&]");
             docksnames.put(strings[5],string1);
             dockcodes.put(strings[0],string1);
         }
    }
    public String findDockcode(String name){
         return docksnames.get(name);
    }
    //    注：使用API接口，开放的对接无需接入即可直接使用！
//
//
//
//    海外节点：香港节点  47.75.191.62    欧洲中部节点   47.91.72.116  替换域名 sms.60ma.net 使用
//
//    登录平台
//
//    http://sms.60ma.net/loginuser?cmd=login&encode=utf-8
//
//    参数                可空              默认值              备注
//    username          否                 无                用户名
//    password          否                 无                用户密码MD5值(32位小写MD5)
//    dtype                是                 xml               规定返回的数据类型 可选 json/xml
//
//    返回示例：{"Return":{"Staus":"0","UserID":"30","UserKey":"85E20AA1B018234565432","ErrorInfo":"登录成功！}}
//        返回用户UID 和用户UserKey将留作下面的API使用
    boolean loginuser(String username, String password, LoginListener loginListener) throws IOException, JSONException {

        String string =GET(www+"/loginuser?cmd=login&encode=utf-8"+"&"+ String.format("username=%s&password=%s&dtype=%s",username,MD5(password),"json"));
        JSONObject jsonObject=new JSONObject(string);
        String UserKey=jsonObject.getJSONObject("Return").getString("UserKey");
        String UserID=jsonObject.getJSONObject("Return").getString("UserID");
        if (UserKey!=null){
            this.UserKey=UserKey;
            this.UserID=UserID;
            loginListener.ok(string);
            return true;
        }
        loginListener.err(string);
        return false;
    }


//        查询信息
//
//        http://sms.60ma.net/queryinfo?cmd=query&encode=utf-8&api=yes
//
//        参数              可空              默认值              备注
//        userid             否                 无                用户UID
//        userkey           否                 无                userkey
//        dtype              是                 xml               规定返回的数据类型 可选 json/xml
//
//        返回示例：{"Return":{"Staus":"0","ErrorInfo":"查询成功！,"Level":"1","Money":"88.38"}}

    String queryinfo() throws IOException, JSONException {
        return GET(www+"/queryinfo?cmd=query&encode=utf-8&api=yes"+"&"+ String.format("userid=%s&userkey=%s&dtype=%s",UserID,UserKey,"json"));
    }
//
//            获取号码
//
//            http://sms.60ma.net/newsmssrv?cmd=gettelnum&encode=utf-8
//
//            参数              可空              默认值              备注
//            userid             否                 无                用户UID
//            userkey           否                 无                userkey
//            docks              否                 无                欲获取号码的对接码(请从客户端查看对接码) 支持组合项目(a,b)
//            province          是                 0                 指定省/直辖市代码
//            city                  是                 0                 指定市代码
//            operator          是                 0                 指定运营商代码
//            telnumsection     是                 无                指定欲获取的号码或号段
//            telback              是                 无                排除的号码或号段(支持多个 ,分隔)
//            gettype             是                 无                指定卡商UID获取号码
//            dtype               是                 xml               规定返回的数据类型 可选 json/xml
//
//            运营商/省/城市代码表 请看上面
//
//            返回示例：{"Return":{"Staus":"0","Telnum":"17011134058","ErrorInfo":"获取成功！}}

    String gettelnum(String telnumsection, String telback) throws IOException, JSONException {
        return GET(www+"/newsmssrv?cmd=gettelnum&encode=utf-8"+"&"+ String.format("userid=%s&userkey=%s&docks=%s&telnumsection=%s&telback=%s&dtype=%s",UserID,UserKey, dockcode,  telnumsection,  telback,"json"));
    }
//                获取短信
//
//                http://sms.60ma.net/newsmssrv?cmd=getsms&encode=utf-8
//
//                参数              可空              默认值              备注
//                userid             否                 无                用户UID
//                userkey           否                 无                userkey
//                dockcode        否                 无                对接码(请从客户端查看对接码)
//                telnum            否                 无                获取到的号码
//                reusing           是                 false             规定是否重用这个号码
//
//                developer       是                 无                  开发者账号(注意: 60码账号通用)
//
//                dtype              是                 xml               规定返回的数据类型 可选 json/xml
//
//                返回示例：{"Return":{"Staus":"0","SmsContent":"你正在注册微信帐号，验证码71408。请勿转发。【腾讯科技】,"ErrorInfo":"成功！}}

    String getsms(String telnum, boolean reusing) throws IOException, JSONException {
        return GET(www+"/newsmssrv?cmd=getsms&encode=utf-8"+"&"+ String.format("userid=%s&userkey=%s&dockcode=%s&telnum=%s&reusing=%s&developer=%s&dtype=%s",UserID,UserKey, dockcode,  telnum,  reusing,developer,"json"));
    }
//                申请发送短信
//
//                http://sms.60ma.net/newsmssrv?cmd=sendsms&encode=utf-8
//
//                参数               可空              默认值              备注
//                userid              否                 无                用户UID
//                userkey            否                 无                userkey
//                dockcode        否                 无                对接码(请从客户端查看对接码)
//                        telnum            否                 无                获取到的号码
//                content           否                 无                欲发送的短信内容（需要UTF-8编码）
//                dtype              是                 xml               规定返回的数据类型 可选 json/xml
//
//                返回示例：{"Return":{"Staus":"0","ErrorInfo":"OK！}}
//
//
//
//
//
//                    获取发送短信结果
//
//                    http://sms.60ma.net/newsmssrv?cmd=getsendsmsstate&encode=utf-8
//
//                    参数               可空              默认值              备注
//                    userid               否                 无                用户UID
//                    userkey             否                 无                userkey
//                    dockcode          否                 无                对接码(请从客户端查看对接码)
//                            telnum              否                 无                获取到的号码
//                    dtype                是                 xml               规定返回的数据类型 可选 json/xml
//
//                    返回示例：{"Return":{"Staus":"0","ErrorInfo":"已成功发送！"}}
//
//
//
//
//
//                    释放号码
//
//                    http://sms.60ma.net/newsmssrv?cmd=freetelnum&encode=utf-8
//
//                    参数              可空              默认值              备注
//                    userid             否                 无                用户UID
//                    userkey           否                 无                userkey
//                    docks              否                 无                对接码(请从客户端查看对接码)
//                            telnum            否                 无                获取到的号码
//                    dtype              是                 xml               规定返回的数据类型 可选 json/xml
//
//                    返回示例：{"Return":{"Staus":"0","ErrorInfo":"OK！}}

    String freetelnum(String telnum) throws IOException, JSONException {
        return GET(www+"/newsmssrv?cmd=freetelnum&encode=utf-8"+"&"+ String.format("userid=%s&userkey=%s&dockcode=%s&telnum=%s&dtype=%s",UserID,UserKey, dockcode,  telnum, "json"));
    }

//                        释放账户占用的所有号码
//
//                        http://sms.60ma.net/newsmssrv?cmd=freetelnumall&encode=utf-8
//
//                        参数              可空              默认值              备注
//                        userid             否                 无                用户UID
//                        userkey           否                 无                userkey
//                        dtype              是                 xml               规定返回的数据类型 可选 json/xml
//
//                        返回示例：{"Return":{"Staus":"0","ErrorInfo":"OK！}}

    String freetelnumall( ) throws IOException, JSONException {
        return GET(www+"/newsmssrv?cmd=freetelnumall&encode=utf-8"+"&"+ String.format("userid=%s&userkey=%s&dockcode=%s&dtype=%s",UserID,UserKey, dockcode, "json"));
    }
    //                            拉黑号码
//
//                            http://sms.60ma.net/newsmssrv?cmd=addblacktelnum&encode=utf-8
//
//                            参数             可空              默认值              备注
//                            userid            否                 无                用户UID
//                            userkey          否                 无                userkey
//                            docks             否                 无                对接码(请从客户端查看对接码)
//                                    telnum           否                 无                获取到的号码
//                            dtype             是                 xml               规定返回的数据类型 可选 json/xml
//
//                            返回示例：{"Return":{"Staus":"0","ErrorInfo":"成功！}}
    String addblacktelnum(String telnum) throws IOException, JSONException {
        return GET(www+"/newsmssrv?cmd=addblacktelnum&encode=utf-8"+"&"+ String.format("userid=%s&userkey=%s&dockcode=%s&telnum=%s&dtype=%s",UserID,UserKey, dockcode,  telnum, "json"));
    }
    //Referer: http://sms.60ma.net/docking?cmd=finddocks&page=0&coupling=0&userid=18582&findtype=4&word=liushima&findcount=1000&fjdata=&
    ///docking?cmd=finddocks&page=0&coupling=0&userid=18582&findtype=4&word=liushima&findcount=1000&fjdata=&dtype=json
    String finddocks() throws IOException, JSONException {
        return GET(www+"/docking?cmd=finddocks"+"&"+ String.format("userid=%s&page=%s&coupling=%s&findtype=%s&word=%s&findcount=%s&dtype=%s&fjdata=",UserID,0, 0,  4,"liushima","", "json"));
    }

}
