package com.chojima;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.jakewharton.scalpel.ScalpelFrameLayout;

public class ScalpelDrawer extends DrawerLayout {

    private ScalpelFrameLayout scalpelView;
    private Switch enabledSwitch;
    private CheckBox drawViews;

    public ScalpelDrawer(Context context) {
        super(context);
        init();
    }

    public ScalpelDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScalpelDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void wrapInside(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        View content = parent.getChildAt(0);
        parent.removeViewAt(0);
        scalpelView.addView(content);
        parent.addView(this);
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.scalpel_drawer, null));
        scalpelView = (ScalpelFrameLayout) findViewById(R.id.scalpel);
        enabledSwitch = (Switch) findViewById(R.id.scalpel_enabled);
        drawViews = (CheckBox) findViewById(R.id.scalpel_draw_views);

        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelView.setLayerInteractionEnabled(isChecked);
                drawViews.setEnabled(isChecked);
            }
        });

        drawViews.setChecked(true);
        drawViews.setEnabled(false);
        drawViews.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelView.setDrawViews(isChecked);
            }
        });
    }

}