package com.tophawks.vm.visualmerchandising;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class ExpandAnimation extends Animation {

    private View mAnimatedView;
    private LinearLayout.LayoutParams mViewLayoutParams;
    public int mMarginStart, mMarginEnd;
    private boolean mIsVisibleAfter = false;
    private boolean mWasEndedAlready = false;
    //private ImageView mImageView;

    public ExpandAnimation(View view, int duration) {
        setDuration(duration);
        //mImageView = imageView;
        mAnimatedView = view;
        mViewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        mMarginStart = mMarginEnd = 0;
        // decide to show or hide the view
        mIsVisibleAfter = (view.getVisibility() == View.VISIBLE);
        //mIsVisibleAfter = (mViewLayoutParams.bottomMargin == 0);
        mMarginStart = mViewLayoutParams.bottomMargin;
        if(mMarginStart != 0)
            mMarginStart = 0 - view.getHeight();
        mMarginEnd = (mMarginStart == 0 ? (0 - view.getHeight()) : 0);

        view.setVisibility(View.VISIBLE);
        //mImageView.setImageResource(R.drawable.available_product);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            // Calculating the new bottom margin, and setting it
            mViewLayoutParams.bottomMargin = mMarginStart + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);
            // Invalidating the layout, making us seeing the changes we made
            mAnimatedView.requestLayout();
            // Making sure we didn't run the ending before (it happens!)
        } else if (!mWasEndedAlready) {
            mViewLayoutParams.bottomMargin = mMarginEnd;
            mAnimatedView.requestLayout();
            if(mMarginEnd != 0) {
                mAnimatedView.setVisibility(View.GONE);
                //mImageView.setImageResource(R.drawable.available_product);
            }
            mWasEndedAlready = true;
        }
    }

}
