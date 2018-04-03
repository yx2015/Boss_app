package com.xyl.boss_app.bean;

/**   
 * @ClassName:  BaseData   
 * @Description: 服务器返回值的基类   
 * @author: Haoxl  
 * @date:   2015-4-1 上午10:08:31   
 */ 
public class BaseDto {
	/**   
	 * 响应码   
	 */ 
	private String code;
	/**   
	 * 提示信息   
	 */ 
	private String msg;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
