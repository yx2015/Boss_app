package com.xyl.boss_app.db.model;

import android.content.ContentValues;
/**   
 * @ClassName:  BaseModel   
 * @Description:表模型的基础接口   
 * @author: Haoxl  
 * @date:   2015-3-26 下午3:12:27   
 *   
 * @param <T>   表模型
 */ 
public abstract class BaseModel<T> {
    /**   
     * @Title: toContentValues   
     * @Description: 将表模型数据封装为ContentValues.   
     * @return ContentValues 封装后的ContentValues
     */
    public abstract ContentValues toContentValues();
}
