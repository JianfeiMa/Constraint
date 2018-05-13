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
            }

            return null;
        }


        @Override
        public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position) {

            if (position == 12 || position == 13) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }

            return super.generateLayoutParamsTo(position);
        }


        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint) {

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
                        .bottomToBottomOfView(11, 0,20);
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

                default:
                    break;
            }
        }


        @Override
        public int getChildCount() {

            return 14;
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
