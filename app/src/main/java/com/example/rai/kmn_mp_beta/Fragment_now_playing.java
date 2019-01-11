package com.example.rai.kmn_mp_beta;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.animation.Animation;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_now_playing extends Fragment {
    Handler han= new Handler();
    TextView txt_songname,txt_duration_max;
    ImageButton btn_play,btn_next,btn_pre,btn_shuffle,btn_repeat;
    View view;
    MediaPlayer mp;
    SeekBar sb;
    ArrayList<Music> musiclist;
    int current;
    String song_name;
    int Total_duration,flag_rp_sf=0;
    Runnable mr;
   // ImageView imageView;
    CircleImageView circleImageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.now_playing,container,false);
        Anhxa();
        InitView();
        sb.setProgress(((MainActivity) getActivity()).GetMediaPlayer().getCurrentPosition()/1000);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    btn_play.setImageResource(R.drawable.img_btn_play);
                    mp.pause();
                }else{
                    btn_play.setImageResource(R.drawable.img_btn_pause);
                    mp.start();
                    playCycle();
                }

            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).NextSong();
                InitView();
            }
        });
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).PreSong();
                InitView();
            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input){
                    mp.seekTo(progress*1000);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                ((MainActivity) getActivity()).NextSong();
                song_name=((MainActivity) getActivity()).GetName();
                Total_duration=((MainActivity) getActivity()).GetDuration();
                InitView();
            }
        });
        //startAnimationFor(imageView,R.anim.rotatedisc);
        return view;
    }
    void playCycle(){
       sb.setProgress(mp.getCurrentPosition()/1000);
        if(mp.isPlaying()){
            mr= new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            han.postDelayed(mr,1000);
        }
    }
    void Anhxa(){
        txt_songname=view.findViewById(R.id.songTitle);
        btn_play=view.findViewById(R.id.btnPlay);
        btn_next=view.findViewById(R.id.btnNext);
        btn_pre=view.findViewById(R.id.btnPrevious);
        sb=view.findViewById(R.id.songProgressBar);
        txt_duration_max=view.findViewById(R.id.songTotalDurationLabel);
        //circleImageView=view.findViewById(R.id.circle_disc);
        circleImageView=view.findViewById(R.id.round_disc);

    }

    void InitView(){
        mp=((MainActivity)getActivity()).GetMediaPlayer();
        song_name=((MainActivity) getActivity()).GetName();
        Total_duration=((MainActivity) getActivity()).GetDuration();
        sb.setMax(Total_duration);
            playCycle();
            try {
                circleImageView.setImageBitmap(((MainActivity) getActivity()).GetMP3Image());
                txt_songname.setText(song_name);
                int min, second;
                min = Total_duration / 60;
                second = Total_duration % 60;
                txt_duration_max.setText(String.valueOf(min) + ":" + String.valueOf(second));
            } catch (Exception e) {

            }
            btn_play.setImageResource(R.drawable.img_btn_pause);
    }
    public void  startAnimationFor(ImageView imageView , int fileAni){
        final Animation animation=AnimationUtils.loadAnimation( getActivity(),fileAni);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    v.startAnimation(animation);
            }
        });
    }

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
