package com.meiku.dev.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.meiku.dev.R;

public class LoadingDialog extends Dialog {

    private Context mContext;
    private String mTitle;

    public LoadingDialog(Context context, String title) {
        super(context, R.style.MyDialog);
        mContext = context;
        mTitle = title;
    }

    public LoadingDialog(Context context, int theme, String title) {
        super(context, theme);
        mContext = context;
        mTitle = title;
        this.setContentView(R.layout.activity_my_progressdialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_my_progressdialog, null);
        setContentView(view);

        TextView tvTitle = (TextView)view.findViewById(R.id.title);
        if("".equals(mTitle)){
            tvTitle.setVisibility(View.GONE);
        }else{
            tvTitle.setText(mTitle);
        }
    }

}
