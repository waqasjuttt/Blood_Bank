package com.example.waqasjutt.blood_bank;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class CustomDialog_Class extends Dialog {

    public Activity activity;
    public Dialog dialog;
    public Button btnYes, btnNo;
    public ImageView imageView;

    public CustomDialog_Class(Activity activity1) {
        super(activity1);
        // TODO Auto-generated constructor stub
        this.activity = activity1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_dialog_exit);

        imageView = (ImageView) findViewById(R.id.ivCloseApp);
        btnYes = (Button) findViewById(R.id.btn_yes);
        btnNo = (Button) findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
}
