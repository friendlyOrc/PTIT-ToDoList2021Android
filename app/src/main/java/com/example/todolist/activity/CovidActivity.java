package com.example.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.model.CatJSON;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CovidActivity extends AppCompatActivity {
    private TextView tvDateVN;
    private TextView tvConfVN;
    private TextView tvDeathVN;
    private TextView tvRecVN;
    private TextView tvActVN;
    private TextView tvDateGlob;
    private TextView tvConfGlob;
    private TextView tvDeathGlob;
    private TextView tvRecGlob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid);
        init();


        OkHttpClient client = new OkHttpClient();

        // Tạo request lên server.
        Request request = new Request.Builder()
                .url("https://api.covid19api.com/total/country/viet-nam")
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

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        JSONArray jsonObject = null;
                        try {
                            jsonObject = new JSONArray(json.toString());
                            JSONObject currVN = (JSONObject) jsonObject.get(jsonObject.length() - 1);
                            tvDateVN.setText(currVN.getString("Date").substring(0, 10));
                            tvConfVN.setText(String.valueOf(currVN.getInt("Confirmed")));
                            tvDeathVN.setText(String.valueOf(currVN.getInt("Deaths")));
                            tvRecVN.setText(String.valueOf(currVN.getInt("Recovered")));
                            tvActVN.setText(String.valueOf(currVN.getInt("Active")));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        request = new Request.Builder()
                .url("https://api.covid19api.com/summary")
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

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json.toString());
                            JSONObject sum = jsonObject.getJSONObject("Global");

                            tvDateGlob.setText(jsonObject.getString("Date").substring(0, 10));
                            tvConfGlob.setText(String.valueOf(sum.getInt("TotalConfirmed")));
                            tvDeathGlob.setText(String.valueOf(sum.getInt("TotalDeaths")));
                            tvRecGlob.setText(String.valueOf(sum.getInt("TotalRecovered")));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public void init(){
        tvDateVN = findViewById(R.id.dateVN);
        tvConfVN = findViewById(R.id.tvConfCov);
        tvDeathVN = findViewById(R.id.tvDeathCov);
        tvRecVN = findViewById(R.id.tvRecCov);
        tvActVN = findViewById(R.id.tvActCov);
        tvDateGlob = findViewById(R.id.dateGlob);
        tvConfGlob = findViewById(R.id.tvConfCovGlob);
        tvDeathGlob = findViewById(R.id.tvDeathCovGlob);
        tvRecGlob = findViewById(R.id.tvRecCovGlob);
    }
}