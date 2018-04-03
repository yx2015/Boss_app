package com.xyl.boss_app.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import com.hxl.universallibrary.netstatus.NetUtils;
import com.hxl.universallibrary.utils.TLog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.UpdateInfoDto;
import com.xyl.boss_app.bean.params.LoginParams;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.protocol.LoginProtocol;
import com.xyl.boss_app.protocol.UpdateAppProtocol;
import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.utils.FileUtils;
import com.xyl.boss_app.utils.PackageUtils;
import com.xyl.boss_app.utils.SPUtil;
import com.xyl.boss_app.utils.UIUtils;
import com.xyl.boss_app.widgets.AlertDialog;
import com.xyl.boss_app.widgets.ProgressDialog;

import java.io.File;

/**
 * Created by yx on 2015/12/21 0021.
 */
public class SplashActivity extends BaseActivity {
    private UpdateInfoDto.UpdateAppInfo updateInfo;
    private File file = new File(FileUtils.getDownloadDir() + "boss.apk");
    private ProgressDialog mdDialog;

    private LinearLayout mBackgroud;

    private String orgCode, userName, pwd;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        /*获取应用版本号*/
        String versionName = PackageUtils.getVersionName();
        SPUtil.putString(Constants.Preferences.VERSION_NAME, versionName);
        orgCode = SPUtil.getString(Constants.Preferences.LAST_LOGIN_ORG_ACCOUNT, "");
        userName = SPUtil.getString(Constants.Preferences.LAST_LOGIN_USER_ACCOUNT, "");
        pwd = SPUtil.getString(Constants.ACCOUNTID + Constants.Preferences.PWD, "");
        mBackgroud = (LinearLayout) findViewById(R.id.ll_bg);

        ObjectAnimator anim = ObjectAnimator.ofFloat(mBackgroud, "alpha", 0, 1.0f);
        anim.setDuration(2000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                /*获取服务器版本信息*/
                getServerVersion();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();


    }

    /**
     * 获取服务器版本信息
     * 更新版本
     */
    private void getServerVersion() {
        if (!NetUtils.isNetworkConnected(this)) {
            UIUtils.showToast("当前无网络连接，请您打开网络连接");
            readyGoThenKill(LoginActivity.class);
        } else {
            new UpdateAppProtocol() {
                @Override
                protected void onCallBack(final UpdateInfoDto.UpdateAppInfo updateInfo) {
                    int versionCode = getAppVersionCode();
                    int serverVersionCode = updateInfo.getVersion();
                    TLog.d("UpdateAppProtocol", "本地获取版本号为：" + versionCode + "， 服务器端版本号为：" + serverVersionCode);
                    if (versionCode < serverVersionCode) {
                        // 弹出升级应用对话框
                        showUpdateDialog(updateInfo);
                    } else {
                        //版本号一致,进入登录界面或主界面
                        toActivity();
                    }
                }

                @Override
                protected void errorBack() {
                    super.errorBack();
                /*进入登录页面或主界面*/
                    toActivity();
                }
            };
        }
    }


    /**
     * 弹出升级对话框
     */
    private void showUpdateDialog(final UpdateInfoDto.UpdateAppInfo updateInfo) {
        AlertDialog dialog = new AlertDialog(this).builder();
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
                        mdDialog = new ProgressDialog(SplashActivity.this).builder()
                                .setCancelable(false)
                                .setTitle("正在下载新版本");

                        /*开始下载*/
                        downloadApp(updateInfo.getDownloadUrl());

                    }
                });
        if (updateInfo.getForceUpdate() == 0) {
            dialog.setNegativeButton("下次再说", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*取消更新，进入登录页面或主界面*/
                    toActivity();
                }
            });
        }
        dialog.show();
    }

    /**
     * 从服务器下载app
     *
     * @param downloadUrl
     */
    private void downloadApp(String downloadUrl) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(downloadUrl, file.getAbsolutePath(), true, false, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                TLog.d(null, "下载成功");
                mdDialog.dismiss();
                /*安装APP*/
                installApp(file);
            }

            @Override
            public void onStart() {
                mdDialog.show();
            }


            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if ("maybe the file has downloaded completely".equals(arg1)) {//若安装包已存在则直接安装
                    /*安装APP*/
                    installApp(file);
                    return;
                }
                // 下载失败
                arg0.printStackTrace();
                UIUtils.showToast("下载失败");
                mdDialog.dismiss();
                toActivity();
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

    /**
     * 跳转到登录页面或主页面
     */
    private void toActivity() {
        if (file.exists() && file.length() > 0) {
            file.delete();
            UIUtils.showToast("已为您删除安装包");
        }
        if (SPUtil.getBoolean("isLogin", false)) {
            new LoginProtocol(new LoginParams(orgCode, userName, pwd, Constants.SOURCE)) {
                @Override
                protected void onCallBack() {
                    readyGoThenKill(MainActivity.class);
                }

                @Override
                protected void onError() {
                    readyGoThenKill(LoginActivity.class);
                }
            };
        } else {
            readyGoThenKill(LoginActivity.class);
        }
    }


}
