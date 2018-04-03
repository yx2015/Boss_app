package com.xyl.boss_app.ui.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hxl.universallibrary.adapter.ViewHolderBase;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.FeeDto;

import java.text.DecimalFormat;

/**
 * Created by yx on 2015/12/23 0023.
 */
public class GrossProfitHolder extends ViewHolderBase<FeeDto.FeeDtoInfo.FeeInfo> {
    private TextView mCompany, mYingShou, mYingFu, mFee;
    private DecimalFormat mFormat;

    public GrossProfitHolder() {
        mFormat = new DecimalFormat("###,###,###,##0.00");
    }

    @Override
    public View createView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.ui_profit_item, null);
        mCompany = (TextView) view.findViewById(R.id.tv_company);
        mYingShou = (TextView) view.findViewById(R.id.tv_yingshou_fee);
        mYingFu = (TextView) view.findViewById(R.id.tv_yingfu_fee);
        mFee = (TextView) view.findViewById(R.id.tv_fee);
        return view;
    }

    @Override
    public void showData(int i, FeeDto.FeeDtoInfo.FeeInfo feeInfo) {
        mCompany.setText(feeInfo.getCheckObject());
        mYingShou.setText(mFormat.format(feeInfo.getReceivableMoney()));
        mYingFu.setText(mFormat.format(feeInfo.getPayableMoney()));
        mFee.setText(feeInfo.getGrossProfit() < 0 ? mFormat.format(feeInfo.getGrossProfit()) : "+" + mFormat.format(feeInfo.getGrossProfit()));
    }
}
