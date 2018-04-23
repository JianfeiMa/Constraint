package com.example.constraintlayout.simple;

import com.example.constraintlayout.Constraint;

/**
 * @author wuxio 2018-04-23:16:01
 * {@link com.example.constraintlayout.Constraint}工具类
 */
public class Constraints {


    /**
     * 约束view左边至parent左边,
     * 约束view右边至parent右边,
     * 约束view上边至parent上边,
     * 约束view下边至parent上边,
     *
     * @param constraint 空白约束
     * @param top        view top 距 parent 距离
     * @param height     view 高度
     * @return 约束
     */
    public static Constraint matchParentWidth(Constraint constraint, int top, int height) {

        return matchParentWidth(constraint, top, height, 0, 0);
    }


    /**
     * 约束view左边至parent左边,
     * 约束view右边至parent右边,
     * 约束view上边至parent上边,
     * 约束view下边至parent上边,
     *
     * @param constraint  空白约束
     * @param top         view top 距 parent 距离
     * @param height      view 高度
     * @param leftMargin  左边margin
     * @param rightMargin 右边margin
     * @return 约束
     */
    public static Constraint matchParentWidth(Constraint constraint,
                                              int top, int height,
                                              int leftMargin, int rightMargin) {

        constraint.topToTopOfParent(top, height);
        constraint.leftToLeftOfParent(leftMargin);
        constraint.rightToRightOfParent(-rightMargin);
        return constraint;
    }


    /**
     * 约束view左边至parent左边,
     * 约束view右边至parent右边,
     * 约束view上边至view上边,
     * 约束view下边至view上边,
     *
     * @param constraint   空白约束
     * @param position     用来参考的view
     * @param bottomMargin view bottom 距离参考view的top的距离
     * @param height       view 高度
     * @return 约束
     */
    public static Constraint matchParentWidthAtTopOfView(Constraint constraint,
                                                         int position,
                                                         int bottomMargin,
                                                         int height) {

        return matchParentWidthAtTopOfView(constraint, position, height, bottomMargin, 0, 0);
    }


    /**
     * 约束view左边至parent左边,
     * 约束view右边至parent右边,
     * 约束view上边至view上边,
     * 约束view下边至view上边,
     *
     * @param constraint   空白约束
     * @param position     用来参考的view
     * @param bottomMargin view bottom 距离参考view的top的距离
     * @param height       view 高度
     * @param leftMargin   左边margin
     * @param rightMargin  右边margin
     * @return 约束
     */
    public static Constraint matchParentWidthAtTopOfView(Constraint constraint,
                                                         int position,
                                                         int bottomMargin,
                                                         int height,
                                                         int leftMargin,
                                                         int rightMargin) {

        constraint.bottomToTopOfView(position, -bottomMargin, height);
        constraint.leftToLeftOfParent(leftMargin);
        constraint.rightToRightOfParent(-rightMargin);
        return constraint;
    }


    /**
     * 约束view左边至parent左边,
     * 约束view右边至parent右边,
     * 约束view上边至view下边,
     * 约束view下边至view下边,
     *
     * @param constraint 空白约束
     * @param position   用来参考的view
     * @param height     view 高度
     * @return 约束
     */
    public static Constraint matchParentWidthAtBottomOfView(Constraint constraint,
                                                            int position,
                                                            int topMargin,
                                                            int height) {

        return matchParentWidthAtBottomOfView(constraint, position, topMargin, height, 0, 0);
    }


    /**
     * 约束view左边至parent左边,
     * 约束view右边至parent右边,
     * 约束view上边至view下边,
     * 约束view下边至view下边,
     *
     * @param constraint  空白约束
     * @param position    用来参考的view
     * @param height      view 高度
     * @param leftMargin  左边margin
     * @param rightMargin 右边margin
     * @return 约束
     */
    public static Constraint matchParentWidthAtBottomOfView(Constraint constraint,
                                                            int position,
                                                            int topMargin,
                                                            int height,
                                                            int leftMargin,
                                                            int rightMargin) {

        constraint.topToBottomOfView(position, topMargin, height);
        constraint.leftToLeftOfParent(leftMargin);
        constraint.rightToRightOfParent(-rightMargin);
        return constraint;
    }


    public static Constraint at(Constraint constraint,
                                int left,
                                int top,
                                int width,
                                int height) {

        constraint.leftToLeftOfParent(left, width);
        constraint.topToTopOfParent(top, height);
        return constraint;
    }


    public static Constraint atTopOfView(Constraint constraint,
                                         int position,
                                         int left,
                                         int bottomMargin,
                                         int width,
                                         int height) {

        constraint.leftToLeftOfParent(left, width);
        constraint.bottomToTopOfView(position, -bottomMargin, height);
        return constraint;
    }


    public static Constraint atBottomOfView(Constraint constraint,
                                            int position,
                                            int left,
                                            int topMargin,
                                            int width,
                                            int height) {

        constraint.leftToLeftOfParent(left, width);
        constraint.topToBottomOfView(position, topMargin, height);
        return constraint;
    }


    public static Constraint copyToRight(Constraint constraint, int position) {

        return copyToRight(constraint, position, 0);
    }


    public static Constraint copyToRight(Constraint constraint, int position, int leftMargin) {

        constraint.copyFrom(position).translateX(constraint.getViewWidth(position) + leftMargin);
        return constraint;
    }

}
