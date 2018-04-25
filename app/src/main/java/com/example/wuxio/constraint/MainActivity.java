package com.example.wuxio.constraint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.banner.adapter.BasePagerAdapter;
import com.example.banner.pager.LoopViewPager;
import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;
import com.example.constraintlayout.simple.Constraints;

import java.util.Locale;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    public static void start(Context context) {

        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    //private static final String TAG = "MainActivity";

    private ConstraintLayout mConstraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConstraintLayout = findViewById(R.id.constraintLayout);

        mConstraintLayout.setAdapter(new MainConstraintConstraintAdapter());

        mConstraintLayout.setOnRelayoutListener(new ConstraintLayout.OnRelayoutListener() {

            @Override
            public boolean onRemeasure(ConstraintLayout layout) {

                layout.remeasureView(0);
                return false;
            }


            @Override
            public boolean onRelayout(ConstraintLayout layout) {

                layout.relayoutView(0);
                return false;
            }
        });

        //mConstraintLayout.setAdapter(new SimpleAdapter());

    }


    private class MainConstraintConstraintAdapter extends BaseConstraintAdapter {

        final int size = 13;


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
                constraint.topToTopOfView(9, 0)
                        .bottomToBottomOfView(11, 0)
                        .leftToLeftOfView(7, 0)
                        .rightToRightOfView(8, 0);
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


        private int layoutCount;


        @Override
        public void afterLayout(int position, View view) {

            super.afterLayout(position, view);
            if (position == 0) {
                Log.i(TAG, "afterLayout:" + ++layoutCount);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.to_main2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.toMain2) {
            Main2Activity.start(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class SimpleAdapter extends BaseConstraintAdapter {

        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint) {

            switch (position) {

                case 0:
                    constraint.leftToLeftOfParent(50)
                            .rightToRightOfParent(-50)
                            .topToTopOfParent(50)
                            .bottomToTopOfParent(500);
                    break;

                case 1:
                    constraint.leftToLeftOfView(0, 0)
                            .topToBottomOfView(0, 10)
                            .rightToLeftOfView(0, 400)
                            .bottomToBottomOfView(0, 400);
                    break;

                case 2:
                    constraint.leftToLeftOfParent(50)
                            .topToTopOfView(0, 0)
                            .rightToRightOfView(0, 0)
                            .bottomToTopOfView(0, 100);
                    break;

                default:
                    break;
            }

            return constraint;
        }


        @Override
        public View generateViewTo(int position) {

            View view;
            switch (position) {
                case 2:
                    view = getTextView(position, getColorC(R.color.skyblue));
                    break;

                default:
                    view = getTextView(position);
                    break;
            }

            return view;
        }


        @Override
        public int getChildCount() {

            return 3;
        }


        @Override
        public void beforeLayout(int position, View view) {

            super.beforeLayout(position, view);
        }


        @Override
        public void afterLayout(int position, View view) {

            super.afterLayout(position, view);
        }
    }


    public int getColorC(int colorID) {

        return getResources().getColor(colorID);
    }

}
