package com.xyl.boss_app.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hxl.universallibrary.adapter.ListViewDataAdapter;
import com.hxl.universallibrary.adapter.ViewHolderBase;
import com.hxl.universallibrary.adapter.ViewHolderCreator;
import com.hxl.universallibrary.netstatus.NetUtils;
import com.hxl.universallibrary.utils.VolleyHelper;
import com.hxl.universallibrary.widgets.XSwipeRefreshLayout;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.CabinetDto;
import com.xyl.boss_app.bean.params.CabinetParams;
import com.xyl.boss_app.common.UrlConstant;
import com.xyl.boss_app.protocol.NetWorkRequest;
import com.xyl.boss_app.ui.activity.base.BaseFragment;
import com.xyl.boss_app.ui.holder.CabinetHolder;
import com.xyl.boss_app.utils.StringUtils;
import com.xyl.boss_app.utils.UIUtils;
import com.xyl.boss_app.widgets.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


/**
 * @ClassName GoodsStateFragment
 * @Description: 业务柜量子页面
 * Created by yx on 2015/12/17
 */
public class CabinetFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {

    @InjectView(R.id.fragment_goods_state_swipe_layout2)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.fragment_goods_state_list_view1)
    LoadMoreListView mListView;

    @InjectView(R.id.ll_bussiness_container)
    LinearLayout mBusssinessContainer;

    @InjectView(R.id.tv_count_all)
    TextView mCountAll;

    @InjectView(R.id.tv_count_20GP)
    TextView mCount20GP;

    @InjectView(R.id.tv_count_40GP)
    TextView mCount40GP;

    @InjectView(R.id.tv_count_40HQ)
    TextView mCount40HQ;

    private ListViewDataAdapter<CabinetDto.CabinetDtoInfo.CabinetInfo> mListViewAdapter = null;
    private List<CabinetDto.CabinetDtoInfo.CabinetInfo> datas;

    private String mBizStatus = "startOnEndBiz";
    private boolean isOnLoadingMore;

    private int mCurrentPageNo = 1;
    private static final int pageSize = 5;
    private boolean isCanShowingView = false;


    @Override
    protected void onFirstUserVisible() {
        checkNetWork();
    }

    /**
     * 检查网络
     */
    private void checkNetWork() {
        if (NetUtils.isNetworkConnected(mContext)) {
            showLoading("努力加载中，请稍候。。。");
            netWorkRequest();
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetUtils.isNetworkConnected(UIUtils.getContext())) {
                        UIUtils.showNoNetWorkDialog();
                        return;
                    }
                    showLoading("努力加载中，请稍候。。。");
                    netWorkRequest();
                }
            });
        }
    }


    /**
     * 请求统计数据
     */
    private void requestStatisticalData() {
        CabinetParams params = new CabinetParams(mBizStatus, pageSize, mCurrentPageNo);
        NetWorkRequest request = new NetWorkRequest(UrlConstant.GET_CABINET_LIST_TOTAL, params, CabinetDto.class,
                new Response.Listener<CabinetDto>() {
                    @Override
                    public void onResponse(CabinetDto cabinetDto) {
                        if ("200".equals(cabinetDto.getCode())) {
                            refreshStatisticalView(cabinetDto.getData());
                        } else {
                            UIUtils.showToast(cabinetDto.getMsg());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mListView.setVisibility(View.INVISIBLE);
                mBusssinessContainer.setVisibility(View.GONE);
                toggleShowError(true, "服务器繁忙，请稍后再试！", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkNetWork();
                    }
                });
            }
        });
        VolleyHelper.getInstance().getRequestQueue().add(request);
    }

    /**
     * 刷新统计数据
     *
     * @param data
     */
    private void refreshStatisticalView(CabinetDto.CabinetDtoInfo data) {
        synchronized (CabinetFragment.class) {
            if (isCanShowingView) {
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                hideLoading();
            } else {
                isCanShowingView = true;
            }
        }
        if (data != null) {
            if (StringUtils.isEmpty(data.getTotalCabinetNum()) && StringUtils.isEmpty(data.getTotalBizGP20Num())
                    && StringUtils.isEmpty(data.getTotalBizGP40Num()) && StringUtils.isEmpty(data.getTotalBizHQ40Num())) {
                mListView.setVisibility(View.INVISIBLE);
                mBusssinessContainer.setVisibility(View.GONE);
                toggleShowEmpty(true, "暂无数据哦~", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkNetWork();
                    }
                });
            } else {
                mCountAll.setText(data.getTotalCabinetNum());
                mCount20GP.setText(data.getTotalBizGP20Num());
                mCount40GP.setText(data.getTotalBizGP40Num());
                mCount40HQ.setText(data.getTotalBizHQ40Num());
            }
        }
    }

    /**
     * 从服务器获取数据
     */

    private void netWorkRequest() {
        if (mCurrentPageNo == 1) {
            requestStatisticalData();
        }
        requestListData();
    }

    /**
     * 请求列表数据
     */
    private void requestListData() {
        CabinetParams params = new CabinetParams(mBizStatus, pageSize, mCurrentPageNo);
        NetWorkRequest request = new NetWorkRequest(UrlConstant.GET_CABINET_LIST, params, CabinetDto.class,
                new Response.Listener<CabinetDto>() {
                    @Override
                    public void onResponse(CabinetDto cabinetDto) {
                        if ("200".equals(cabinetDto.getCode())) {
                            refreshListView(cabinetDto.getData());
                        } else {
                            UIUtils.showToast(cabinetDto.getMsg());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mListView.setVisibility(View.INVISIBLE);
                mBusssinessContainer.setVisibility(View.GONE);
                toggleShowError(true, "服务器繁忙，请稍后再试！", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkNetWork();
                    }
                });
            }
        });
        VolleyHelper.getInstance().getRequestQueue().add(request);
    }


    /**
     * 刷新列表数据
     *
     * @param cabinetDtoInfo
     */
    private void refreshListView(CabinetDto.CabinetDtoInfo cabinetDtoInfo) {
        synchronized (CabinetFragment.class) {
            if (isCanShowingView) {
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                hideLoading();
            } else {
                isCanShowingView = true;
            }
        }
        if (cabinetDtoInfo == null) {
            mListView.setVisibility(View.INVISIBLE);
            mBusssinessContainer.setVisibility(View.GONE);
            toggleShowEmpty(true, "暂无数据哦~", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkNetWork();
                }
            });
        } else {
            datas = cabinetDtoInfo.getList();
            mListView.setVisibility(View.VISIBLE);
            mBusssinessContainer.setVisibility(View.VISIBLE);

            if (mCurrentPageNo == 1) {
                if (datas.size() <= 0 || datas == null) {
                    mListView.setVisibility(View.INVISIBLE);
                    mBusssinessContainer.setVisibility(View.GONE);
                    toggleShowEmpty(true, "暂无数据哦~", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkNetWork();
                        }
                    });
                } else {
                    mListView.setVisibility(View.VISIBLE);
                    mBusssinessContainer.setVisibility(View.VISIBLE);
                    mListView.setCanLoadMore(true);
                    mListViewAdapter.getDataList().clear();
                    mListViewAdapter.getDataList().addAll(datas);
                    mListViewAdapter.notifyDataSetChanged();
                }
            } else {
                if (null != mListView) {
                    isOnLoadingMore = false;
                    mListView.onLoadMoreComplete();
                }
                if (datas == null || datas.size() == 0) {
                    mCurrentPageNo--;
                    mListView.setCanLoadMore(false);
                } else {
                    mListViewAdapter.getDataList().addAll(datas);
                    mListViewAdapter.notifyDataSetChanged();
                    mListView.setCanLoadMore(true);
                }
            }

        }

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void initViewsAndEvents() {
        datas = new ArrayList<>();
        mListViewAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<CabinetDto.CabinetDtoInfo.CabinetInfo>() {
            @Override
            public ViewHolderBase createViewHolder(int position) {
                return new CabinetHolder(mBizStatus);
            }
        });
        mListView.setOnLoadMoreListener(this);
        mListView.setAdapter(mListViewAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_cabinet;
    }

    public void onPageSelected(int position, String keywords) {
        mBizStatus = keywords;
    }

    @Override
    public void onRefresh() {
        if (!NetUtils.isNetworkConnected(UIUtils.getContext())) {
            mSwipeRefreshLayout.setRefreshing(false);
            UIUtils.showNoNetWorkDialog();
            return;
        }
        if (!isOnLoadingMore) {
            mCurrentPageNo = 1;
            netWorkRequest();
        }
    }

    @Override
    public void onLoadMore() {
        if (!NetUtils.isNetworkConnected(UIUtils.getContext())) {
            mListView.onLoadMoreComplete();
            UIUtils.showNoNetWorkDialog();
            return;
        }
        if (!mSwipeRefreshLayout.isRefreshing()) {
            isOnLoadingMore = true;
            mCurrentPageNo++;
            netWorkRequest();
        }
    }


}