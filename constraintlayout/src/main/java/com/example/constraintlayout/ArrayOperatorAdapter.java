package com.example.constraintlayout;

/**
 * @author wuxio 2018-04-23:15:12
 */

import android.view.View;

/**
 * 使用{@link ViewOperator}创建一个{@link BaseAdapter},和{@link ConstraintLayout#setUpWith(ViewOperator[])}配合使用
 *
 * @author wuxio
 */
@SuppressWarnings("unchecked")
public class ArrayOperatorAdapter extends BaseAdapter {

    private ViewOperator[] mOperators;


    public ArrayOperatorAdapter(ViewOperator[] operators) {

        mOperators = operators;
    }


    @Override
    public Constraint generateConstraintTo(int position, Constraint constraint) {

        return mOperators[position].onGenerateConstraint(position, constraint);
    }


    @Override
    public View generateViewTo(int position) {

        return mOperators[position].onGenerateView(position);
    }


    @Override
    public int getChildCount() {

        return mOperators.length;
    }


    @Override
    public void beforeLayout(int position, View view) {

        mOperators[position].onBeforeLayout(position, view);
    }


    @Override
    public void afterLayout(int position, View view) {

        mOperators[position].onAfterLayout(position, view);
    }
}