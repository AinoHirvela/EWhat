package com.example.a.ewhat;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by ZhangYuqing on 2019/4/2.
 */

public class TimeCountUntil extends CountDownTimer {
    private Button button;

    public TimeCountUntil(Button button,long millisInFuture,long countDownInterval) {
        super(millisInFuture,countDownInterval);
        this.button=button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //设置按钮不可用
        button.setEnabled(false);
        String showText=millisUntilFinished/1000+"秒后重新发送";
        button.setText(showText);
    }

    @Override
    public void onFinish() {
        //设置按钮可用
        button.setEnabled(true);
        button.setText("重新获取");
    }
}
