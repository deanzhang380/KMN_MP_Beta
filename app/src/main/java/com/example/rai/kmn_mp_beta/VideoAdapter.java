package com.example.rai.kmn_mp_beta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KhoaPhamPC on 18/10/2017.
 */

public class VideoAdapter extends BaseAdapter {
    Context context;
    ArrayList<Video> videoArrayList;

    public VideoAdapter(Context context, ArrayList<Video> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @Override
    public int getCount() {
        return videoArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return videoArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHolder {
        TextView txttitle;
        ImageView imgvideo;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.dong_video, null);
            viewHolder = new ViewHolder();
            viewHolder.txttitle = view.findViewById(R.id.textviewtitle);
            viewHolder.imgvideo = view.findViewById(R.id.imageviewvideo);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Video video = (Video) getItem(i);
        viewHolder.txttitle.setText(video.getTitle());
        Picasso.with(context).load(video.getUrlImage()).into(viewHolder.imgvideo);
        return view;
    }
}
