package com.example.wuxio.constraint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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
                constraint.setHorizontalBias(0.5f);
            }

            if (position == 1) {

                constraint.leftToLeftOfParent(0);
                constraint.rightToRightOfParent(0);
                constraint.topToTopOfParent(0);
                constraint.bottomToBottomOfParent(0);
                constraint.setVerticalBias(0.5f);
            }

            return constraint;
        }


        @Override
        public View generateViewTo(int position) {

            //            TextView textView = new TextView(MainActivity.this);

            TextView textView= (TextView) LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_text,
                            mConstraintLayout,
                            false
                    );


            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(getResources().getColor(R.color.orange));
            return textView;
        }


        @Override
        public int getChildCount() {

            return 2;
        }


        @Override
        public void beforeLayout(int position, View view) {

            ((TextView) view).setText(String.valueOf(position));
        }
    }
}
