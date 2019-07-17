package xin.banghua.beiyuan.Adapter;

import android.content.Context;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import xin.banghua.beiyuan.R;

public class DongtaiAdapter extends RecyclerView.Adapter<DongtaiAdapter.ViewHolder>{
    private static final String TAG = "DongtaiAdapter";
    //用户id,头像，昵称，动态文字，动态图片，动态时间
    private ArrayList<String> mUserID = new ArrayList<>();
    private ArrayList<String> mUserPortrait = new ArrayList<>();
    private ArrayList<String> mUserNickName = new ArrayList<>();
    private ArrayList<String> mDongtaiWord = new ArrayList<>();
    private ArrayList<String> mDongtaiImage = new ArrayList<>();
    private ArrayList<String> mDongtaiTime = new ArrayList<>();
    private Context mContext;

    public DongtaiAdapter(Context mContext,ArrayList<String> mUserID, ArrayList<String> mUserPortrait, ArrayList<String> mUserNickName, ArrayList<String> mDongtaiWord, ArrayList<String> mDongtaiImage, ArrayList<String> mDongtaiTime) {
        this.mUserID = mUserID;
        this.mUserPortrait = mUserPortrait;
        this.mUserNickName = mUserNickName;
        this.mDongtaiWord = mDongtaiWord;
        this.mDongtaiImage = mDongtaiImage;
        this.mDongtaiTime = mDongtaiTime;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        viewHolder.userID.setText(mUserID.get(i));
        Glide.with(mContext)
                .asBitmap()
                .load(mUserPortrait.get(i))
                .into(viewHolder.userPortrait);
        viewHolder.userNickName.setText(mUserNickName.get(i));
        viewHolder.dongtaiWord.setText(mDongtaiWord.get(i));
        Glide.with(mContext)
                .asBitmap()
                .load(mDongtaiImage.get(i))
                .into(viewHolder.dongtaiImage);
        viewHolder.dongtaiTime.setText(mDongtaiTime.get(i));

        viewHolder.dongtaiLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: "+mUserID.get(i));
                Toast.makeText(mContext,mUserID.get(i)+mUserNickName.get(i),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserID.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userID;
        CircleImageView userPortrait;
        TextView userNickName;
        TextView dongtaiWord;
        ZoomInImageView dongtaiImage;
        TextView dongtaiTime;
        RelativeLayout dongtaiLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            userPortrait = itemView.findViewById(R.id.authportrait);
            userNickName = itemView.findViewById(R.id.userNickName);
            dongtaiWord = itemView.findViewById(R.id.dongtaiWord);
            dongtaiImage = itemView.findViewById(R.id.dongtaiImage);
            dongtaiTime = itemView.findViewById(R.id.dongtaiTime);

            dongtaiLayout = itemView.findViewById(R.id.dongtai_layout);
        }
    }
}
