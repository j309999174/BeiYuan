package xin.banghua.beiyuan.MainBranch;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.navigation.Navigation;
import xin.banghua.beiyuan.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SousuoFragment extends Fragment {

    private static final String TAG = "SousuoFragment";

    //昵称，年龄，地区
    EditText userNickname_et,userAge_et,userRegion_et,direct_et;
    //性别，标签
    RadioGroup userGender_rg,userProperty_rg;
    RadioButton male_rb,female_rb,zProperty_rb,bProperty_rb,dProperty_rb;

    //
    String logtype,userAccount,userPassword,userNickname,userAge,userRegion,userGender,userProperty,userPortrait;
    Button direct_btn,condition_btn;


    public SousuoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sousuo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化导航按钮
        initNavigateButton(view);

        //监听提交按钮
        submitValue(view);
    }
    //三个按钮初始化
    private void initNavigateButton(View view){
        Button tuijian = view.findViewById(R.id.tuijian_btn);
        Button fujin = view.findViewById(R.id.fujin_btn);
        //Button sousuo = view.findViewById(R.id.sousuo_btn);

        tuijian.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.sousuo_tuijian_action));
        fujin.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.sousuo_fujin_action));
    }

    //搜索提交的传值
    private void submitValue(final View view){

        //直接搜索
        direct_et = view.findViewById(R.id.direct_et);
        direct_btn = view.findViewById(R.id.direct_btn);
        //条件搜索
        userAge_et = view.findViewById(R.id.userAge);
        userRegion_et = view.findViewById(R.id.userRegion);
        userGender_rg = view.findViewById(R.id.userGender);
        userProperty_rg = view.findViewById(R.id.userProperty);
        condition_btn = view.findViewById(R.id.submit_btn);

        //会直接运行getDirectBundle，所以没法动态传值
        //direct_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.sousuo_result_action,getDirectBundle()));
        //condition_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.sousuo_result_action,getConditionBundle(view)));
        direct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 直接搜索"+getDirectBundle().getString("nameOrPhone"));
                Navigation.findNavController(v).navigate(R.id.sousuo_result_action, getDirectBundle());
            }
        });
        condition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 条件搜索");
                Navigation.findNavController(v).navigate(R.id.sousuo_result_action, getConditionBundle(view));
            }
        });

    }

    //获取值作为监听参数
    private Bundle getDirectBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("type","direct");
        bundle.putString("nameOrPhone",direct_et.getText().toString());
        return bundle;
    }
    //获取值作为监听参数
    private Bundle getConditionBundle(View view){
        Bundle bundle = new Bundle();
        bundle.putString("type","condition");
        bundle.putString("userAge",userAge_et.getText().toString());
        bundle.putString("userRegion",userRegion_et.getText().toString());
        userGender = ((RadioButton) view.findViewById(userGender_rg.getCheckedRadioButtonId())).getText().toString();
        userProperty = ((RadioButton) view.findViewById(userProperty_rg.getCheckedRadioButtonId())).getText().toString();
        bundle.putString("userGender",userGender);
        bundle.putString("userProperty",userProperty);
        return bundle;
    }
}
