package com.example.verificationcodereceivingplatform;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

public class Game9688 extends http {
    private String www="http://wechat.9688game.com";
    private String spreadId= URLEncoder.encode("hZx+ACIhA0z8yxEzM/bN+A==","UTF-8");
    private String url=www+ String.format("/SpreadLink.aspx?spreadId=%s",spreadId);
    private String __VIEWSTATE;
    private String __VIEWSTATEGENERATOR;
    private String __EVENTVALIDATION;
    private String cookie;

    public Game9688() throws IOException {
        if (Thread.currentThread().getName().equals("main")){
            new Thread(){
                @Override
                public void run() {
                    try {
                        init();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }else {
            init();
        }
    }
    public void  init() throws IOException {
        String string=SpreadLink();
        Document doc = Jsoup.parse(string);
         __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
         __VIEWSTATEGENERATOR = doc.getElementById("__VIEWSTATEGENERATOR").val();
         __EVENTVALIDATION = doc.getElementById("__EVENTVALIDATION").val();
    }
    //GET http://wechat.9688game.com/SpreadLink.aspx?spreadId=hZx%2bACIhA0z8yxEzM%2fbN%2bA%3d%3d HTTP/1.1
    public String SpreadLink() throws IOException {
        HashMap<String, String> hashMap =GETs(url);
        cookie=hashMap.get("Cookie");
        return hashMap.get("data");
    }
    //POST http://wechat.9688game.com/WSSpread.asmx/CheckName HTTP/1.1
    public String CheckName(String userName) throws IOException {
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("Content-Type"," application/json");
        hashMap.put("Referer",url);
        return POST(www+"/WSSpread.asmx/CheckName", String.format("{userName:'%s'}",userName),hashMap);
    }
    //POST http://wechat.9688game.com/WeixinFeedback.ashx?action=GetWxConfig
    public String GetWxConfig() throws IOException {
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("Content-Type"," application/json");
        hashMap.put("Referer",url);
        return POST(www+"/WeixinFeedback.ashx?action=GetWxConfig", String.format("shareUrl=%s", URLEncoder.encode(url,"UTF-8")),hashMap);
    }
    //POST http://wechat.9688game.com/WSSpread.asmx/GetSecurityCode HTTP/1.1
    public String GetSecurityCode(String mobilePhone) throws IOException {
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("Content-Type"," application/json");
        hashMap.put("Referer",url);
        return POST(www+"/WSSpread.asmx/GetSecurityCode", String.format("{mobilePhone:'%s'}",mobilePhone),hashMap);
    }
    //POST http://wechat.9688game.com/WSSpread.asmx/VerifyCode HTTP/1.1
    public String VerifyCode(String phone, String code) throws IOException {
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("Content-Type"," application/json");
        hashMap.put("Referer",url);
        return POST(www+"/WSSpread.asmx/VerifyCode", String.format("{phone:'%s',code:%s}",phone,code),hashMap);
    }
    //POST http://wechat.9688game.com/SpreadLink.aspx?spreadId=hZx%2bACIhA0z8yxEzM%2fbN%2bA%3d%3d HTTP/1.1
    public String SpreadLink(String number, String name, String password, String code) throws IOException {
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("Content-Type"," application/json");
        hashMap.put("Referer",url);
        hashMap.put("Cookie",cookie);
        //__VIEWSTATE=%2FwEPDwUKMTE1MDAyMDUwOGRkA7wKr2c2Jks%2B8nHq677YiBv0RrX6M6g9b8ueaCEZEG4%3D&__VIEWSTATEGENERATOR=BCB92CA6&__EVENTVALIDATION=%2FwEdAAZ363vy4%2BKQDefxKNURHceDFcTd2TBrVxEAJVrcwengG3PH8rtbwYy35hAH8Peimudxj4ZiV47m7wk3YuKbdj00d8ucLXrfp21UJ%2Fx5sXo%2FVoVrfPTZCZCdgWOPfArF%2BOsoOjP%2FI4SUqvpMeDzKR6c6HYH5SOAgEW2CvngSYD7imA%3D%3D&txtMobile=17086305478&txtNickname=%E4%BB%A5%E4%B8%80%E9%A3%8E%E6%9C%88&txtLogonPass=123456a&txtConfirmPass=123456a&txtCode=9704
        return POST(url, String.format("__VIEWSTATE=%s&__VIEWSTATEGENERATOR=%s&__EVENTVALIDATION=%s&txtMobile=%s&txtNickname=%s&txtLogonPass=%s&txtConfirmPass=%s&txtCode=%s",__VIEWSTATE,__VIEWSTATEGENERATOR,__EVENTVALIDATION,number, URLEncoder.encode(name,"UTF-8"),password,password,code),hashMap);
    }
    //GET http://wechat.9688game.com/BindAccount.aspx?code=001hlodX1F86MT0yFXcX1NQBdX1hlodh&state=STATE HTTP/1.1
    public String BindAccount(String code) throws IOException {
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n");
        hashMap.put("User-Agent","Mozilla/5.0 (Linux; Android 4.4.2; vivo X20A Build/NMF26X) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 MicroMessenger/6.6.7.1321(0x26060739) NetType/WIFI Language/zh_CN\n");
           return GETs(String.format(www+"/BindAccount.aspx?code=%s&state=STATE",code),hashMap).get("data");
    }
    //POST http://wechat.9688game.com/WSWeixin.asmx/BindUser HTTP/1.1
    public String BindUser(String userName, String pwd, String code) throws IOException {
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("Content-Type"," application/json");
        hashMap.put("Referer", String.format(www+"/BindAccount.aspx?code=%s&state=STATE",code));
        return POST(www+"/WSWeixin.asmx/BindUser", String.format("{openId:'oQopT1EhIZQrFfYnvl7fRO8ozmMc',unionId:'obnwxv9EavHMbGO_1S10NP1nXSfU',userName:'%s',pwd:'%s'}",userName,pwd),hashMap);
    }
}
