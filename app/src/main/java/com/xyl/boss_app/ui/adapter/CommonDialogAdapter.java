package com.xyl.boss_app.ui.adapter;

import android.widget.AbsListView;

import com.xyl.boss_app.ui.holder.BaseHolder;
import com.xyl.boss_app.ui.holder.CommonDialogHolder;

import java.util.List;


public abstract class CommonDialogAdapter extends DefaultAdapter<String> {

	public CommonDialogAdapter(AbsListView listView, List<String> datas) {
		super(listView, datas);
	}

	@Override
	protected BaseHolder<String> getHolder() {
		return new CommonDialogHolder();
	}

	@Override
	public void onItemClickInner(int position) {
		super.onItemClickInner(position);
		onItemClick(position);
	}

	protected abstract void onItemClick(int position);
}
