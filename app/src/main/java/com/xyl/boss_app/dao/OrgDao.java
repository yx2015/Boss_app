package com.xyl.boss_app.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xyl.boss_app.db.DatabaseHelper;
import com.xyl.boss_app.db.model.OrgModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yx on 2015/12/30 0030.
 */
public class OrgDao {
    /**
     * SQLiteDatabase.
     */
    private SQLiteDatabase mDb;

    public OrgDao() {
        mDb = DatabaseHelper.getDatabase();
    }

    public void closeDB() {
        mDb.close();
    }

    /**
     * 获取所有公司代码
     */
    public List<String> getOrgCodes() {
        final List<String> list = new ArrayList<>();
        String sql = "select " + OrgModel.ORG_CODE + " from " + OrgModel.TABLE_NAME;
        Cursor cursor = mDb.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(OrgModel.ORG_CODE)));
        }
        cursor.close();
        return list;
    }

    /**
     * 查询公司logo
     */
    public String getOrgLogo(String orgCode) {
        String sql = "select " + OrgModel.LOGO + " from " + OrgModel.TABLE_NAME + " where " + OrgModel.ORG_CODE + " = ?";
        Cursor cursor = mDb.rawQuery(sql, new String[]{orgCode.toUpperCase()});
        String orgLogo = null;
        if (cursor.moveToFirst()) {
            orgLogo = cursor.getString(cursor.getColumnIndex(OrgModel.LOGO));
        }
        cursor.close();
        return orgLogo;
    }

    /**
     * 获取公司信息数据.
     *
     * @return 公司信息数据，null表示未找到
     */
    public OrgModel getOrgInfo(String orgCode) {
        String sql = "select * from " + OrgModel.TABLE_NAME + " where " + OrgModel.ORG_CODE + " = ?";
        Cursor cursor = mDb.rawQuery(sql, new String[]{orgCode.toUpperCase()});
        OrgModel model = null;
        if (cursor.moveToFirst()) {
            model = OrgModel.parse(cursor);
        }
        cursor.close();
        return model;
    }


    /**
     * 保存公司信息数据
     *
     * @param model 待保存的公司信息数据
     * @return 保存成功返回true，否则返回false
     */

    public boolean save(OrgModel model) {
        OrgModel existModel = getOrgInfo(model.getOrgCode());
        boolean bIsSuccess = false;
        long lRowId = -1;
        if (existModel == null) {
            lRowId = mDb.insert(OrgModel.TABLE_NAME, null, model.toContentValues());
        } else {
            lRowId = updateLogo(model);
        }
        if (lRowId != -1) {
            bIsSuccess = true;
        }
        return bIsSuccess;
    }


    /**
     * 更新密码.
     *
     * @return 受影响的行数
     */
    public int updateLogo(final OrgModel model) {
        int iRows = -1;
        try {
            mDb.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(OrgModel.LOGO, model.getLogo());
            iRows = mDb.update(OrgModel.TABLE_NAME, contentValues, "orgCode=?", new String[]{model.getOrgCode()});
            if (iRows != -1) {
                mDb.setTransactionSuccessful();
            }
        } finally {
            mDb.endTransaction();
        }
        return iRows;
    }
}
