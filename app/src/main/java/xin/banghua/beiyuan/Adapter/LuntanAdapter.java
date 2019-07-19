package xin.banghua.beiyuan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xin.banghua.beiyuan.Main4Branch.PostListActivity;
import xin.banghua.beiyuan.Personage.PersonageActivity;
import xin.banghua.beiyuan.R;

public class LuntanAdapter extends RecyclerView.Adapter<LuntanAdapter.ViewHolder> {
    private static final String TAG = "LuntanAdapter";

    private List<LuntanList> luntanLists;
    private Context mContext;

    public LuntanAdapter(List<LuntanList> luntanLists, Context mContext) {
        Log.d(TAG, "LuntanAdapter: start");
        this.luntanLists = luntanLists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_luntan,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final LuntanList currentItem = luntanLists.get(i);

        viewHolder.id.setText(currentItem.getId());
        viewHolder.plateid.setText(currentItem.getPlateid());
        viewHolder.platename.setText(currentItem.getPlatename());
        viewHolder.authid.setText(currentItem.getAuthid());
        viewHolder.authnickname.setText(currentItem.getAuthnickname());
        Glide.with(mContext)
                .asBitmap()
                .load(currentItem.getAuthportrait())
                .into(viewHolder.authportrait);
        viewHolder.posttip.setText(currentItem.getPosttip());
        viewHolder.posttitle.setText(currentItem.getPosttitle());
        viewHolder.posttext.setText(currentItem.getPosttext());
        if (currentItem.getPostpicture()[0].isEmpty()){
            viewHolder.postpicture.setVisibility(View.GONE);
        }else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getPostpicture()[0])
                    .into(viewHolder.postpicture);
        }
        viewHolder.like.setText(currentItem.getLike());
        viewHolder.favorite.setText(currentItem.getFavorite());
        viewHolder.time.setText(currentItem.getTime());


        viewHolder.authnickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PersonageActivity.class);
                intent.putExtra("userID",currentItem.getAuthid());
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.authportrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PersonageActivity.class);
                intent.putExtra("userID",currentItem.getAuthid());
                v.getContext().startActivity(intent);
            }
        });

        viewHolder.posttitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostListActivity.class);
                intent.putExtra("postid",currentItem.getId());
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.posttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostListActivity.class);
                intent.putExtra("postid",currentItem.getId());
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.postpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostListActivity.class);
                intent.putExtra("postid",currentItem.getId());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return luntanLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView plateid;
        TextView platename;
        TextView authid;
        TextView authnickname;
        CircleImageView authportrait;
        TextView posttip;
        TextView posttitle;
        TextView posttext;
        ImageView postpicture;
        TextView like;
        TextView favorite;
        TextView time;

        RelativeLayout luntanLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            plateid = itemView.findViewById(R.id.plateid);
            platename = itemView.findViewById(R.id.platename);
            authid = itemView.findViewById(R.id.authid);
            authnickname = itemView.findViewById(R.id.authnickname);
            authportrait = itemView.findViewById(R.id.authportrait);
            posttip = itemView.findViewById(R.id.posttip);
            posttitle = itemView.findViewById(R.id.posttitle);
            posttext = itemView.findViewById(R.id.posttext);
            postpicture = itemView.findViewById(R.id.postpicture);
            like = itemView.findViewById(R.id.like);
            favorite = itemView.findViewById(R.id.favorite);
            time = itemView.findViewById(R.id.time);

            luntanLayout = itemView.findViewById(R.id.luntanLayout);


        }
    }

}
