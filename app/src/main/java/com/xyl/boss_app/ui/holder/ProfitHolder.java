package com.xyl.boss_app.ui.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hxl.universallibrary.adapter.ViewHolderBase;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.FeeDto;

/**
 * Created by yx on 2015/12/17 0017.
 */
public class ProfitHolder extends ViewHolderBase<FeeDto> {

    private TextView mCompany;

    @Override
    public View createView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.ui_profit_item, null);
        mCompany = (TextView) view.findViewById(R.id.tv_company);

        return view;
    }

    @Override
    public void showData(int i, FeeDto feeStateDto) {
        mCompany.setText("");


    }
}
