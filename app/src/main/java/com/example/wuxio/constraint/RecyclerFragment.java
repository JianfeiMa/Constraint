package com.example.wuxio.constraint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        mRecyclerView = view.findViewById(R.id.recycler);
    }

    //============================ adapter ============================

    private class RecyclerAdapter extends RecyclerView.Adapter< Holder > {

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent,
                    false);
            return new Holder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

        }


        @Override
        public int getItemCount() {

            return 50;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {

            super(itemView);
        }
    }

}