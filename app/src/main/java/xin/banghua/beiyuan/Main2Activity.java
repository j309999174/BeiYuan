package xin.banghua.beiyuan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.Adapter.FriendAdapter;
import xin.banghua.beiyuan.Adapter.FriendList;
import xin.banghua.beiyuan.Main2Branch.NewFriend;
import xin.banghua.beiyuan.ParseJSON.ParseJSONArray;
import xin.banghua.beiyuan.SharedPreferences.SharedHelper;

public class Main2Activity extends AppCompatActivity implements RongIM.UserInfoProvider{
    private static final String TAG = "Main2Activity";


    private List<FriendList> friendList = new ArrayList<>();

    private FriendAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_shenbian:

                    Intent intent1 = new Intent(Main2Activity.this, MainActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_haoyou:

                    //Intent intent2 = new Intent(Main2Activity.this, Main2Activity.class);
                    //startActivity(intent2);
                    return true;
                case R.id.navigation_xiaoxi:

                    Intent intent3 = new Intent(Main2Activity.this, Main3Activity.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_dongtai:

                    Intent intent4 = new Intent(Main2Activity.this, Main4Activity.class);
                    startActivity(intent4);
                    return true;
                case R.id.navigation_wode:

                    Intent intent5 = new Intent(Main2Activity.this, Main5Activity.class);
                    startActivity(intent5);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_haoyou);

        getDataFriends("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=friends&m=socialchat");

        Button newFriend = findViewById(R.id.new_friend);
        newFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, NewFriend.class);
                startActivity(intent);
            }
        });

        SearchView searchView = findViewById(R.id.friend_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        RongIM.setUserInfoProvider(this,true);


    }


    //TODO 初始化用户列表
    private void initFriends(View view, JSONArray jsonArray) throws JSONException {
        Log.d(TAG, "initFriends: ");

        if (jsonArray.length()>0){
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FriendList friends = new FriendList(jsonObject.getString("yourid"),jsonObject.getString("yourportrait"),jsonObject.getString("yournickname"));
                friendList.add(friends);
            }
        }


        initRecyclerView(view);
    }

    //TODO 初始化好友recyclerview
    private void initRecyclerView(View view){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        final PullLoadMoreRecyclerView recyclerView = view.findViewById(R.id.haoyou_RecyclerView);
        adapter = new FriendAdapter(Main2Activity.this,friendList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLinearLayout();
        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

                Log.d(TAG, "onRefresh: start");
                recyclerView.setPullLoadMoreCompleted();
            }

            @Override
            public void onLoadMore() {

                Log.d(TAG, "onLoadMore: start");
                recyclerView.setPullLoadMoreCompleted();
            }
        });
    }
    //处理返回的数据
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

                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        initFriends(getWindow().getDecorView(),jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    //TODO okhttp获取用户信息
    public void getDataFriends(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shvalue = new SharedHelper(getApplicationContext());
                String userID = shvalue.readUserInfo().get("userID");
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("myid", userID)
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
                    Log.d(TAG, "run: Userinfo发送的值"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public UserInfo getUserInfo(String s) {
        //自己的头像也是在列表中选取，所以需要加入自己的信息
        SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
        String myid = shuserinfo.readUserInfo().get("userID");
        String mynickname = shuserinfo.readUserInfo().get("userNickName");
        String myportrait = shuserinfo.readUserInfo().get("userPortrait");
        FriendList me = new FriendList(myid,myportrait,mynickname);
        friendList.add(me);
        //获取用户信息
        for (FriendList i:friendList){
            if (i.getmUserID().equals(s)){
                Log.d(TAG, "getUserInfo: 进入"+s);
                return new UserInfo(i.getmUserID(),i.getmUserNickName(), Uri.parse(i.getmUserPortrait()));
            }
        }
        Log.d(TAG, "getUserInfo: 没进入"+s);
        return null;
    }
}
