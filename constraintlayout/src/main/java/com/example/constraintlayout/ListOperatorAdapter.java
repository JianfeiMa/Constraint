package com.example.constraintlayout;

/**
 * @author wuxio 2018-04-23:15:13
 */

import android.view.View;

import java.util.List;

/**
 * 使用{@link ViewOperator}创建一个{@link BaseAdapter},和{@link ConstraintLayout#setUpWith(List)}配合使用
 *
 * @author wuxio
 */
@SuppressWarnings("unchecked")
public class ListOperatorAdapter extends BaseAdapter {

    private List< ViewOperator > mOperators;


    public ListOperatorAdapter(List< ViewOperator > operators) {

        mOperators = operators;
    }


    @Override
    public Constraint generateConstraintTo(int position, Constraint constraint) {

        return mOperators.get(position).onGenerateConstraint(position, constraint);
    }


    @Override
    public View generateViewTo(int position) {

        return mOperators.get(position).onGenerateView(position);
    }


    @Override
    public int getChildCount() {

        return mOperators.size();
    }


    @Override
    public void beforeLayout(int position, View view) {

        mOperators.get(position).onBeforeLayout(position, view);
    }


    @Override
    public void afterLayout(int position, View view) {

        mOperators.get(position).onAfterLayout(position, view);
    }
}