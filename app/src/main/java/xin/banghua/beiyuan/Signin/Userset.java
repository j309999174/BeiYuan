package xin.banghua.beiyuan.Signin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.MainActivity;
import xin.banghua.beiyuan.R;
import xin.banghua.beiyuan.SharedPreferences.SharedHelper;

public class Userset extends AppCompatActivity {
    private Context mContext;
    //昵称，年龄，地区
    EditText userNickname_et,userAge_et,userRegion_et,userSignature_et;
    //性别，标签
    RadioGroup userGender_rg,userProperty_rg;
    RadioButton male_rb,female_rb,zProperty_rb,bProperty_rb,dProperty_rb;

    //
    String logtype,userAccount,userPassword,userNickname,userAge,userRegion,userGender,userProperty,userPortrait,userSignature;
    Button submit_btn;
    //
    ImageView userPortrait_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userset);

        userPortrait = "";

        mContext = getApplicationContext();


        userNickname_et = findViewById(R.id.userNickName);
        userAge_et = findViewById(R.id.userAge);
        userRegion_et = findViewById(R.id.userRegion);
        userGender_rg = findViewById(R.id.userGender);
        userProperty_rg = findViewById(R.id.userProperty);
        male_rb = findViewById(R.id.male);
        female_rb = findViewById(R.id.female);
        zProperty_rb = findViewById(R.id.zProperty);
        bProperty_rb = findViewById(R.id.bProperty);
        dProperty_rb = findViewById(R.id.dProperty);
        submit_btn = findViewById(R.id.submit_btn);
        userPortrait_iv = findViewById(R.id.userPortrait);
        userPortrait_iv.setImageResource(R.drawable.favicon);
        userSignature_et = findViewById(R.id.userSignature);



        Intent getIntent = getIntent();
        logtype = getIntent.getStringExtra("logtype");
        userAccount = getIntent.getStringExtra("userAccount");
        userPassword = getIntent.getStringExtra("userPassword");

        Log.d("账号密码", userAccount+"/"+userPassword);

        userPortrait_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(Userset.this)
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


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNickname = userNickname_et.getText().toString();
                userAge = userAge_et.getText().toString();
                userRegion = userRegion_et.getText().toString();
                userSignature = userSignature_et.getText().toString();
                userGender = ((RadioButton) findViewById(userGender_rg.getCheckedRadioButtonId())).getText().toString();
                userProperty = ((RadioButton) findViewById(userProperty_rg.getCheckedRadioButtonId())).getText().toString();

                if (logtype.equals("1")){
                    postSignUp("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=signup&m=socialchat");
                }
            }
        });


    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("进入handler", "handler");
                if (msg.arg1==1) {
                    Log.d("跳转", "intent");

                    Intent intent = new Intent(Userset.this, MainActivity.class);
                    startActivity(intent);
                }

        }
    };

    //TODO 注册 form形式的post
    public void postSignUp(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                //获取文件名
                Log.d("进入run","run");
                File tempFile =new File(userPortrait.trim());
                String fileName = tempFile.getName();
                //开始网络传输
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("userAccount", userAccount)
                        .addFormDataPart("userPassword", userPassword)
                        .addFormDataPart("userNickname", userNickname)
                        .addFormDataPart("userAge", userAge)
                        .addFormDataPart("userRegion", userRegion)
                        .addFormDataPart("userGender", userGender)
                        .addFormDataPart("userProperty", userProperty)
                        .addFormDataPart("userSignature", userSignature)
                        .addFormDataPart("userPortrait",fileName,RequestBody.create(new File(userPortrait),MEDIA_TYPE_PNG))
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
                    Log.d("用户信息",userAccount+"/"+userPassword+"/"+userNickname+"/"+userAge+"/"+userRegion+"/"+userGender+"/"+userProperty);
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            //Your Code
            ListIterator<String> listIterator = mPaths.listIterator();
            while (listIterator.hasNext()){
                String mPath = listIterator.next();
                Log.d("path", mPath);
                userPortrait_iv.setImageURI(Uri.parse(mPath));
                userPortrait = mPath;
            }
        }
    }
}
