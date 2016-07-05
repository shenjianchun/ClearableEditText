package com.github.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 带有显示密码按钮的EditText
 * Created by JC on 2016-06-30.
 */
public class PasswordEditText extends EditText implements EditText.OnFocusChangeListener {

    public static final String TAG = "ClearableEditText";

    public PasswordEditText(Context context) {
        super(context);
        init(context, null, 0 , 0);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0 , 0);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private Drawable mRightDrawable;
    /**
     * Right Drawable 是否可见
     */
    private boolean mIsVisible;

    private boolean mIsShown = false;

    private int mDefaultInputType;

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {

        if (mRightDrawable == null) {
            Drawable drawables[] = getCompoundDrawables();
            mRightDrawable = drawables[2]; // Right Drawable;
        }

        final Resources.Theme theme = context.getTheme();

        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.ClearableEditText,
                defStyleAttr, defStyleRes);

        int rightDrawableColor = a.getColor(R.styleable.ClearableEditText_right_drawable_color, Color.BLACK);

        a.recycle();

        DrawableCompat.setTint(mRightDrawable, rightDrawableColor);

        setOnFocusChangeListener(this);

        mDefaultInputType = getInputType();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mIsVisible && event.getAction() == MotionEvent.ACTION_UP) {

            float x = event.getX();
            if (x >= getWidth() - getTotalPaddingRight() && x <= getWidth() - getPaddingRight()) {
                Log.d(TAG, "点击密码按钮！");

                showPassword(!mIsShown);

                return true; // 消耗event
            }

        }

        return super.onTouchEvent(event);
    }


    /**
     * 清空输入框
     */
    private void showPassword(boolean isShown) {

        int inputType = mDefaultInputType;
        if (isShown) {
            inputType |= InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        }

        setInputType(inputType);

        mIsShown = isShown;
    }


    /**
     * 设置Right Drawable是否可见
     *
     * @param isVisible true for visible , false for invisible
     */
    public void setDrawableVisible(boolean isVisible) {

        if (mRightDrawable == null) {
            mRightDrawable = getCompoundDrawables()[2];
        }

        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                isVisible ? mRightDrawable : null, getCompoundDrawables()[3]);

        mIsVisible = isVisible;

        Log.d(TAG, "getHeight:" + getHeight());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "getTotalPaddingTop = " + getTotalPaddingTop());
        Log.d(TAG, "getExtendedPaddingTop = " + getExtendedPaddingTop());

        if (hasFocus) {
            if (getText().length() > 0) {
                setDrawableVisible(true);
            }
        } else {
            setDrawableVisible(false);
        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        Log.d(TAG, "onTextChanged " + text);
        setDrawableVisible(text.length() > 0);
    }
}
