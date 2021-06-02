package com.example.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.model.CatJSON;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private Spinner spnWea;
    private Button btnWea;
    private ImageView img;
    private TextView tvTemp;
    private TextView tvAQI;
    private TextView tvNote;
    private TextView tvHumid;
    private TextView tvWind;
    private LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        init();
        view.setVisibility(LinearLayout.GONE);

        btnWea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = spnWea.getSelectedItem().toString();
                OkHttpClient client = new OkHttpClient();

                // Tạo request lên server.
                Request request = new Request.Builder()
                        .url("http://api.airvisual.com/v2/city?city=" + city +"&state=Hanoi&country=Vietnam&key=2f081656-6713-4701-b682-749ae1df0105")
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
                                view.setVisibility(LinearLayout.VISIBLE);
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(json);
                                    JSONObject weather = jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather");
                                    JSONObject pollution = jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution");

                                    String imgName = weather.getString("ic");
                                    System.out.println("====================" + imgName);
                                    switch (imgName){
                                        case "01d":
                                            img.setImageResource(R.drawable.w01d);
                                            break;
                                        case "01n":
                                            img.setImageResource(R.drawable.w01n);
                                            break;
                                        case "02d":
                                            img.setImageResource(R.drawable.w02d);
                                            break;
                                        case "02n":
                                            img.setImageResource(R.drawable.w02n);
                                            break;
                                        case "03d":
                                            img.setImageResource(R.drawable.w03d);
                                            break;
                                        case "04d":
                                            img.setImageResource(R.drawable.w04d);
                                            break;
                                        case "04n":
                                            img.setImageResource(R.drawable.w04d);
                                            break;
                                        case "09d":
                                            img.setImageResource(R.drawable.w09d);
                                            break;
                                        case "10d":
                                            img.setImageResource(R.drawable.w10d);
                                            break;
                                        case "10n":
                                            img.setImageResource(R.drawable.w10n);
                                            break;
                                        case "11d":
                                            img.setImageResource(R.drawable.w11d);
                                            break;
                                        case "13d":
                                            img.setImageResource(R.drawable.w13d);
                                            break;
                                        case "50d":
                                            img.setImageResource(R.drawable.w50d);
                                            break;
                                        default:
                                            img.setImageResource(R.drawable.w01d);
                                    }

                                    tvTemp.setText(String.valueOf(weather.getInt("tp")));
                                    tvHumid.setText(String.valueOf(weather.getDouble("hu")));
                                    tvWind.setText(String.valueOf(weather.getDouble("ws")));
                                    Double aqi = pollution.getDouble("aqius");

                                    tvAQI.setText(String.valueOf(aqi));
                                    if(aqi > 301){
                                        tvNote.setText("Nguy hiểm");
                                    }else if(aqi <= 300 && aqi >=201){
                                        tvNote.setText("Rất ô nhiễm");
                                    }else if(aqi <= 200 && aqi >= 151){
                                        tvNote.setText("Ô nhiễm");
                                    }else if(aqi <= 150 && aqi >= 101){
                                        tvNote.setText("Không tốt cho người thuộc nhóm nhạy cảm");
                                    }else if(aqi <= 100 && aqi >= 51){
                                        tvNote.setText("Vừa phải");
                                    }else{
                                        tvNote.setText("Tốt");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }
                });
            }
        });

    }
    public void init(){
        spnWea = findViewById(R.id.spnCityWea);
        btnWea = findViewById(R.id.btnWea);
        img = findViewById(R.id.imgWeather);
        tvTemp = findViewById(R.id.tvTempWea);
        tvAQI = findViewById(R.id.tvAQIWea);
        tvNote = findViewById(R.id.tvNoteWea);
        tvHumid = findViewById(R.id.tvHumidWea);
        tvWind = findViewById(R.id.tvWindWea);
        view = findViewById(R.id.weatherInfo);
    }
}