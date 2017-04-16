package com.example.uberv.a8_1addingnativebitswithjni;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * A view designed to measure out its height to be 85 percent of the height of its parent
 * This allows us to use HeaderView as a measured spacer, even though it contains no real content
 * This approach is more flexible to device screen differences than hard-coding a fixed view height
 */
public class HeaderView extends View {

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
    * Measure this view's height to always be 85% of the
    * measured height from the parent view (ListView)
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View parent = (View) getParent();
        int parentHeight = parent.getMeasuredHeight();
        int height = Math.round(parentHeight * 0.85f);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
