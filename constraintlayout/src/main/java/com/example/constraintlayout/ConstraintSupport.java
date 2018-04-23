package com.example.constraintlayout;

/**
 * Created by LiuJin on 2018-04-03:12:08
 *
 * @author wuxio
 *
 * 约束支持接口,实现该接口可以支持{@link Constraint}操作
 */
public interface ConstraintSupport {

    /**
     * 用于{@link Constraint}获取父布局左边的坐标,和PaddingLeft有关
     *
     * @return 子view可以使用的最左边的坐标
     */
    int getParentLeft();

    /**
     * 用于{@link Constraint}获取父布局顶边的坐标,和PaddingTop有关
     *
     * @return 子view可以使用的最上边的坐标
     */
    int getParentTop();

    /**
     * 用于{@link Constraint}获取父布局右边的坐标,和PaddingRight和宽度是否是wrap_content有关,
     * 如果宽度是wrap_content,需要返回-1,表明右边约束不可用
     *
     * @return 子view可以使用的最右边的坐标, 或者-1右边约束不可用
     */
    int getParentRight();

    /**
     * 用于{@link Constraint}获取父布局底边的坐标,和PaddingRight和高度是否是wrap_content有关,
     * 如果高度是wrap_content,需要返回-1,表明底边约束不可用
     *
     * @return 子view可以使用的最底边的坐标, 或者-1底边约束不可用
     */
    int getParentBottom();

    /**
     * 获取该布局位置的view的left坐标
     *
     * @param position 需要得到坐标的view的布局位置
     * @return view的left
     */
    int getViewLeft(int position);

    /**
     * 获取该布局位置的view的top坐标
     *
     * @param position 需要得到坐标的view的布局位置
     * @return view的top
     */
    int getViewTop(int position);

    /**
     * 获取该布局位置的view的right坐标
     *
     * @param position 需要得到坐标的view的布局位置
     * @return view的right
     */
    int getViewRight(int position);

    /**
     * 获取该布局位置的view的bottom坐标
     *
     * @param position 需要得到坐标的view的布局位置
     * @return view的bottom
     */
    int getViewBottom(int position);

}
