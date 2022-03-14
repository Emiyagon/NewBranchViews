package com.illyasr.mydempviews.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.DialogCityBinding;
import com.illyasr.mydempviews.util.ToastUtils;
import com.illyasr.mydempviews.util.Utils;
import com.illyasr.mydempviews.view.ComPopupDialog;
import com.illyasr.mydempviews.view.ioswheel.OnItemSelectedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CityDialog extends Dialog {

    private  View contentView;
    private DialogCityBinding binding;
    private Context context;
    private int dialogGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    private boolean onTouchOutside = true;// 点击外部是否取消,true可以,false不可以
    private boolean cancerAble = true;//是否响应返回键,true响应,false不响应
    private int styleAnim = R.style.AnimBottom;
    private Gson gson = new Gson();

    private String areaStr,cityStr, provinceStr = "";

    private int s1Pos,s2Pos,s3Pos = 0;

    public CityDialog setOnTouchOutside(boolean onTouchOutside) {
        this.onTouchOutside = onTouchOutside;
        return this;
    }

    public CityDialog setCancerAble(boolean cancerAble) {
        this.cancerAble = cancerAble;
        return this;
    }

    public CityDialog(@NonNull Context context) {
        super(context, R.style.MyPopupDialog);
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_city, null, false);
        binding = DataBindingUtil.bind(contentView);
        initView();

        binding.wleel1.setListener((index,s) -> {
            //wheel2,wheel3重新加载
            s1Pos = index;
            s2Pos=0;
            s3Pos = 0;

            binding.wleel2.setItems(options2Items.get(s1Pos));
            binding.wleel2.setInitPosition(s2Pos);
            binding.wleel3.setItems(options3Items.get(s1Pos).get(s2Pos));
            binding.wleel3.setInitPosition(s3Pos);
        });
        binding.wleel2.setListener((index,s) -> {
            //wheel3重新加载
            s2Pos = index;
            s3Pos = 0;
            Log.e("https", "s1Pos = " + s1Pos+", s2Pos = "+s2Pos);
            binding.wleel3.setItems(options3Items.get(s1Pos).get(s2Pos));
            binding.wleel3.setInitPosition(s3Pos);
        });
        binding.wleel3.setListener((index,s) -> {
            provinceStr = binding.wleel3.getSelectorThing() + "";
        });

        binding.tvToast.setOnClickListener(v -> {
            dismiss();
            ToastUtils.showToast( String.format("%s省%s市%s区",options1Items.get(s1Pos)
                    ,options2Items.get(s1Pos).get(s2Pos),options3Items.get(s1Pos).get(s2Pos).get(s3Pos)));
        });

    }
    //名字
    private static ArrayList<String> options1Items = new ArrayList<>();//一级
    private static ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//二级
    private static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//三级

    private List<CityDataModel> dataModelList = new ArrayList<>();//

    private void initView() {
        try {
            String cityJson = getString(context.getAssets().open("city.json"));
            dataModelList = gson.fromJson(cityJson, new TypeToken<List<CityDataModel>>() {}.getType());
          /*  for (int i = 0; i < dataModelList.size(); i++) {// 第一次循环
                options1Items.add(dataModelList.get(i).areaName);
                ArrayList<String> secondList = new ArrayList<>();
                  ArrayList<String> item3 = new ArrayList<>();
                 ArrayList<ArrayList<String>> item3_item = new ArrayList<>();
                for (int j = 0 ; j < dataModelList.get(i).cities.size();j++){//第二次循环
                    secondList.add(dataModelList.get(i).cities.get(j).areaName);
                    for (int k = 0 ; k < dataModelList.get(i).cities.get(j).counties.size();k++){//第三次循环
                        item3.add(dataModelList.get(i).cities.get(j).counties.get(k).areaName);
                    }
                    item3_item.add(item3);
                }
                options2Items.add(secondList);
                options3Items.add(item3_item);
            }
            */
            for (int i = 0; i < dataModelList.size(); i++) {
                options1Items.add(dataModelList.get(i).areaName);//省
                ArrayList<String> options2Items_item = new ArrayList<>();
                ArrayList<ArrayList<String>> options3Items_item = new ArrayList<>();

                for (int j = 0; j < dataModelList.get(i).cities.size(); j++) {//市
                    options2Items_item.add(dataModelList.get(i).cities.get(j).areaName);
                    ArrayList<String> options3Items_item_item = new ArrayList<>();
                    for (CityDataModel.CitiesBean.CountiesBean countiesBean : dataModelList.get(i).cities.get(j).counties) {//区
                        options3Items_item_item.add(countiesBean.areaName);
                    }
                    options3Items_item.add(options3Items_item_item);
                }
                options2Items.add(options2Items_item);
                options3Items.add(options3Items_item);
            }

            binding.wleel1.setItems(options1Items);
            binding.wleel2.setItems(options2Items.get(0));
            binding.wleel3.setItems(options3Items.get(0).get(0));


        }catch (Exception e){
            e.printStackTrace();
            Log.e("HTTPS", e.toString());
        }


    }




    public static String getString(InputStream inputStream) throws Exception {
        InputStreamReader inputStreamReader = null;

            inputStreamReader = new InputStreamReader(inputStream, "utf-8");

        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);
        setCanceledOnTouchOutside(onTouchOutside);// 点击外部是否取消
        setCancelable(cancerAble);
        Window window = getWindow();
        window.setWindowAnimations(styleAnim);
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.gravity = dialogGravity;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);

    }

    float startY;
    float moveY = 0;
    private boolean isGetFinger = false;

    public CityDialog setGetFinger(boolean getFinger) {
        isGetFinger = getFinger;
        return this;
    }
    /**
     *  这里设置了响应手势
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (! isGetFinger){
                    return true;
                }
                moveY = Math.abs(ev.getY() - startY);
                if (styleAnim ==  R.style.AnimBottom){
                    contentView.scrollBy(0, -(int) moveY);
                    startY = ev.getY();
                    if (contentView.getScrollY() > 0 && isGetFinger) {
                        contentView.scrollTo(0, 0);
                    }
                }else {
                    contentView.scrollBy(0, (int) moveY);
                    startY = ev.getY();
                    if (contentView.getScrollY() < 0 && isGetFinger) {
                        contentView.scrollTo(0, 0);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (! isGetFinger){
                    return super.onTouchEvent(ev);
                }

                if (Math.abs(contentView.getScrollY()) - this.getWindow().getAttributes().height / 4 >= 0 && moveY > 0 && isGetFinger) {
                    this.dismiss();
                }
                contentView.scrollTo(0, 0);
                break;
        }
        return super.onTouchEvent(ev);
    }

}
