package com.xyl.boss_app.ui.activity;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.xyl.boss_app.R;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.utils.SPUtil;
import com.xyl.boss_app.utils.TelephonyUtils;


/**
 * Created by yx on 2015/11/19 0019.
 */
public class AboutUsActivity extends BaseActivity {
    private TextView mProtocol, mVersion, mContract;

    @Override
    protected boolean setToolBar() {
        mTitle.setText("关于我们");
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about_us;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        mVersion = (TextView) findViewById(R.id.tv_version);
        mContract = (TextView) findViewById(R.id.tv_contract);
        mVersion.setText("新集运老板APP：" + SPUtil.getString(Constants.Preferences.VERSION_NAME, ""));
        mContract.setText(Html.fromHtml("联系电话：" + "<font color='#1976D2'>" + Constants.CUSTOM_PHONE + "</font>"));
        mProtocol = (TextView) findViewById(R.id.tv_protocol);
        mProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //软件协议
//                Intent intent = new Intent(AboutUsActivity.this, WebviewActivity.class);
//                intent.putExtra(Constants.Extra.WEBVIEW_TITLE, "软件许可及服务协议");
//                intent.putExtra(Constants.Extra.WEBVIEW_URL, Api.USER_AGREEMENT_HTML);
//                startActivity(intent);
            }
        });

        mContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyUtils.CallSysDial(AboutUsActivity.this, Constants.CUSTOM_PHONE);
            }
        });

    }
}
