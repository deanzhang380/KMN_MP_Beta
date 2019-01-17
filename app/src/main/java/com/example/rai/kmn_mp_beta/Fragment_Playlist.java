package com.example.rai.kmn_mp_beta;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Fragment_Playlist extends Fragment {
    View view;
    ImageButton imageButton;
    GridView gridView;
    PlayListAdapter playListAdapter;
    int num_playlist = 0;
    MainActivity mainActivity;
    FragmentManager fragmentManager;
    EditText editText;
    Button btn_accept, btn_cancel;
    String name = "";
    static ArrayList<PlayList> playListArrayList;
    final ArrayList<Music> musicArrayList = new ArrayList<Music>();

    ArrayList<PlayList> lists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.playlist, container, false);

        mainActivity = MainActivity.getInstance();
        fragmentManager = mainActivity.fragmentManager;
        //TinyDB tinyDB= new TinyDB(getContext());
        anhxa();
        try {
            playListArrayList = mainActivity.getArrayList("playlist");
            if (playListArrayList == null) {
                playListArrayList = new ArrayList<>();
            }
            playListAdapter = new PlayListAdapter(getContext(), R.layout.dong_grid_playlist, playListArrayList);
            gridView.setAdapter(playListAdapter);
        } catch (Exception e) {

        }


        //ArrayList<Music> musicArrayList= new ArrayList<>();
        //playListArrayList.add(new PlayList(BitmapFactory.decodeResource(getResources(), R.drawable.playlist),"AAA",musicArrayList));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.create_playlist_dialog);
                dialog.setTitle("Create Play List");
                dialog.setCanceledOnTouchOutside(false);
                editText = dialog.findViewById(R.id.edittextuser);
                btn_accept = dialog.findViewById(R.id.buttonxacnhan);
                btn_cancel = dialog.findViewById(R.id.buttonhuy);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String playlist_name = editText.getText().toString();
                        name = playlist_name;
                        playListArrayList.add(new PlayList(BitmapFactory.decodeResource(getResources(), R.drawable.playlist), name, musicArrayList));
                        playListAdapter = new PlayListAdapter(getContext(), R.layout.dong_grid_playlist, playListArrayList);
                        gridView.setAdapter(playListAdapter);
                        mainActivity.saveArrayList(playListArrayList, ("playlist"));
                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.current_fragment = 4;
                mainActivity.list_temp = playListArrayList.get(position).getMusic();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_PlayList_detail fragment_playList_detail = new Fragment_PlayList_detail();
                fragmentTransaction.replace(R.id.main_view, fragment_playList_detail, "fragment_playlist_detail");
                fragmentTransaction.commit();
            }
        });
        return view;

    }

    private void anhxa() {
        imageButton = view.findViewById(R.id.add_playlist);
        gridView = view.findViewById(R.id.playlist_gridview);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
