package xin.banghua.beiyuan.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import xin.banghua.beiyuan.R;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "FriendAdapter";
    //用户id,头像，昵称
//    private ArrayList<String> mUserID = new ArrayList<>();
//    private ArrayList<String> mUserPortrait = new ArrayList<>();
//    private ArrayList<String> mUserNickName = new ArrayList<>();


    private List<FriendList> friendList;
    private List<FriendList> friendListFull;


    private Context mContext;

    public FriendAdapter(Context mContext, List<FriendList> friendList) {

        this.mContext = mContext;
        this.friendList = friendList;
        this.friendListFull = new ArrayList<>(friendList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_haoyou,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        final FriendList currentItem = friendList.get(i);

        viewHolder.userID.setText(currentItem.getmUserID());
        Glide.with(mContext)
                .asBitmap()
                .load(currentItem.getmUserPortrait())
                .into(viewHolder.userPortrait);
        viewHolder.userNickName.setText(currentItem.getmUserNickName());
        viewHolder.userLeaveWords.setText("");


        viewHolder.haoyouLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: rongyun开始");
                //启动会话界面
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(mContext, currentItem.getmUserID(), currentItem.getmUserNickName());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    //过滤器
    @Override
    public Filter getFilter() {
        return filter;
    }

    //过滤器
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FriendList> filteredList = new ArrayList();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(friendListFull);
            }else {
                String filterPattern = constraint.toString().trim();
                for (FriendList item : friendListFull){
                    if (item.getmUserNickName().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            friendList.clear();
            friendList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userID;
        CircleImageView userPortrait;
        TextView userNickName;
        TextView userLeaveWords;

        RelativeLayout haoyouLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            userPortrait = itemView.findViewById(R.id.authportrait);
            userNickName = itemView.findViewById(R.id.userNickName);
            userLeaveWords = itemView.findViewById(R.id.userLeaveWords);


            haoyouLayout = itemView.findViewById(R.id.haoyou_layout);
        }
    }
}
