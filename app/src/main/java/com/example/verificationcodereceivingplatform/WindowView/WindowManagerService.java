package com.example.verificationcodereceivingplatform.WindowView;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WindowManagerService extends Service {
    static public final int SHOW_DEFAULT=0;
    static public final int SHOW_DIALOG=1;

    WindowManager windowManager ;
    WindowManager.LayoutParams wmParams=configure();
    View mFloatLayout;
    View childView;
    ArrayList<View> ChildViewArray=new ArrayList<>();
    HashMap<View, WindowManager.LayoutParams> hashMap=new HashMap<>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onDestroy()
    {
        // TODO Auto-generated method stub

        super.onDestroy();
        dismissChildViewAll();
        windowManager.removeViewImmediate(mFloatLayout);
    }
    WindowManager getWindowManager(){
        return windowManager;
    }
    WindowManager.LayoutParams getLayoutParams(){
        return wmParams;
    }
    void setContentView(View view){
        windowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        mFloatLayout=view;
        windowManager.addView(mFloatLayout,wmParams);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
    }
    void setContentView(int id){
        windowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        mFloatLayout= View.inflate(getApplicationContext(),id,null);
        windowManager.addView(mFloatLayout,wmParams);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
    }
    public RelativeLayout getRootView(){
        if (mFloatLayout instanceof RelativeLayout)
            return (RelativeLayout) mFloatLayout;
        return null;
    }
    public void openChildView(View view, ViewGroup.LayoutParams params){
        if (childView!=null)
            getRootView().removeView(childView);
        childView=view;
        getRootView().addView(view,params);
    }
    public void openChildView(View view){
        if (childView!=null)
            getRootView().removeView(childView);
        childView=view;
        getRootView().addView(childView,0);

    }

    public void showChildView(View view, WindowManager.LayoutParams layoutParams, int Mode){
        hashMap.put(view,layoutParams);
        ChildViewArray.add(view);
        windowManager.addView(view,layoutParams);
        switch (Mode){
            case SHOW_DEFAULT:break;
            case SHOW_DIALOG:
            {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (v!=null&&event!=null){
                            WindowManager.LayoutParams layoutParams=hashMap.get(v);
                            if (layoutParams!=null)
                                if (event.getRawX()>=layoutParams.x&&event.getRawX()<=(layoutParams.x+v.getWidth())&&event.getRawY()>=layoutParams.y&&event.getRawY()<=(layoutParams.y+v.getHeight())){
                                }else {
                                    dismissChildView(v);
                                }
                        }
                        return false;
                    }
                });
                view.setFocusable(true);
                view.requestFocus();
                view.requestFocusFromTouch();
            }break;
        }


    }
    public void dismissChildView(View view){
        windowManager.removeView(view);
        hashMap.remove(view);
        ChildViewArray.remove(view);

    }
    public void dismissChildView(int i){
        View view=ChildViewArray.remove(i);
        hashMap.remove(view);
        windowManager.removeView( view);
    }
    public void dismissChildViewAll(){
      for (View view:ChildViewArray){
          windowManager.removeView( view);

      }
        ChildViewArray=new ArrayList<>();
        hashMap=new HashMap<>();
    }
    public void closeChildView(){
        getRootView().removeView(childView);
        WindowManager.LayoutParams layoutParams=getLayoutParams();
        layoutParams.width= LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height=   LinearLayout.LayoutParams.WRAP_CONTENT;
        //刷新
        getWindowManager().updateViewLayout(getRootView(), layoutParams);
    }
    public  <T extends View> T findViewById(int id){
       return mFloatLayout.findViewById(id);
    }

    public WindowManager.LayoutParams configure(){
        WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =
//          WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|  WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//          LayoutParams.FLAG_NOT_TOUCHABLE
        ;

        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;


        //设置悬浮窗口长宽数据
        wmParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        wmParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        return wmParams;
    }
    public WindowManager.LayoutParams ChildLayoutParams(Rect rect, int flags){
        WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =flags;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = rect.left;
        wmParams.y =rect.top;
        //设置悬浮窗口长宽数据
        wmParams.width = rect.right- rect.left;
        wmParams.height = rect.bottom-rect.top;
        return wmParams;
    }
    public WindowManager.LayoutParams ChildLayoutParams(int x, int y, int w, int h, int flags){
        WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =flags;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = x;
        wmParams.y = y;
        //设置悬浮窗口长宽数据
        wmParams.width = w;
        wmParams.height = h;
        return wmParams;
    }
    public WindowManager.LayoutParams ChildLayoutParams(int x, int y){
        WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =
//          WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|  WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//          LayoutParams.FLAG_NOT_TOUCHABLE
        ;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = x;
        wmParams.y = y;



        //设置悬浮窗口长宽数据
        wmParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        wmParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        return wmParams;
    }
    public WindowManager.LayoutParams ChildLayoutParams(int x, int y, int w, int h){
        WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =
//          WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
               // WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//          LayoutParams.FLAG_NOT_TOUCHABLE
        ;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = x;
        wmParams.y = y;


        //设置悬浮窗口长宽数据
        wmParams.width = w;
        wmParams.height = h;

        return wmParams;
    }
    Rect JSONObjectToRect(JSONObject jsonObject) throws JSONException {
        //int left, int top, int right, int bottom
        return new Rect(jsonObject.getInt("left"),jsonObject.getInt("top"),jsonObject.getInt("right"),jsonObject.getInt("bottom"));
    }
    Rect getRootRect(View view){
        int left=view.getLeft();
        int top=view.getTop();
        int right=view.getRight();
        int bottom=view.getBottom();
     try {
         View Parent= (View) view.getParent();
         while (Parent!=null){
             left+=Parent.getLeft();
             top+=Parent.getTop();
             try {
                 Parent= (View) Parent.getParent();
             }catch (Exception e){
                 e.printStackTrace();
                 break;
             }
         }
     }catch (Exception e){
         e.printStackTrace();
     }
        return new Rect( left,  top,  right,  bottom);
    }
}
