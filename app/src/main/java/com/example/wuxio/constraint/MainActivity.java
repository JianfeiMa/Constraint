package com.example.wuxio.constraint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.constraintlayout.BaseAdapter;
import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

    private ConstraintLayout< Adapter > mConstraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConstraintLayout = findViewById(R.id.constraintLayout);
        mConstraintLayout.setAdapter(new Adapter());
    }


    class Adapter extends BaseAdapter {

        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint) {

            if (position == 0) {
                constraint.leftToLeftOfParent(0);
                constraint.rightToRightOfParent(0);
                constraint.topToTopOfParent(0);
                constraint.bottomToTopOfParent(400);
            }

            int size = constraint.getWeightWidth(5, 1, 60);

            if (position == 1) {

                constraint.leftToLeftOfParent(
                        10,
                        size);
                constraint.topToBottomOfView(0, 10, size);
            }

            if (position >= 2 && position <= 5) {
                constraint.copyFrom(position - 1).translateX(10 + size);
            }

            if (position == 6) {
                constraint.leftToLeftOfParent(
                        10,
                        size);
                constraint.topToBottomOfView(5, 10, size);
            }

            if (position >= 7 && position <= 11) {
                constraint.copyFrom(position - 1).translateX(10 + size);
            }

            return constraint;
        }


        @Override
        public View generateViewTo(int position) {

            TextView textView = new TextView(MainActivity.this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);

            if (position == 0) {
                textView.setBackgroundResource(R.drawable.rect);
            } else if (position < 11) {

                textView.setBackgroundResource(R.drawable.circle);
            }

            return textView;
        }


        @Override
        public int getChildCount() {

            return 11;
        }


        @Override
        public void beforeLayout(int position, View view) {

            ((TextView) view).setText(String.valueOf(position));
        }
    }
}
