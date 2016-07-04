package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


public class UserScoreBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    public UserScoreBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout  + 150);child, View dependency) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        child.setY(dependency.getY());
        layoutParams.height = (int) (dependency.getY() * .227
        dependency.setPadding(dependency.getPaddingLeft(), layoutParams.height, dependency.getPaddingRight(), dependency.getPaddingBottom());
        return true;
    }
}