package com.example.wuxio.constraint;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.banner.adapter.BasePagerAdapter;
import com.example.banner.pager.LoopViewPager;
import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;
import com.example.constraintlayout.simple.Constraints;
import com.example.constraintlayout.simple.ViewOperator;

import java.util.Locale;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ConstraintLayout mConstraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConstraintLayout = findViewById(R.id.constraintLayout);
        mConstraintLayout.setAdapter(new MainConstraintConstraintAdapter());

    }


    private class MainConstraintConstraintAdapter extends BaseConstraintAdapter {

        final int size = 12;


        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint) {

            if (position == 0) {
                Constraints.matchParentWidth(constraint, 0, 500);
                return constraint;
            }
            if (position == 1) {
                Constraints.matchParentWidthAtBottomOfView(constraint, 0, 10, 100);
                return constraint;
            }

            if (position == 2) {
                int size = constraint.getWeightWidth(4, 1);
                Constraints.atBottomOfView(constraint, 1, 0, 10, size, size);
                return constraint;
            }

            if (position <= 5) {
                Constraints.copyToRight(constraint, position - 1);
                return constraint;
            }

            if (position == 6) {
                int size = constraint.getWeightWidth(3, 1, 10 * 2);
                Constraints.atBottomOfView(constraint, 2, 0, 10, size, size);
                return constraint;
            }

            if (position <= 8) {
                Constraints.copyToRight(constraint, position - 1, 10);
                return constraint;
            }

            if (position == 9) {

                /* 因为该view是wrap content 所以宽度高度不重要,只要是比view大就行*/

                Constraints.atBottomOfView(constraint, 8, 10, 10, 1500, 1500);
            }

            if (position == 10) {
                Constraints.atBottomOfView(constraint, 9, 10, 10, 1500, 1500);
            }

            if (position == 11) {
                Constraints.atBottomOfView(constraint, 10, 10, 10, 1500, 1500);
            }

            if (position == 12) {

            }

            return constraint;
        }


        @Override
        public View generateViewTo(int position) {

            if (position == 0) {
                return getPager();
            }

            if (position == 9) {
                return getTextView("Test text 1", 12, getResources().getColor(R.color.deeppink));
            }

            if (position == 10) {
                return getTextView("Test text 2", 24, getResources().getColor(R.color.deeppink));
            }

            if (position == 11) {
                return getTextView("Test text 3", 18, getResources().getColor(R.color.deeppink));
            }

            if (position < size) {
                return getTextView(position);
            }
            return null;
        }


        @Override
        public int getChildCount() {

            return size;
        }


        @Override
        public void beforeLayout(int position, View view) {

            super.beforeLayout(position, view);
            if (position == 9) {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                Log.i(TAG, "beforeLayout: 9: " + params);
                Log.i(TAG, "beforeLayout: 9: " + params.width);
                Log.i(TAG, "beforeLayout: 9: " + params.height);
                Log.i(TAG, "beforeLayout: 9: " + view.getMeasuredWidth());
                Log.i(TAG, "beforeLayout: 9: " + view.getMeasuredHeight());
            }
        }
    }


    private TextView getTextView(int i) {

        TextView textView = new TextView(MainActivity.this);
        textView.setText(String.valueOf(i));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(getResources().getColor(R.color.orange));

        return textView;
    }


    private TextView getTextView(int i, @ColorInt int color) {

        TextView textView = new TextView(MainActivity.this);
        textView.setText(String.valueOf(i));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(color);

        return textView;
    }


    private TextView getTextView(String text, int textSize, @ColorInt int color) {

        TextView textView = (TextView) getLayoutInflater().inflate(R.layout.item_text_2, mConstraintLayout,
                false);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(color);

        return textView;
    }


    private LoopViewPager getPager() {

        final int size = 5;
        int[] colors = {
                getResources().getColor(R.color.violet),
                getResources().getColor(R.color.plum),
                getResources().getColor(R.color.lavender),
                getResources().getColor(R.color.darkseagreen),
                getResources().getColor(R.color.skyblue),
        };
        LoopViewPager pager = new LoopViewPager(MainActivity.this);
        pager.setAdapter(new BasePagerAdapter< String, TextView >() {
            @Override
            public int getCount() {

                return size;
            }


            @Override
            public String getData(int i) {

                return String.format(Locale.CHINA, "loop data %d", i);
            }


            @Override
            public TextView getView(int i) {

                return getTextView(i, colors[i]);
            }


            @Override
            public void bindData(int i, String s, TextView view) {

                view.setText(s);
            }
        });

        return pager;
    }


    private class Operators {

        private static final String TAG = "Operators";

        final int size = 22;

        ViewOperator[] mOperators = new ViewOperator[size];

        private Constraint mConstraint;


        public ViewOperator[] getOperators() {

            return mOperators;
        }


        public Operators(Constraint constraint) {

            mConstraint = constraint;

            mOperators[0] = new ParentOperator(10, 10, 10, 400);

            mOperators[1] = new LeftBelowWeightOperator(0, 20, 20, 5);
            mOperators[2] = new LeftCopyOperator(1);
            mOperators[3] = new LeftCopyOperator(2);
            mOperators[4] = new LeftCopyOperator(3);
            mOperators[5] = new LeftCopyOperator(4);

            mOperators[6] = new TopCopyOperator(1);
            mOperators[7] = new LeftCopyOperator(6);
            mOperators[8] = new LeftCopyOperator(7);
            mOperators[9] = new LeftCopyOperator(8);
            mOperators[10] = new LeftCopyOperator(9);

            for (int i = 11; i < 20; i++) {
                mOperators[i] = new BottomOperator();
            }

            mOperators[20] = new LayoutBottomOperator();
            mOperators[21] = new PagerOperator();
        }

    }

    private class ParentOperator implements ViewOperator< TextView > {

        int leftMargin;
        int topMargin;
        int rightMargin;
        int height;


        public ParentOperator(int leftMargin, int topMargin, int rightMargin, int height) {

            this.leftMargin = leftMargin;
            this.topMargin = topMargin;
            this.rightMargin = rightMargin;
            this.height = height;
        }


        @Override
        public Constraint onGenerateConstraint(int position, Constraint constraint) {

            constraint.leftToLeftOfParent(leftMargin)
                    .rightToRightOfParent(-rightMargin)
                    .topToTopOfParent(topMargin)
                    .bottomToTopOfParent(topMargin + height);

            return constraint;
        }


        @Override
        public TextView onGenerateView(int position) {

            TextView textView = new TextView(MainActivity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.rect);
            return textView;
        }


        @Override
        public void onBeforeLayout(int position, TextView v) {

            v.setText(String.valueOf(position));
        }
    }

    private class LeftBelowWeightOperator implements ViewOperator< TextView > {

        private int position;
        private int leftMargin;
        private int topMargin;
        private int base;


        public LeftBelowWeightOperator(int position, int leftMargin, int topMargin, int base) {

            this.position = position;
            this.leftMargin = leftMargin;
            this.topMargin = topMargin;
            this.base = base;
        }


        @Override
        public Constraint onGenerateConstraint(int position, Constraint constraint) {

            int size = constraint.getWeightWidth(base, 1, (base + 1) * 20);

            constraint.leftToLeftOfParent(leftMargin, size)
                    .topToBottomOfView(this.position, topMargin, size);

            return constraint;
        }


        @Override
        public TextView onGenerateView(int position) {

            TextView textView = new TextView(MainActivity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.circle);
            return textView;
        }
    }

    private class LeftCopyOperator implements ViewOperator< TextView > {

        private int position;


        public LeftCopyOperator(int position) {

            this.position = position;
        }


        @Override
        public Constraint onGenerateConstraint(int position, Constraint constraint) {

            constraint.copyFrom(this.position).translateX(constraint.getViewWidth(this.position) + 20);
            return constraint;
        }


        @Override
        public TextView onGenerateView(int position) {

            TextView textView = new TextView(MainActivity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.circle);
            return textView;
        }
    }

    private class TopCopyOperator implements ViewOperator< TextView > {

        private int position;


        public TopCopyOperator(int position) {

            this.position = position;
        }


        @Override
        public Constraint onGenerateConstraint(int position, Constraint constraint) {

            constraint.copyFrom(this.position).translateY(constraint.getViewHeight(this.position) + 20);
            return constraint;
        }


        @Override
        public TextView onGenerateView(int position) {

            TextView textView = new TextView(MainActivity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.circle);
            return textView;
        }
    }

    private class BottomOperator implements ViewOperator< TextView > {

        @Override
        public Constraint onGenerateConstraint(int position, Constraint constraint) {

            constraint.leftToLeftOfParent(50)
                    .rightToRightOfParent(-50)
                    .topToBottomOfView(position - 1, 50)
                    .bottomToBottomOfView(position - 1, 350);

            return constraint;
        }


        @Override
        public TextView onGenerateView(int position) {

            TextView textView = new TextView(MainActivity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.rect);
            return textView;
        }


        @Override
        public void onBeforeLayout(int position, TextView v) {

            v.setText(String.valueOf(position));
        }
    }

    private class LayoutBottomOperator implements ViewOperator< LinearLayout > {

        @Override
        public Constraint onGenerateConstraint(int position, Constraint constraint) {

            constraint.leftToLeftOfParent(50)
                    .rightToRightOfParent(-50)
                    .topToBottomOfView(position - 1, 50)
                    .bottomToBottomOfView(position - 1, 1350);

            return constraint;
        }


        @Override
        public LinearLayout onGenerateView(int position) {

            LinearLayout view = (LinearLayout) LayoutInflater.from(MainActivity.this)
                    .inflate(
                            R.layout.item_linear,
                            mConstraintLayout,
                            false
                    );

            return view;
        }
    }

    private class PagerOperator implements ViewOperator< ViewPager > {

        @Override
        public Constraint onGenerateConstraint(int position, Constraint constraint) {

            constraint.leftToLeftOfParent(50)
                    .rightToRightOfParent(-50)
                    .topToBottomOfView(position - 1, 50)
                    .bottomToBottomOfView(position - 1, 550);

            return constraint;
        }


        @Override
        public ViewPager onGenerateView(int position) {

            ViewPager pager = new ViewPager(MainActivity.this);
            pager.setAdapter(new PagerAdapter());
            return pager;
        }


        class PagerAdapter extends android.support.v4.view.PagerAdapter {

            private String[] data = {
                    "java",
                    "android",
                    "ios",
                    "java scrip",
                    "shell"
            };


            @Override
            public int getCount() {

                return 5;
            }


            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {

                TextView textView = new TextView(MainActivity.this);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.rect);
                textView.setText(data[position]);

                container.addView(textView);

                return textView;
            }


            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

                return view == object;
            }


            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

                container.removeView((View) object);
            }
        }
    }
}
