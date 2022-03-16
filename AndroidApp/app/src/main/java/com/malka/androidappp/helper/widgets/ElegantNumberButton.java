package com.malka.androidappp.helper.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.malka.androidappp.R;

public class ElegantNumberButton extends RelativeLayout {
    private Context context;
    private AttributeSet attrs;
    private int styleAttr;
    private OnClickListener mListener;
    private int initialNumber;
    private int lastNumber;
    private int currentNumber;
    private int finalNumber;
    private TextView textView;
    private LinearLayout addBtn, subtractBtn;
    private OnValueChangeListener
            mOnValueChangeListener;

    public ElegantNumberButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        inflate(context, R.layout.elegantnumberbutton_design, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ElegantNumberButton, styleAttr, 0);

        initialNumber = a.getInt(R.styleable.ElegantNumberButton_initialNumber, 1);
        finalNumber = a.getInt(R.styleable.ElegantNumberButton_finalNumber, Integer.MAX_VALUE);

        subtractBtn =  findViewById(R.id.subtract_btn);
        addBtn =  findViewById(R.id.add_btn);
        textView = findViewById(R.id.number_counter);



        textView.setText(String.valueOf(initialNumber));

        currentNumber = initialNumber;
        lastNumber = initialNumber;

        subtractBtn.setOnClickListener(mView -> {
            int num = Integer.valueOf(textView.getText().toString());
            if (num != 1) {
                setNumber(String.valueOf(num - 1), true);

            }
        });
        addBtn.setOnClickListener(mView -> {
            int num = Integer.valueOf(textView.getText().toString());
            setNumber(String.valueOf(num + 1), true);
        });
        a.recycle();
    }

    private void callListener(View view) {
        if (mListener != null) {
            mListener.onClick(view);
        }

        if (mOnValueChangeListener != null) {
            if (lastNumber != currentNumber) {
                mOnValueChangeListener.onValueChange(this, lastNumber, currentNumber);
            }
        }
    }


    public void setNumber(String number) {
        lastNumber = currentNumber;
        this.currentNumber = Integer.parseInt(number);
        if (this.currentNumber > finalNumber) {
            this.currentNumber = finalNumber;
        }
        if (this.currentNumber < initialNumber) {
            this.currentNumber = initialNumber;
        }
        textView.setText(String.valueOf(currentNumber));
    }

    public void setNumber(String number, boolean notifyListener) {
        setNumber(number);
        if (notifyListener) {
            callListener(this);
        }
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    public String getNumber() {
        return String.valueOf(currentNumber);
    }

    public interface OnClickListener {

        void onClick(View view);

    }

    public void setRange(Integer startingNumber, Integer endingNumber) {
        this.initialNumber = startingNumber;
        this.finalNumber = endingNumber;
    }

    public interface OnValueChangeListener {
        void onValueChange(ElegantNumberButton view, int oldValue, int newValue);
    }

}
