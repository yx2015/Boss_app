package com.xyl.boss_app.ui.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hxl.universallibrary.adapter.ViewHolderBase;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.FeeDto;

import java.text.DecimalFormat;

/**
 * Created by yx on 2015/12/17 0017.
 */
public class FeeStateHolder extends ViewHolderBase<FeeDto.FeeDtoInfo.FeeInfo> {

    private TextView mCompany, mFee, mCount20GP, mCount40GP, mCount40HQ, mShiShouFee, mTv_20GP, mTv_40GP, mTv_40HQ;
    private DecimalFormat mFormat;

    public FeeStateHolder() {
        mFormat = new DecimalFormat("###,###,###,##0.00");
    }

    @Override
    public View createView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.ui_yingshou_item, null);
        mCompany = (TextView) view.findViewById(R.id.tv_company);
        mFee = (TextView) view.findViewById(R.id.tv_fee);
        mCount20GP = (TextView) view.findViewById(R.id.tv_count_20GP);
        mTv_20GP = (TextView) view.findViewById(R.id.tv_20GP);
        mCount40GP = (TextView) view.findViewById(R.id.tv_count_40GP);
        mTv_40GP = (TextView) view.findViewById(R.id.tv_40GP);
        mCount40HQ = (TextView) view.findViewById(R.id.tv_count_40HQ);
        mTv_40HQ = (TextView) view.findViewById(R.id.tv_40HQ);
        mShiShouFee = (TextView) view.findViewById(R.id.tv_shishou_fee);
        return view;
    }


    @Override
    public void showData(int i, FeeDto.FeeDtoInfo.FeeInfo feeInfo) {
        mCompany.setText(feeInfo.getCheckObject());
        mFee.setText("+" + mFormat.format(feeInfo.getReceivableMoney()));
        if (feeInfo.getBizGP20Num() == 0) {
            mTv_20GP.setVisibility(View.GONE);
            mCount20GP.setVisibility(View.GONE);
        } else {
            mTv_20GP.setVisibility(View.VISIBLE);
            mCount20GP.setVisibility(View.VISIBLE);
            mCount20GP.setText(String.valueOf(feeInfo.getBizGP20Num()));
        }
        if (feeInfo.getBizGP40Num() == 0) {
            mTv_40GP.setVisibility(View.GONE);
            mCount40GP.setVisibility(View.GONE);
        } else {
            mTv_40GP.setVisibility(View.VISIBLE);
            mCount40GP.setVisibility(View.VISIBLE);
            mCount40GP.setText(String.valueOf(feeInfo.getBizGP40Num()));
        }
        if (feeInfo.getBizHQ40Num() == 0) {
            mTv_40HQ.setVisibility(View.GONE);
            mCount40HQ.setVisibility(View.GONE);
        } else {
            mTv_40HQ.setVisibility(View.VISIBLE);
            mCount40HQ.setVisibility(View.VISIBLE);
            mCount40HQ.setText(String.valueOf(feeInfo.getBizHQ40Num()));
        }
        if (feeInfo.getBizGP40Num() == 0 && feeInfo.getBizHQ40Num() == 0) {
            mTv_20GP.setText("*20GP");
        } else {
            mTv_20GP.setText("*20GP ;  ");
        }
        if (feeInfo.getBizHQ40Num() == 0) {
            mTv_40GP.setText("*40GP");
        } else {
            mTv_40GP.setText("*40GP ;  ");
        }
        mShiShouFee.setText("实收" + mFormat.format(feeInfo.getReceivedMoney()));
    }
}
