package com.kanj.apps.hybridtextimageviews;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by naraykan on 06/02/17.
 */

public class MaxHeightScrollView extends NestedScrollView {
    private int mMaxHeight;

    public MaxHeightScrollView(Context context) {
        super(context);
        mMaxHeight = context.getResources().getDimensionPixelSize(R.dimen.text_image_input_max_height);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMaxHeight = context.getResources().getDimensionPixelSize(R.dimen.text_image_input_max_height);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaxHeight = context.getResources().getDimensionPixelSize(R.dimen.text_image_input_max_height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (heightSize > mMaxHeight) {
                heightSize = mMaxHeight;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            getLayoutParams().height = heightSize;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
