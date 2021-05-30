package com.example.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.model.CatJSON;
import com.example.todolist.model.MyAsyncTask;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RandomCat extends AppCompatActivity {
    private Button btn;
    private ImageView img;
    private ProgressBar progressBar;
    private TextView tvMeow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivy_randomcat);
        init();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.cat_face);


                MyAsyncTask task = new MyAsyncTask(RandomCat.this);
                task.execute();
                btn.setEnabled(false);

                // Khởi tạo OkHttpClient để lấy dữ liệu.
                OkHttpClient client = new OkHttpClient();

                // Khởi tạo Moshi adapter để biến đổi json sang model java
                Moshi moshi = new Moshi.Builder().build();
                Type usersType = Types.newParameterizedType(CatJSON.class);
                final JsonAdapter<CatJSON> jsonAdapter = moshi.adapter(usersType);

                // Tạo request lên server.
                Request request = new Request.Builder()
                        .url("https://aws.random.cat/meow")
                        .build();

                // Thực thi request.
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("Error", "Network Error");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        // Lấy thông tin JSON trả về
                        String json = response.body().string();
                        final CatJSON cat = jsonAdapter.fromJson(json);

                        // Cho hiển thị
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                task.cancel(true);

                                Picasso.get().load(cat.getFile()).placeholder(R.drawable.cat_face).into(img);
                                progressBar.setProgress(100);
                                btn.setEnabled(true);
                                tvMeow.setText("MEOW");
                            }
                        });
                    }
                });
            }
        });
    }

    public void init(){
        img = findViewById(R.id.mainImage);
        btn = findViewById(R.id.moreCatBtn);
        progressBar = findViewById(R.id.progressCat);
        tvMeow = findViewById(R.id.tvMeow);
    }


}