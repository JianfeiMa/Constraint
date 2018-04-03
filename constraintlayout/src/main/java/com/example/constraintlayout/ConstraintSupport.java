package com.example.constraintlayout;

/**
 * Created by LiuJin on 2018-04-03:12:08
 *
 * @author wuxio
 */
public interface ConstraintSupport {

    int getParentLeft();

    int getParentTop();

    int getParentRight();

    int getParentBottom();

    int getViewLeft(int position);

    int getViewTop(int position);

    int getViewRight(int position);

    int getViewBottom(int position);

}
