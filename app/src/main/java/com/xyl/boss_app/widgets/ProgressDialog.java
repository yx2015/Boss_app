package com.xyl.boss_app.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.xyl.boss_app.R;

public class ProgressDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private LinearLayout lLayout_bg;

    private LinearLayout lLayout_bg1;
    private TextView txt_title;
    private TextView txt_msg;
    public EditText et_input;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private NumberProgressBar progressBar;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showInput = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean isDismiss = true;
    public String inputStr;

    public ProgressDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

    }

    public void init() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.ui_progress_dialog, null);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.ll_progress_dialog);
        dialog = new Dialog(context, R.style.ProgressDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.3), (int) (display.getWidth() * 0.3)));
    }


    public ProgressDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.ui_progressdialog, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg1 = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);
        progressBar = (NumberProgressBar) view.findViewById(R.id.number_progress_bar);
//        progressBar.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg1.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        return this;
    }

    public ProgressDialog setProgress(int progress){
        progressBar.setProgress(progress);
        return this;
    }

    public ProgressDialog setMaxProgress(int maxProgress){
        progressBar.setMax(maxProgress);
        return this;
    }


    public ProgressDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(Html.fromHtml(title));
        }
        return this;
    }

    /**
     * 设置消息内容，默认居左
     *
     * @param msg
     * @return
     */
    public ProgressDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(Html.fromHtml(msg));
        }
        return this;
    }

    /**
     * 设置消息内容，可指定文字位置
     *
     * @param msg
     * @param gravity
     * @return
     */
    public ProgressDialog setMsg(String msg, int gravity) {
        txt_msg.setGravity(gravity);
        return setMsg(msg);
    }

    /**
     * 设置返回键
     *
     * @param cancel
     * @return
     */
    public ProgressDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ProgressDialog setPositiveButton(String text, final View.OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (showInput) {
                        inputStr = et_input.getText().toString().trim();
                    }
                    listener.onClick(v);
                    if (!isDismiss) {
                        isDismiss = true;
                        return;
                    }
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public ProgressDialog setNegativeButton(String text, final View.OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                et_input.setError(null);
                isDismiss = true;
                dialog.dismiss();
            }
        });
        return this;
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    private void setLayout() {
        if (!showTitle && !showMsg && !showInput) {
            txt_title.setText("提示ʾ");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

}
