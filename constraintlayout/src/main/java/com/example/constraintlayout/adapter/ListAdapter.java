package com.example.constraintlayout.adapter;

/**
 * @author wuxio 2018-04-23:15:13
 */

import android.view.View;

import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.simple.ConstraintOperator;

import java.util.List;

/**
 * 使用{@link ConstraintOperator}创建一个{@link BaseConstraintAdapter},和{@link ConstraintLayout#setUpWith(List)}配合使用
 *
 * @author wuxio
 */
@SuppressWarnings("unchecked")
public class ListAdapter extends BaseConstraintAdapter {

    private List< ConstraintOperator > mOperators;


    public ListAdapter(List< ConstraintOperator > operators) {

        mOperators = operators;
    }


    @Override
    public View generateViewTo(int position) {

        return mOperators.get(position).onGenerateView(position);
    }


    @Override
    public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position, View view) {

        return mOperators.get(position).onGenerateLayoutParams(position, view);
    }


    @Override
    public Constraint generateConstraintTo(int position, Constraint constraint, View view) {

        return mOperators.get(position).onGenerateConstraint(position, constraint, view);
    }


    @Override
    public int getChildCount() {

        return mOperators.size();
    }


    @Override
    public void beforeMeasure(int position, View view) {

        mOperators.get(position).onBeforeMeasure(position, view);
    }


    @Override
    public void afterMeasure(int position, View view) {

        mOperators.get(position).onBeforeMeasure(position, view);
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