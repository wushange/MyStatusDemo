package com.demo.mystatusdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EncryptView extends CardView {
    private ImageView mIv;
    private TextView mTvUser;
    private Button mBtnEncrypt;

    public EncryptView(@NonNull Context context) {
        super(context);
    }

    public EncryptView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //加载视图的布局
        LayoutInflater.from(context).inflate(R.layout.view_encrypt, this, true);


        mIv = findViewById(R.id.iv);
        mTvUser = findViewById(R.id.tv_user);
        mBtnEncrypt = findViewById(R.id.btn_encrypt);


        mTvUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ScaleAnimation scaleAnimation=new ScaleAnimation(1,-5,1,1);//默认从（0,0）
                scaleAnimation.setDuration(3000);
                mBtnEncrypt.startAnimation(scaleAnimation);
                Toast.makeText(getContext(), "人数", Toast.LENGTH_SHORT).show();
            }
        });
        mBtnEncrypt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "加密", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public EncryptView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
