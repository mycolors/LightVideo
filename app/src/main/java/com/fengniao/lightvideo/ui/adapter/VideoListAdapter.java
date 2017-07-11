package com.fengniao.lightvideo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fengniao.lightvideo.R;
import com.fengniao.lightvideo.bean.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Video> mList;
    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public VideoListAdapter(Context mContext, List<Video> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_video, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
       if (mOnItemClickListener!= null){
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   mOnItemClickListener.onItemClick(holder.getAdapterPosition());
               }
           });
       }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title)
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
