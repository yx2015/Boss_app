package com.xyl.boss_app.ui.holder;

import android.view.View;

/**   
 * @ClassName:  BaseHolder   
 * @Description: 页面的基础类   
 * @author: Haoxl  
 * @date:   2015-3-20 下午4:46:36   
 *   
 * @param <Data>   
 */ 
public abstract class BaseHolder<Data> {
	protected View mRootView;
	protected int mPosition;
	protected Data mData;

	public BaseHolder() {
		mRootView = initView();
		mRootView.setTag(this);
	}

	public BaseHolder(Data T) {
	}

	public View getRootView() {
		return mRootView;
	}

	public void setData(Data data) {
		mData = data;
		refreshView();
	}

	public Data getData() {
		return mData;
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public int getPosition() {
		return mPosition;
	}

	/** 子类必须覆盖用于实现UI初始化 */
	protected abstract View initView();

	/** 子类必须覆盖用于实现UI刷新 */
	public abstract void refreshView();

	/** 用于回收 */
	public void recycle() {

	}
}
