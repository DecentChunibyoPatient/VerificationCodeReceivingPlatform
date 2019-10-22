package com.example.verificationcodereceivingplatform.WindowView;

import android.view.MotionEvent;
import android.view.View;

public class TouchListener implements View.OnTouchListener {
    boolean click=false;
    boolean click2=true;
    boolean buttonbarB=true;
    Touch touch;
    public TouchListener(Touch touch){
        this.touch=touch;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case  MotionEvent.ACTION_UP:
                if (click){
                    click=false;
                }else {
                    //单击点击触发
                    if (buttonbarB){
                        touch.open();
                       // getLinearLayout().addView(buttonbar);
                    }else {
                        touch.close();
                        //getLinearLayout().removeView(buttonbar);
                    }
                    buttonbarB=!buttonbarB;
                }
                click2=false;
                break;
            case  MotionEvent.ACTION_DOWN:
                click2=true;
                new Thread(){
                    int i=0;
                    @Override
                    public void run() {
                        while (click2){
                            try {
                                Thread.sleep(30);
                                i++;
                                if (i>=10){click=true;break;}
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                break;
        }
        if (click){
            touch.move( v,  event);
          /*  //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
            wmParams.x = (int) (event.getRawX() - v.getMeasuredWidth()/2);
            //25为状态栏的高度
            wmParams.y = (int) event.getRawY() - v.getMeasuredHeight()/2 - 25;
            //刷新
            windowManager.updateViewLayout(relativeLayout, wmParams);*/

        }
        return true;
    }
    public interface Touch {
        void open();
        void close();
        void move(View v, MotionEvent event);
    }

}
