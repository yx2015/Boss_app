package com.xyl.boss_app.manager;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.AlertDialog;

import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

/**
 * @ClassName: CrashHandler
 * @Description: 应用程序未捕获异常的全局处理类
 * @author: Haoxl
 * @date: 2015-3-27 上午9:49:26
 */
public final class CrashHandler implements UncaughtExceptionHandler {

    /**
     * 应用崩溃捕获器.
     */
    private static CrashHandler mCrashHandler;

    /**
     * 系统默认捕获器.
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * 私有的构造方法.
     */
    private CrashHandler() {
    }

    private static AlertDialog alertDialog;

    /**
     * @Title: init
     * @Description: 初始化方法.
     */
    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * @return CrashHandler 应用崩溃捕获器实例
     * @Title: getInstance
     * @Description: 获取应用崩溃捕获器实例.
     */
    public static synchronized CrashHandler getInstance() {
        if (CrashHandler.mCrashHandler == null) {
            CrashHandler.mCrashHandler = new CrashHandler();
        }
        return CrashHandler.mCrashHandler;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        ex.printStackTrace();
        if (!handleException(ex) && mDefaultHandler != null) {
            //将错误日志保存到本地
            try {
                Field[] fields = Build.class.getDeclaredFields();
                StringBuffer sb = new StringBuffer();
                for (Field field : fields) {
                    String value = field.get(null).toString();
                    String name = field.getName();
                    sb.append(name);
                    sb.append(":");
                    sb.append(value);
                    sb.append("\n");
                }
                File file = new File(FileUtils.getLogsDir());
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream out = new FileOutputStream(FileUtils.getLogsDir() + "/error.log");
                StringWriter wr = new StringWriter();
                PrintWriter err = new PrintWriter(wr);
                ex.printStackTrace(err);
                String errorlog = wr.toString();
                sb.append(errorlog);
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //此处可以将错误日志上传服务器

            // 未处理的异常交给系统处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * @param ex 异常信息
     * @return boolean  true表示已处理，false表示未处理
     * @Title: handleException
     * @Description: 处理异常.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                new AlertDialog.Builder(BaseActivity.getCurrentActivity())
                        .setTitle("温馨提示")
                        .setCancelable(false)
                        .setMessage("抱歉！程序出错了！程序猿正在加紧修复中！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        }).show();
                Looper.loop();
            }
        }).start();
        return true;
    }
}
