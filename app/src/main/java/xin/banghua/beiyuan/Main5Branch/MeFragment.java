package xin.banghua.beiyuan.Main5Branch;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.navigation.Navigation;
import de.hdodenhof.circleimageview.CircleImageView;
import xin.banghua.beiyuan.R;
import xin.banghua.beiyuan.SharedPreferences.SharedHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    CircleImageView userportrait_iv;
    TextView usernickname_tv;
    Button beiyuanid_btn;
    Button personalinfo_btn;
    Button xiangce_btn;
    Button openvip_btn;
    Button jifen_btn;
    Button tuiguangma_btn;
    Button sawme_btn;
    Button setting_btn;


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);
    }

    private void initData(View view) {
        userportrait_iv = view.findViewById(R.id.userportrait_iv);
        usernickname_tv = view.findViewById(R.id.usernickname_tv);
        beiyuanid_btn = view.findViewById(R.id.beiyuanid_btn);
        personalinfo_btn = view.findViewById(R.id.personalinfo_btn);
        xiangce_btn = view.findViewById(R.id.xiangce_btn);
        openvip_btn = view.findViewById(R.id.openvip_btn);
        jifen_btn = view.findViewById(R.id.jifen_btn);
        tuiguangma_btn = view.findViewById(R.id.tuiguangma_btn);
        sawme_btn = view.findViewById(R.id.sawme_btn);
        setting_btn = view.findViewById(R.id.setting_btn);

        SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
        String myid = shuserinfo.readUserInfo().get("userID");
        String mynickname = shuserinfo.readUserInfo().get("userNickName");
        String myportrait = shuserinfo.readUserInfo().get("userPortrait");

        Glide.with(view.getContext())
                .asBitmap()
                .load(myportrait)
                .into(userportrait_iv);
        usernickname_tv.setText(mynickname);


        setting_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.me_setting_action));
        xiangce_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CircleActivity.class);
                startActivity(intent);
            }
        });

    }
}
