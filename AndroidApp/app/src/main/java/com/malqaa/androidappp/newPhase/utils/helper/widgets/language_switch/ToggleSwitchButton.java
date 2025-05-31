package com.malqaa.androidappp.newPhase.utils.helper.widgets.language_switch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.malqaa.androidappp.R;


/**
 * Created by lorenzorigato on 4/1/16.
 */
public class ToggleSwitchButton{

    private View view;
    private TextView textView;
    private View separator;

    public ToggleSwitchButton(Context context) {
        this(LayoutInflater.from(context).inflate(R.layout.item_widget_toggle_switch, null));
    }

    public ToggleSwitchButton(View view) {
        this.view = view;
        this.textView = (TextView) view.findViewById(R.id.text_view);
        this.separator = view.findViewById(R.id.separator);
    }

    public View getView() {
        return view;
    }

    public TextView getTextView() {
        return textView;
    }

    public View getSeparator() {
        return separator;
    }

    public void showSeparator(){
        getSeparator().setVisibility(View.VISIBLE);
    }

    public void hideSeparator(){
        getSeparator().setVisibility(View.INVISIBLE);
    }
}
