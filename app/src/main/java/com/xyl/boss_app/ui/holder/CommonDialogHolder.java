package com.xyl.boss_app.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.xyl.boss_app.R;
import com.xyl.boss_app.utils.UIUtils;


public class CommonDialogHolder extends BaseHolder<String> {
	private TextView tv;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.ui_text_view_item);
		tv = (TextView) view.findViewById(R.id.tv_item);
		return view;
	}

	@Override
	public void refreshView() {
		String type = getData();
		tv.setText(type);
	}

	
}
