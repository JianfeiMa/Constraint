package com.example.constraintlayout;

/**
 * Created by LiuJin on 2018-03-28:6:55
 */

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * 约束,用于对布局中的view进行约束布局
 *
 * @author wuxio
 */
public class Constraint {

    /**
     * 约束后的尺寸
     */
    int left;
    int top;
    int right;
    int bottom;
    float horizontalBias = 0f;
    float verticalBias   = 0f;

    private ConstraintSupport mParent;


    @Override
    public String toString() {

        return "Constraint{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", horizontalBias=" + horizontalBias +
                ", verticalBias=" + verticalBias +
                '}';
    }


    Constraint(ConstraintSupport parent) {

        this.mParent = parent;
    }


    public ViewGroup getParent() {

        return (ViewGroup) mParent;
    }


    /**
     * 重置状态
     */
    public void init() {

        left = 0;
        top = 0;
        right = mParent.getParentRight();
        bottom = mParent.getParentBottom();

        horizontalBias = 0f;
        verticalBias = 0f;
    }


    /**
     * 使用一个Rect 初始化
     */
    public void init(Rect rect) {

        left = rect.left;
        top = rect.top;
        right = rect.right;
        bottom = rect.bottom;

        horizontalBias = 0f;
        verticalBias = 0f;
    }


    /**
     * 使用一个位置 初始化
     */
    public void init(int left, int top, int right, int bottom) {

        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;

        horizontalBias = 0f;
        verticalBias = 0f;
    }


    /**
     * 设置水平偏移量
     *
     * @param horizontalBias 水平偏移量
     */
    public void setHorizontalBias(float horizontalBias) {

        final int min = 0;
        final int max = 1;

        if (horizontalBias < min) {
            horizontalBias = min;
        }

        if (horizontalBias > max) {
            horizontalBias = max;
        }

        this.horizontalBias = horizontalBias;
    }


    /**
     * 设置垂直偏移量
     *
     * @param verticalBias 垂直偏移量
     */
    public void setVerticalBias(float verticalBias) {

        final int min = 0;
        final int max = 1;

        if (verticalBias < min) {
            verticalBias = min;
        }

        if (verticalBias > max) {
            verticalBias = max;
        }

        this.verticalBias = verticalBias;
    }

    //============================生成spec============================


    public boolean isLegal() {

        boolean b = right >= left && bottom >= top;

        if (!b) {
            String message = " right must >= left, bottom must >= top, current is: left=%d ," +
                    "top=%d ,right=%d , bottom=%d";
            String format = String.format(Locale.CHINA, message, left, top, right, bottom);
            throw new RuntimeException(format);
        }

        return b;
    }


    public int makeWidthSpec() {

        if (left < right) {
            return View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY);
        }

