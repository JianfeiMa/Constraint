package com.example.constraintlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.constraintlayout.adapter.ArrayOperatorConstraintAdapter;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;
import com.example.constraintlayout.adapter.ListOperatorConstraintAdapter;
import com.example.constraintlayout.simple.ViewOperator;

import java.util.List;

/**
 * Created by LiuJin on 2018-04-03:9:53
 *
 * @author wuxio
 */
public class ConstraintLayout extends ViewGroup implements ConstraintSupport {

    private static final String TAG = "ConstraintLayout";

    /**
     * adapter 用于使用约束布局
     */
    private BaseConstraintAdapter mAdapter;

    /**
     * 约束
     */
    private Constraint mConstraint;

    /**
     * 用于决定是否约束是否非法,当wrap_content时,右边约束或者底部约束不可用
     */
    private int mParentRight;
    private int mParentBottom;


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

        /* 初始化一个约束,用来复用 */

        mConstraint = new Constraint(this);
    }


    /**
     * @return 一个空约束, 复用已有的
     */
    public Constraint obtainConstraint() {

        mConstraint.init();
        return mConstraint;
    }


    /**
     * @return new 新创建一个,推荐使用{@link #obtainConstraint()}获取约束
     */
    public Constraint newConstraint() {

        Constraint constraint = new Constraint(this);
        constraint.init();
        return constraint;
    }


    /**
     * 设置adapter
     *
     * @param adapter 用来布局的adapter
     */
    public void setAdapter(BaseConstraintAdapter adapter) {

        if (mAdapter != null) {
            mAdapter = adapter;
            requestLayout();
        } else {

            mAdapter = adapter;
        }
    }


    public BaseConstraintAdapter getAdapter() {

        return mAdapter;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthFromParent = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightFromParent = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /* 判断右边和底边约束是否可用 */

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

        /* 用adapter提供的约束测量view,并且设置位置信息给view的layoutP啊让魔术,在之后的onLayout中可以直接布局简化操作 */

        BaseConstraintAdapter adapter = mAdapter;

        final int childCount = adapter.getChildCount();
        int mostRight = 0;
        int mostBottom = 0;

        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            if (child == null) {

                child = adapter.generateViewTo(i);
                addView(child);
            }

            /* 1. 先测量 */

            Constraint constraint = adapter.generateConstraintTo(i, obtainConstraint());
            constraint.check();
            int widthSpec = constraint.makeWidthSpec();
            int heightSpec = constraint.makeHeightSpec();
            measureChild(child,
                    widthSpec,
                    heightSpec
            );

            /* 2. 记录测量之后该view的位置 */

            LayoutParams params = setChildLayoutParams(constraint, child);

            /* 记录最右边最下边已经使用到的尺寸,用于之后设置自己的尺寸 */

            if (params.right > mostRight) {
                mostRight = params.right;
            }
            if (params.bottom > mostBottom) {
                mostBottom = params.bottom;
            }
        }

        /* 根据模式设置尺寸信息 */

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

        setMeasuredDimension(width, height);
    }


    /**
     * 在{@link #onMeasure(int, int)}中调用,用来设置位置信息
     * used for set layout info
     *
     * @param constraint child's constraint
     * @param child      child in group
     * @return child's layoutParams with layout Info
     */
    private LayoutParams setChildLayoutParams(Constraint constraint, View child) {

        final int minBias = 0;

        LayoutParams params = getChildLayoutParams(child);

        /* 读取约束信息 */

        int constraintLeft = constraint.left;
        int constraintTop = constraint.top;
        int constraintRight = constraint.right;
        int constraintBottom = constraint.bottom;

        /* 根据水平偏移比调整 left right */

        if (constraint.horizontalBias == minBias) {

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

        /* 根据垂直偏移比调整 top bottom */

        if (constraint.verticalBias == minBias) {

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

        BaseConstraintAdapter adapter = mAdapter;
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

    /* 工具方法简化创建操作 */


    public void setUpWith(ViewOperator[] viewOperators) {

        setAdapter(new ArrayOperatorConstraintAdapter(viewOperators));
    }


    public void setUpWith(List< ViewOperator > viewOperators) {

        setAdapter(new ListOperatorConstraintAdapter(viewOperators));
    }

    //============================add view============================

    /**
     * 标记,添加删除 ExtraView 时,不重新测量布局,因为其他的view布局位置是确定的,不需要重新布局
     */
    private boolean addOrRemoveExtraView = false;


    /**
     * 额外添加一个view,通常是需要弹窗的情况使用
     *
     * @param view       额外的view
     * @param constraint 对view的约束
     */
    public void addExtraView(View view, Constraint constraint) {

        addExtraView(view, generateDefaultLayoutParams(), constraint);
    }


    /**
     * 额外添加一个view,通常是需要弹窗的情况使用
     *
     * @param view         额外的view
     * @param layoutParams 该view布局参数
     * @param constraint   对view的约束
     */
    public void addExtraView(View view, LayoutParams layoutParams, Constraint constraint) {

        addOrRemoveExtraView = true;
        addView(view, layoutParams);

        int widthSpec = constraint.makeWidthSpec();
        int heightSpec = constraint.makeHeightSpec();
        measureChild(view,
                widthSpec,
                heightSpec
        );
        LayoutParams params = setChildLayoutParams(constraint, view);

        view.layout(params.left, params.top, params.right, params.bottom);

        addOrRemoveExtraView = false;
    }


    /**
     * 移除额外添加的view,该方法没有判断移除的是否是{@link #addExtraView(View, Constraint)}添加的view
     *
     * @param view 一个view
     */
    public void removeExtraView(View view) {

        addOrRemoveExtraView = true;
        removeView(view);
        addOrRemoveExtraView = false;
    }


    public void setToRelayout(boolean isToRelayout) {

    }


    @Override
    public void requestLayout() {

        if (addOrRemoveExtraView) {
            return;
        }

        super.requestLayout();
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


        /**
         * 这几个变量记录view的布局位置,用来在{@link #onLayout(boolean, int, int, int, int)}中直接布局
         */
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
            return -1;
        } else {
            return mParentRight - getPaddingRight();
        }
    }


    @Override
    public int getParentBottom() {

        if (mParentBottom == -1) {
            return -1;
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
