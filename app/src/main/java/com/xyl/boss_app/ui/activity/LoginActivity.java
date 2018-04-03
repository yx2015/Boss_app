package com.xyl.boss_app.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxl.universallibrary.netstatus.NetUtils;
import com.xyl.boss_app.R;
import com.xyl.boss_app.bean.params.LoginParams;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.dao.OrgDao;
import com.xyl.boss_app.protocol.LoginProtocol;
import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.utils.SPUtil;
import com.xyl.boss_app.utils.SignUtils;
import com.xyl.boss_app.utils.StringUtils;
import com.xyl.boss_app.utils.UIUtils;
import com.xyl.boss_app.widgets.ActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yx on 2015/12/14 0014.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private AutoCompleteTextView mCompanyCode, mLoginName;
    private EditText mPwd;
    private Button mSubmit;
    private String companyCode, loginName, password;
    private TextView mHasQuestion;
    private CheckBox mShowPwd;
    private String[] strs;
    private ImageView mRecentCompany, mRecentLoginName;
    private PopupWindow popupWindow;
    private String[] userNums;//用户账户数
    private String orgCode, userName, SPuserName;
    private ImageView mLogo;
    private List<String> list;
    private ActionSheetDialog dialog;

    private OrgDao orgDao;

    private List<String> orgCodeNums;
    private List<String> userNameNums;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        orgCodeNums = new ArrayList<>();
        userNameNums = new ArrayList<>();

        orgDao = new OrgDao();
        orgCodeNums = orgDao.getOrgCodes();
//        TLog.e("公司信息", orgCodeNums.toString());

        initView();//初始化控件
        loadData();//加载本地数据
        setClickListener();//设置点击事件
        strs = UIUtils.getResources().getStringArray(R.array.use_help);
        list = new ArrayList<>();

    }

    /**
     * 加载本地公司账号数以及用户账号数
     */
    private void loadData() {
        /*设置输入框本文值*/
        setData();

        /*获取最近登录用户名*/
        SPuserName = SPUtil.getString(Constants.Preferences.USER_NUMS, "");
        if (!StringUtils.isEmpty(SPuserName)) {
            userNums = SPuserName.split("#");
            if (userNums.length > 0) {
                for (int i = 0; i < userNums.length; i++) {
                    userNameNums.add(userNums[i]);
                }
            }
            mRecentLoginName.setEnabled(true);
        } else {
            mRecentLoginName.setEnabled(false);
        }

    }

    /**
     * 设置输入框文本
     */
    private void setData() {
        orgCode = SPUtil.getString(Constants.Preferences.LAST_LOGIN_ORG_ACCOUNT, "");
        userName = SPUtil.getString(Constants.Preferences.LAST_LOGIN_USER_ACCOUNT, "");
        mCompanyCode.setText(orgCode);
        mCompanyCode.setSelection(orgCode.length());
        mLoginName.setText(userName);
        mLoginName.setSelection(userName.length());

        String logo = SPUtil.getString(Constants.Preferences.LAST_LOGIN_ORG_LOGO, "");
        Glide.with(LoginActivity.this).load(logo).error(R.mipmap.ic_org_logo).fitCenter().into(mLogo);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mCompanyCode = (AutoCompleteTextView) findViewById(R.id.et_company_code);
        mLoginName = (AutoCompleteTextView) findViewById(R.id.et_login_name);
        mPwd = (EditText) findViewById(R.id.et_login_pwd);
        mSubmit = (Button) findViewById(R.id.btn_login);
        mHasQuestion = (TextView) findViewById(R.id.tv_has_question);

        mRecentCompany = (ImageView) findViewById(R.id.iv_expand_copany_code);
        mRecentLoginName = (ImageView) findViewById(R.id.iv_expand_login_name);
        mShowPwd = (CheckBox) findViewById(R.id.cb_show_pwd);
        mLogo = (ImageView) findViewById(R.id.iv_logo);

        mCompanyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String logo = orgDao.getOrgLogo(s.toString());
                Glide.with(LoginActivity.this).load(logo).error(R.mipmap.ic_org_logo).fitCenter().into(mLogo);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 设置点击事件
     */
    private void setClickListener() {
        mSubmit.setOnClickListener(this);
        mHasQuestion.setOnClickListener(this);
        mRecentCompany.setOnClickListener(this);
        mRecentLoginName.setOnClickListener(this);

        mShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPwd.requestFocus();
                showOrHidePwd(mPwd, isChecked);
            }
        });

    }

    @Override
    protected boolean setToolBar() {
        mTitle.setText(UIUtils.getString(R.string.login));
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                companyCode = mCompanyCode.getText().toString().trim();
                loginName = mLoginName.getText().toString().trim();
                password = mPwd.getText().toString().trim();

                if (checkData()) {
                    if (NetUtils.isNetworkConnected(this)) {
                        new LoginProtocol(new LoginParams(companyCode, loginName, SignUtils.md5Encode(password), Constants.SOURCE)) {
                            @Override
                            protected void onCallBack() {
                                readyGoThenKill(MainActivity.class);
                            }
                        };
                    } else {
                        UIUtils.showToast("当前无网络连接，请您打开网络连接");
                    }
                }
                break;
            case R.id.tv_has_question:
                list.clear();
                for (int i = 0; i < strs.length; i++) {
                    list.add(strs[i]);
                }
                dialog = new ActionSheetDialog(this).builder();
                dialog.setTitle("登录遇到问题")
                        .addSheetItem(list, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                readyGo(HelpActivity.class);
                            }
                        })
                        .setCancelable(false)
                        .show();

                break;
            case R.id.iv_expand_copany_code:
                mCompanyCode.requestFocus();
                createPopWindow(mCompanyCode, orgCodeNums);//创建PopupWindow
                break;
            case R.id.iv_expand_login_name:
                mLoginName.requestFocus();
                createPopWindow(mLoginName, userNameNums);//创建PopupWindow
                break;
        }


    }

    /**
     * 判断是否输入框为空
     *
     * @return true 不为空 false 为空
     */
    private boolean checkData() {
        if (StringUtils.isEmpty(companyCode)) {
            UIUtils.showToast("请输入公司代码");
            return false;
        }
        if (StringUtils.isEmpty(loginName)) {
            UIUtils.showToast("请输入用户名");
            return false;
        }
        if (StringUtils.isEmpty(password)) {
            UIUtils.showToast("请输入密码");
            return false;
        }


        return true;
    }

    /**
     * 是否显示密码（明文显示）
     *
     * @param editText
     */
    private void showOrHidePwd(EditText editText, boolean showPwd) {
        if (showPwd) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        editText.setSelection(editText.getText().toString().length());
    }


    /**
     * 创建popupwindow
     *
     * @param editText
     * @param datas
     */
    private void createPopWindow(final AutoCompleteTextView editText, final List<String> datas) {
        if (datas.size() <= 0) {
            return;
        }

        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.popup_window, null);
        popupWindow = new PopupWindow(contentView);
        ListView mListView = (ListView) contentView.findViewById(R.id.lv_content);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.remember_num, datas);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(datas.get(position));
                editText.setSelection(datas.get(position).length());
                if (editText.getId() == R.id.et_company_code) {
                    String logo = orgDao.getOrgLogo(datas.get(position));
                    Glide.with(LoginActivity.this).load(logo).error(R.mipmap.ic_org_logo).fitCenter().into(mLogo);
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(editText.getDropDownBackground());
        popupWindow.setFocusable(true);
        popupWindow.setWidth(editText.getWidth() - editText.getPaddingLeft() - editText.getPaddingRight());
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(editText, 0, editText.getDropDownVerticalOffset());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
