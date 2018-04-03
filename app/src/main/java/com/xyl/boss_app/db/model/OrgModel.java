package com.xyl.boss_app.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.lidroid.xutils.util.LogUtils;
import com.xyl.boss_app.utils.StringUtils;

/**
 * Created by yx on 2015/12/30 0030.
 */
public class OrgModel extends BaseModel<OrgModel> {

    /***************************** 表信息 ************************************/
    /**
     * 表名
     */
    public static final String TABLE_NAME = "Org";
    /**
     * 公司名：orgName
     */
    public static final String ORG_CODE = "orgCode";

    /**
     * 公司logo：logo
     */
    public static final String LOGO = "logo";


    /**
     * 创建表
     */
    public static final String CREATE_TABLE = "create table if not exists " + OrgModel.TABLE_NAME
            + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + OrgModel.ORG_CODE + " TEXT , "
            + OrgModel.LOGO + " TEXT "
            + ");";


    /**
     * 列数组
     */
    public static final String[] COLUMNS = {OrgModel.ORG_CODE, OrgModel.LOGO};


    /*************************** 对象信息 **************************************/
    /**
     * 公司名：orgCode
     */
    public String orgCode;

    /**
     * 公司logo：logo
     */
    public String logo;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (!StringUtils.isEmpty(orgCode)) {
            values.put(OrgModel.ORG_CODE, orgCode);
        }
        if (!StringUtils.isEmpty(logo)) {
            values.put(OrgModel.LOGO, logo);
        }
        return values;

    }


    /**
     * 将游标中的数据封装为表模型
     *
     * @param cursor
     * @return 封装后的表模型，null表示为封装成功
     */
    public static OrgModel parse(final Cursor cursor) {
        if (cursor == null) {
            LogUtils.e("参数不能为空");
            return null;
        }

        OrgModel model = new OrgModel();
        int iIndex = 0;

        if ((iIndex = cursor.getColumnIndex(OrgModel.ORG_CODE)) != -1) {
            model.setOrgCode(cursor.getString(iIndex));
        }
        if ((iIndex = cursor.getColumnIndex(OrgModel.LOGO)) != -1) {
            model.setLogo(cursor.getString(iIndex));
        }

        return model;

    }


}
