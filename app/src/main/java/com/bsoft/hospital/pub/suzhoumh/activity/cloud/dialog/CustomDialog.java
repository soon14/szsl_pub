package com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.util.SizeUtils;
import com.bsoft.hospital.pub.suzhoumh.view.RoundTextView;

public class CustomDialog extends Dialog implements DialogInterface {

    private CustomDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    public static class Builder {
        private Context mContext;

        private View mContentView;
        private String mContent;
        private String mNegativeText;
        private String mPositiveText;

        private boolean mCancelable = true;

        private OnClickListener mPositiveClickListener;
        private OnClickListener mNegativeClickListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setContent(String content) {
            this.mContent = content;
            return this;
        }

        public Builder setNegativeButton(String negativeText, OnClickListener negativeClickListener) {
            this.mNegativeText = negativeText;
            this.mNegativeClickListener = negativeClickListener;
            return this;
        }

        public Builder setPositiveButton(String positiveText, OnClickListener positiveClickListener) {
            this.mPositiveText = positiveText;
            this.mPositiveClickListener = positiveClickListener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(mContext);
            mContentView = View.inflate(mContext, R.layout.dialog_custom, null);
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    SizeUtils.dp2px(300), LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(mContentView, layoutParams);

            TextView contentTv = mContentView.findViewById(R.id.tv_content);
            RoundTextView negativeTv = mContentView.findViewById(R.id.tv_negative);
            ImageView dividerIv = mContentView.findViewById(R.id.iv_divider);
            RoundTextView positiveTv = mContentView.findViewById(R.id.tv_positive);

            Bitmap bitmap = Bitmap.createBitmap(SizeUtils.dp2px(60), SizeUtils.dp2px(60),
                    Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(ContextCompat.getColor(mContext, R.color.white));
            contentTv.setText(mContent);
            positiveTv.setText(mPositiveText);
            if (null != mNegativeClickListener) {
                negativeTv.setText(mNegativeText);
                negativeTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNegativeClickListener.onClick(dialog, BUTTON_NEGATIVE);
                    }
                });
            } else {
                negativeTv.setVisibility(View.GONE);
                dividerIv.setVisibility(View.GONE);
                positiveTv.getDelegate().setCornerRadius_BL(SizeUtils.dp2px(6));
            }
            if (null != mPositiveClickListener) {
                positiveTv.setText(mPositiveText);
                positiveTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPositiveClickListener.onClick(dialog, BUTTON_POSITIVE);
                    }
                });
            } else {
                positiveTv.setVisibility(View.GONE);
                dividerIv.setVisibility(View.GONE);
                negativeTv.getDelegate().setCornerRadius_BR(SizeUtils.dp2px(6));
            }

            dialog.setCancelable(mCancelable);

            return dialog;
        }

        public CustomDialog show() {
            final CustomDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
