package com.xyl.boss_app.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.xyl.boss_app.bean.FeeDto;
import com.xyl.boss_app.bean.params.FeeParams;
import com.xyl.boss_app.common.UrlConstant;
import com.xyl.boss_app.protocol.NetWorkRequest;
import com.xyl.boss_app.ui.activity.base.BaseFragment;
import com.xyl.boss_app.ui.holder.GrossProfitHolder;
import com.xyl.boss_app.utils.UIUtils;
import com.xyl.boss_app.widgets.LoadMoreListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * @ClassName:GrossProfitFragment
 * @Drcription 利润页面
 * Created by yx on 2015/12/23 0023.
 */
public class GrossProfitFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {
    private static final String REQUEST_TAG_PROFITS_FRAGMENT = GrossProfitFragment.class.getSimpleName();
    @InjectView(R.id.fragment_profits_swipe_layout)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.fragment_profits_list_view)
    LoadMoreListView mListView;

    @InjectView(R.id.ll_profits)
    RelativeLayout mFee;

    @InjectView(R.id.tv_count_fragment_profits)
    TextView mCount;

    private ListViewDataAdapter<FeeDto.FeeDtoInfo.FeeInfo> mListViewAdapter = null;

    private List<FeeDto.FeeDtoInfo.FeeInfo> feeInfos;

    private boolean isOnLoadingMore;
    private int pageNo = 1;
    private DecimalFormat mFormat;
    private FeeParams feeParams;
    private boolean isNeedRefresh;
    private boolean isFirstUserVisibleFinished;
    private boolean isCanShowingView = false;

    @Override
    protected void onFirstUserVisible() {
        if (!isFirstUserVisibleFinished) {
            if (feeParams != null) {
                requestDataFromNet();
                isNeedRefresh = false;
            }
            isFirstUserVisibleFinished = true;
        } else {
            onUserVisible();
        }
    }

    @Override
    protected void onUserVisible() {
        if (isNeedRefresh) {
            if (feeParams != null) {
                pageNo = 1;
                requestDataFromNet();
                isNeedRefresh = false;
            }
        }
    }

    /**
     * 检查网络
     */
    private void requestDataFromNet() {
        mFee.setVisibility(View.GONE);
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
                    //TODO
                    showLoading("努力加载中，请稍候。。。");
                    netWorkRequest();
                }
            });
        }
    }

    /**
     * 从服务器获取数据
     */
    private void netWorkRequest() {
        if (pageNo == 1) {
            requestStatisticalData();
        }
        requestListData();
    }

    /**
     * 请求统计数据
     */
    private void requestStatisticalData() {
        feeParams.setPageNo(pageNo);
        feeParams.setFeeType("");
        NetWorkRequest request = new NetWorkRequest(UrlConstant.GET_FEE_LIST_TOTAL, feeParams, FeeDto.class,
                new Response.Listener<FeeDto>() {
                    @Override
                    public void onResponse(FeeDto feeStateDto) {
                        if ("200".equals(feeStateDto.getCode())) {
                            refreshStatisticalView(feeStateDto.getData());
                        } else {
                            UIUtils.showToast(feeStateDto.getMsg());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                mListView.setVisibility(View.INVISIBLE);
                mFee.setVisibility(View.GONE);
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                toggleShowError(true, "服务器繁忙，请稍后再试！", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestDataFromNet();
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
    private void refreshStatisticalView(FeeDto.FeeDtoInfo data) {
        synchronized (GrossProfitFragment.class) {
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
            if (data.getTotalGrossProfit() == 0) {
                mListView.setVisibility(View.INVISIBLE);
                mFee.setVisibility(View.GONE);
                toggleShowEmpty(true, "暂无数据哦~", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestDataFromNet();
                    }
                });
            } else {
                mCount.setText(data.getTotalGrossProfit() < 0 ? mFormat.format(data.getTotalGrossProfit()) : "+" + mFormat.format(data.getTotalGrossProfit()));
            }
        }
    }

    /**
     * 请求列表数据
     */
    private void requestListData() {
        feeParams.setPageNo(pageNo);
        feeParams.setFeeType("");
        VolleyHelper.getInstance().getRequestQueue().cancelAll(REQUEST_TAG_PROFITS_FRAGMENT);
        NetWorkRequest request = new NetWorkRequest(UrlConstant.GET_FEE_LIST, feeParams, FeeDto.class,
                new Response.Listener<FeeDto>() {
                    @Override
                    public void onResponse(FeeDto feeStateDto) {
                        if ("200".equals(feeStateDto.getCode())) {
                            refreshView(feeStateDto.getData());
                        } else {
                            UIUtils.showToast(feeStateDto.getMsg());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                mListView.setVisibility(View.INVISIBLE);
                mFee.setVisibility(View.GONE);
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                toggleShowError(true, "服务器繁忙，请稍后再试！", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestDataFromNet();
                    }
                });
            }
        });
        VolleyHelper.getInstance().getRequestQueue().add(request).setTag(REQUEST_TAG_PROFITS_FRAGMENT);
    }


    /**
     * 刷新页面
     *
     * @param feeStateInfo
     */
    private void refreshView(final FeeDto.FeeDtoInfo feeStateInfo) {
        synchronized (GrossProfitFragment.class) {
            if (isCanShowingView) {
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                hideLoading();
            } else {
                isCanShowingView = true;
            }
        }
        mListView.onLoadMoreComplete();
        if (feeStateInfo == null) {
            mListView.setVisibility(View.INVISIBLE);
            mFee.setVisibility(View.GONE);

            toggleShowEmpty(true, "暂无数据哦~", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestDataFromNet();
                }
            });
        } else {
            feeInfos = feeStateInfo.getList();
            if (pageNo == 1) {
                if (feeInfos.size() <= 0) {
                    mListView.setVisibility(View.INVISIBLE);
                    mFee.setVisibility(View.GONE);
                    toggleShowEmpty(true, "暂无数据哦~", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestDataFromNet();
                        }
                    });
                } else {
                    mListView.setCanLoadMore(true);
                    mListView.setVisibility(View.VISIBLE);
                    mFee.setVisibility(View.VISIBLE);

                    mListViewAdapter.getDataList().clear();
                    mListViewAdapter.getDataList().addAll(feeInfos);
                    mListViewAdapter.notifyDataSetChanged();
                }
            } else {
                if (feeInfos.size() < feeParams.getPageSize()) {
                    mListView.setCanLoadMore(false);
                }
                isOnLoadingMore = false;
                mListViewAdapter.getDataList().addAll(feeInfos);
                mListViewAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void initViewsAndEvents() {
        mFormat = new DecimalFormat("###,###,###,##0.00");
        feeInfos = new ArrayList<>();
        mListView.setOnLoadMoreListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListViewAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<FeeDto.FeeDtoInfo.FeeInfo>() {
            @Override
            public ViewHolderBase createViewHolder(int position) {
                return new GrossProfitHolder();
            }
        });
        mListView.setAdapter(mListViewAdapter);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_profits;
    }

    @Override
    public void onLoadMore() {
        if (!NetUtils.isNetworkConnected(UIUtils.getContext())) {
            mListView.onLoadMoreComplete();
            UIUtils.showNoNetWorkDialog();
            return;
        }

        if (!mSwipeRefreshLayout.isRefreshing()) {
            //TODO
            isOnLoadingMore = true;
            pageNo++;
            netWorkRequest();
        }
    }

    @Override
    public void onRefresh() {
        //TODO
        if (!NetUtils.isNetworkConnected(UIUtils.getContext())) {
            mSwipeRefreshLayout.setRefreshing(false);
            UIUtils.showNoNetWorkDialog();
            return;
        }
        if (!isOnLoadingMore) {
            pageNo = 1;
            netWorkRequest();
        }
    }

    public void onPageSelected(boolean isNeedToRefresh, FeeParams feeParams) {
        this.feeParams = feeParams;
        pageNo = 1;
        if (!this.isNeedRefresh) {
            this.isNeedRefresh = isNeedToRefresh;
            if ("".equals(feeParams.getFeeType()) && isNeedToRefresh) {
                requestDataFromNet();
                this.isNeedRefresh = false;
            }
        }
    }
}
