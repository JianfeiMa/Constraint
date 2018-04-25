package com.example.wuxio.constraint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.simple.ConstraintOperator;

/**
 * @author wuxio
 */
public class Main2Activity extends AppCompatActivity {

    public static void start(Context context) {

        Intent starter = new Intent(context, Main2Activity.class);
        context.startActivity(starter);
    }


    protected ConstraintLayout mConstraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main2);
        initView();
        Operators operators = new Operators();
        mConstraintLayout.setUpWith(operators.getOperators());
        mConstraintLayout.setOnRelayoutListener(new ConstraintLayout.OnRelayoutListener() {

            @Override
            public boolean onRemeasure(ConstraintLayout layout) {
                layout.remeasureView(21);
                return false;
            }


            @Override
            public boolean onRelayout(ConstraintLayout layout) {
                layout.relayoutView(21);
                return false;
            }
        });
    }


    private void initView() {

        mConstraintLayout = findViewById(R.id.constraintLayout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.to_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.toMain) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class Operators {

        private static final String TAG = "Operators";

        final int size = 22;

        ConstraintOperator[] mOperators = new ConstraintOperator[size];


        public ConstraintOperator[] getOperators() {

            return mOperators;
        }


        public Operators() {

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

    private class ParentOperator implements ConstraintOperator< TextView > {

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

            TextView textView = new TextView(Main2Activity.this);
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

    private class LeftBelowWeightOperator implements ConstraintOperator< TextView > {

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

            TextView textView = new TextView(Main2Activity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.circle);
            return textView;
        }
    }

    private class LeftCopyOperator implements ConstraintOperator< TextView > {

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

            TextView textView = new TextView(Main2Activity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.circle);
            return textView;
        }
    }

    private class TopCopyOperator implements ConstraintOperator< TextView > {

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

            TextView textView = new TextView(Main2Activity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.circle);
            return textView;
        }
    }

    private class BottomOperator implements ConstraintOperator< TextView > {

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

            TextView textView = new TextView(Main2Activity.this);
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

    private class LayoutBottomOperator implements ConstraintOperator< LinearLayout > {

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

            LinearLayout view = (LinearLayout) LayoutInflater.from(Main2Activity.this)
                    .inflate(
                            R.layout.item_linear,
                            mConstraintLayout,
                            false
                    );

            return view;
        }
    }

    private class PagerOperator implements ConstraintOperator< ViewPager > {

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

            ViewPager pager = new ViewPager(Main2Activity.this);
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

                return data.length;
            }


            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {

                TextView textView = new TextView(Main2Activity.this);
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
