package com.example.constraintlayout.adapter;

/**
 * @author wuxio 2018-04-23:15:12
 */

import android.view.View;

import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.simple.ConstraintOperator;

/**
 * 使用{@link ConstraintOperator}创建一个{@link BaseConstraintAdapter},和{@link ConstraintLayout#setUpWith(ConstraintOperator[])}配合使用
 *
 * @author wuxio
 */
@SuppressWarnings("unchecked")
public class ArrayAdapter extends BaseConstraintAdapter {

    private ConstraintOperator[] mOperators;


    public ArrayAdapter(ConstraintOperator[] operators) {

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