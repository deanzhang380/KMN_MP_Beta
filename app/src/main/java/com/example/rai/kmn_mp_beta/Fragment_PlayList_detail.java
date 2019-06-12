package com.example.rai.kmn_mp_beta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Fragment_PlayList_detail extends Fragment {
    View view;
    MainActivity mainActivity;
    ArrayList<Music> musicArrayList;
    Fragment_Playlist fragment_playlist;
    ListView listView;
    TextView txt_songname2;
    ImageButton btn_play2, btn_next2;
    ImageView imageView2;
    public int current_position = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.playlist_detail, container, false);
        Anhxa();
        Init();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.flag_playlist = 1;
                mainActivity.current_position = position;
                current_position = position;
                init_songbar();
                mainActivity.showNotification();
            }
        });
        btn_play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.MP_OnPause();
            }
        });
        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.NextSong();
            }
        });
        return view;
    }

    private void Anhxa() {
        listView = view.findViewById(R.id.playlist_detail_listview);
        txt_songname2 = view.findViewById(R.id.song_name2);
        btn_play2 = view.findViewById(R.id.song_play2);
        btn_next2 = view.findViewById(R.id.song_next2);
        imageView2 = view.findViewById(R.id.song_image2);

    }

    private void Init() {
        mainActivity = MainActivity.getInstance();
        musicArrayList = mainActivity.list_temp;
        MusicListView musicListView = new MusicListView(getContext(), R.layout.music_item, musicArrayList);
        listView.setAdapter(musicListView);
    }

    public void CreateList(String key) {

    }

    public void init_songbar() {
        mainActivity.PlayNhacMp3(musicArrayList.get(current_position).getPath(), mainActivity.GetMediaPlayer(), musicArrayList.get(current_position).getName());
        imageView2.setImageBitmap(musicArrayList.get(current_position).getPicture());
        txt_songname2.setText(musicArrayList.get(current_position).getName());
        btn_play2.setImageResource(R.drawable.pause2);
    }

}
