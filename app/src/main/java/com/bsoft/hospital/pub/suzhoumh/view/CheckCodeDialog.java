package com.bsoft.hospital.pub.suzhoumh.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.R;

/**
 * 短信验证Dialog
 * <p>
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-8-15 8:53
 */
public class CheckCodeDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private TextView titleView;
    private ImageView closeView;
    private EditText codeText;
    private TextView requestView;
    private TextView confirmView;
    private TextView hintView;

    private String titleText;
    private String confirmText;
    private String hintText;

    private CountDownHelper countDownHelper;
    private int time = 60;
    private int interval = 1;

    public CheckCodeDialog(Context context) {
        super(context, R.style.alertDialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check);
//        getWindow().setBackgroundDrawable(new BitmapDrawable());
        setCancelable(false);

        findView();
        initView();
    }

    private void initView() {
        if (!StringUtil.isEmpty(titleText)) {
            titleView.setText(titleText);
        }
        if (!StringUtil.isEmpty(confirmText)) {
            confirmView.setText(confirmText);
        }
        if (!StringUtil.isEmpty(hintText)) {
            hintView.setText(hintText);
        }

        codeText.setInputType(InputType.TYPE_CLASS_NUMBER);

        closeView.setOnClickListener(this);
        requestView.setOnClickListener(this);
        confirmView.setOnClickListener(this);

        codeText.addTextChangedListener(mTextWatcher);

        setConfirmUnclickable();

        countDownHelper = new CountDownHelper(time, interval);
        countDownHelper.setCountDownListener(new CountDownHelper.CountDownListener() {
            @Override
            public void onCounting(long time) {
                if (isShowing()) {
                    setRequestViewText("（" + time + "）秒");
                    setRequestUnclickable();
                }
            }

            @Override
            public void onFinish() {
                if (isShowing()) {
                    setRequestViewText("获取验证码");
                    setRequestClickable();
                }
            }
        });
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                setConfirmUnclickable();
            } else {
                setConfirmClickable();
            }
        }
    };

    private void findView() {
        titleView = (TextView) findViewById(R.id.tv_title);
        closeView = (ImageView) findViewById(R.id.iv_close);
        codeText = (EditText) findViewById(R.id.et_code);
        requestView = (TextView) findViewById(R.id.tv_request);
        confirmView = (TextView) findViewById(R.id.tv_confirm);
        hintView = (TextView) findViewById(R.id.tv_hint);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                close();
                break;
            case R.id.tv_request:
                if (checkCodeListener != null) {
                    checkCodeListener.onRequest();
                    countDownHelper.start();
                }
                break;
            case R.id.tv_confirm:
                if (checkCodeListener != null) {
                    if (StringUtil.isEmpty(codeText.getText().toString())) {
                        checkCodeListener.onEmpty();
                    } else {
                        hideInput();
                        checkCodeListener.onSubmit();
                    }
                }
                break;
        }
    }

    public interface CheckCodeListener {

        void onRequest();

        void onSubmit();

        void onEmpty();
    }

    private CheckCodeListener checkCodeListener;

    public void setCheckCodeListener(CheckCodeListener checkCodeListener) {
        this.checkCodeListener = checkCodeListener;
    }

    public void setRequestViewText(String text) {
        requestView.setText(text);
    }

    public void setRequestClickable() {
        requestView.setClickable(true);
        requestView.setTextColor(context.getResources().getColor(R.color.blue));
    }

    public void setRequestUnclickable() {
        requestView.setClickable(false);
        requestView.setTextColor(context.getResources().getColor(R.color.hint_text));
    }

    public void setConfirmClickable() {
        confirmView.setClickable(true);
        confirmView.setBackgroundColor(context.getResources().getColor(R.color.blue));
    }

    public void setConfirmUnclickable() {
        confirmView.setClickable(false);
        confirmView.setBackgroundColor(context.getResources().getColor(R.color.hint_text));
    }

    public String getCodeEditText() {
        return codeText.getText().toString();
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public void close() {
        hideInput();
        dismiss();
    }

    private void hideInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(codeText.getWindowToken(), 0);
    }
}
