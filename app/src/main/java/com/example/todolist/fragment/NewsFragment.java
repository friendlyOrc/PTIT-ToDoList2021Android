package com.example.todolist.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.StrictMode;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.todolist.R;
import com.example.todolist.activity.RandomCat;
import com.example.todolist.model.Article;
import com.example.todolist.model.ArticleRecyclerViewAdapter;
import com.example.todolist.model.CatJSON;
import com.example.todolist.model.MyAsyncTask;
import com.example.todolist.model.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsFragment extends Fragment {

    private ArticleRecyclerViewAdapter adapter;
    private RecyclerView view;
    private SwipeRefreshLayout swipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_news, container, false);
        init(rootView);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        adapter = new ArticleRecyclerViewAdapter(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        view.setLayoutManager(manager);
        view.setAdapter(adapter);

        reload();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                swipe.setRefreshing(false);
            }
        });

        return rootView;
    }

    public void init(View v){
        view = v.findViewById(R.id.revViewNews);
        swipe = v.findViewById(R.id.swipeLayout);
    }
    public List<Article> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String url = null;
        String time = null;
        boolean isItem = false;
        List<Article> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

//                    Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("guid")) {
                    link = result;
                }else if (name.equalsIgnoreCase("pubDate")) {
                    time = result.substring(0, 16);
                }else if(name.equalsIgnoreCase("description")){
//                    System.out.println(result);
                    Matcher matcher = Pattern.compile("<img src=\"([^\"]+)").matcher(result);
                    if(matcher.find())
                        url = matcher.group(1);
                }

                if (title != null && link != null && url != null && time != null) {
                    if(isItem) {
                        Article item = new Article(link, title, url, time);
                        items.add(item);
                    }

                    title = null;
                    link = null;
                    url = null;
                    time = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }
    public void reload(){
        URL url = null;
        try {
            url = new URL("https://vnexpress.net/rss/tin-moi-nhat.rss");
            InputStream inputStream = url.openConnection().getInputStream();

            List<Article> list = parseFeed(inputStream);
            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setListArticle(list);
                        view.setAdapter(adapter);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}