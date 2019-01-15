package com.example.rai.kmn_mp_beta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlayListAdapter extends ArrayAdapter<PlayList> {

    public PlayListAdapter(@NonNull Context context, int resource, List<PlayList> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.dong_grid_playlist, null);

        ImageView imageView = convertView.findViewById(R.id.imageviewThuvienanh);
        TextView textView = convertView.findViewById(R.id.playlist_name);
        PlayList playList = getItem(position);

        imageView.setImageBitmap(playList.getImage());
        textView.setText(playList.getName());
        return convertView;
    }
}
