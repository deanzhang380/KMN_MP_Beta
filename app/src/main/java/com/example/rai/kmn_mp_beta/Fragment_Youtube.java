package com.example.rai.kmn_mp_beta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_Youtube extends Fragment {
    public static String Youtube_API_Key = "AIzaSyDGtfNWT2DQZ1rL98CxT2mIO_eP_pasavI";
    String IDPlayList = "PLzrVYRai0riSRJ3M3bifVWWRq5eJMu6tv";
    ArrayList<Video> mangvideoyoutube;
    VideoAdapter videoAdapter;
    ListView listView;
    EditText edtsearch;
    Button btnsearch;
    String KEYWORD = "";
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.youtube, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        btnsearch = (Button) view.findViewById(R.id.buttonsearch);
        edtsearch = (EditText) view.findViewById(R.id.edittextsearch);


        mangvideoyoutube = new ArrayList<>();
        videoAdapter = new VideoAdapter(getContext(), mangvideoyoutube);
        listView.setAdapter(videoAdapter);
        getVideoYoutube(IDPlayList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), YoutubeActivity.class);
                intent.putExtra("resourceid", mangvideoyoutube.get(i).getUrlVideo());
                startActivity(intent);
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KEYWORD = edtsearch.getText().toString();
                KEYWORD = KEYWORD.replace(" ", "%20");
                getSearchVideYoutube(KEYWORD);
            }
        });
        return view;
    }

    private void getSearchVideYoutube(String keyword) {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&q=" + keyword + "&type=video&key=" + Youtube_API_Key;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override

            public void onResponse(JSONObject response) {
                mangvideoyoutube.clear();
                try {
                    JSONArray jsonArrayitem = response.getJSONArray("items");
                    for (int i = 0; i < jsonArrayitem.length(); i++) {
                        JSONObject jsonObject = jsonArrayitem.getJSONObject(i);

                        JSONObject jsonObjectId = jsonObject.getJSONObject("id");
                        String resourceid = jsonObjectId.getString("videoId");


                        JSONObject jsonObjectsnippet = jsonObject.getJSONObject("snippet");
                        String title = jsonObjectsnippet.getString("title");

                        JSONObject jsonObjectThumbnail = jsonObjectsnippet.getJSONObject("thumbnails");
                        JSONObject jsonObjectDefault = jsonObjectThumbnail.getJSONObject("default");
                        String url = jsonObjectDefault.getString("url");

                        mangvideoyoutube.add(new Video(i, title, url, resourceid));
                    }
                    videoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getVideoYoutube(String idPlayList) {
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + idPlayList + "&key=" + Youtube_API_Key + "&maxResults=50";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectitem = jsonArray.getJSONObject(i);

                        JSONObject jsonObjectsnippet = jsonObjectitem.getJSONObject("snippet");
                        String title = jsonObjectsnippet.getString("title");

                        JSONObject jsonObjectthumnail = jsonObjectsnippet.getJSONObject("thumbnails");
                        JSONObject jsonObjectdefault = jsonObjectthumnail.getJSONObject("default");
                        String urlimage = jsonObjectdefault.getString("url");

                        JSONObject jsonObjectresourceId = jsonObjectsnippet.getJSONObject("resourceId");
                        String resourceid = jsonObjectresourceId.getString("videoId");

                        mangvideoyoutube.add(new Video(i, title, urlimage, resourceid));
                    }
                    videoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
