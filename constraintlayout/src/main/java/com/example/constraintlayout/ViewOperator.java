package com.example.constraintlayout;

import android.view.View;

/**
 * Created by LiuJin on 2018-04-03:16:43
 *
 * @author wuxio
 */
public interface ViewOperator < T extends View > {

    Constraint onGenerateConstraint(int position, Constraint constraint);

    T onGenerateView(int position);

    default void onBeforeLayout(int position, T v) {

    }

    default void onAfterLayout(int position, T v) {

    }

}
