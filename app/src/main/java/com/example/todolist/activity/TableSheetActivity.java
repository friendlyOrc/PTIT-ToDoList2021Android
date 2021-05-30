package com.example.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.fragment.NewsFragment;
import com.example.todolist.model.Article;
import com.example.todolist.model.TableSheet;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TableSheetActivity extends AppCompatActivity {
    private EditText etId;
    private Spinner spnWeek;
    private Button searchBtn;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tvDay;
    private ArrayList<TableSheet> classList = new ArrayList<>();
    private Button nextBtn;
    private Button backBtn;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_sheet);
        init();


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText("Loading...");
                tv2.setText("Loading...");
                tv3.setText("Loading...");
                tv4.setText("Loading...");
                tv5.setText("Loading...");
                tv6.setText("Loading...");

                getTableData();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day < 4){
                    day++;
                }
                tv1.setText("Loading...");
                tv2.setText("Loading...");
                tv3.setText("Loading...");
                tv4.setText("Loading...");
                tv5.setText("Loading...");
                tv6.setText("Loading...");
                setTable(classList.get(day));
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day > 0){
                    day--;
                }
                tv1.setText("Loading...");
                tv2.setText("Loading...");
                tv3.setText("Loading...");
                tv4.setText("Loading...");
                tv5.setText("Loading...");
                tv6.setText("Loading...");
                setTable(classList.get(day));
            }
        });

    }

    public void init(){
        etId = findViewById(R.id.etMSVTable);
        spnWeek = findViewById(R.id.spnWeekTable);
        searchBtn = findViewById(R.id.btnSearchTable);
        tv1 = findViewById(R.id.class1Table);
        tv2 = findViewById(R.id.class2Table);
        tv3 = findViewById(R.id.class3Table);
        tv4 = findViewById(R.id.class4Table);
        tv5 = findViewById(R.id.class5Table);
        tv6 = findViewById(R.id.class6Table);
        tvDay = findViewById(R.id.tvDayTable);
        nextBtn = findViewById(R.id.btnNextTable);
        backBtn = findViewById(R.id.btnPreTable);
        day = 0;
        backBtn.setEnabled(false);
        nextBtn.setEnabled(false);
    }

    public void setTable(TableSheet ts){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                backBtn.setEnabled(true);
                nextBtn.setEnabled(true);
                switch (ts.getDay()){
                    case 2:
                        tvDay.setText("MONDAY");
                        break;
                    case 3:
                        tvDay.setText("TUESDAY");
                        break;
                    case 4:
                        tvDay.setText("WEDNESDAY");
                        break;
                    case 5:
                        tvDay.setText("THURSDAY");
                        break;
                    case 6:
                        tvDay.setText("FRIDAY");
                        break;
                }
                ArrayList<String> classes = ts.getClasses();
                if(classes.get(0).length() == 0){
                    tv1.setText("No Class");
                }else{
                    tv1.setText(classes.get(0));
                }
                if(classes.get(1).length() == 0){
                    tv2.setText("No Class");
                }else{
                    tv2.setText(classes.get(1));
                }
                if(classes.get(2).length() == 0){
                    tv3.setText("No Class");
                }else{
                    tv3.setText(classes.get(2));
                }
                if(classes.get(3).length() == 0){
                    tv4.setText("No Class");
                }else{
                    tv4.setText(classes.get(3));
                }
                if(classes.get(4).length() == 0){
                    tv5.setText("No Class");
                }else{
                    tv5.setText(classes.get(4));
                }
                if(classes.get(5).length() == 0){
                    tv6.setText("No Class");
                }else{
                    tv6.setText(classes.get(5));
                }
            }
        });
    }

    public void getTableData(){
        String msv = etId.getText().toString();
        String week = spnWeek.getSelectedItem().toString();

        if(msv.length() < 10){
            Toast.makeText(TableSheetActivity.this, "Invalid MSV", Toast.LENGTH_LONG).show();
        }else{

            // Khởi tạo OkHttpClient để lấy dữ liệu.
            OkHttpClient client = new OkHttpClient();

            // Tạo request lên server.
            Request request = new Request.Builder()
                    .url("http://ptit-crawler-app.herokuapp.com/xemtkb/" + msv)
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
                    try {

                        JSONObject json = new JSONObject(response.body().string());
                        // Cho hiển thị
                        for(int i = 2; i <= 6; i++){
                            JSONObject obj = json.getJSONObject(String.valueOf(i));
                            TableSheet sheet = new TableSheet();
                            ArrayList<String> classStr = new ArrayList<>();
                            sheet.setDay(i);
                            for(int j = 1; j <= 6; j++){
                                String c = obj.getString(String.valueOf(j));
                                classStr.add(c);
                            }
                            sheet.setClasses(classStr);
                            classList.add(sheet);
                        }

                        setTable(classList.get(day));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}