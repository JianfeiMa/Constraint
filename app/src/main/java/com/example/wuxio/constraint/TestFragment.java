package com.example.wuxio.constraint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;

/**
 * @author wuxio 2018-05-13:19:23
 */
public class TestFragment extends Fragment {

    private static final String TAG = "YunFragment";
    private ConstraintLayout mConstraintLayout;


    public static TestFragment newInstance() {

        TestFragment fragment = new TestFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mConstraintLayout = view.findViewById(R.id.constraint);
        mConstraintLayout.setAdapter(new ConstraintAdapter());

        //        mConstraintLayout.post(new Runnable() {
        //            @Override
        //            public void run() {
        //
        //                Constraint constraint = mConstraintLayout.obtainConstraint();
        //
        //                constraint.leftToLeftOfView(0, 0);
        //                boolean leftConstraint = constraint.isLeftConstraint();
        //                boolean topConstraint = constraint.isTopConstraint();
        //                boolean rightConstraint = constraint.isRightConstraint();
        //                boolean bottomConstraint = constraint.isBottomConstraint();
        //                Log.i(TAG, "onViewCreated 01:" + leftConstraint + " " + topConstraint + " " +
        // rightConstraint
        //                        + " " +
        //                        bottomConstraint);
        //
        //                constraint.rightToRightOfView(0, 0);
        //                leftConstraint = constraint.isLeftConstraint();
        //                topConstraint = constraint.isTopConstraint();
        //                rightConstraint = constraint.isRightConstraint();
        //                bottomConstraint = constraint.isBottomConstraint();
        //                Log.i(TAG, "onViewCreated 02:" + leftConstraint + " " + topConstraint + " " +
        // rightConstraint
        //                        + " " +
        //                        bottomConstraint);
        //
        //                constraint.topToTopOfView(0, 0);
        //                leftConstraint = constraint.isLeftConstraint();
        //                topConstraint = constraint.isTopConstraint();
        //                rightConstraint = constraint.isRightConstraint();
        //                bottomConstraint = constraint.isBottomConstraint();
        //                Log.i(TAG, "onViewCreated 03:" + leftConstraint + " " + topConstraint + " " +
        // rightConstraint
        //                        + " " +
        //                        bottomConstraint);
        //
        //                constraint.bottomToTopOfView(0, 0);
        //                leftConstraint = constraint.isLeftConstraint();
        //                topConstraint = constraint.isTopConstraint();
        //                rightConstraint = constraint.isRightConstraint();
        //                bottomConstraint = constraint.isBottomConstraint();
        //                Log.i(TAG, "onViewCreated 04:" + leftConstraint + " " + topConstraint + " " +
        // rightConstraint
        //                        + " " +
        //                        bottomConstraint);
        //            }
        //        });

    }

    //============================ create view ============================


    public TextView getTextView() {

        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setBackgroundColor(getResources().getColor(R.color.orange));

        return textView;
    }


    public ImageView getImageView() {

        ImageView imageView = new ImageView(getContext());

        return imageView;
    }

    //============================ nbl ============================

    private class ConstraintAdapter extends BaseConstraintAdapter {

        @Override
        public View generateViewTo(int position) {

            switch (position) {

                case 0:

                    return getTextView();
                default:

                    break;
            }

            TextView view = getTextView();
            view.setText("Test");
            return view;
        }


        @Override
        public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position,View view) {

            if (position > 0) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }

            return super.generateLayoutParamsTo(position,view);
        }


        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint,View view) {

            switch (position) {

                case 0:
                    constraint.topToTopOfParent(500, 400).leftToLeftOfParent(250, 500);
                    break;

                case 1:
                    constraint.leftToLeftOfView(0, 0).bottomToTopOfView(0, 0);
                    break;

                case 2:
                    constraint.rightToRightOfView(0, 0).bottomToTopOfView(0, 0);
                    break;

                case 3:
                    constraint.leftToLeftOfView(0, 0).topToBottomOfView(0, 0);
                    break;

                case 4:
                    constraint.rightToRightOfView(0, 0).topToBottomOfView(0, 0);
                    break;

                case 5:
                    constraint.leftToLeftOfParent(0).rightToRightOfParent(0).bottomToTopOfParent(200)
                            .setHorizontalBias(0.5f);
                    break;

                case 6:
                    constraint.topToTopOfParent(0).bottomToBottomOfParent(0).rightToRightOfParent(0)
                            .setVerticalBias(0.5f);
                    break;
                default:
                    break;
            }

            return constraint;
        }


        @Override
        public void beforeMeasure(int position, View view) {

            switch (position) {

                default:
                    break;
            }
        }


        @Override
        public int getChildCount() {

            return 7;
        }
    }

}
