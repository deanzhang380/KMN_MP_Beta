package com.example.rai.kmn_mp_beta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    String urlVideo;
    YouTubePlayerView youTubePlayerView;
    int Request_Code_ErorUser = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeplayerview);

        Intent intent = getIntent();
        if (intent != null) {
            urlVideo = intent.getStringExtra("resourceid");
            youTubePlayerView.initialize(MainActivity.Youtube_API_Key, this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(urlVideo);
        youTubePlayer.setFullscreen(true);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(YoutubeActivity.this, Request_Code_ErorUser);
        } else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Request_Code_ErorUser) {
            youTubePlayerView.initialize(MainActivity.Youtube_API_Key, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
