package com.example.wuxio.constraint;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bitmapreader.BitmapReader;
import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;
import com.example.jsonparser.JsonParser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-13:19:23
 */
public class JsonFragment extends Fragment {

    private static final String TAG = "JsonFragment";
    private ViewFromJson mViewFromJson;


    public static JsonFragment newInstance() {

        JsonFragment fragment = new JsonFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_json, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout constraintLayout = view.findViewById(R.id.constraint);
        constraintLayout.setAdapter(new ConstraintAdapter());

        /* 模拟解析数据 */

        try {
            parseJson();
            Log.i(TAG, "onViewCreated:" + mViewFromJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //============================ json 数据 ============================

    /**
     * 模拟json数据,根据它生成界面
     */
    public static final String JSON = "{\n" +
            "\t\"text00\":\"自爱残妆晓镜中，环钗漫篸绿丝丛。须臾日射胭脂颊，一朵红苏旋欲融。\",\n" +
            "\t\"pic00\":0,\n" +
            "\t\"text01\":\"山泉散漫绕阶流，万树桃花映小楼。闲读道书慵未起，水晶帘下看梳头。\",\n" +
            "\t\"pic01\":1,\n" +
            "\t\"pic02\":2,\n" +
            "\t\"text02\":\"红罗著压逐时新，吉了花纱嫩麴尘。第一莫嫌材地弱，些些纰缦最宜人。\",\n" +
            "\t\"pic03\":3,\n" +
            "\t\"text03\":\"曾经沧海难为水，除却巫山不是云。取次花丛懒回顾，半缘修道半缘君。\",\n" +
            "\t\"pic04\":[4,5,6],\n" +
            "\t\"text04\":\"寻常百种花齐发，偏摘梨花与白人。今日江头两三树，可怜和叶度残春。\",\n" +
            "\t\"pic05\":[7,8,9,10,11]\n" +
            "}";

    /**
     * 模拟图片资源
     */
    public static final int[] pics = {
            R.drawable.a0,
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a0,
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a0,
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6
    };

    //============================ bean ============================

    /**
     * 保存view数据/布局信息
     */
    private class ViewInfo {

        /**
         * 数据
         * 如果 {@link #viewType} = 0, data is string, else = 1, data is int drawable resource
         */
        private Object data;

        /**
         * 0 = textView
         * 1 = imageView
         */
        private int viewType;

        /**
         * 0 = 占据宽度,高度wrap
         * 1 = 3等分宽度,从左到右,从上到下排列
         */
        private int layoutType;


        public ViewInfo(Object data, int viewType, int layoutType) {

            this.data = data;
            this.viewType = viewType;
            this.layoutType = layoutType;
        }


        @Override
        public String toString() {

            return "ViewInfo{" +
                    "data=" + data +
                    ", viewType=" + viewType +
                    ", layoutType=" + layoutType +
                    '}';
        }
    }

    /**
     * 保存从json解析出来的view信息
     */
    private class ViewFromJson {

        private List< ViewInfo > mViewInfos = new ArrayList<>();


        private void addTextViewInfo(String text) {

            mViewInfos.add(new ViewInfo(text, 0, 0));
        }


        private void addImageViewInfo(int resIndex) {

            mViewInfos.add(new ViewInfo(resIndex, 1, 0));
        }


        private void addHorImageViewInfo(int resIndex) {

            mViewInfos.add(new ViewInfo(resIndex, 1, 1));
        }


        /**
         * @return view 数量
         */
        public int size() {

            return mViewInfos.size();
        }


        public ViewInfo getViewInfo(int position) {

            return mViewInfos.get(position);
        }


        public int getViewType(int position) {

            return mViewInfos.get(position).viewType;
        }


        public int getLayoutType(int position) {

            return mViewInfos.get(position).layoutType;
        }


        public Object getViewData(int position) {

            return mViewInfos.get(position).data;
        }


        @Override
        public String toString() {

            return "ViewFromJson{" +
                    "mViewInfos=" + mViewInfos +
                    '}';
        }
    }


    /**
     * 模拟解析数据
     */
    private void parseJson() {

        mViewFromJson = new ViewFromJson();

        JsonParser.create((nodes, key, valueHolder) -> {

            if (nodes.get(0).name.startsWith("text")) {

                mViewFromJson.addTextViewInfo(valueHolder.value());

            } else if (nodes.get(0).name.startsWith("pic")) {

                if (nodes.get(0).type == JsonParser.ARRAY) {

                    mViewFromJson.addHorImageViewInfo(valueHolder.intValle());

                } else {

                    mViewFromJson.addImageViewInfo(valueHolder.intValle());
                }
            }
        }).parse(new StringReader(JSON));

    }

    //============================ create view ============================


    public TextView getTextView() {

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

        return textView;
    }


    public ImageView getImageView() {

        ImageView imageView = new ImageView(getContext());

        return imageView;
    }

    //============================ adapter ============================

    private class ConstraintAdapter extends BaseConstraintAdapter {

        private Constraint mConstraint;


        @Override

        public View generateViewTo(int position) {

            int type = mViewFromJson.getViewType(position);

            if (type == 0) {
                return getTextView();
            }

            if (type == 1) {
                return getImageView();
            }

            return null;
        }


        @Override
        public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position, View view) {

            int layoutType = mViewFromJson.getLayoutType(position);

            if (layoutType == 0) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }

            if (layoutType == 1) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
            }

            return super.generateLayoutParamsTo(position, view);
        }


        @Override
        public Constraint generateConstraintTo(int position, Constraint constraint, View view) {

            mConstraint = constraint;

            if (position == 0) {

                constraint.leftToLeftOfParent(20).rightToRightOfParent(-20).topToTopOfParent(20);
                return constraint;
            }

            int layoutType = mViewFromJson.getLayoutType(position);

            if (layoutType == 0) {

                constraint.leftToLeftOfParent(20)
                        .rightToRightOfParent(-20)
                        .topToBottomOfView(position - 1, 20);

            } else if (layoutType == 1) {

                int size = constraint.getWeightWidth(3, 1, 20 * 4);

                /* 通过上一个layoutType判断是否是网格第一个 */

                int type = mViewFromJson.getLayoutType(position - 1);

                if (type == 0) {

                    constraint.leftToLeftOfParent(20, size)
                            .topToBottomOfView(position - 1, 20, size);

                } else {

                    /* 假设当前view横向排在上一个view的右边,如果超出界面显示区域,那么需要换行 */

                    int rightMaybe = constraint.getViewRight(position - 1) + size + 20;

                    if (rightMaybe > constraint.getParentWidth()) {

                        /* 换到下一行 */

                        constraint.copyFrom(position - 1).translateY(size + 20).translateLeftTo(20);

                    } else {

                        /* 横向排在上一个view右侧 */

                        constraint.copyFrom(position - 1).translateX(size + 20);
                    }
                }
            }

            return constraint;
        }


        @Override
        public void beforeMeasure(int position, View view) {

            int type = mViewFromJson.getViewType(position);

            if (type == 0) {

                String text = (String) mViewFromJson.getViewData(position);
                ((TextView) view).setText(text);

            } else if (type == 1) {

                int layoutType = mViewFromJson.getLayoutType(position);
                if (layoutType == 0) {

                    int width = mConstraint.getParentWidth() - 20 * 2;
                    int height = (int) (width * 16f / 9) + 1;

                    Integer index = (Integer) mViewFromJson.getViewData(position);
                    int picRes = pics[index];

                    Bitmap bitmap = BitmapReader.decodeMaxSampledBitmap(
                            getResources(),
                            picRes,
                            width,
                            height
                    );

                    ((ImageView) view).setImageBitmap(bitmap);

                } else if (layoutType == 1) {

                    Integer index = (Integer) mViewFromJson.getViewData(position);

                    int size = mConstraint.getWeightWidth(3, 1, 20 * 4);
                    int picRes = pics[index];
                    Bitmap bitmap = BitmapReader.decodeMaxSampledBitmap(getResources(), picRes, size, size);

                    ((ImageView) view).setImageBitmap(bitmap);
                }
            }
        }


        @Override
        public int getChildCount() {

            return mViewFromJson.size();
        }
    }
}
