package com.xyl.boss_app.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hxl.universallibrary.eventbus.EventCenter;
import com.hxl.universallibrary.netstatus.NetUtils;
import com.hxl.universallibrary.utils.VolleyHelper;
import com.hxl.universallibrary.widgets.XSwipeRefreshLayout;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.StatisticDto;
import com.xyl.boss_app.bean.params.StatisticParams;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.common.UrlConstant;
import com.xyl.boss_app.protocol.NetWorkRequest;
import com.xyl.boss_app.ui.activity.base.BaseFragment;
import com.xyl.boss_app.utils.SPUtil;
import com.xyl.boss_app.utils.StringUtils;
import com.xyl.boss_app.utils.TimeUtils;
import com.xyl.boss_app.utils.UIUtils;
import com.xyl.boss_app.widgets.RiseNumberView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * @ClassName MainFragment
 * @Description: 首页
 * Created by yx on 2015/12/15 0015.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.spinner_main_fragment)
    TextView mSpinner;
    @InjectView(R.id.rl_main_fragment)
    View mConditionsView;
    @InjectView(R.id.tv_start_time_main)
    TextView mStartDateTv;
    @InjectView(R.id.tv_end_time_main)
    TextView mEndDateTv;
    @InjectView(R.id.tv_profits)
    RiseNumberView mProfitsTv;
    @InjectView(R.id.tv_yingshou_main_fragment)
    RiseNumberView mYingShouTv;
    @InjectView(R.id.tv_yingfu_main_fragment)
    RiseNumberView mYingFuTv;
    @InjectView(R.id.tv_shishou_main_fragment)
    RiseNumberView mShiShouTv;
    @InjectView(R.id.tv_shifu_main_fragment)
    RiseNumberView mShiFuTv;
    @InjectView(R.id.ll_select_date)
    LinearLayout mSelectDateView;
    @InjectView(R.id.chart)
    BarChart mChart;
    @InjectView(R.id.fragment_main_swipe_layout)
    XSwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.iv_logo)
    ImageView mLogo;
    @InjectView(R.id.tv_org)
    TextView mOrgName;
    @InjectView(R.id.ll_yingshou)
    View mViewYingshou;
    @InjectView(R.id.ll_yingfu)
    View mViewYingfu;
    @InjectView(R.id.view_start_biz)
    View mViewStartBiz;
    @InjectView(R.id.view_on_biz)
    View mViewOnBiz;
    @InjectView(R.id.view_end_biz)
    View mViewEndBiz;
    @InjectView(R.id.scrollview)
    View mScrollView;

    private static final String TIME_TYPE_DELIVER_GOODS = "1";
    private static final String TIME_TYPE_BUSSINESS = "2";
    private static final String TIME_TYPE_STEVEDORING = "3";
    private String mTimeType = TIME_TYPE_DELIVER_GOODS;
    private String fromDate = TimeUtils.getYesterdayDate();
    private String toDate = TimeUtils.getYesterdayDate();
    private StatisticDto.StatisticInfo data;
    private PopupWindow mDropDownView;
    private View contentView;

    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
        }

        @Override
        public void onDateTimeRecurrenceSet(Date startDate, Date endDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            fromDate = TimeUtils.parseTime(startDate, TimeUtils.yyyy_MM_dd);
            toDate = TimeUtils.parseTime(endDate, TimeUtils.yyyy_MM_dd);
            mStartDateTv.setText(fromDate);
            mEndDateTv.setText(toDate);
            //TODO
            requestFromNet();
        }
    };
    private boolean isCanShowingView = false;

    @Override
    protected void onFirstUserVisible() {
        mOrgName.setText(SPUtil.getString(Constants.Preferences.LAST_LOGIN_ORG_NAME, ""));
        String logo = SPUtil.getString(Constants.Preferences.LAST_LOGIN_ORG_LOGO, "");
        if (!StringUtils.isEmpty(logo)) {
            Glide.with(getActivity()).load(logo).error(R.mipmap.ic_org_logo).fitCenter().into(mLogo);
        }
        requestFromNet();
    }

    private void requestFromNet() {
        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != mSwipeRefreshLayout) {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showLoading("努力加载中，请稍候。。。");
                        StatisticParams mParams = new StatisticParams(mTimeType, fromDate, toDate);
                        netWorkRequest(mParams);
                    }
                }, 0);
            }
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetUtils.isNetworkConnected(UIUtils.getContext())) {
                        UIUtils.showNoNetWorkDialog();
                        return;
                    }
                    showLoading("努力加载中，请稍候。。。");
                    StatisticParams mParams = new StatisticParams(mTimeType, fromDate, toDate);
                    netWorkRequest(mParams);
                }
            });
        }
    }

    private void netWorkRequest(StatisticParams mParams) {
        requestFeeData(mParams);
        requestCabinetData(mParams);
    }

    /**
     * 请求费用数据
     *
     * @param mParams
     */
    private void requestFeeData(StatisticParams mParams) {
        NetWorkRequest request = new NetWorkRequest(UrlConstant.GET_STATISTICS_FEE, mParams, StatisticDto.class,
                new Response.Listener<StatisticDto>() {
                    @Override
                    public void onResponse(StatisticDto datas) {
                        if ("200".equals(datas.getCode())) {
                            data = datas.getData();
                            refreshFeeView(data);
                        } else {
                            UIUtils.showToast(datas.getMsg());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
                mScrollView.setVisibility(View.INVISIBLE);
                toggleShowError(true, "服务器繁忙，请稍后再试！", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading("努力加载中，请稍候。。。");
                        StatisticParams mParams = new StatisticParams(mTimeType, fromDate, toDate);
                        netWorkRequest(mParams);
                    }
                });
            }
        });
        VolleyHelper.getInstance().getRequestQueue().add(request);
    }

    /**
     * 请求柜量数据
     *
     * @param mParams
     */
    private void requestCabinetData(StatisticParams mParams) {
        NetWorkRequest request = new NetWorkRequest(UrlConstant.GET_STATISTICS_CABINET, mParams, StatisticDto.class,
                new Response.Listener<StatisticDto>() {
                    @Override
                    public void onResponse(StatisticDto datas) {
                        if ("200".equals(datas.getCode())) {
                            refreshCabinetView(datas.getData());
                        } else {
                            UIUtils.showToast(datas.getMsg());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
                mScrollView.setVisibility(View.INVISIBLE);
                toggleShowError(true, "服务器繁忙，请稍后再试！", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoading("努力加载中，请稍候。。。");
                        StatisticParams mParams = new StatisticParams(mTimeType, fromDate, toDate);
                        netWorkRequest(mParams);
                    }
                });
            }
        });
        VolleyHelper.getInstance().getRequestQueue().add(request);
    }

    /**
     * 刷新费用页面
     *
     * @param data
     */
    private void refreshFeeView(StatisticDto.StatisticInfo data) {
        synchronized (MainFragment.class) {
            if (isCanShowingView) {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                hideLoading();
            } else {
                isCanShowingView = true;
            }
        }
        if (data == null) {
            mScrollView.setVisibility(View.INVISIBLE);
            toggleShowEmpty(true, "暂无数据哦~", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading("努力加载中，请稍候。。。");
                    StatisticParams mParams = new StatisticParams(mTimeType, fromDate, toDate);
                    netWorkRequest(mParams);
                }
            });
        } else {
            mScrollView.setVisibility(View.VISIBLE);
            mProfitsTv.showNumberWithAnimation(data.getGrossProfit());
            mYingShouTv.showNumberWithAnimation(data.getReceivableMoney());
            mYingFuTv.showNumberWithAnimation(data.getPayableMoney());
            mShiShouTv.showNumberWithAnimation(data.getReceivedMoney());
            mShiFuTv.showNumberWithAnimation(data.getPrepaidMoney());
        }
    }

    /**
     * 刷新柜量页面
     *
     * @param data
     */
    private void refreshCabinetView(StatisticDto.StatisticInfo data) {
        synchronized (MainFragment.class) {
            if (isCanShowingView) {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                hideLoading();
            } else {
                isCanShowingView = true;
            }
        }
        if (data == null) {
            mScrollView.setVisibility(View.INVISIBLE);
        } else {
            notifyDataSetChanged(data);
        }
        mChart.invalidate();
        mChart.animateY(600);
    }

    private void notifyDataSetChanged(StatisticDto.StatisticInfo data) {
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("起运港");
        xVals.add("海上运输");
        xVals.add("目的港");
        ArrayList<BarEntry> value_20GP = new ArrayList<BarEntry>();
        ArrayList<BarEntry> value_40GP = new ArrayList<BarEntry>();
        ArrayList<BarEntry> value_40HQ = new ArrayList<BarEntry>();
        if (data.getStartBiz() != null) {
            value_20GP.add(new BarEntry(data.getStartBiz().getGP_20(), 0));
            value_40GP.add(new BarEntry(data.getStartBiz().getGP_40(), 0));
            value_40HQ.add(new BarEntry(data.getStartBiz().getHQ_40(), 0));
        } else {
            value_20GP.add(new BarEntry(0, 0));
            value_40GP.add(new BarEntry(0, 0));
            value_40HQ.add(new BarEntry(0, 0));
        }
        if (data.getOnBiz() != null) {
            value_20GP.add(new BarEntry(data.getOnBiz().getGP_20(), 1));
            value_40GP.add(new BarEntry(data.getOnBiz().getGP_40(), 1));
            value_40HQ.add(new BarEntry(data.getOnBiz().getHQ_40(), 1));
        } else {
            value_20GP.add(new BarEntry(0, 1));
            value_40GP.add(new BarEntry(0, 1));
            value_40HQ.add(new BarEntry(0, 1));
        }
        if (data.getEndBiz() != null) {
            value_20GP.add(new BarEntry(data.getEndBiz().getGP_20(), 2));
            value_40GP.add(new BarEntry(data.getEndBiz().getGP_40(), 2));
            value_40HQ.add(new BarEntry(data.getEndBiz().getHQ_40(), 2));
        } else {
            value_20GP.add(new BarEntry(0, 2));
            value_40GP.add(new BarEntry(0, 2));
            value_40HQ.add(new BarEntry(0, 2));
        }

        // create 3 datasets with different types
        BarDataSet set1 = new BarDataSet(value_20GP, "20GP");
        set1.setValueFormatter(new MyValueFormatter());
        set1.setColor(UIUtils.getColor(R.color.bar_20GP));
        set1.setValueTextColor(UIUtils.getColor(R.color.secondary_text));
        set1.setValueTextSize(UIUtils.getDimens(R.dimen.normal_text_size));
        BarDataSet set2 = new BarDataSet(value_40GP, "40GP");
        set2.setValueFormatter(new MyValueFormatter());
        set2.setColor(UIUtils.getColor(R.color.bar_40GP));
        set2.setValueTextColor(UIUtils.getColor(R.color.secondary_text));
        set2.setValueTextSize(UIUtils.getDimens(R.dimen.normal_text_size));
        BarDataSet set3 = new BarDataSet(value_40HQ, "40HQ");
        set3.setValueFormatter(new MyValueFormatter());
        set3.setColor(UIUtils.getColor(R.color.bar_40HQ));
        set3.setValueTextColor(UIUtils.getColor(R.color.secondary_text));
        set3.setValueTextSize(UIUtils.getDimens(R.dimen.normal_text_size));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        BarData data0 = new BarData(xVals, dataSets);
        data0.setGroupSpace(100f);
        data0.setValueFormatter(new MyValueFormatter());

        mChart.setData(data0);
        mChart.invalidate();
        mChart.animateY(600);
    }

    class MyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
            return String.valueOf(((int) v));
        }
    }

    @Override
    protected void onUserVisible() {
        if (data != null) {
            mProfitsTv.showNumberWithAnimation(data.getGrossProfit());
            mYingShouTv.showNumberWithAnimation(data.getReceivableMoney());
            mYingFuTv.showNumberWithAnimation(data.getPayableMoney());
            mShiShouTv.showNumberWithAnimation(data.getReceivedMoney());
            mShiFuTv.showNumberWithAnimation(data.getPrepaidMoney());
            mChart.animateY(600);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void initViewsAndEvents() {
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mStartDateTv.setText(fromDate);
        mEndDateTv.setText(toDate);
        mSpinner.setOnClickListener(this);

        mSelectDateView.setOnClickListener(this);

        mProfitsTv.setOnClickListener(this);
        mViewYingshou.setOnClickListener(this);
        mViewYingfu.setOnClickListener(this);
        mViewStartBiz.setOnClickListener(this);
        mViewOnBiz.setOnClickListener(this);
        mViewEndBiz.setOnClickListener(this);

        contentView = UIUtils.inflate(R.layout.ui_drop_down);
        RadioButton mDeliverTimeBtn = (RadioButton) contentView.findViewById(R.id.rb_deliver_goods_time);
        RadioButton mBussinessTimeBtn = (RadioButton) contentView.findViewById(R.id.rb_bussiness_time);
        RadioButton mStevedoringTimeBtn = (RadioButton) contentView.findViewById(R.id.rb_stevedoring_time);

        mDeliverTimeBtn.setOnClickListener(this);
        mBussinessTimeBtn.setOnClickListener(this);
        mStevedoringTimeBtn.setOnClickListener(this);

        initChart();
    }

    private void initChart() {
        mChart.setNoDataTextDescription("暂无数据");
        mChart.setDescription("");
        mChart.setDrawBorders(false);
        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(false); // 设置是否可以触摸
        mChart.setDragEnabled(false);// 是否可以拖拽
        mChart.setScaleEnabled(false);// 是否可以缩放

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setCustom(new int[]{UIUtils.getColor(R.color.transparent)}, new String[]{""});

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawGridLines(false);
        xl.setAxisLineColor(UIUtils.getColor(R.color.divider));
        xl.setTextColor(UIUtils.getColor(R.color.secondary_text));
        xl.setTextSize(UIUtils.getDimens(R.dimen.normal_text_size));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(UIUtils.getColor(R.color.chart_divider));
        leftAxis.setSpaceTop(10f);

        mChart.getAxisRight().setEnabled(false);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select_date:
                // DialogFragment to host SublimePicker
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                // Options
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                if (!optionsPair.first) { // If options are not valid
                    Toast.makeText(UIUtils.getContext(), "No pickers activated",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Valid options
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
                break;

            case R.id.ll_yingshou:
                EventBus.getDefault().post(new EventCenter<StatisticParams>(Constants.EVENT_YINGSHOU, new StatisticParams(mTimeType, fromDate, toDate)));
                break;
            case R.id.ll_yingfu:
                EventBus.getDefault().post(new EventCenter<StatisticParams>(Constants.EVENT_YINGFU, new StatisticParams(mTimeType, fromDate, toDate)));
                break;
            case R.id.tv_profits:
                EventBus.getDefault().post(new EventCenter<StatisticParams>(Constants.EVENT_PROFITS, new StatisticParams(mTimeType, fromDate, toDate)));
                break;
            case R.id.view_start_biz:
                EventBus.getDefault().post(new EventCenter<StatisticParams>(Constants.EVENT_START_BIZ));
                break;
            case R.id.view_on_biz:
                EventBus.getDefault().post(new EventCenter<StatisticParams>(Constants.EVENT_ON_BIZ));
                break;
            case R.id.view_end_biz:
                EventBus.getDefault().post(new EventCenter<StatisticParams>(Constants.EVENT_END_BIZ));
                break;
            case R.id.spinner_main_fragment:
                if (mDropDownView == null) {
                    mDropDownView = new PopupWindow(contentView, -1, -1);
                    mDropDownView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置悬浮窗体的透明背景
                    mDropDownView.setOutsideTouchable(true);
//                    mSortView.setAnimationStyle(R.style.PopSortAnimation);
                    mDropDownView.showAsDropDown(mConditionsView);
                    contentView.findViewById(R.id.view_mask).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDropDownView.dismiss();
                        }
                    });
                } else if (!mDropDownView.isShowing()) {
                    mDropDownView.showAsDropDown(mConditionsView);
                }
                break;
            case R.id.rb_deliver_goods_time:
                mTimeType = TIME_TYPE_DELIVER_GOODS;
                mSpinner.setText("送货时间");
                requestFromNet();
                if (mDropDownView != null && mDropDownView.isShowing()) {
                    mDropDownView.dismiss();
                }
                break;
            case R.id.rb_bussiness_time:
                mTimeType = TIME_TYPE_BUSSINESS;
                mSpinner.setText("业务时间");
                requestFromNet();
                if (mDropDownView != null && mDropDownView.isShowing()) {
                    mDropDownView.dismiss();
                }
                break;
            case R.id.rb_stevedoring_time:
                mTimeType = TIME_TYPE_STEVEDORING;
                mSpinner.setText("装卸时间");
                requestFromNet();
                if (mDropDownView != null && mDropDownView.isShowing()) {
                    mDropDownView.dismiss();
                }
                break;
        }
    }

    // Validates & returns SublimePicker options
    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

        options.setDisplayOptions(displayOptions);

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    @Override
    public void onRefresh() {
        if (!NetUtils.isNetworkConnected(UIUtils.getContext())) {
            mSwipeRefreshLayout.setRefreshing(false);
            UIUtils.showNoNetWorkDialog();
            return;
        }
        StatisticParams mParams = new StatisticParams(mTimeType, fromDate, toDate);
        netWorkRequest(mParams);
    }
}
