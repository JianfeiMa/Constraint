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

/**
 * @author wuxio 2018-05-13:19:23
 */
public class YunFragment extends Fragment {

    private static final String TAG = "YunFragment";
    private ConstraintLayout mConstraintLayout;


    public static YunFragment newInstance() {

        YunFragment fragment = new YunFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yun, container, false);
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
                    return getBanner();
                case 1:
                case 2:
                case 3:
                case 4:
                    ImageView imageView = getImageView();
                    imageView.setBackground(getResources().getDrawable(R.drawable.circle_red_line));
                    imageView.setPadding(30, 30, 30, 30);
                    return imageView;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    return getTextView();
                case 14:
                    return new View(getContext());

                default:
                    break;
            }

            return null;
        }


        @Override
        public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position, View view) {

            return super.generateLayoutParamsTo(position, view);
        }


        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint, View view) {

            int marginFor1234 = 40;

            switch (position) {

                /* banner */

                case 0:
                    constraint.leftToLeftOfParent(0)
                            .rightToRightOfParent(0)
                            .topToTopOfParent(0, 400);
                    break;

                /* navigation */

                case 1:

                    int weightSize1234 = constraint.getWeightWidth(4, 1, marginFor1234 * 5);
                    constraint.topToBottomOfView(0, marginFor1234, weightSize1234)
                            .leftToLeftOfParent(marginFor1234, weightSize1234);
                    break;
                case 2:
                    constraint.copyFrom(1).translateX(constraint.getViewWidth(1) + marginFor1234);
                    break;

                case 3:
                    constraint.copyFrom(2).translateX(constraint.getViewWidth(2) + marginFor1234);
                    break;

                case 4:
                    constraint.copyFrom(3).translateX(constraint.getViewWidth(3) + marginFor1234);
                    break;

                /* recommend */

                case 5:
                    int weightSize567 = constraint.getWeightWidth(3, 1, 20 + 10 + 10 + 20);
                    constraint.topToBottomOfView(4, marginFor1234, weightSize567)
                            .leftToLeftOfParent(20, weightSize567);
                    break;
                case 6:
                    constraint.copyFrom(5).translateX(constraint.getViewWidth(5) + 10);
                    break;
                case 7:
                    constraint.copyFrom(6).translateX(constraint.getViewWidth(6) + 10);
                    break;
                case 8:
                    constraint.copyFrom(5).translateY(constraint.getViewHeight(5) + 10);
                    break;
                case 9:
                    constraint.copyFrom(6).translateY(constraint.getViewHeight(6) + 10);
                    break;
                case 10:
                    constraint.copyFrom(7).translateY(constraint.getViewHeight(7) + 10);
                    break;

                /* video */

                case 11:
                    int weightSize1112 = constraint.getWeightWidth(2, 1, 20 + 10 + 20);
                    constraint.leftToLeftOfParent(20, weightSize1112)
                            .topToBottomOfView(10, 20, 300);
                    break;
                case 12:
                    constraint.copyFrom(11).translateX(constraint.getViewWidth(11) + 10);
                    break;

                /* ad */

                case 13:
                    constraint.leftToLeftOfParent(20)
                            .rightToRightOfParent(-20)
                            .topToBottomOfView(12, marginFor1234, 300);
                    break;

                /* empty */

                case 14:
                    constraint.leftToLeftOfParent(0)
                            .rightToRightOfParent(0)
                            .topToBottomOfView(position - 1, 0, 100);
                    break;
                default:
                    break;
            }

            return constraint;
        }


        @Override
        public void beforeMeasure(int position, View view) {

            switch (position) {

                case 1:
                    ((ImageView) view).setImageResource(R.drawable.b_music);
                    break;
                case 2:
                    ((ImageView) view).setImageResource(R.drawable.b_history);
                    break;
                case 3:
                    ((ImageView) view).setImageResource(R.drawable.b_basketball);
                    break;
                case 4:
                    ((ImageView) view).setImageResource(R.drawable.b_read);
                    break;

                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    String s = String.format(Locale.CHINA, "推荐 %d", position - 4);
                    ((TextView) view).setText(s);
                    break;

                case 11:
                case 12:
                    String s1 = String.format(Locale.CHINA, "视频 %d", position - 10);
                    ((TextView) view).setText(s1);
                    break;

                case 13:
                    String s2 = String.format(Locale.CHINA, "广告 %d", 1);
                    ((TextView) view).setText(s2);
                    break;

                default:
                    break;
            }
        }


        @Override
        public int getChildCount() {

            return 15;
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
        public TextView getView(ViewGroup container, int position) {

            return getTextView();
        }


        @Override
        public void bindData(int i, String s, TextView textView) {

            textView.setText(s);
        }
    }
}
