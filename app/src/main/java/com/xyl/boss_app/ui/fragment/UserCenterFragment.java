package com.xyl.boss_app.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxl.universallibrary.netstatus.NetUtils;
import com.hxl.universallibrary.utils.TLog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.UpdateInfoDto;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.protocol.UpdateAppProtocol;
import com.xyl.boss_app.ui.activity.AboutUsActivity;
import com.xyl.boss_app.ui.activity.HelpActivity;
import com.xyl.boss_app.ui.activity.LoginActivity;
import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.ui.activity.base.BaseFragment;
import com.xyl.boss_app.utils.FileUtils;
import com.xyl.boss_app.utils.SPUtil;
import com.xyl.boss_app.utils.StringUtils;
import com.xyl.boss_app.utils.UIUtils;
import com.xyl.boss_app.widgets.AlertDialog;
import com.xyl.boss_app.widgets.ProgressDialog;

import java.io.File;

import butterknife.InjectView;

/**
 * @ClassName MainFragment
 * @Description: 我的
 * Created by yx on 2015/12/15 0015.
 */
public class UserCenterFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.ll_help)
    LinearLayout mHelp;
    @InjectView(R.id.btn_exit)
    Button mExit;
    @InjectView(R.id.ll_about_us)
    LinearLayout mAboutUs;
    @InjectView(R.id.ll_has_update)
    LinearLayout mHasUpdate;
    private AlertDialog dialog;

    @InjectView(R.id.tv_version_name)
    TextView mVersionName;

    @InjectView(R.id.tv_has_update)
    TextView mUpdate;

    @InjectView(R.id.tv_user)
    TextView mUserName;

    @InjectView(R.id.tv_company)
    TextView mCompany;

    private String versionName;
    private UpdateInfoDto.UpdateAppInfo updateInfo;
    private ProgressDialog mdDialog;
    private File file = new File(FileUtils.getDownloadDir() + "boss.apk");
    private String userName, companyName;


    @Override
    protected void onFirstUserVisible() {
        /*获取服务器版本信息*/
        getServerVersion();
    }

    @Override
    protected void onUserVisible() {
//        getServerVersion();
    }

    /**
     * 设置点击事件
     */
    private void setClickListner() {
        mHelp.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mHasUpdate.setOnClickListener(this);
    }

    /**
     * 获取服务器版本信息
     * 更新版本
     */
    private void getServerVersion() {
        if (!NetUtils.isNetworkConnected(getActivity())) {
            UIUtils.showNoNetWorkDialog();
            return;
        }
        new UpdateAppProtocol() {
            @Override
            protected void onCallBack(UpdateInfoDto.UpdateAppInfo updateInfo) {
                int versionCode = getAppVersionCode();
                int serverVersionCode = updateInfo.getVersion();
                if (versionCode < serverVersionCode) {
                    mUpdate.setVisibility(View.VISIBLE);
                    mUpdate.setText("有更新");
                    setUpdateInfo(updateInfo);
                } else {
                    mUpdate.setVisibility(View.GONE);
                }
            }
        };
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        // 获取应用版本号
        versionName = SPUtil.getString(Constants.Preferences.VERSION_NAME, "");
        userName = SPUtil.getString(Constants.Preferences.LAST_LOGIN_USER_ACCOUNT, "");
        companyName = SPUtil.getString(Constants.Preferences.LAST_LOGIN_ORG_NAME, "");
        String name = SPUtil.getString(Constants.ACCOUNTID + Constants.Preferences.NAME, "");
        mVersionName.setText("V" + versionName);
        if (StringUtils.isEmpty(name)) {
            mUserName.setText(userName);
        } else {
            mUserName.setText(userName + "(" + name + ")");
        }
        mCompany.setText(companyName);
        /*设置点击事件*/
        setClickListner();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_has_update:
                if (View.VISIBLE == mUpdate.getVisibility()) {
                    // 弹出升级应用对话框
                    showUpdateDialog();
                } else {
                    UIUtils.showToast("已是最新版本");
                }
                break;
            case R.id.ll_help:
                readyGo(HelpActivity.class);
                break;
            case R.id.ll_about_us:
                readyGo(AboutUsActivity.class);
                break;
            case R.id.btn_exit:
                dialog = new AlertDialog(getActivity()).builder();
                dialog.setTitle("退出登录")
                        .setMsg("您真的要退出登录吗？")
                        .setCancelable(false)
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SPUtil.putBoolean("isLogin", false);
                                BaseActivity.getForegroundActivity().finishActivities();
                                readyGo(LoginActivity.class);
                            }
                        }).show();
                break;
        }

    }

    /**
     * 弹出升级对话框
     */
    private void showUpdateDialog() {
        AlertDialog dialog = new AlertDialog(getActivity()).builder();
        dialog.setCancelable(false)
                .setTitle("软件更新提醒")
                .setMsg(updateInfo.getDesc())
                .setPositiveButton("立刻更新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!NetUtils.isNetworkConnected(UIUtils.getContext())) {
                            UIUtils.showNoNetWorkDialog();
                            return;
                        }
                        mdDialog = new ProgressDialog(getActivity()).builder()
                                .setCancelable(false)
                                .setTitle("正在下载新版本");
                        //开始下载
                        downloadApp(updateInfo.getDownloadUrl());

                    }
                });
        if (updateInfo.getForceUpdate() == 0) {
            dialog.setNegativeButton("下次再说", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        dialog.show();
    }

    /**
     * 获取应用的版本号
     */
    private static int getAppVersionCode() {
        try {
            PackageManager pm = UIUtils.getContext().getPackageManager();
            PackageInfo info = pm.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


    private void downloadApp(String downloadUrl) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(downloadUrl, file.getAbsolutePath(), true, false, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                TLog.d(null, "下载成功");
                // 安装APP
                mdDialog.dismiss();
                installApp(file);
            }

            @Override
            public void onStart() {
                mdDialog.show();
            }


            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if ("maybe the file has downloaded completely".equals(arg1)) {//若安装包已存在则直接安装
                    installApp(file);
                    return;
                }
                // 下载失败
                arg0.printStackTrace();
                UIUtils.showToast("下载失败");
                mdDialog.dismiss();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                // 下载过程中，设置进度条
                mdDialog.setProgress((int) current);
                mdDialog.setMaxProgress((int) total);
            }

        });
    }

    /**
     * 安装应用
     */
    private static void installApp(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        BaseActivity.getForegroundActivity().startActivity(intent);
    }

    public void setUpdateInfo(UpdateInfoDto.UpdateAppInfo updateInfo) {
        this.updateInfo = updateInfo;
    }
}
