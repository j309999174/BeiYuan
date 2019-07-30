package xin.banghua.beiyuan.Main4Branch;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import androidx.navigation.Navigation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.R;
import xin.banghua.beiyuan.SharedPreferences.SharedHelper;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FabudongtaiFragment extends Fragment {

    private static final String TAG = "FabudongtaiFragment";

    public Map<String,String> userInfo;
    private SharedHelper sh;

    RadioGroup guangchang_rg;
    RadioButton yes_rb,no_rb;

    View mView;

    EditText dongtaiWord_et;
    ImageView dongtaiImage_iv;

    Button release_btn;

    String userID,userNickname,userPortrait,dongtaiWord,dongtaiImage,dongtaiShare;
    public FabudongtaiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fabudongtai, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dongtaiWord_et = mView.findViewById(R.id.dongtai_word);
        dongtaiImage_iv = mView.findViewById(R.id.dongtai_image);
        guangchang_rg = mView.findViewById(R.id.guangchang_rg);
        yes_rb = mView.findViewById(R.id.yes_rb);
        no_rb = mView.findViewById(R.id.no_rb);
        release_btn = mView.findViewById(R.id.release_btn);
        dongtaiImage_iv.setImageResource(R.drawable.plus);
        dongtaiImage = "";

        dongtaiImage_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
            }
        });

        release_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sh = new SharedHelper(mView.getContext());
                userInfo = sh.readUserInfo();
                Log.d(TAG, "onClick: 输出本地用户信息："+userInfo.toString());
                userID = userInfo.get("userID");
                userNickname = userInfo.get("userNickName");
                userPortrait = userInfo.get("userPortrait");
                dongtaiWord = dongtaiWord_et.getText().toString();
                dongtaiShare = ((RadioButton)mView.findViewById(guangchang_rg.getCheckedRadioButtonId())).getText().toString();
                Log.d(TAG, "onClick: 分享的选项："+((RadioButton)mView.findViewById(guangchang_rg.getCheckedRadioButtonId())).getText().toString());
                Log.d(TAG, "onClick: 又分享的谢谢"+(mView.findViewById(guangchang_rg.getCheckedRadioButtonId())).toString());

                postFabudongtai("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=fabudongtai&m=socialchat");

            }
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("进入handler", "handler");
            if (msg.arg1==1) {
                Log.d("跳转", "Navigation");
                Navigation.findNavController(mView).navigate(R.id.fubudongtai_guangchang_action);
            }

        }
    };

    //TODO 注册 form形式的post
    public void postFabudongtai(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                //获取文件名
                Log.d("进入run","run");
                File tempFile =new File(dongtaiImage.trim());
                String fileName = tempFile.getName();
                //开始网络传输
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("userID", userID)
                        .addFormDataPart("userNickname", userNickname)
                        .addFormDataPart("userPortrait", userPortrait)
                        .addFormDataPart("dongtaiWord", dongtaiWord)
                        .addFormDataPart("dongtaiShare", dongtaiShare)
                        .addFormDataPart("dongtaiImage",fileName,RequestBody.create(new File(dongtaiImage),MEDIA_TYPE_PNG))
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Log.d("进入try","try");
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    //Log.d("form形式的post",response.body().string());
                    //格式：{"error":"0","info":"登陆成功"}
                    Message message=handler.obtainMessage();
                    message.arg1=1;
                    Log.d("动态",userID+"/"+userPortrait+"/"+userNickname+"/"+"/"+"/"+dongtaiShare+"/"+dongtaiWord);
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: 动态图片地址");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            //Your Code
            ListIterator<String> listIterator = mPaths.listIterator();
            while (listIterator.hasNext()){
                String mPath = listIterator.next();

                Log.d("path", mPath);
                dongtaiImage_iv.setImageURI(Uri.parse(mPath));
                dongtaiImage = mPath;
                //Log.d(TAG, "onActivityResult: 动态图片地址"+dongtaiImage);
            }
        }
    }
}
