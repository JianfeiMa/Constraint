package com.example.constraintlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.constraintlayout.adapter.ArrayAdapter;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;
import com.example.constraintlayout.adapter.ListAdapter;
import com.example.constraintlayout.simple.ConstraintOperator;

import java.util.List;

/**
 * Created by LiuJin on 2018-04-03:9:53
 *
 * @author wuxio
 */
public class ConstraintLayout extends ViewGroup implements ConstraintSupport {

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

    //============================ 设置Adapter ============================


    /**
     * 设置adapter
     *
     * @param adapter 用来布局的adapter
     */
    public void setAdapter(BaseConstraintAdapter adapter) {

        if (mAdapter != null) {
            mAdapter = adapter;
            super.requestLayout();

        } else {

            mAdapter = adapter;
        }
    }


    public BaseConstraintAdapter getAdapter() {

        return mAdapter;
    }


    /**
     * 使用数组创建一个布局
     */
    public void setUpWith(ConstraintOperator[] constraintOperators) {

        setAdapter(new ArrayAdapter(constraintOperators));
    }


    /**
     * 使用list创建一个布局
     */
    public void setUpWith(List< ConstraintOperator > constraintOperators) {

        setAdapter(new ListAdapter(constraintOperators));
    }

    //============================测量布局过程============================


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mAdapter == null) {
            setMeasuredDimension(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec)
            );
            return;
        }

        mAdapter.onStartConstraintChildren(this);

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
                LayoutParams params = adapter.generateLayoutParamsTo(i, child);
                addView(child, params);
            }

            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams params = measureViewWithConstraint(adapter, i, child);

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
     * 记录测量之后view的布局位置,简化{@link #onLayout(boolean, int, int, int, int)}操作
     */
    private LayoutParams measureViewWithConstraint(BaseConstraintAdapter adapter, int position, View child) {

        /* 1. 先测量 */

        Constraint constraint = adapter.generateConstraintTo(position, obtainConstraint(), child);

        constraint.check(child, position);

        int widthSpec = constraint.makeWidthSpec(child);
        int heightSpec = constraint.makeHeightSpec(child);

        adapter.beforeMeasure(position, child);
        measureChild(child,
                widthSpec,
                heightSpec
        );
        adapter.afterMeasure(position, child);

        /* 2. 记录测量之后该view的位置 */

        return setChildLayoutParams(constraint, child);
    }


    /**
     * 使用约束测量单个view,该方法用于更新约束
     */
    private void measureViewWithConstraint(int position,
                                           View child,
                                           Constraint constraint) {

        /* 1. 先测量 */

        constraint.check(child, position);

        int widthSpec = constraint.makeWidthSpec(child);
        int heightSpec = constraint.makeHeightSpec(child);

        measureChild(child,
                widthSpec,
                heightSpec
        );

        /* 2. 记录测量之后该view的位置 */

        setChildLayoutParams(constraint, child);
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

            if (constraint.isLeftConstraint()) {
                params.left = constraintLeft;
                params.right = constraintLeft + child.getMeasuredWidth();
            } else {
                params.left = constraintRight - child.getMeasuredWidth();
                params.right = constraintRight;
            }

        } else {

            /* have horizontal offset */

            int childMeasuredWidth = child.getMeasuredWidth();
            int constraintWidth = constraintRight - constraintLeft;

            int extraSpace = constraintWidth - childMeasuredWidth;

            if (extraSpace > 0) {

                if (constraint.isLeftConstraint()) {

                    float offset = constraint.horizontalBias * extraSpace;
                    params.left = (int) (constraintLeft + offset) + 1;
                    params.right = params.left + child.getMeasuredWidth();

                } else {

                    float offset = (1 - constraint.horizontalBias) * extraSpace;
                    params.right = (int) (constraintRight - offset) + 1;
                    params.left = params.right - child.getMeasuredWidth();

                }

            } else {

                if (constraint.isLeftConstraint()) {
                    params.left = constraintLeft;
                    params.right = constraintLeft + child.getMeasuredWidth();
                } else {
                    params.left = constraintRight - child.getMeasuredWidth();
                    params.right = constraintRight;
                }
            }
        }

        /* 根据垂直偏移比调整 top bottom */

        if (constraint.verticalBias == minBias) {

            if (constraint.isTopConstraint()) {
                params.top = constraintTop;
                params.bottom = params.top + child.getMeasuredHeight();
            } else {
                params.top = constraintBottom - child.getMeasuredHeight();
                params.bottom = constraintBottom;
            }
        } else {

            /* have vertical offset */

            int childMeasuredHeight = child.getMeasuredHeight();
            int constraintHeight = constraintBottom - constraintTop;

            int extraSpace = constraintHeight - childMeasuredHeight;

            if (extraSpace > 0) {

                if (constraint.isTopConstraint()) {

                    float offset = constraint.verticalBias * extraSpace;
                    params.top = (int) (constraintTop + offset) + 1;
                    params.bottom = params.top + childMeasuredHeight;

                } else {

                    float offset = (1 - constraint.verticalBias) * extraSpace;
                    params.top = constraintBottom - child.getMeasuredHeight();
                    params.bottom = (int) (constraintBottom - offset) - 1;

                }

            } else {

                if (constraint.isTopConstraint()) {
                    params.top = constraintTop;
                    params.bottom = params.top + child.getMeasuredHeight();
                } else {
                    params.top = constraintBottom - child.getMeasuredHeight();
                    params.bottom = constraintBottom;
                }
            }
        }

        return params;
    }

    //============================ 测量最小尺寸 ============================


    /**
     * 使用该方法可以测量出一个view完全显示需要的最小尺寸(不包括margin),对一个view调用该方法之后,
     * 可以使用{@link View#getMeasuredWidth()}和{@link View#getMeasuredHeight()} ()}读取测量的值,
     * 该方法只测量view,一般用来判断剩余viewGroup剩余空间能否显示的下view
     */
    public void measureAtMostSize(View view) {

        int measureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 1, MeasureSpec.AT_MOST);
        measureChild(view, measureSpec, measureSpec);

    }

    //============================ layout ============================


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (mAdapter == null) {
            return;
        }

        BaseConstraintAdapter adapter = mAdapter;

        int count = adapter.getChildCount();
        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);

            if (child.getVisibility() == VISIBLE) {
                layoutChildWithLayoutParams(adapter, i, child);
            }
        }
    }


    /**
     * 使用布局参数布局view
     */
    private void layoutChildWithLayoutParams(BaseConstraintAdapter adapter, int position, View child) {

        LayoutParams params = getChildLayoutParams(child);
        adapter.beforeLayout(position, child);
        child.layout(params.left, params.top, params.right, params.bottom);
        adapter.afterLayout(position, child);
    }


    @Override
    protected void onDetachedFromWindow() {

        if (mAdapter != null) {
            mAdapter.onDetachedFromWindow(this);
        }

        super.onDetachedFromWindow();
    }

    //============================add view 额外添加一个view ============================

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
     * @param child        额外的view
     * @param layoutParams 该view布局参数
     * @param constraint   对view的约束
     */
    public void addExtraView(View child, LayoutParams layoutParams, Constraint constraint) {

        addOrRemoveExtraView = true;
        addView(child, layoutParams);

        int widthSpec = constraint.makeWidthSpec(child);
        int heightSpec = constraint.makeHeightSpec(child);
        measureChild(child,
                widthSpec,
                heightSpec
        );
        LayoutParams params = setChildLayoutParams(constraint, child);

        child.layout(params.left, params.top, params.right, params.bottom);

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

    //============================ 重新布局 ============================


    @Override
    public void requestLayout() {

        if (addOrRemoveExtraView) {
            return;
        }

        super.requestLayout();
    }


    /**
     * 根据view找到布局位置
     *
     * @param view view
     * @return 布局位置
     */
    public int findLayoutPosition(View view) {

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == view) {
                return i;
            }
        }

        return -1;
    }

    //============================ 更新一个约束 ============================


    public void updateConstraint(int position, Constraint constraint) {

        View view = getChildAt(position);
        if (view == null) {
            return;
        }
        updateConstraint(position, view, constraint);
    }


    public void updateConstraint(View view, Constraint constraint) {

        int position = findLayoutPosition(view);
        updateConstraint(position, view, constraint);
    }


    public void updateConstraint(int position, View view, Constraint constraint) {

        measureViewWithConstraint(position, view, constraint);
        if (view.getVisibility() != GONE) {
            layoutChildWithLayoutParams(mAdapter, position, view);
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
