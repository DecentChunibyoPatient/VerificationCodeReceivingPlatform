package com.example.verificationcodereceivingplatform.tool;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by ibm on 2018/5/27.
 */

public class LoadSO {
    /**
     * 复制单个文件
     * @param inStream InputStream 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    static public void copyFile(InputStream inStream, File newPath) {
        try {

            FileOutputStream fs = new FileOutputStream(newPath);
            byte[]bytes=toByteArray(inStream);
            fs.write(bytes, 0, bytes.length);
            inStream.close();
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }
    public static byte[] toByteArray(InputStream input)
            throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }
    public static int copy(InputStream input, OutputStream output)
            throws IOException
    {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int)count;
    }
    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException
    {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    public static void init(String packageName, byte[]data){
        try {
            String dataDir="/data/data/"+packageName;
            File file=new File(dataDir+"/files","app-debug.apk");
            copyFile(data,file);//复制到一个地方
            UnzipSpecificFile(file);//然后解压它
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static public int[][] haar(ClassLoader classLoader, Bitmap bitmap , Bitmap[] sample){
        long startTime = System.currentTimeMillis();
        Object object=invokeMethod(classLoader,"com.wqg.gameocr.OCR","Haar",new Class[]{Bitmap.class , Bitmap[].class  },new Object[]{ bitmap ,   sample});//在然后就是加载它
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println(Float.toString(seconds) + " seconds.");
        return (int[][])object;
    }
    static public int[][] haarRect(ClassLoader classLoader, Bitmap bitmap , Bitmap[] sample , Rect[]rects){
        long startTime = System.currentTimeMillis();
        Object object=invokeMethod(classLoader,"com.wqg.gameocr.OCR","HaarRect",new Class[]{Bitmap.class , Bitmap[].class , Rect[].class },new Object[]{ bitmap , sample ,rects});//在然后就是加载它
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println(Float.toString(seconds) + " seconds.");
        return (int[][])object;
    }
    static public String[] OcrData(ClassLoader classLoader, Bitmap bitmap, int[][]CoordinateSize, int libI){

        long startTime = System.currentTimeMillis();
        Object object=invokeMethod(classLoader,"com.wqg.gameocr.OCR","OcrData",new Class[]{Bitmap.class,int[][].class ,int.class},new Object[]{ bitmap , CoordinateSize,libI});//在然后就是加载它
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println(Float.toString(seconds) + " seconds.");
        System.out.println("OcrData="+object);
        return (String[])object;
    }
    static public String baiduOCR(ClassLoader classLoader, Bitmap bitmap){
        long startTime = System.currentTimeMillis();
        Object object=invokeMethod(classLoader,"com.wqg.gameocr.OCR","baiduOCR",new Class[]{Bitmap.class},new Object[]{ bitmap});//在然后就是加载它
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println(Float.toString(seconds) + " seconds.");
        System.out.println("OcrData="+object);
        return (String)object;
    }
    static public String[] baiduOCR(ClassLoader classLoader, Bitmap bitmap, int[][]CoordinateSize){
        long startTime = System.currentTimeMillis();
        Object object=invokeMethod(classLoader,"com.wqg.gameocr.OCR","baiduOCR",new Class[]{Bitmap.class,int[][].class },new Object[]{ bitmap , CoordinateSize});//在然后就是加载它
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println(Float.toString(seconds) + " seconds.");
        System.out.println("OcrData="+object);
        return (String[])object;
    }
    static public void MakeImageLibS(ClassLoader classLoader, Bitmap[][]bitmaps, String[][]names){

        long startTime = System.currentTimeMillis();
        invokeMethod(classLoader,"com.wqg.gameocr.OCR","MakeImageLibS",new Class[]{Bitmap[][].class, String[][].class },new Object[]{ bitmaps ,  names});//在然后就是加载它
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println(Float.toString(seconds) + " seconds.");
    }
    static void UnzipSpecificFile(File zipFile){
        ArrayList<String> arrayList =new ArrayList<>();
        arrayList.add("lib");
        UnzipSpecificFile(zipFile,zipFile.getParentFile().getPath(),arrayList);
    }
    /**
     * 利用ClassLoader，DexClassLoader和反射将apk中的界面启动
     * @param classLoader apk 动态加载的apk本地路径
     * @param className 要打开的动态加载类的类名
     */
    public static Object invokeMethod(ClassLoader classLoader, String className, String MethodName, Class[]parameterTypes, Object[]data) {

        try {
            System.out.println("classLoader="+classLoader);
            Class<?> localClass = classLoader.loadClass(className);
            System.out.println(localClass.getName());
            Method method = localClass.getMethod(MethodName,parameterTypes);
            System.out.println("method="+method);
            method.setAccessible(true);
            Object object=method.invoke(localClass.newInstance(), data);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object invokeMethod(String className, String MethodName, Class[]parameterTypes, Object[]data) {

        try {
            Class<?> localClass = Class.forName(className);
            System.out.println(localClass.getName());
            Method method = localClass.getMethod(MethodName,parameterTypes);
            method.setAccessible(true);
            Object object=method.invoke(localClass.newInstance(), data);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object invokeMethod(Class<?> localClass, String MethodName, Class[]parameterTypes, Object[]data) {
        try {
            System.out.println(localClass.getName());
            Method method = localClass.getMethod(MethodName,parameterTypes);
            System.out.println("method="+method);
            method.setAccessible(true);
            Object object=method.invoke(localClass.newInstance(), data);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object invokeMethod(Object obj, String MethodName, Class[]parameterTypes, Object[]data) {
        try {
            System.out.println(obj.getClass().getName());
            Method method = obj.getClass().getMethod(MethodName,parameterTypes);
            System.out.println("method="+method);
            method.setAccessible(true);
            Object object=method.invoke(obj, data);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 复制单个文件
     * @param bytes byte[] 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(byte[] bytes, File newPath) throws IOException {
        FileOutputStream fs = new FileOutputStream(newPath);
        fs.write(bytes, 0, bytes.length);
    }
    private static boolean IsDirEquals(String srcfile, String objDir)
    {
        try{
            int index = srcfile.lastIndexOf("/");
            String dir = null;
            String firstDirName = null;
            if(index != -1)
                dir = srcfile.substring(0,index );

            index = srcfile.indexOf("/");
            if(index != -1)
                firstDirName = srcfile.substring(0, index);


            if(null != dir && dir.equalsIgnoreCase(objDir))
            {
                return true;
            }
            else if(null != firstDirName && firstDirName.equalsIgnoreCase(objDir))
            {
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;


    }

    public static void UnzipSpecificFile(File zipFile, String targetDir, ArrayList<String> objDirList) {
        int BUFFER = 4096; // 这里缓冲区我们使用4KB，
        String strEntry; // 保存每个zip的条目名称
        ZipInputStream zis = null;
        try {
            BufferedOutputStream dest = null; // 缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; // 每个zip条目的实例
            while ((entry = zis.getNextEntry()) != null) {

                try {
                    // Log.i("Unzip: ","="+ entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();

                    boolean find = false;
                    for (String dir : objDirList) {
                        if (IsDirEquals(strEntry.toString(), dir)) {
                            find = true;
                            break;
                        }
                    }

                    if (false == find)
                        continue;

                    File entryFile = new File(targetDir +"/"+ strEntry);
                    File entryDir = new File(entryFile.getParent());

                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (null != dest)
                        dest.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != zis)
                    zis.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    // 获取手机CPU类型信息
    final static public String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率
        String[] arrayOfString;
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
            fr = new FileReader(str1);
            localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2]; // cpu频率。
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null)
                    fr.close();

                if (localBufferedReader != null)
                    localBufferedReader.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        if ((cpuInfo[0].toLowerCase().contains("armv7"))) {
            return "armeabi-v7a";
        } else if ((cpuInfo[0].toLowerCase().contains("arm"))) {
            return "armeabi";
        } else if ((cpuInfo[0].toLowerCase().contains("mips"))) {
            return "mips";
        } else {
            return "x86";
        }

    }
}
