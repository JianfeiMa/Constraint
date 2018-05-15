package com.example.wuxio.constraint;

import com.google.gson.Gson;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

            holder.bind(position);
        }


        @Override
        public int getItemCount() {

            return mDatas.size();
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {

            super(itemView);
        }


        void bind(int position) {

        }
    }

}