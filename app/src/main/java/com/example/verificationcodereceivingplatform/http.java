package com.example.verificationcodereceivingplatform;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ibm on 2017/4/6.
 */

public class http {
    /**
     * 字符串MD5加密
     *
     * @param s
     *            原始字符串
     * @return 加密后字符串
     */
    public final static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String GET(String url, String charsetName) throws IOException {
        url=url.replace(" ","");
        URL Url=new URL(url);
        HttpURLConnection httpURLConnection=(HttpURLConnection)Url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoInput(true);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),charsetName));
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }


        return stringBuffer.toString();
    }
    public static String GET(String url) throws IOException {
        url=url.replace(" ","");
        URL Url=new URL(url);
        HttpURLConnection httpURLConnection=(HttpURLConnection)Url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setConnectTimeout(3000);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"GBK"));
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        Map<String, List<String>> cookie_map = httpURLConnection.getHeaderFields();
        List<String> cookies = cookie_map.get("Set-Cookie");
        if (null != cookies && 0 < cookies.size()) {
            String s = "";
            for (String cookie : cookies) {
                if (s.isEmpty()) {
                    s = cookie;
                } else {
                    s += ";" + cookie;
                }
            }
            Log.i("ss", s);
        }
        return stringBuffer.toString();
    }
    public static HashMap<String, String> GETs(String url) throws IOException {
        url=url.replace(" ","");
        URL Url=new URL(url);
        HttpURLConnection httpURLConnection=(HttpURLConnection)Url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setConnectTimeout(3000);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        Map<String, List<String>> cookie_map = httpURLConnection.getHeaderFields();
        List<String> cookies = cookie_map.get("Set-Cookie");
        String s = "";
        if (null != cookies && 0 < cookies.size()) {

            for (String cookie : cookies) {
                if (s.isEmpty()) {
                    s = cookie;
                } else {
                    s += ";" + cookie;
                }
            }
            Log.i("ss", s);
        }
        HashMap<String, String> hashMap=new HashMap<String, String>();
        hashMap.put("data",stringBuffer.toString());
        hashMap.put("Cookie",s);
        return hashMap;
    }
    public static HashMap<String, String> GETs(String url, HashMap<String, String> HM) throws IOException {
        url=url.replace(" ","");
        URL Url=new URL(url);
        HttpURLConnection httpURLConnection=(HttpURLConnection)Url.openConnection();
        Iterator iter = HM.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            httpURLConnection.setRequestProperty(key,val);
        }
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setConnectTimeout(3000);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
        }catch (Exception e){
            bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream(),"UTF-8"));
        }
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        Map<String, List<String>> cookie_map = httpURLConnection.getHeaderFields();
        List<String> Location = cookie_map.get("Location");
        String Locations = "";
        if (Location!=null){
            if (null != Location && 0 < Location.size()) {

                for (String cookie : Location) {
                    if (Locations.isEmpty()) {
                        Locations = cookie;
                    } else {
                        Locations += ";" + cookie;
                    }
                }
                Log.i("Location", Locations);
            }
        }
        List<String> cookies = cookie_map.get("Set-Cookie");
        String s = "";
        if (null != cookies && 0 < cookies.size()) {

            for (String cookie : cookies) {
                if (s.isEmpty()) {
                    s = cookie;
                } else {
                    s += ";" + cookie;
                }
            }
            Log.i("ss", s);
        }
        HashMap<String, String> hashMap=new HashMap<String, String>();
        hashMap.put("Location",Locations);
        hashMap.put("data",stringBuffer.toString());
        hashMap.put("Cookie",s);
        return hashMap;
    }
    public static String GET(String url, String proxyHost, int proxyPort, String charsetName) throws IOException {
        url=url.replace(" ","");
        URL Url=new URL(url);
        HttpURLConnection httpURLConnection;
        if(proxyHost!=null){//使用代理模式
            @SuppressWarnings("static-access")
            Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            httpURLConnection = (HttpURLConnection) Url.openConnection(proxy);
        }else{
            httpURLConnection = (HttpURLConnection) Url.openConnection();
        }
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setConnectTimeout(3000);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),charsetName));
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }
    static public String POST(String URL, String data) throws IOException {
        java.net.URL url=new URL(URL);
        HttpURLConnection httpURLConnection;
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8");
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        return  stringBuffer.toString();
    }
    static public String POST(String URL, String data, HashMap<String, String> HM) throws IOException {
        java.net.URL url=new URL(URL);
        HttpURLConnection httpURLConnection;
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        Iterator iter = HM.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            httpURLConnection.setRequestProperty(key,val);
        }
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8");
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        return  stringBuffer.toString();
    }
    static public String POST(String URL, HashMap<String, String> HM, String data, String proxyHost, int proxyPort) throws IOException {
        java.net.URL url=new URL(URL);
        HttpURLConnection httpURLConnection;
        if(proxyHost!=null){//使用代理模式
            @SuppressWarnings("static-access")
            Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
        }else{
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }
        httpURLConnection.setRequestMethod("POST");
        Iterator iter = HM.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            httpURLConnection.setRequestProperty(key,val);
        }
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8");
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        return  stringBuffer.toString();
    }
    static public HashMap<String, String> POSTs(String URL, HashMap<String, String> HM, String data, String proxyHost, int proxyPort) throws IOException {
        java.net.URL url=new URL(URL);
        HttpURLConnection httpURLConnection;
        if(proxyHost!=null){//使用代理模式
            @SuppressWarnings("static-access")
            Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
        }else{
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }
        httpURLConnection.setRequestMethod("POST");
        Iterator iter = HM.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            httpURLConnection.setRequestProperty(key,val);
        }
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8");
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        BufferedReader bufferedReader = null;
       try {
            bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
       }catch (Exception e){
           bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream(),"UTF-8"));
       }
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        Map<String, List<String>> cookie_map = httpURLConnection.getHeaderFields();
        List<String> Location = cookie_map.get("Location");
        String Locations = "";
        Log.i("cookie_map", ""+cookie_map);
       if (Location!=null){
           if (null != Location && 0 < Location.size()) {

               for (String cookie : Location) {
                   if (Locations.isEmpty()) {
                       Locations = cookie;
                   } else {
                       Locations += ";" + cookie;
                   }
               }
               Log.i("Location", Locations);
           }
       }
        List<String> cookies = cookie_map.get("Set-Cookie");
        String s = "";
        if (null != cookies && 0 < cookies.size()) {

            for (String cookie : cookies) {
                if (s.isEmpty()) {
                    s = cookie;
                } else {
                    s += ";" + cookie;
                }
            }
            Log.i("ss", s);
        }
        HashMap<String, String> hashMap=new HashMap<String, String>();
        hashMap.put("Location",Locations);
        hashMap.put("data",stringBuffer.toString());
        hashMap.put("Cookie",s);
        hashMap.put("getHeaderFields",httpURLConnection.getHeaderFields().toString());
        return hashMap;
    }
    static public HashMap<String, String> POSTs(String URL, HashMap<String, String> HM, String data) throws IOException {
        java.net.URL url=new URL(URL);
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        Iterator iter = HM.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            httpURLConnection.setRequestProperty(key,val);
        }
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setReadTimeout(3000);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8");
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
        }catch (Exception e){
            bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream(),"UTF-8"));
        }
        StringBuffer stringBuffer=new StringBuffer();
        String string;
        while ((string=bufferedReader.readLine())!=null){
            stringBuffer.append(string);
        }
        Map<String, List<String>> cookie_map = httpURLConnection.getHeaderFields();
        List<String> Location = cookie_map.get("Location");
        String Locations = "";
        if (Location!=null){
            if (null != Location && 0 < Location.size()) {

                for (String cookie : Location) {
                    if (Locations.isEmpty()) {
                        Locations = cookie;
                    } else {
                        Locations += ";" + cookie;
                    }
                }
                Log.i("Location", Locations);
            }
        }
        List<String> cookies = cookie_map.get("Set-Cookie");
        String s = "";
        if (null != cookies && 0 < cookies.size()) {
            for (String cookie : cookies) {
                if (s.isEmpty()) {
                    s = cookie;
                } else {
                    s += ";" + cookie;
                }
            }
            Log.i("ss", s);
        }
        HashMap<String, String> hashMap=new HashMap<String, String>();
        hashMap.put("Location",Locations);
        hashMap.put("data",stringBuffer.toString());
        hashMap.put("Cookie",s);
        return hashMap;
    }
}
