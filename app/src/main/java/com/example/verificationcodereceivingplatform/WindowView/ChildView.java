package com.example.verificationcodereceivingplatform.WindowView;

import android.content.Context;
import android.view.View;

import com.example.verificationcodereceivingplatform.R;


public class ChildView {
    static public String TAG="ChildView";
    static View buttonbarinit(Context context, final CmdInterface cmdInterface){
        View buttonbar= View.inflate(context, R.layout.button_bar,null);
        buttonbar.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdInterface.test();
            }
        });
        return buttonbar;
    }
    public interface CmdInterface {
        void test();
    }
}
