package xin.banghua.beiyuan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zolad.zoominimageview.ZoomInImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.R;
import xin.banghua.beiyuan.SharedPreferences.SharedHelper;

public class DongtaiAdapter extends RecyclerView.Adapter<DongtaiAdapter.ViewHolder>{
    private static final String TAG = "DongtaiAdapter";

    private Context mContext;

    ViewHolder viewHolder_btn;


    private List<DongtaiList> dongtaiLists;

    public DongtaiAdapter(Context mContext,List<DongtaiList> dongtaiList) {
        this.dongtaiLists = dongtaiList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_dongtai,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        final DongtaiList currentItem = dongtaiLists.get(i);

        viewHolder.userID.setText(currentItem.getMyid());
        Glide.with(mContext)
                .asBitmap()
                .load(currentItem.getMyportrait())
                .into(viewHolder.userPortrait);
        viewHolder.userNickName.setText(currentItem.getMynickname());
        viewHolder.dongtaiWord.setText(currentItem.getContext());
        Glide.with(mContext)
                .asBitmap()
                .load(currentItem.getPicture())
                .into(viewHolder.dongtaiImage);
        viewHolder.dongtaiTime.setText(currentItem.getTime());
        viewHolder.like.setText("赞"+currentItem.getLike());
        viewHolder.like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + currentItem.getId());
                //Toast.makeText(mContext, mUserID.get(i) + mUserNickName.get(i), Toast.LENGTH_LONG).show();
                viewHolder_btn = viewHolder;
                like("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=guangchanglike&m=socialchat",currentItem.getId());
            }
        });
        viewHolder.dongtaiLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: "+currentItem.getId());
                //Toast.makeText(mContext,mUserID.get(i)+mUserNickName.get(i),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dongtaiLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userID;
        CircleImageView userPortrait;
        TextView userNickName;
        TextView dongtaiWord;
        ZoomInImageView dongtaiImage;
        TextView dongtaiTime;
        TextView like;
        RelativeLayout dongtaiLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            userPortrait = itemView.findViewById(R.id.authportrait);
            userNickName = itemView.findViewById(R.id.userNickName);
            dongtaiWord = itemView.findViewById(R.id.dongtaiWord);
            dongtaiImage = itemView.findViewById(R.id.dongtaiImage);
            dongtaiTime = itemView.findViewById(R.id.dongtaiTime);
            like = itemView.findViewById(R.id.like);

            dongtaiLayout = itemView.findViewById(R.id.dongtai_layout);
        }
    }


    //网络数据部分
//处理返回的数据
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //1是用户数据，2是幻灯片
            switch (msg.what){
                case 1:
                    viewHolder_btn.like.setText("赞"+msg.obj.toString());
                    break;
            }
        }
    };
    //TODO okhttp点赞
    public void like(final String url,final String circleid){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("circleid", circleid)
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
}
