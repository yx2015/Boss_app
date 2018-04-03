package com.xyl.boss_app.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.hxl.universallibrary.eventbus.EventCenter;
import com.hxl.universallibrary.widgets.XViewPager;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.FeeState;
import com.xyl.boss_app.bean.params.FeeParams;
import com.xyl.boss_app.bean.params.StatisticParams;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.ui.activity.base.BaseFragment;
import com.xyl.boss_app.ui.adapter.FeeStateAdapter;
import com.xyl.boss_app.utils.TimeUtils;
import com.xyl.boss_app.utils.UIUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;

/**
 * @ClassName BussinessFeeFragment
 * @Description 业务费用
 * Created by yx on 2015/12/15 0015.
 */
public class BussinessFeeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TIME_TYPE_DELIVER_GOODS = "1";
    private static final String TIME_TYPE_BUSSINESS = "2";
    private static final String TIME_TYPE_STEVEDORING = "3";
    @InjectView(R.id.tabs_fragment_bussiness_fee)
    TabLayout tabLayout;
    @InjectView(R.id.fragment_goods_state_pager)
    XViewPager mViewPager;
    @InjectView(R.id.spinner_fee_fragment)
    TextView mSpinner;
    @InjectView(R.id.rl_bussiness_fee_fragment)
    View mConditionsView;
    @InjectView(R.id.ll_select_date1)
    LinearLayout mSelectDateView;
    @InjectView(R.id.tv_start_time_fee)
    TextView mStartDateTv;
    @InjectView(R.id.tv_end_time_fee)
    TextView mEndDateTv;

    private String mTimeType = TIME_TYPE_DELIVER_GOODS;

    private String fromDate = TimeUtils.getYesterdayDate();
    private String toDate = TimeUtils.getYesterdayDate();

    private FeeParams feeParams;
    private PopupWindow mDropDownView;
    private RadioButton mDeliverTimeBtn;
    private RadioButton mBussinessTimeBtn;
    private RadioButton mStevedoringTimeBtn;
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
            feeParams.setEndTime(toDate);
            feeParams.setStartTime(fromDate);
            // 通知页面刷新
            notifyParamsChanged();
        }
    };

    private void notifyParamsChanged() {
        ReceiveableFragment fragment_receiveable = (ReceiveableFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
        fragment_receiveable.onPageSelected(true, feeParams);
        PayableFragment fragment_payable = (PayableFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 1);
        fragment_payable.onPageSelected(true, feeParams);
        GrossProfitFragment fragment_profits = (GrossProfitFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 2);
        fragment_profits.onPageSelected(true, feeParams);
    }

    @Override
    protected void onFirstUserVisible() {
        if (mViewPager.getCurrentItem() == 0) {
            ReceiveableFragment fragment_receiveable = (ReceiveableFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
            fragment_receiveable.onPageSelected(true, feeParams);
            fragment_receiveable.onFirstUserVisible();
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        feeParams = new FeeParams();
        feeParams.setEndTime(toDate);
        feeParams.setStartTime(fromDate);
        feeParams.setTimeType(mTimeType);
        feeParams.setFeeType(String.valueOf(1));
        mStartDateTv.setText(fromDate);
        mEndDateTv.setText(toDate);

        contentView = UIUtils.inflate(R.layout.ui_drop_down);
        mDeliverTimeBtn = (RadioButton) contentView.findViewById(R.id.rb_deliver_goods_time);
        mBussinessTimeBtn = (RadioButton) contentView.findViewById(R.id.rb_bussiness_time);
        mStevedoringTimeBtn = (RadioButton) contentView.findViewById(R.id.rb_stevedoring_time);

        mDeliverTimeBtn.setOnClickListener(this);
        mBussinessTimeBtn.setOnClickListener(this);
        mStevedoringTimeBtn.setOnClickListener(this);
        mSelectDateView.setOnClickListener(this);
        mSpinner.setOnClickListener(this);

        List<FeeState> resultData = new ArrayList<>();
        String[] feeStateArray = UIUtils.getStringArray(R.array.fee_state);
        resultData.add(new FeeState(feeStateArray[0], new ReceiveableFragment()));
        resultData.add(new FeeState(feeStateArray[1], new PayableFragment()));
        resultData.add(new FeeState(feeStateArray[2], new GrossProfitFragment()));

        if (!resultData.isEmpty()) {
            mViewPager.setOffscreenPageLimit(resultData.size());
            mViewPager.setAdapter(new FeeStateAdapter(getSupportFragmentManager(), resultData));
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabTextColors(UIUtils.getColor(R.color.primary_text), UIUtils.getColor(R.color.orange));//设置文本在选中和为选中时候的颜色
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            feeParams.setFeeType(String.valueOf(1));
                            ReceiveableFragment fragment_receiveable = (ReceiveableFragment) mViewPager.getAdapter().instantiateItem(mViewPager, tab.getPosition());
                            fragment_receiveable.onPageSelected(false, feeParams);
                            break;
                        case 1:
                            feeParams.setFeeType(String.valueOf(2));
                            PayableFragment fragment_payable = (PayableFragment) mViewPager.getAdapter().instantiateItem(mViewPager, tab.getPosition());
                            fragment_payable.onPageSelected(false, feeParams);
                            break;
                        case 2:
                            feeParams.setFeeType("");
                            GrossProfitFragment fragment_profits = (GrossProfitFragment) mViewPager.getAdapter().instantiateItem(mViewPager, tab.getPosition());
                            fragment_profits.onPageSelected(false, feeParams);
                            break;
                    }
                    mViewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        feeParams.setFeeType(String.valueOf(1));
                        ReceiveableFragment fragment_receiveable = (ReceiveableFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
                        fragment_receiveable.onFirstUserVisible();
                        break;
                    case 1:
                        feeParams.setFeeType(String.valueOf(2));
                        PayableFragment fragment_payable = (PayableFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 1);
                        fragment_payable.onFirstUserVisible();
                        break;
                    case 2:
                        feeParams.setFeeType("");
                        GrossProfitFragment fragment_profits = (GrossProfitFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 2);
                        fragment_profits.onFirstUserVisible();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_bussiness_fee;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select_date1:
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
            case R.id.spinner_fee_fragment:
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
                feeParams.setTimeType(mTimeType);
                // 通知页面刷新
                notifyParamsChanged();
                if (mDropDownView != null && mDropDownView.isShowing()) {
                    mDropDownView.dismiss();
                }
                break;
            case R.id.rb_bussiness_time:
                mTimeType = TIME_TYPE_BUSSINESS;
                mSpinner.setText("业务时间");
                feeParams.setTimeType(mTimeType);
                // 通知页面刷新
                notifyParamsChanged();
                if (mDropDownView != null && mDropDownView.isShowing()) {
                    mDropDownView.dismiss();
                }
                break;
            case R.id.rb_stevedoring_time:
                mTimeType = TIME_TYPE_STEVEDORING;
                mSpinner.setText("装卸时间");
                feeParams.setTimeType(mTimeType);
                // 通知页面刷新
                notifyParamsChanged();
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
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        StatisticParams statisticParams = null;
        if(eventCenter.getData() != null){
            statisticParams = (StatisticParams) eventCenter.getData();
        }
        if(statisticParams == null){
            return;
        }
        switch (statisticParams.getTimeType()) {
            case TIME_TYPE_DELIVER_GOODS:
                mSpinner.setText("送货时间");
                mDeliverTimeBtn.setChecked(true);
                break;
            case TIME_TYPE_BUSSINESS:
                mSpinner.setText("业务时间");
                mBussinessTimeBtn.setChecked(true);
                break;
            case TIME_TYPE_STEVEDORING:
                mSpinner.setText("装卸时间");
                mStevedoringTimeBtn.setChecked(true);
                break;
        }
        mStartDateTv.setText(statisticParams.getStartTime());
        mEndDateTv.setText(statisticParams.getEndTime());

        feeParams.setTimeType(statisticParams.getTimeType());
        feeParams.setStartTime(statisticParams.getStartTime());
        feeParams.setEndTime(statisticParams.getEndTime());

        switch (eventCenter.getEventCode()) {
            case Constants.EVENT_YINGSHOU:
                mViewPager.setCurrentItem(0, false);
                break;
            case Constants.EVENT_YINGFU:
                mViewPager.setCurrentItem(1, false);
                break;
            case Constants.EVENT_PROFITS:
                mViewPager.setCurrentItem(2, false);
                break;
        }
        notifyParamsChanged();
    }

    protected void dismissDropDonwnView() {
        if (mDropDownView != null && mDropDownView.isShowing()) {
            mDropDownView.dismiss();
            mDropDownView = null;
        }
    }

    @Override
    public void onDestroyView() {
        dismissDropDonwnView();
        super.onDestroyView();
    }
}
