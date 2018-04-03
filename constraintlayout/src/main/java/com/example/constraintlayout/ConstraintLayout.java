package com.example.constraintlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LiuJin on 2018-04-03:9:53
 *
 * @author wuxio
 */
public class ConstraintLayout < T extends BaseAdapter > extends ViewGroup implements ConstraintSupport {

    private static final String TAG = "ConstraintLayout";

    private T mAdapter;

    private Constraint mConstraint;

    private int mParentRight;
    private int mParentBottom;

    private boolean isOnMeasure;


    public ConstraintLayout(Context context) {

        this(context, null, 0);
    }


    public ConstraintLayout(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }


    public ConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        mConstraint = new Constraint(this);
    }


    /**
     * @return 一个空约束
     */
    public Constraint obtainConstraint() {

        mConstraint.init();
        return mConstraint;
    }


    public void setAdapter(T adapter) {

        mAdapter = adapter;
    }


    public T getAdapter() {

        return mAdapter;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthFromParent = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightFromParent = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mParentRight = widthFromParent;
        } else {
            mParentRight = -1;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mParentBottom = heightFromParent;
        } else {
            mParentBottom = -1;
        }

        T adapter = mAdapter;
        final int childCount = adapter.getChildCount();

        int mostRight = 0;
        int mostBottom = 0;

        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            if (child == null) {

                child = adapter.generateViewTo(i);
                addView(child);
            }

            Constraint constraint = adapter.generateConstraintTo(i, obtainConstraint());
            constraint.isLegal();
            int widthSpec = constraint.makeWidthSpec();
            int heightSpec = constraint.makeHeightSpec();
            measureChild(child,
                    widthSpec,
                    heightSpec
            );
            LayoutParams params = setChildLayoutParams(constraint, child);

            if (params.right > mostRight) {
                mostRight = params.right;
            }
            if (params.bottom > mostBottom) {
                mostBottom = params.bottom;
            }

            Log.i(TAG, "onMeasure:" + i + " " + constraint);
            Log.i(TAG, "onMeasure:" + i + " " + params);
        }

        Log.i(TAG, "onMeasure:most: " + mostRight + " " + mostBottom);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthFromParent;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(mostRight, widthFromParent);
        } else {
            width = mostRight;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightFromParent;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(mostBottom, heightFromParent);
        } else {
            height = mostBottom;
        }

        Log.i(TAG, "onMeasure:size final:" + width + " " + height);

        setMeasuredDimension(width, height);
    }


    /**
     * used for set layout info
     *
     * @param constraint child's constraint
     * @param child      child in group
     * @return child's layoutParams with layout Info
     */
    private LayoutParams setChildLayoutParams(Constraint constraint, View child) {

        final int min = 0;

        LayoutParams params = getChildLayoutParams(child);

        int constraintLeft = constraint.left;
        int constraintTop = constraint.top;
        int constraintRight = constraint.right;
        int constraintBottom = constraint.bottom;

        if (constraint.horizontalBias == min) {

            params.left = constraintLeft;
            params.right = params.left + child.getMeasuredWidth();
        } else {

            /* have horizontal offset */

            int childMeasuredWidth = child.getMeasuredWidth();
            int constraintWidth = constraintRight - constraintLeft;

            int extraSpace = constraintWidth - childMeasuredWidth;

            if (extraSpace > 0) {

                float offset = constraint.horizontalBias * extraSpace;
                params.left = (int) (constraintLeft + offset) + 1;
                params.right = params.left + child.getMeasuredWidth();

            } else {
                params.left = constraintLeft;
                params.right = params.left + child.getMeasuredWidth();
            }
        }

        if (constraint.verticalBias == min) {

            params.top = constraintTop;
            params.bottom = params.top + child.getMeasuredHeight();
        } else {

            /* have vertical offset */

            int childMeasuredHeight = child.getMeasuredHeight();
            int constraintHeight = constraintBottom - constraintTop;

            int extraSpace = constraintHeight - childMeasuredHeight;

            if (extraSpace > 0) {

                float offset = constraint.verticalBias * extraSpace;
                params.top = (int) (constraintTop + offset) + 1;
                params.bottom = params.top + childMeasuredHeight;

            } else {
                params.top = constraintTop;
                params.bottom = params.top + child.getMeasuredHeight();
            }
        }

        return params;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        T adapter = mAdapter;
        int count = adapter.getChildCount();
        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams params = getChildLayoutParams(child);
            adapter.beforeLayout(i, child);
            child.layout(params.left, params.top, params.right, params.bottom);
            adapter.afterLayout(i, child);
        }

    }

    //============================Layout Params============================


    @Override
    public ViewGroup.LayoutParams getLayoutParams() {

        return super.getLayoutParams();
    }


    private LayoutParams getChildLayoutParams(int position) {

        View child = getChildAt(position);
        return ((LayoutParams) child.getLayoutParams());
    }


    private LayoutParams getChildLayoutParams(View view) {

        return ((LayoutParams) view.getLayoutParams());
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {

        return new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {

        return new LayoutParams(getContext(), attrs);
    }


    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {

        return new LayoutParams(p);
    }


    /**
     * why not extends MarginLayoutParams because constraint is contain margin
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {

        int left;
        int top;
        int right;
        int bottom;


        @Override
        public String toString() {

            return "LayoutParams{" +
                    "left=" + left +
                    ", top=" + top +
                    ", right=" + right +
                    ", bottom=" + bottom +
                    '}';
        }


        public int getLeft() {

            return left;
        }


        public int getTop() {

            return top;
        }


        public int getRight() {

            return right;
        }


        public int getBottom() {

            return bottom;
        }


        public LayoutParams(Context c, AttributeSet attrs) {

            super(c, attrs);
        }


        public LayoutParams(int width, int height) {

            super(width, height);
        }


        public LayoutParams(MarginLayoutParams source) {

            super(source);
        }


        public LayoutParams(ViewGroup.LayoutParams source) {

            super(source);
        }

    }

    //============================constraint support============================


    @Override
    public int getParentLeft() {

        return getPaddingLeft();
    }


    @Override
    public int getParentTop() {

        return getPaddingTop();
    }


    @Override
    public int getParentRight() {

        if (mParentRight == -1) {
            return mParentRight;
        } else {
            return mParentRight - getPaddingRight();
        }
    }


    @Override
    public int getParentBottom() {

        if (mParentBottom == -1) {
            return mParentBottom;
        } else {
            return mParentBottom - getPaddingBottom();
        }
    }


    @Override
    public int getViewLeft(int position) {

        return getChildLayoutParams(position).left;
    }


    @Override
    public int getViewTop(int position) {

        return getChildLayoutParams(position).top;
    }


    @Override
    public int getViewRight(int position) {

        return getChildLayoutParams(position).right;
    }


    @Override
    public int getViewBottom(int position) {

        return getChildLayoutParams(position).bottom;
    }
}
