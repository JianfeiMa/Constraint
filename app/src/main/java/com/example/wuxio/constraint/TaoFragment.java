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

import com.example.banner.BannerView;
import com.example.banner.adapter.BasePagerAdapter;
import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;

import java.util.Locale;
import java.util.Random;

/**
 * @author wuxio 2018-05-13:19:23
 */
public class TaoFragment extends Fragment {

    private static final String TAG = "YunFragment";
    private ConstraintLayout mConstraintLayout;


    public static TaoFragment newInstance() {

        TaoFragment fragment = new TaoFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tao, container, false);
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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setBackgroundColor(getResources().getColor(R.color.orange));

        return textView;
    }


    public TextView getNoGroundTextView() {

        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

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

            if (position == 0) {
                return getBanner();
            } else if (position <= 10) {
                ImageView view = getImageView();
                view.setBackground(getResources().getDrawable(R.drawable.circle_red_line));
                view.setPadding(10, 10, 10, 10);
                return view;
            } else if (position == 11) {
                TextView view = getNoGroundTextView();
                view.setLines(2);
                view.setText("淘宝头条");
                view.setBackgroundColor(getResources().getColor(R.color.skyblue));
                return view;
            } else if (position <= 13) {

                TextView view = new TextView(getContext());
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                view.setText("头条:买一送一,买二送二,买三送三");
                return view;
            } else if (position == 14 || position == 17) {

                TextView view = getNoGroundTextView();
                view.setText("好货");
                return view;
            } else if (position <= 19) {

                return getImageView();
            } else if (position == 20) {

                TextView textView = getTextView();
                textView.setText("广告 1");
                return textView;
            } else if (position <= 25) {

                TextView view = getTextView();
                int index = position - 20;
                String s = String.format(Locale.CHINA, "直播 %d", index);
                view.setText(s);
                return view;
            } else if (position == 26) {

                TextView textView = getNoGroundTextView();
                textView.setText("好店推荐");
                return textView;
            } else if (position <= 35) {

                TextView textView = getTextView();
                String s = String.format(Locale.CHINA, "好店 %d", position - 26);
                textView.setText(s);
                return textView;
            } else if (position == 36) {

                TextView view = getNoGroundTextView();
                view.setText("猜你喜欢");
                return view;
            } else if (position <= 42) {

                TextView textView = getTextView();
                String s = String.format(Locale.CHINA, "推荐 %d", position - 36);
                textView.setText(s);
                return textView;
            }

            return getTextView();
        }


        @Override
        public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position, View view) {

            if (position == 12 || position == 13 || position == 36) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            } else if (position == 14 || position == 17 || position == 26) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }

            return super.generateLayoutParamsTo(position, view);
        }


        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint, View view) {

            if (position == 0) {
                constraint.leftToLeftOfParent(0)
                        .rightToRightOfParent(0)
                        .topToTopOfParent(0, 400);
            } else if (position == 1) {

                int weightSize = constraint.getWeightWidth(5, 1, 80 * 5);
                constraint.leftToLeftOfParent(40, weightSize)
                        .topToBottomOfView(0, 20, weightSize);

            } else if (position <= 5) {

                constraint.copyFrom(position - 1)
                        .translateX(constraint.getViewWidth(position - 1) + 80);
            } else if (position <= 10) {

                constraint.copyFrom(position - 5)
                        .translateY(constraint.getViewHeight(position - 5) + 80);
            } else if (position == 11) {

                constraint.leftToLeftOfParent(20, 150).topToBottomOfView(10, 20, 150);
            } else if (position == 12) {

                constraint.leftToRightOfView(11, 20)
                        .rightToRightOfParent(-20)
                        .topToTopOfView(11, 0);

            } else if (position == 13) {

                constraint.leftToRightOfView(11, 20)
                        .rightToRightOfParent(-20)
                        .bottomToBottomOfView(11, 0);
            } else if (position == 14) {

                constraint.leftToLeftOfParent(20).topToBottomOfView(13, 20);
            } else if (position == 15) {

                constraint.topToBottomOfView(14, 5, 200)
                        .leftToLeftOfParent(20, 280);
            } else if (position == 16) {

                constraint.leftToRightOfView(15, 5)
                        .rightToLeftOfParent(constraint.getParentWidth() / 2 - 5)
                        .topToTopOfView(14, 0)
                        .bottomToBottomOfView(15, 0);

            } else if (position == 17) {

                constraint.copyFrom(14).translateOnLeftTo(constraint.getParentWidth() / 2 + 5);
            } else if (position == 18) {

                constraint.copyFrom(15).translateOnLeftTo(constraint.getParentWidth() / 2 + 5);
            } else if (position == 19) {

                constraint.copyFrom(16).translateOnRightTo(constraint.getParentWidth() - 20);
            } else if (position == 20) {

                constraint.topToBottomOfView(19, 20, 360)
                        .leftToLeftOfParent(0)
                        .rightToRightOfParent(0);

            } else if (position == 21) {

                int size = constraint.getWeightWidth(3, 2);
                constraint.leftToLeftOfParent(0, size)
                        .topToBottomOfView(20, 20, 300);
            } else if (position == 22) {

                constraint.leftToRightOfView(21, 2)
                        .topToTopOfView(21, 0)
                        .bottomToBottomOfView(21, 0);
            } else if (position == 23) {
                int size = constraint.getWeightWidth(3, 1, 2 * 2);
                constraint.leftToLeftOfParent(0, size)
                        .topToBottomOfView(22, 2, constraint.getViewHeight(21));
            } else if (position == 24) {

                constraint.leftToRightOfView(23, 2)
                        .rightToRightOfView(21, 0)
                        .topToTopOfView(23, 0)
                        .bottomToBottomOfView(23, 0);
            } else if (position == 25) {

                constraint.leftToRightOfView(24, 2)
                        .rightToRightOfParent(0)
                        .topToTopOfView(24, 0)
                        .bottomToBottomOfView(24, 1);
            } else if (position == 26) {

                constraint.leftToLeftOfParent(0)
                        .topToBottomOfView(25, 20);
            } else if (position == 27) {

                int size = constraint.getWeightWidth(3, 1, 2 * 2);
                constraint.leftToLeftOfParent(0, size)
                        .topToBottomOfView(position - 1, 20, size);

            } else if (position == 28) {

                constraint.copyFrom(position - 1)
                        .translateX(constraint.getViewWidth(position - 1) + 2);
            } else if (position == 29) {

                constraint.leftToRightOfView(position - 1, 2)
                        .rightToRightOfParent(0)
                        .topToTopOfView(position - 1, 0)
                        .bottomToBottomOfView(position - 1, 0);
            } else if (position <= 35) {

                constraint.copyFrom(position - 3)
                        .translateY(constraint.getViewHeight(position - 3) + 2);
            } else if (position == 36) {

                constraint.leftToLeftOfParent(0)
                        .rightToRightOfParent(0)
                        .topToBottomOfView(position - 1, 20);
            } else if (position == 37) {

                constraint.leftToLeftOfParent(0)
                        .rightToLeftOfParent(constraint.getParentWidth() / 2 - 10)
                        .topToBottomOfView(position - 1, 20, 400);

            } else if (position == 38) {

                constraint.leftToLeftOfParent(constraint.getParentWidth() / 2 + 10)
                        .rightToRightOfParent(0)
                        .topToBottomOfView(position - 2, 20, 300);
            } else if (position <= 42) {

                Random random = new Random();
                int nextInt = random.nextInt(200);
                constraint.copyFrom(position - 2)
                        .translateY(constraint.getViewHeight(position - 2) + 10)
                        .translate(0, 0, 0, nextInt);
            }

            return constraint;
        }


        @Override
        public void beforeMeasure(int position, View view) {

            switch (position) {

                case 1:
                    ((ImageView) view).setImageResource(R.drawable.b_read);
                    break;
                case 2:
                    ((ImageView) view).setImageResource(R.drawable.b_self);
                    break;
                case 3:
                    ((ImageView) view).setImageResource(R.drawable.b_policy);
                    break;
                case 4:
                    ((ImageView) view).setImageResource(R.drawable.b_police);
                    break;
                case 5:
                    ((ImageView) view).setImageResource(R.drawable.b_english);
                    break;
                case 6:
                    ((ImageView) view).setImageResource(R.drawable.b_construct);
                    break;
                case 7:
                    ((ImageView) view).setImageResource(R.drawable.b_chi);
                    break;
                case 8:
                    ((ImageView) view).setImageResource(R.drawable.b_music);
                    break;
                case 9:
                    ((ImageView) view).setImageResource(R.drawable.b_history);
                    break;
                case 10:
                    ((ImageView) view).setImageResource(R.drawable.b_basketball);
                    break;
                case 15:
                case 18:
                    ((ImageView) view).setImageResource(R.drawable.ace);
                    break;
                case 16:
                case 19:
                    ((ImageView) view).setImageResource(R.drawable.a15456);
                default:
                    break;
            }
        }


        @Override
        public int getChildCount() {

            return 43;
        }
    }


    public BannerView getBanner() {

        BannerView bannerView = (BannerView) LayoutInflater.from(getContext()).inflate(
                R.layout.item_banner,
                mConstraintLayout,
                false
        );
        bannerView.setAdapter(new BannerAdapter());
        return bannerView;
    }


    private class BannerAdapter extends BasePagerAdapter< String, TextView > {


        @Override
        public int getCount() {

            return 5;
        }


        @Override
        public String getData(int i) {

            return String.valueOf(i);
        }


        @Override
        public TextView getView(int i) {

            return getTextView();
        }


        @Override
        public void bindData(int i, String s, TextView textView) {

            textView.setText(s);
        }
    }
}
