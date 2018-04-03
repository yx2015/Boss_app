package com.xyl.boss_app.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.xyl.boss_app.R;
import com.xyl.boss_app.ui.adapter.CommonDialogAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private FrameLayout contentView;
    private Display display;
    private List<String> datas;
    private CommonDialogAdapter adapter;

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        datas = new ArrayList<String>();
    }

    public ActionSheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.ui_actionsheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 获取自定义Dialog布局中的控件
        contentView = (FrameLayout) view.findViewById(R.id.fl_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionBottomDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }


    /**
     * 对话框是否显示
     *
     * @return
     */
    public boolean isShowing() {
        return dialog.isShowing();
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public ActionSheetDialog setTitle(String title) {
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(Html.fromHtml(title));
        return this;
    }


    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 设置外部点击
     *
     * @param cancel
     * @return
     */
    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * @param strItems
     * @param listener
     * @return ActionSheetDialog
     * @Title: addSheetItem
     * @Description: 添加条目
     */
    public ActionSheetDialog addSheetItem(List<String> strItems, final OnSheetItemClickListener listener) {
        int size = strItems.size();
        datas.clear();
        datas.addAll(strItems);
        // 添加条目过多的时候控制高度
        if (size >= 6) {
            LayoutParams params = (LayoutParams) contentView.getLayoutParams();
            params.height = display.getHeight() / 2;
            contentView.setLayoutParams(params);
        }
        View view = View.inflate(context, R.layout.ui_list_view, null);
        ListView listView = (ListView) view.findViewById(R.id.lv_content);
        adapter = new CommonDialogAdapter(listView, datas) {
            @Override
            protected void onItemClick(int position) {
                listener.onClick(position);
                dialog.dismiss();
            }
        };
        listView.setAdapter(adapter);
        contentView.addView(view);
        return this;
    }


    public void show() {
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }


    public void dismiss() {
        dialog.dismiss();
    }


}
