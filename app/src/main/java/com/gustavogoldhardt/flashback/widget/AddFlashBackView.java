package com.gustavogoldhardt.flashback.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.gustavogoldhardt.flashback.R;

/**
 * Created by gustavogoldhardt on 12/24/14.
 */
public class AddFlashBackView extends CardView {

    public AddFlashBackView(Context context, ViewGroup viewGroup) {
        super(context);
        init(context, viewGroup);
    }

    public AddFlashBackView(Context context, AttributeSet attrs, ViewGroup viewGroup) {
        super(context, attrs);
        init(context, viewGroup);
    }

    public AddFlashBackView(Context context, AttributeSet attrs, int defStyleAttr, ViewGroup viewGroup) {
        super(context, attrs, defStyleAttr);
        init(context, viewGroup);
    }

    private void init(Context context, ViewGroup viewGroup) {
        inflate(context, R.layout.add_flashback, this);
    }
}
