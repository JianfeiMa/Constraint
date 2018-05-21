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
import android.widget.TextView;

import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;

/**
 * @author wuxio 2018-05-13:19:23
 */
public class FlowFragment extends Fragment {

    private static final String TAG = "YunFragment";
    private ConstraintLayout mConstraintLayout;


    public static FlowFragment newInstance() {

        FlowFragment fragment = new FlowFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_flow, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mConstraintLayout = view.findViewById(R.id.constraint);
        mConstraintLayout.setAdapter(new ConstraintAdapter());
    }

    //============================ create view ============================


    public TextView getTextView() {

        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setBackgroundResource(R.drawable.flow_text_background);
        textView.setPadding(20, 10, 20, 10);
        return textView;
    }

    //============================ nbl ============================

    private class ConstraintAdapter extends BaseConstraintAdapter {

        private String[] texts = {
                "离思五首·其四",
                "唐代",
                "元稹",
                "曾经沧海难为水",
                "除却巫山不是云",
                "取次花丛懒回顾",
                "半缘修道半缘君",
                "满江红·写怀",
                "宋代",
                "岳飞",
                "怒发冲冠",
                "凭栏处",
                "潇潇雨歇",
                "抬望眼",
                "仰天长啸",
                "壮怀激烈",
                "三十功名尘与土",
                "八千里路云和月",
                "莫等闲",
                "白了少年头",
                "空悲切",
                "靖康耻",
                "犹未雪",
                "臣子恨",
                "何时灭",
                "驾长车",
                "踏破贺兰山缺",
                "壮志饥餐胡虏肉",
                "笑谈渴饮匈奴血",
                "待从头",
                "收拾旧山河",
                "朝天阙",
                "唐",
                "刘禹锡",
                "《金陵怀古》",
                "兴废由人事",
                "山川空地形",
                "后庭花一曲",
                "幽怨不堪听",
        };


        @Override
        public View generateViewTo(int position) {

            return getTextView();
        }


        @Override
        public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position, View view) {

            return new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }


        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint, View view) {

            if (position == 0) {

                ((TextView) view).setText(texts[position]);
                constraint.leftToLeftOfParent(20).topToTopOfParent(20);
                return constraint;

            } else {

                ((TextView) view).setText(texts[position]);
                mConstraintLayout.measureAtMostSize(view);

                int width = view.getMeasuredWidth();
                int i = width + constraint.getViewRight(position - 1);
                int parentWidth = constraint.getParentWidth();

                if (i > parentWidth - 20) {

                    constraint.leftToLeftOfParent(20)
                            .topToBottomOfView(position - 1, 10);
                } else {

                    constraint.leftToRightOfView(position - 1, 10)
                            .topToTopOfView(position - 1, 0);
                }

                return constraint;
            }
        }


        @Override
        public int getChildCount() {

            return texts.length;
        }
    }
}
