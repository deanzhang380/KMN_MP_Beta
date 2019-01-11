package com.example.rai.kmn_mp_beta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.v4.widget.ExploreByTouchHelper.INVALID_ID;

public class MusicListView extends ArrayAdapter<Music> {
    Context context;
    int layout;
    ArrayList<Music> ms;
    HashMap<Music, Integer> mIdMap = new HashMap<Music, Integer>();

    public MusicListView(Context context, int layout, ArrayList<Music> ms) {
        super(context,layout,ms);
        this.context = context;
        this.layout = layout;
        for (int i = 0; i < ms.size(); ++i) {
            mIdMap.put(ms.get(i), i);
        }
    }

    @Override
    public int getCount() {
        return mIdMap.size();
    }


    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Music item =  getItem(position);
        return mIdMap.get(item);
    }

    public long getIDFromName(String S){
        int i=0;
        for (; i<mIdMap.size();i++){
            String temp=mIdMap.get(i).getClass().getName();
            if(temp.indexOf(S)!=-1){

            }

            break;
        }
        return i;
    }


    class ViewHolder{
        ImageView imgview;
        TextView txtName,txtArtist;

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh=null ;
        if(view==null){

            LayoutInflater layoutInflater= LayoutInflater.from(context);
            view=layoutInflater.inflate(layout,null);
            vh=new ViewHolder();
            vh.imgview=view.findViewById(R.id.img);
            vh.txtName=view.findViewById(R.id.name);
            vh.txtArtist=view.findViewById(R.id. artist);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        Music music=(Music)getItem(position);
        vh.imgview.setImageBitmap(music.getPicture());
        vh.txtName.setText(music.getName());
        vh.txtArtist.setText(music.getArtist());
        return view;
    }
}
