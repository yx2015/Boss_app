package com.xyl.boss_app.ui.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxl.universallibrary.adapter.ViewHolderBase;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.CabinetDto;

/**
 * Created by yx on 2015/12/17 0017.
 */
public class CabinetHolder extends ViewHolderBase<CabinetDto.CabinetDtoInfo.CabinetInfo> {
    private TextView mCompanyName, mStartPort20GP, mStartPort40GP, mStartPort40HQ;
    private TextView mOnBroad20GP, mOnBroad40GP, mOnBroad40HQ;
    private TextView mEnd20GP, mEnd40GP, mEnd40HQ;
    public LinearLayout mStartPort, mOnBroad, mEndPort;
    private String mBizStatus;

    public CabinetHolder(String mBizStatus) {
        this.mBizStatus = mBizStatus;
    }


    @Override
    public View createView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.ui_container_item, null);
        mCompanyName = (TextView) view.findViewById(R.id.tv_company);
        mStartPort20GP = (TextView) view.findViewById(R.id.tv_qiyun_port_20GP);
        mStartPort40GP = (TextView) view.findViewById(R.id.tv_qiyun_port_40GP);
        mStartPort40HQ = (TextView) view.findViewById(R.id.tv_qiyun_port_40HQ);
        mOnBroad20GP = (TextView) view.findViewById(R.id.tv_haishang_port_20GP);
        mOnBroad40GP = (TextView) view.findViewById(R.id.tv_haishang_port_40GP);
        mOnBroad40HQ = (TextView) view.findViewById(R.id.tv_haishang_port_40HQ);
        mEnd20GP = (TextView) view.findViewById(R.id.tv_arrvie_port_20GP);
        mEnd40GP = (TextView) view.findViewById(R.id.tv_arrvie_port_40GP);
        mEnd40HQ = (TextView) view.findViewById(R.id.tv_arrvie_port_40HQ);
        mStartPort = (LinearLayout) view.findViewById(R.id.ll_start_port);
        mOnBroad = (LinearLayout) view.findViewById(R.id.ll_on_sea);
        mEndPort = (LinearLayout) view.findViewById(R.id.ll_end_port);

        if (mBizStatus.equals("startBiz")) {
            mOnBroad.setVisibility(View.GONE);
            mEndPort.setVisibility(View.GONE);
        } else if (mBizStatus.equals("onBiz")) {
            mStartPort.setVisibility(View.GONE);
            mEndPort.setVisibility(View.GONE);
        } else if (mBizStatus.equals("endBiz")) {
            mStartPort.setVisibility(View.GONE);
            mOnBroad.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void showData(int i, CabinetDto.CabinetDtoInfo.CabinetInfo info) {
        mCompanyName.setText(info.getConsignor());
        mStartPort20GP.setText(info.getStartBiz().getGP_20() + "");
        mStartPort40GP.setText(info.getStartBiz().getGP_40() + "");
        mStartPort40HQ.setText(info.getStartBiz().getHQ_40() + "");
        mOnBroad20GP.setText(info.getOnBiz().getGP_20() + "");
        mOnBroad40GP.setText(info.getOnBiz().getGP_40() + "");
        mOnBroad40HQ.setText(info.getOnBiz().getHQ_40() + "");
        mEnd20GP.setText(info.getEndBiz().getGP_20() + "");
        mEnd40GP.setText(info.getEndBiz().getGP_40() + "");
        mEnd40HQ.setText(info.getEndBiz().getHQ_40() + "");
    }


}
