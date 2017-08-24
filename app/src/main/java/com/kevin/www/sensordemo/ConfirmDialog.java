package com.kevin.www.sensordemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ConfirmDialog extends Dialog implements
        View.OnClickListener {


    public interface OnConfirmClickListener {
        void onFeedBack();

        void onScreenShot();
    }

    public void setOnConfirmListener(OnConfirmClickListener l) {
        this.l = l;
    }

    private OnConfirmClickListener l;

    public ConfirmDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    private TextView feedback, screenshot, cancel;
    private String msg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        feedback = findViewById(R.id.feedback);
        screenshot = findViewById(R.id.screenshot);
        cancel = findViewById(R.id.cancel);

    }

    private void setListeners() {
        cancel.setOnClickListener(this);
        feedback.setOnClickListener(this);
        screenshot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == cancel) {
            dismiss();
            return;
        }
        if (l != null) {
            dismiss();
            if (v == feedback)
                l.onFeedBack();
            else
                l.onScreenShot();

        }
    }
}