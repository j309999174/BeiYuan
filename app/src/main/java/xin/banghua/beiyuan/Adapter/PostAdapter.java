package xin.banghua.beiyuan.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xin.banghua.beiyuan.R;

public class PostAdapter extends RecyclerView.Adapter{
    private static final String TAG = "PostAdapter";
    private final static int HEAD_COUNT = 1;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;

    private List<PostHead> postHeads;
    private Context mContext;


    public PostAdapter(Context context, List<PostHead> postHeads) {
        Log.d(TAG, "PostAdapter: ");

        this.mContext = context;
        this.postHeads = postHeads;
    }


    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: position"+position);
        if ( position == 0){ // 头部
            Log.d(TAG, "getItemViewType: 头");
            return TYPE_HEAD;
        }else{
            Log.d(TAG, "getItemViewType: 身");
            return TYPE_CONTENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_HEAD){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_posthead,viewGroup,false);
            PostAdapter.HeadHolder viewHolder = new PostAdapter.HeadHolder(view);
            return viewHolder;
        }else if(i == TYPE_CONTENT){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_postlist,viewGroup,false);
            PostAdapter.ContentHolder viewHolder = new PostAdapter.ContentHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof PostAdapter.HeadHolder){ // 头部
            final PostHead currentItem = postHeads.get(i);
            ((HeadHolder) viewHolder).posttitle.setText(currentItem.getPosttitle());
            ((HeadHolder) viewHolder).authid.setText(currentItem.getAuthid());
            ((HeadHolder) viewHolder).authnickname.setText(currentItem.getAuthnickname());
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getAuthportrait())
                    .into(((HeadHolder) viewHolder).authportrait);
            ((HeadHolder) viewHolder).posttext.setText(currentItem.getPosttext());
            if (currentItem.getPostpicture1()!=""){
                Glide.with(mContext)
                        .asBitmap()
                        .load(currentItem.getPostpicture1())
                        .into(((HeadHolder) viewHolder).postpicture1);
                ((HeadHolder) viewHolder).postpicture1.setVisibility(View.VISIBLE);
            }
            if (currentItem.getPostpicture2()!=""){
                Glide.with(mContext)
                        .asBitmap()
                        .load(currentItem.getPostpicture2())
                        .into(((HeadHolder) viewHolder).postpicture2);
                ((HeadHolder) viewHolder).postpicture2.setVisibility(View.VISIBLE);
            }
            if (currentItem.getPostpicture3()!=""){
                Glide.with(mContext)
                        .asBitmap()
                        .load(currentItem.getPostpicture3())
                        .into(((HeadHolder) viewHolder).postpicture3);
                ((HeadHolder) viewHolder).postpicture3.setVisibility(View.VISIBLE);
            }
            ((HeadHolder) viewHolder).time.setText(currentItem.getTime());
        }else if(viewHolder instanceof PostAdapter.ContentHolder){ // 内容
            final PostHead currentItem = postHeads.get(i);
            ((ContentHolder) viewHolder).authid.setText(currentItem.getAuthid());
            ((ContentHolder) viewHolder).authnickname.setText(currentItem.getAuthnickname());
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getAuthportrait())
                    .into(((ContentHolder) viewHolder).authportrait);
            ((ContentHolder) viewHolder).followtext.setText(currentItem.getFollowtext());
            if (currentItem.getFollowpicture1()!=""){
                Glide.with(mContext)
                        .asBitmap()
                        .load(currentItem.getFollowpicture1())
                        .into(((ContentHolder) viewHolder).followpicture1);
                ((ContentHolder) viewHolder).followpicture1.setVisibility(View.VISIBLE);
            }
            if (currentItem.getFollowpicture2()!=""){
                Glide.with(mContext)
                        .asBitmap()
                        .load(currentItem.getFollowpicture2())
                        .into(((ContentHolder) viewHolder).followpicture2);
                ((ContentHolder) viewHolder).followpicture2.setVisibility(View.VISIBLE);
            }
            if (currentItem.getFollowpicture3()!=""){
                Glide.with(mContext)
                        .asBitmap()
                        .load(currentItem.getFollowpicture3())
                        .into(((ContentHolder) viewHolder).followpicture3);
                ((ContentHolder) viewHolder).followpicture3.setVisibility(View.VISIBLE);
            }
            ((ContentHolder) viewHolder).time.setText(currentItem.getTime());

        }
    }

    @Override
    public int getItemCount() {

        return postHeads.size();
    }


    // 头部
    private class HeadHolder extends RecyclerView.ViewHolder{
        TextView posttitle;
        TextView authid;
        TextView authnickname;
        CircleImageView authportrait;
        TextView posttext;
        ImageView postpicture1;
        ImageView postpicture2;
        ImageView postpicture3;
        TextView time;

        public HeadHolder(View itemView) {
            super(itemView);

            authid = itemView.findViewById(R.id.authid);
            authnickname = itemView.findViewById(R.id.authnickname);
            authportrait = itemView.findViewById(R.id.authportrait);
            posttitle = itemView.findViewById(R.id.posttitle);
            posttext = itemView.findViewById(R.id.posttext);
            postpicture1 = itemView.findViewById(R.id.postpicture1);
            postpicture2 = itemView.findViewById(R.id.postpicture2);
            postpicture3 = itemView.findViewById(R.id.postpicture3);
            time = itemView.findViewById(R.id.time);

        }
    }

    // 内容
    private class ContentHolder extends RecyclerView.ViewHolder{
        TextView authid;
        TextView authnickname;
        CircleImageView authportrait;
        TextView followtext;
        ImageView followpicture1;
        ImageView followpicture2;
        ImageView followpicture3;
        TextView time;

        public ContentHolder(View itemView) {
            super(itemView);
            authid = itemView.findViewById(R.id.authid);
            authnickname = itemView.findViewById(R.id.authnickname);
            authportrait = itemView.findViewById(R.id.authportrait);
            followtext = itemView.findViewById(R.id.followtext);
            followpicture1 = itemView.findViewById(R.id.followpicture1);
            followpicture2 = itemView.findViewById(R.id.followpicture2);
            followpicture3 = itemView.findViewById(R.id.followpicture3);
            time = itemView.findViewById(R.id.time);
        }
    }


}
