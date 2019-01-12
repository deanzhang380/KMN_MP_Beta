package com.example.rai.kmn_mp_beta;

import android.Manifest;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;

import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_Request_code=1;
    private static final int Record_Request_code=2;
    private static final int Alert_Request_code=3;
    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private int currentState;
    private static MainActivity instance;
    private int notification_id;
    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;

    ArrayList<Music> musicArrayList;
    CustomMusicList    lv;
    MusicListView adapter;
    TextView txt_songname;
    ImageButton btn_play,btn_next;
    int current_position=0;
    DrawerLayout drawerLayout;
    RelativeLayout song_bar_layout;
    Bundle bundle;
    MediaPlayer mediaPlayer,mp1;
    int Total_duration=0;
    android.app.Fragment fragment= null;
    Intent svc;
    public int flag_rd = 0;
    public int flag_loop = 0;

    HashMap<ArrayList<Music>, Integer> playlist = new HashMap<>();

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instance=this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Anhxa();
        musicArrayList= new ArrayList<Music>();
        mediaPlayer=new MediaPlayer();
        initializeTextToSpeech();

        svc = new Intent(this, OverlayShowingService.class);

        initializeSpeechRecognize();


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_Request_code);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_Request_code);
            }


        }
        else{
            doStuff();
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(MainActivity.this,musicArrayList.get(position).getPath(), Toast.LENGTH_SHORT).show();
                current_position=position;
                PlayNhacMp3(musicArrayList.get(position).getPath(),mediaPlayer,musicArrayList.get(position).getName());

            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            MP_OnPause();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextSong();
            }
        });
        song_bar_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case MY_Request_code:{
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        doStuff();
                    }

                }
            }
            case Record_Request_code:{

            }
            case Alert_Request_code: {

            }
        }
    }

    void Anhxa() {
        lv = findViewById(R.id.lv);
        txt_songname = findViewById(R.id.song_name);
        btn_play = findViewById(R.id.song_play);
        btn_next = findViewById(R.id.song_next);
        song_bar_layout=findViewById(R.id.song_bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        img=findViewById(R.id.song_image);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        startService(svc);
        myTTS.shutdown();

    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(svc);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager=getFragmentManager();
        if (id == R.id.nav_camera) {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        } else if (id == R.id.nav_gallery) {
            fragment=new Fragment_now_playing();
            //fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.main_view,fragment).addToBackStack("main").commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    MediaPlayer GetMediaPlayer(){
        return mediaPlayer;
    }
    String GetName(){return txt_songname.getText().toString();}
    int GetDuration(){return musicArrayList.get(current_position).getDuration()/1000;};

    //Play Music
    public void PlayNhacMp3(String url, MediaPlayer mediaPlayer, String Song_name) {
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    // Total_duration=mp.getDuration()/1000;

                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    current_position += 1;
                    PlayNhacMp3(musicArrayList.get(current_position).getPath(), mp, musicArrayList.get(current_position).getName());
                    //Total_duration=mp.getDuration()/1000;
                }
            });
            img.setImageBitmap(musicArrayList.get(current_position).getPicture());
            txt_songname.setText(Song_name);
            btn_play.setImageResource(R.drawable.pause2);
            showNotification();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void NextSong() {
        if (flag_rd == 0 && flag_loop == 0) {
            current_position += 1;
            PlayNhacMp3(musicArrayList.get(current_position).getPath(), mediaPlayer, musicArrayList.get(current_position).getName());
            //Total_duration=mediaPlayer.getDuration()/1000;
        } else {
            if (flag_rd == 1 && flag_loop == 0) {
                Random random = new Random();
                current_position = random.nextInt((musicArrayList.size() - 1) + 1);
                PlayNhacMp3(musicArrayList.get(current_position).getPath(), mediaPlayer, musicArrayList.get(current_position).getName());
            } else {
                PlayNhacMp3(musicArrayList.get(current_position).getPath(), mediaPlayer, musicArrayList.get(current_position).getName());
            }
        }
    }
    void PreSong(){
        if (flag_rd == 0 && flag_loop == 0) {
            current_position -= 1;
            PlayNhacMp3(musicArrayList.get(current_position).getPath(), mediaPlayer, musicArrayList.get(current_position).getName());
            //Total_duration=mediaPlayer.getDuration()/1000;
        } else {
            if (flag_rd == 1 && flag_loop == 0) {
                Random random = new Random();
                current_position = random.nextInt((musicArrayList.size() - 1) + 1);
                PlayNhacMp3(musicArrayList.get(current_position).getPath(), mediaPlayer, musicArrayList.get(current_position).getName());
            } else {
                PlayNhacMp3(musicArrayList.get(current_position).getPath(), mediaPlayer, musicArrayList.get(current_position).getName());
            }
        }
    }

    public void MP_OnPause() {
        if (mediaPlayer.isPlaying() == true) {
            mediaPlayer.pause();
            btn_play.setImageResource(R.drawable.play2);
        } else {
            mediaPlayer.start();
            btn_play.setImageResource(R.drawable.pause2);
        }
    }

    //Get List
    public void GetMusic() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.disc);

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String[] projection = new String[]{MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST};
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    Bitmap image;
                    Music songs = null;

                    try {
                        if (coverpicture(url) != null) {
                            songs = new Music(name, url, coverpicture(url), artist, getDurationFromFile(url));
                        } else {
                            songs = new Music(name, url, bm, artist, getDurationFromFile(url));
                        }
                    } catch (Exception e) {
                    }


                    musicArrayList.add(songs);
                } while (cursor.moveToNext());

            }

            cursor.close();
        }
    }

    void doStuff() {
        GetMusic();
        adapter = new MusicListView(MainActivity.this, R.layout.music_item, musicArrayList);
        lv.setAdapter(adapter);
        lv.setCheeseList(musicArrayList);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
    public  Bitmap coverpicture(String path) {

        MediaMetadataRetriever mr = new MediaMetadataRetriever();

        mr.setDataSource(path);

        byte[] byte1 = mr.getEmbeddedPicture();
        mr.release();
        if(byte1 != null)
            return BitmapFactory.decodeByteArray(byte1, 0, byte1.length);
        else
            return  null;

    }
    public int getDurationFromFile(String path){
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);
        String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.parseInt(duration);
    }
    public Bitmap GetMP3Image(){
        return musicArrayList.get(current_position).getPicture();
    }

    //Notification
    public void showNotification(){
        new MyNotification(this,txt_songname.getText().toString(),musicArrayList.get(current_position).getPicture());

    }


    //Voice control
    public void Recognize() {
        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        mySpeechRecognizer.startListening(intent);
    }

    private void initializeSpeechRecognize() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String s) {
        s = s.toLowerCase();
        Toast.makeText(instance, s + "", Toast.LENGTH_SHORT).show();
        if (s.indexOf("next") != -1) {
            speak("Move to the next song.");
            NextSong();
        }
        if (s.indexOf("previous") != -1) {
            speak("Move to the previous song.");
            PreSong();
        }
        if (s.indexOf("pause") != -1 || s.indexOf("play") != -1) {
            MP_OnPause();
        }

        if (s.indexOf("open") != -1) {
            int i = comparename(s);
            if (i != -1) {
                try {
                    PlayNhacMp3(musicArrayList.get(i).getPath(), mediaPlayer, musicArrayList.get(i).getName());
                } catch (Exception e) {

                }
            }
        }
        if (s.indexOf("random") != -1) {
            flag_rd = 1;
        } else if (s.indexOf("loop") != -1) {
            flag_loop = 1;
        } else if (s.indexOf("default") != -1) {
            flag_rd = 0;
            flag_loop = 0;
        }
    }


    private void initializeTextToSpeech() {
        myTTS= new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(MainActivity.this,"There is no TTs engine on your device",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.US);
                    speak("Hello: Welcome to music WORLD.");
                }
            }
        });
    }

    private void speak(String s) {
        if(Build.VERSION.SDK_INT>=21) {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            myTTS.speak(s,TextToSpeech.QUEUE_FLUSH,null);
        }
    }


    private int comparename(String s){
        int flag=0;
        int i=0;
        for(;i<musicArrayList.size();i++){
            String temp=musicArrayList.get(i).getName().toLowerCase();
            if(temp.indexOf(s.substring(6))!=-1){
                flag=1;
                break;
            }
        }if(flag==1) {
            return i;
        }
        return -1;
    }


}
