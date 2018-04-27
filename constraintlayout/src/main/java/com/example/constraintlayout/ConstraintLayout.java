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


    /**
     * 监听,用于{@link #requestLayout()}中调用,询问用户是哪个view需要更新布局
     */
    private OnRelayoutListener mOnRelayoutListener;


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
            requestLayout();
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

    //============================记录布局状态,用于OnRelayoutListener============================

    /**
     * 0位为1表示已经测量过
     * 1位为1表示已经确定尺寸
     * 2位为1表示已经layout
     * 3位为1表示有view需要更新布局
     * 4位为1表示view reMeasured
     * 5位为1表示view reLayouted
     */
    private int layoutState = 0;


    /**
     * 调用后标记{@link #onMeasure(int, int)}过
     */
    private void setMeasured() {

        layoutState |= 1;
    }


    /**
     * 调用后标记{@link #onSizeChanged(int, int, int, int)}过
     */
    private void setSizeChanged() {

        layoutState |= 0b10;
    }


    /**
     * 调用后标记{@link #onLayout(boolean, int, int, int, int)}过
     */
    private void setLayouted() {

        layoutState |= 0b100;
    }


    /**
     * 调用后标记{@link #requestLayout()}调用了
     */
    private void setReLayoutView() {

        layoutState |= 0b1000;
    }


    /**
     * 调用后标记{@link OnRelayoutListener#onRemeasure(ConstraintLayout)}过
     */
    private void setRemeasured() {

        layoutState |= 0b10000;
    }


    /**
     * 调用后标记{@link OnRelayoutListener#onRelayout(ConstraintLayout)}过
     */
    private void setRelayouted() {

        layoutState |= 0b100000;
    }


    /**
     * @return true 布局已经初始化
     */
    private boolean isFirstLayoutFinished() {

        return (layoutState & 0b111) == 0b111;
    }


    /**
     * @return 需要更新布局
     */
    private boolean hasViewNeedReLayout() {

        return (layoutState & 0b1000) == 0b1000;
    }


    /**
     * 重置状态为没有view需要更新布局
     */
    private void finishRelayout() {

        layoutState &= 0b111;

    }

    //============================测量布局过程============================


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (hasViewNeedReLayout()) {

            setMeasuredDimension(getWidth(), getHeight());

            if (mOnRelayoutListener != null) {
                boolean remeasure = mOnRelayoutListener.onRemeasure(this);
                setRemeasured();
                if (!remeasure) {
                    return;
                }
            }
        }

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

        setMeasured();
    }


    /**
     * 记录测量之后view的布局位置,简化{@link #onLayout(boolean, int, int, int, int)}操作
     */
    private LayoutParams measureViewWithConstraint(BaseConstraintAdapter adapter, int position, View child) {

        /* 1. 先测量 */

        Constraint constraint = adapter.generateConstraintTo(position, obtainConstraint());
        constraint.check(child);
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
     * 使用约束测量单个view
     */
    private LayoutParams measureViewWithConstraint(BaseConstraintAdapter adapter,
                                                   int i,
                                                   View child,
                                                   Constraint constraint) {

        /* 1. 先测量 */

        constraint.check(child);
        int widthSpec = constraint.makeWidthSpec(child);
        int heightSpec = constraint.makeHeightSpec(child);
        measureChild(child,
                widthSpec,
                heightSpec
        );

        /* 2. 记录测量之后该view的位置 */

        return setChildLayoutParams(constraint, child);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        setSizeChanged();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (hasViewNeedReLayout()) {

            if (mOnRelayoutListener != null) {
                boolean relayout = mOnRelayoutListener.onRelayout(this);
                setRelayouted();
                finishRelayout();
                if (!relayout) {
                    return;
                }
            }

            finishRelayout();
        }

        BaseConstraintAdapter adapter = mAdapter;
        int count = adapter.getChildCount();
        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            layoutChildWithLayoutParams(adapter, i, child);
        }

        setLayouted();
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


    /**
     * 因为该Layout是静态布局,布局之后,调用该方法,没有必要更新全部view的布局,根据情况调用不同API,更新布局
     *
     * 如果需要更新一个view的尺寸调用{@link #updateConstraint(int, Constraint)},
     * 如果是{@link android.support.v4.view.ViewPager}这种需要不断请求重新布局的view,
     * 设置{@link #setOnRelayoutListener(OnRelayoutListener)}监听
     */
    @Override
    public void requestLayout() {

        /* 额外添加删除view时不重新布局 */

        if (addOrRemoveExtraView) {
            return;
        }

        if (isFirstLayoutFinished()) {
            setReLayoutView();
        }

        super.requestLayout();
    }

    //============================ relayout request ============================

    /**
     * 当{@link #requestLayout()}调用时会回调该接口,用来询问用户哪个view需要更新
     */
    public interface OnRelayoutListener {

        /**
         * 重新测量view
         *
         * @param layout parent
         * @return true: 将会全部重新测量
         */
        boolean onRemeasure(ConstraintLayout layout);

        /**
         * 重新布局view
         *
         * @param layout parent
         * @return true :将会全部重新布局
         */
        boolean onRelayout(ConstraintLayout layout);
    }


    public void setOnRelayoutListener(OnRelayoutListener onRelayoutListener) {

        mOnRelayoutListener = onRelayoutListener;
    }

    //============================ 重新布局 ============================


    /**
     * 重新测量该位置的view,需要和{@link OnRelayoutListener#onRemeasure(ConstraintLayout)}配合使用
     *
     * @param position 需要更新布局的view的位置
     */
    public void remeasureView(int position) {

        View child = getChildAt(position);
        if (child == null) {
            return;
        }
        Constraint constraint = mAdapter.generateConstraintTo(position, obtainConstraint());
        constraint.check(child);
        int widthSpec = constraint.makeWidthSpec(child);
        int heightSpec = constraint.makeHeightSpec(child);
        measureChild(child,
                widthSpec,
                heightSpec
        );

        setChildLayoutParams(constraint, child);
    }


    /**
     * 重新测量该位置的view,需要和{@link OnRelayoutListener#onRemeasure(ConstraintLayout)}配合使用
     *
     * @param position   需要更新布局的view的位置
     * @param constraint 新的约束
     */
    public void remeasureView(int position, Constraint constraint) {

        View child = getChildAt(position);
        if (child == null) {
            return;
        }

        constraint.check(child);
        int widthSpec = constraint.makeWidthSpec(child);
        int heightSpec = constraint.makeHeightSpec(child);
        measureChild(child,
                widthSpec,
                heightSpec
        );

        setChildLayoutParams(constraint, child);
    }


    /**
     * 重新测量该位置的view,需要和{@link OnRelayoutListener#onRelayout(ConstraintLayout)}配合使用
     *
     * @param position 需要更新布局的view的位置
     */
    public void relayoutView(int position) {

        View child = getChildAt(position);
        LayoutParams params = getChildLayoutParams(child);
        child.layout(params.left, params.top, params.right, params.bottom);
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

        measureViewWithConstraint(mAdapter, position, view, constraint);
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