        return View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
    }


    public int makeHeightSpec() {

        if (bottom > top) {
            return View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY);
        }

        return View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
    }

    //============================约束至Parent============================


    /**
     * 约束自己的左边至布局的左边
     *
     * @param offset 偏移量,与坐标轴同向
     */
    public void leftToLeftOfParent(int offset) {

        left = mParent.getParentLeft() + offset;
    }


    /**
     * 约束自己的左边至布局的左边,并指定宽度
     *
     * @param offset 偏移量,与坐标轴同向
     * @param width  宽度
     */
    public void leftToLeftOfParent(int offset, int width) {

        left = mParent.getParentLeft() + offset;
        right = left + width;
    }


    private void checkParentRight() {

        if (mParent.getParentRight() == -1) {
            throw new RuntimeException(" you can't constraint to parent right when parent width is not " +
                    "match_parent or a exactly dimensions ");
        }
    }


    private void checkParentBottom() {

        if (mParent.getParentBottom() == -1) {
            throw new RuntimeException(" you can't constraint to parent bottom when parent height is not " +
                    "match_parent or a exactly dimensions ");
        }
    }


    /**
     * 约束自己的左边至布局的右边
     *
     * @param offset 偏移量,与坐标轴同向
     */
    public void leftToRightOfParent(int offset) {

        checkParentRight();
        left = mParent.getParentRight() + offset;
    }


    /**
     * 约束自己的左边至布局的右边,并指定宽度
     *
     * @param offset 偏移量,与坐标轴同向
     * @param width  期望的宽度
     */
    public void leftToRightOfParent(int offset, int width) {

        checkParentRight();

        left = mParent.getParentRight() + offset;
        right = left + width;
    }


    /**
     * 约束自己的右边至布局的左边
     *
     * @param offset 偏移量,与坐标轴同向
     */
    public void rightToLeftOfParent(int offset) {

        right = mParent.getParentLeft() + offset;
    }


    /**
     * 约束自己的右边至布局的左边,并指定宽度
     *
     * @param offset 偏移量,与坐标轴同向
     * @param width  期望的宽度
     */
    public void rightToLeftOfParent(int offset, int width) {

        right = mParent.getParentLeft() + offset;
        left = right - width;
    }


    /**
     * 约束自己的右边至布局的右边
     *
     * @param offset 偏移量,与坐标轴同向
     */
    public void rightToRightOfParent(int offset) {

        checkParentRight();

        right = mParent.getParentRight() + offset;
    }


    /**
     * 约束自己的右边至布局的右边,并指定宽度
     *
     * @param offset 偏移量,与坐标轴同向
     * @param width  期望的宽度
     */
    public void rightToRightOfParent(int offset, int width) {

        checkParentRight();

        right = mParent.getParentRight() + offset;
        left = right - width;
    }


    /**
     * 约束自己的上边至布局的上边
     *
     * @param offset 偏移量,与坐标轴同向
     */
    public void topToTopOfParent(int offset) {

        top = mParent.getParentTop() + offset;
    }


    /**
     * 约束自己的上边至布局的上边,并指定宽度
     *
     * @param offset 偏移量,与坐标轴同向
     * @param height 期望view的高度
     */
    public void topToTopOfParent(int offset, int height) {

        top = mParent.getParentTop() + offset;
        bottom = top + height;
    }


    /**
     * 约束自己的上边至布局的下边
     *
     * @param offset 偏移量,与坐标轴同向
     */
    public void topToBottomOfParent(int offset) {

        checkParentBottom();

        top = mParent.getParentBottom() + offset;
    }


    /**
     * 约束自己的上边至布局的下边,并指定高度
     *
     * @param offset 偏移量,与坐标轴同向
     * @param height 期望的高度
     */
    public void topToBottomOfParent(int offset, int height) {

        checkParentBottom();

        top = mParent.getParentBottom() + offset;
        bottom = top + height;
    }


    /**
     * 约束自己的下边至布局的上边
     *
     * @param offset 偏移量,与坐标轴同向
     */
    public void bottomToTopOfParent(int offset) {

        bottom = mParent.getParentTop() + offset;
    }


    /**
     * 约束自己的下边至布局的上边,并指定高度
     *
     * @param offset 偏移量,与坐标轴同向
     * @param height 期望的高度
     */
    public void bottomToTopOfParent(int offset, int height) {

        bottom = mParent.getParentTop() + offset;
        top = bottom - height;
    }


    /**
     * 约束自己的下边至布局的下边
     *
     * @param offset 偏移量,与坐标轴同向
     */
    public void bottomToBottomOfParent(int offset) {

        checkParentBottom();

        bottom = mParent.getParentBottom() + offset;
    }


    /**
     * 约束自己的下边至布局的下边,并指定高度
     *
     * @param offset 偏移量,与坐标轴同向
     * @param height 期望的高度
     */
    public void bottomToBottomOfParent(int offset, int height) {

        checkParentBottom();

        bottom = mParent.getParentBottom() + offset;
        top = bottom - height;
    }


    /**
     * 约束自己的左边至位于position view的左边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     */
    public void leftToLeftOfView(int position, int offset) {

        left = mParent.getViewLeft(position) + offset;

    }

    //============================约束至view============================


    /**
     * 约束自己的左边至位于position view的左边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     * @param width    期望的宽度
     */
    public void leftToLeftOfView(int position, int offset, int width) {

        left = mParent.getViewLeft(position) + offset;
        right = left + width;

    }


    /**
     * 约束自己的左边至位于position View的右边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     */
    public void leftToRightOfView(int position, int offset) {

        left = mParent.getViewRight(position) + offset;
    }


    /**
     * 约束自己的左边至位于position View的右边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     * @param width    期望的宽度
     */
    public void leftToRightOfView(int position, int offset, int width) {

        left = mParent.getViewRight(position) + offset;
        right = left + width;

    }


    /**
     * 约束自己的右边至位于position View的左边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     */
    public void rightToLeftOfView(int position, int offset) {

        right = mParent.getViewLeft(position) + offset;
    }


    /**
     * 约束自己的右边至位于position View的左边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     * @param width    期望的宽度
     */
    public void rightToLeftOfView(int position, int offset, int width) {

        right = mParent.getViewLeft(position) + offset;
        left = right - width;
    }


    /**
     * 约束自己的右边至位于position View的右边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     */
    public void rightToRightOfView(int position, int offset) {

        right = mParent.getViewRight(position) + offset;
    }


    /**
     * 约束自己的右边至位于position View的右边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     * @param width    期望的宽度
     */
    public void rightToRightOfView(int position, int offset, int width) {

        right = mParent.getViewRight(position) + offset;
        left = right - width;
    }


    /**
     * 约束自己的上边至位于position View的上边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     */
    public void topToTopOfView(int position, int offset) {

        top = mParent.getViewTop(position) + offset;
    }


    /**
     * 约束自己的上边至位于position View的上边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     * @param height   期望的高度
     */
    public void topToTopOfView(int position, int offset, int height) {

        top = mParent.getViewTop(position) + offset;
        bottom = top + height;

    }


    /**
     * 约束自己的上边至位于position View的下边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     */
    public void topToBottomOfView(int position, int offset) {

        top = mParent.getViewBottom(position) + offset;
    }


    /**
     * 约束自己的上边至位于position View的下边,并指定高度
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     * @param height   期望的高度
     */
    public void topToBottomOfView(int position, int offset, int height) {

        top = mParent.getViewBottom(position) + offset;
        bottom = top + height;
    }


    /**
     * 约束自己的下边至位于position View的上边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     */
    public void bottomToTopOfView(int position, int offset) {

        bottom = mParent.getViewTop(position) + offset;
    }


    /**
     * 约束自己的下边至位于position View的上边,并指定高度
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     * @param height   期望的高度
     */
    public void bottomToTopOfView(int position, int offset, int height) {

        bottom = mParent.getViewTop(position) + offset;
        top = bottom - height;

    }


    /**
     * 约束自己的下边至位于position View的下边
     *
     * @param position view的position
     * @param offset   偏移量,与坐标轴同向
     */
    public void bottomToBottomOfView(int position, int offset) {

        bottom = mParent.getViewBottom(position) + offset;
    }


    /**
     * 约束自己的下边至位于position View的下边,并指定高度
     *
     * @param position view的position
     * @param offset   两条边之间的margin值
     * @param height   期望的高度
     */
    public void bottomToBottomOfView(int position, int offset, int height) {

        bottom = mParent.getViewBottom(position) + offset;
        top = bottom - height;
    }

    //============================从一个view复制一个约束============================


    public void copyFrom(int position) {

        left = mParent.getViewLeft(position);
        top = mParent.getViewLeft(position);
        right = mParent.getViewLeft(position);
        bottom = mParent.getViewLeft(position);
    }


    public void translateX(int offset) {

        left += offset;
        right += offset;
    }


    public void translateY(int offset) {

        top += offset;
        bottom += offset;
    }


    public void translate(int leftOffset, int topOffset, int rightOffset, int bottomOffset) {

        left += leftOffset;
        top += topOffset;
        right += rightOffset;
        bottom += bottomOffset;
    }
}
