package com.xyl.boss_app.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lidroid.xutils.util.LogUtils;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.db.model.OrgModel;
import com.xyl.boss_app.utils.UIUtils;


/**
 * @ClassName: DatabaseHelper
 * @Description: 管理数据库创建和版本管理
 * @author: Haoxl
 * @date: 2015-3-30 下午3:37:41
 */
public final class DatabaseHelper extends SQLiteOpenHelper {


    /**
     * DatabaseHelper.
     */
    public static DatabaseHelper sDbHelper;

    /**
     * SQLiteDatabase.
     */
    public static SQLiteDatabase sDb;

    /**
     * 对象锁.
     */
    private static Object sObjLock = new Object();


    public DatabaseHelper() {
        super(UIUtils.getContext(), Constants.Database.DB_NAME + Constants.Database.EXTENSION, null, Constants.Database.VERSION);

    }

    /**
     * 获取DatabaseHelper实例.
     *
     * @return DatabaseHelper实例
     */
    private static DatabaseHelper getInstance() {
        if (DatabaseHelper.sDbHelper == null) {
            synchronized (DatabaseHelper.sObjLock) {
                if (DatabaseHelper.sDbHelper == null) {
                    DatabaseHelper.sDbHelper = new DatabaseHelper();
                }
            }
        }
        return DatabaseHelper.sDbHelper;
    }

    /**
     * 获取SQLiteDatabase实例.
     *
     * @return SQLiteDatabase实例
     */
    public static SQLiteDatabase getDatabase() {
        if (DatabaseHelper.sDb == null) {
            synchronized (DatabaseHelper.sObjLock) {
                if (DatabaseHelper.sDb == null) {
                    DatabaseHelper.sDb = DatabaseHelper.getInstance().getWritableDatabase();
                    LogUtils.i("新建SQLiteDatabase实例");
                }
            }
        } else {
            while (DatabaseHelper.sDb.isDbLockedByCurrentThread() || DatabaseHelper.sDb.isDbLockedByCurrentThread()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    LogUtils.e("数据库被占用，请求等待10ms失败", e);
                }
            }
        }
        return DatabaseHelper.sDb;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        LogUtils.i("数据库被创建");
        db.execSQL(OrgModel.CREATE_TABLE);
        LogUtils.i("创建公司信息表");

    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
