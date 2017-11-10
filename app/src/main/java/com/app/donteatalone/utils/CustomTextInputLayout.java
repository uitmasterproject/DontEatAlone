package com.app.donteatalone.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.widget.EditText;

import com.app.donteatalone.R;


/**
 * Created by ChomChom on 16-Oct-17
 */

public class CustomTextInputLayout extends TextInputLayout {

    public CustomTextInputLayout(Context context) {
        super(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView);
        String customFont = typedArray.getString(R.styleable.MyCustomView_customFont);
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), customFont);
            setTypeface(tf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        typedArray.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        replaceBackground();
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        super.setError(error);
        replaceBackground();
    }

    private void replaceBackground() {
        EditText editText = getEditText();
        if (editText != null) {
            editText.getBackground().clearColorFilter();
        }
    }
}
