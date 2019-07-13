package xin.banghua.beiyuan.Personage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.ParseJSON.ParseJSONObject;
import xin.banghua.beiyuan.R;
import xin.banghua.beiyuan.SharedPreferences.SharedHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonageFragment extends Fragment {
    private static final String TAG = "PersonageFragment";

    public String mUserID;
    private View mView;

    private String mUserNickName;
    private String mUserPortrait;


    private TextView mUserNickName_tv;
    private ImageView mUserPortrait_iv;
    private TextView mUserAge_tv;
    private TextView mUserRegion_tv;
    private TextView mUserGender_tv;
    private TextView mUserProperty_tv;
    private TextView mUserSignature_tv;
    private EditText mLeaveWords_et;

    private Button make_friend;

    private Context mContext;


    public PersonageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_personage, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: 进入person");
        //取出选中的用户id
        //SharedHelper shvalue = new SharedHelper(getActivity().getApplicationContext());
        //mUserID = shvalue.readValue().get("value");

        String result = getActivity().getIntent().getStringExtra("userID");
        mUserID = result;

        mUserNickName_tv=view.findViewById(R.id.user_nickname);
        mUserPortrait_iv=view.findViewById(R.id.user_portrait);
        mUserAge_tv=view.findViewById(R.id.user_age);
        mUserRegion_tv=view.findViewById(R.id.user_region);
        mUserGender_tv=view.findViewById(R.id.user_gender);
        mUserProperty_tv=view.findViewById(R.id.user_property);
        mUserSignature_tv=view.findViewById(R.id.user_signature);
        mLeaveWords_et=view.findViewById(R.id.leave_words);

        make_friend = view.findViewById(R.id.make_friend);
        make_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFriend("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=addfriend&m=socialchat");

            }
        });

        getDataPersonage("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=personage&m=socialchat");
    }

    public void initPersonage(View view,JSONObject jsonObject) throws JSONException {

        mUserNickName=jsonObject.getString("nickname");
        mUserPortrait=jsonObject.getString("portrait");


        mUserNickName_tv.setText(jsonObject.getString("nickname"));
        Glide.with(view)
                .asBitmap()
                .load(jsonObject.getString("portrait"))
                .into(mUserPortrait_iv);
        mUserAge_tv.setText(jsonObject.getString("age"));
        mUserRegion_tv.setText(jsonObject.getString("region"));
        mUserProperty_tv.setText(jsonObject.getString("property"));
        mUserGender_tv.setText(jsonObject.getString("gender"));
        mUserSignature_tv.setText(jsonObject.getString("signature"));

    }
    //网络数据部分
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //1是用户数据，2是幻灯片
            switch (msg.what){
                case 1:
                    try {
                        String resultJson1 = msg.obj.toString();
                        Log.d(TAG, "handleMessage: 用户数据接收的值"+msg.obj.toString());

                        JSONObject jsonObject = new ParseJSONObject(msg.obj.toString()).getParseJSON();
                        initPersonage(mView,jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Toast.makeText(mContext,"已申请好友",Toast.LENGTH_LONG);
                    break;
            }
        }
    };
    //TODO okhttp获取用户信息
    public void getDataPersonage(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("userid", mUserID)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.obj=response.body().string();
                    message.what=1;
                    Log.d(TAG, "run: getDataPersonage"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //TODO 添加好友
    public void makeFriend(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
                String yourid = shuserinfo.readUserInfo().get("userID");
                String yournickname = shuserinfo.readUserInfo().get("userNickName");
                String yourportrait = shuserinfo.readUserInfo().get("userPortrait");
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("myid", mUserID)
                        .add("yourid", yourid)
                        .add("yournickname", yournickname)
                        .add("yourportrait", yourportrait)
                        .add("yourwords",mLeaveWords_et.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.obj=response.body().string();
                    message.what=1;
                    Log.d(TAG, "run: getDataPersonage"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
