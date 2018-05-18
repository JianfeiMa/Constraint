package com.example.wuxio.constraint;

import com.google.gson.Gson;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;
import com.example.wuxio.constraint.bean.GankBean;
import com.example.wuxio.constraint.bean.GankJson;

import java.util.List;

/**
 * @author wuxio 2018-05-13:19:23
 */
public class RecyclerFragment extends Fragment {

    private static final String TAG = "YunFragment";
    private RecyclerView mRecyclerView;


    public static RecyclerFragment newInstance() {

        RecyclerFragment fragment = new RecyclerFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Gson gson = new Gson();
        GankBean gankBean = gson.fromJson(GankJson.JSON, GankBean.class);

        Log.i(TAG, "onViewCreated:" + gankBean.getResults().size());

        mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new RecyclerAdapter(gankBean.getResults()));
    }

    //============================ adapter ============================

    private class RecyclerAdapter extends RecyclerView.Adapter< Holder > {

        private List< GankBean.ResultsBean > mDatas;


        public RecyclerAdapter(List< GankBean.ResultsBean > datas) {

            mDatas = datas;
        }


        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent,
                    false);
            return new Holder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.bind(position, mDatas.get(position));
        }


        @Override
        public int getItemCount() {

            return mDatas.size();
        }
    }


    private class Holder extends RecyclerView.ViewHolder {

        private GankBean.ResultsBean mResultsBean;
        private ConstraintLayoutAdapter mAdapter = new ConstraintLayoutAdapter();


        public Holder(View itemView) {

            super(itemView);
        }


        void bind(int position, GankBean.ResultsBean resultsBean) {

            mAdapter.setResultsBean(resultsBean);
            ((ConstraintLayout) itemView.findViewById(R.id.constraintLayout)).setAdapter(mAdapter);
        }
    }

    private class ConstraintLayoutAdapter extends BaseConstraintAdapter {

        private GankBean.ResultsBean mResultsBean;
        private TextView             mTextView;
        private TextView             mTextView1;
        private TextView             mTextView2;
        private TextView             mTextView3;


        public ConstraintLayoutAdapter() {

        }


        public ConstraintLayoutAdapter(GankBean.ResultsBean resultsBean) {

            mResultsBean = resultsBean;
        }


        public void setResultsBean(GankBean.ResultsBean resultsBean) {

            mResultsBean = resultsBean;
        }


        @Override
        public View generateViewTo(int position) {

            if (position == 0) {
                if (mTextView == null) {
                    mTextView = new TextView(getContext());
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    mTextView.setTextColor(Color.BLACK);
                }
                mTextView.setText(mResultsBean.getDesc());
                return mTextView;
            }

            if (position == 1) {
                if (mTextView1 == null) {
                    mTextView1 = new TextView(getContext());
                    mTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mTextView1.setTextColor(Color.LTGRAY);
                }
                mTextView1.setText(mResultsBean.getWho());
                return mTextView1;
            }

            if (position == 2) {
                if (mTextView2 == null) {
                    mTextView2 = new TextView(getContext());
                    mTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mTextView2.setTextColor(Color.LTGRAY);
                }
                mTextView2.setText(mResultsBean.getPublishedAt().substring(0, 11));
                return mTextView2;
            }

            if (position == 3) {
                if (mTextView3 == null) {
                    mTextView3 = new TextView(getContext());
                    mTextView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mTextView3.setTextColor(Color.LTGRAY);
                }
                mTextView3.setText(mResultsBean.getType());
                return mTextView3;
            } else {

                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.ace);
                return imageView;
            }
        }


        @Override
        public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position, View view) {

            if (position == 0) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            } else if (position <= 3) {
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
                constraint.leftToLeftOfParent(20).rightToRightOfParent(-20).topToTopOfParent(20);
            } else if (position == 1) {
                constraint.leftToLeftOfParent(20).topToBottomOfView(0, 20);
            } else if (position == 2) {
                constraint.rightToRightOfParent(-20).topToBottomOfView(0, 20);
            } else if (position == 3) {
                constraint.leftToLeftOfParent(0)
                        .rightToRightOfParent(0)
                        .setHorizontalBias(0.5f)
                        .topToBottomOfView(0, 20);
            } else if (position == 4) {

                int size = constraint.getWeightWidth(3, 1, 20 * 4);
                constraint.leftToLeftOfParent(20, size)
                        .topToBottomOfView(3, 20, size);
            } else if (position == 5) {

                constraint.copyFrom(4).translateX(constraint.getViewWidth(4) + 20);

            } else if (position == 6) {

                constraint.copyFrom(5).translateX(constraint.getViewWidth(5) + 20);

            }

            return constraint;
        }


        @Override
        public int getChildCount() {

            int size = mResultsBean.getImages() == null ? 0 : mResultsBean.getImages().size();
            return 4 + size;
        }
    }
}