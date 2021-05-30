package com.example.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.todolist.R;
import com.example.todolist.model.Task;

public class ArticleWebViewActivity extends AppCompatActivity {
    private WebView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_web_view);

        view = findViewById(R.id.webviewArticle);

        Intent i = getIntent();
        String url = (String) i.getSerializableExtra("url");
        view.loadUrl(url);
    }
}