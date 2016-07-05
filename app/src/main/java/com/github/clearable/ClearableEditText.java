package com.github.clearable;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 带有删除按钮的EditText
 * Created by 14110105 on 2016-06-30.
 */
public class ClearableEditText extends EditText
        implements EditText.OnFocusChangeListener {

    public static final String TAG = "ClearableEditText";

    public ClearableEditText(Context context) {
       this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private Drawable mClearDrawable;
    /**
     * Right Drawable 是否可见
     */
    private boolean mIsClearVisible;
    /**
     * 是否正在显示Error
     */
    private boolean mErrorShowing;

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {

        Drawable drawables[] = getCompoundDrawables();
        mClearDrawable = drawables[2]; // Right Drawable;

        final Resources.Theme theme = context.getTheme();

        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.ClearableEditText,
                defStyleAttr, defStyleRes);

        int rightDrawableColor = a.getColor(R.styleable.ClearableEditText_right_drawable_color,
                Color.BLACK);

        a.recycle();

        // 给mRightDrawable上色
        DrawableCompat.setTint(mClearDrawable, rightDrawableColor);

        setOnFocusChangeListener(this);

        // 添加TextChangedListener
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged " + s);

                setClearDrawableVisible(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 第一次隐藏
        setClearDrawableVisible(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // error drawable 不显示 && clear drawable 显示 && action up
        if (!mErrorShowing && mIsClearVisible && event.getAction() == MotionEvent.ACTION_UP) {

            float x = event.getX();
            if (x >= getWidth() - getTotalPaddingRight() && x <= getWidth() - getPaddingRight()) {
                Log.d(TAG, "点击清除按钮！");

                clearText();

                return true; // 消耗event
            }

        }

        return super.onTouchEvent(event);
    }


    /**
     * 清空输入框
     */
    private void clearText() {
        if (getText().length() > 0) {
            setText("");
        }
    }


    /**
     * 设置Right Drawable是否可见
     *
     * @param isVisible true for visible , false for invisible
     */
    public void setClearDrawableVisible(boolean isVisible) {

        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                isVisible ? mClearDrawable : null, getCompoundDrawables()[3]);

        mIsClearVisible = isVisible;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "getTotalPaddingTop = " + getTotalPaddingTop());
        Log.d(TAG, "getExtendedPaddingTop = " + getExtendedPaddingTop());

        // error drawable 不显示的时候
        if (!mErrorShowing) {
            if (hasFocus) {
                if (getText().length() > 0) {
                    setClearDrawableVisible(true);
                }
            } else {
                setClearDrawableVisible(false);
            }
        }
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        super.setError(error, icon);
        // 如果error != null 代表错误提示正在显示，所以要隐藏mClearingDrawable
        mErrorShowing = error != null;
    }
}
