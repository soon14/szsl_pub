package com.app.tanklib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * 关键字容器，用于显示任意数量的关键词，特点是会自动换行并且在宽度方面充满父View
 */
public class KeywordContainer extends LinearLineWrapLayout implements View.OnClickListener{
    private OnClickKeywordListener onClickKeywordListener;
    private KeywordViewFactory keywordViewFactory;

    public KeywordContainer(Context context, AttributeSet attrs){
        super(context, attrs);
        setAdjustChildWidthWithParent(true);
    }

    public KeywordContainer(Context context) {
        super(context);
        setAdjustChildWidthWithParent(true);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() == null){
            throw new IllegalArgumentException("没有Tag");
        }

        if(!(v.getTag() instanceof Integer)){
            throw new IllegalArgumentException("Tag不是Integer, 请不要占用Tag，因为"+ KeywordContainer.class.getSimpleName()+"将在Tag中保存Keyword的索引");
        }

        if(onClickKeywordListener != null){
            onClickKeywordListener.onClickKeyword((Integer) v.getTag());
        }
    }

    /**
     * 设置关键字
     * @param keywords 关键字数组
     */
    public void setKeywords(String... keywords){
        if(keywordViewFactory == null){
            throw new IllegalStateException("你必须设置keywordViewFactory");
        }
        removeAllViews();
        TextView keywordTextView;
        for (int w = 0; w < keywords.length; w++) {
            keywordTextView = keywordViewFactory.makeKeywordView();
            if(keywordTextView == null){
                throw new IllegalArgumentException("KeywordViewFactory.makeKeywordView()不能返回null");
            }
            keywordTextView.setText(keywords[w]);
            keywordTextView.setTag(w);
            keywordTextView.setOnClickListener(this);
            addView(keywordTextView);
        }
        startLayoutAnimation();
    }

    public void setOnClickKeywordListener(OnClickKeywordListener onClickKeywordListener) {
        this.onClickKeywordListener = onClickKeywordListener;
    }

    public void setKeywordViewFactory(KeywordViewFactory keywordViewFactory) {
        this.keywordViewFactory = keywordViewFactory;
    }

    public interface OnClickKeywordListener {
        public void onClickKeyword(int position);
    }

    public interface KeywordViewFactory{
        public TextView makeKeywordView();
    }
}
