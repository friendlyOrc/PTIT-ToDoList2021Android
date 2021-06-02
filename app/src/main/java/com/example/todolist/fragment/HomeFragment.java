package com.example.todolist.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.activity.AddTaskActivity;
import com.example.todolist.activity.MainActivity;
import com.example.todolist.model.Account;
import com.example.todolist.model.MyReceiver;
import com.example.todolist.model.RecyclerViewAdapter;
import com.example.todolist.model.SQLiteHelper;
import com.example.todolist.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

//    private Button getBtn;
    private RecyclerView rev;
    private RecyclerViewAdapter adapter;
    private FloatingActionButton floatBtn;
    private SQLiteHelper sqlHelper;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Account acc;
    private List<Task> list;
    private EditText etSearch;
    private TextView noRs;
    private TextView noTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        init(rootView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        noRs.setVisibility(View.GONE);
        noTask.setVisibility(View.GONE);

        AlarmManager am =
                (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // Signed in successfully, show authenticated UI.
        acc = new Account();
        acc.setName(user.getDisplayName());
        acc.setEmail(user.getEmail());
        acc.setId(user.getUid().toString());

        adapter = new RecyclerViewAdapter(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rev.setLayoutManager(manager);
        rev.setAdapter(adapter);

        sqlHelper = new SQLiteHelper(getActivity());

//        floatBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent t = new Intent(getActivity(), AddTaskActivity.class);
//                startActivity(t);
//            }
//        });

//        getBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                updateList();
//
//            }
//        });
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noRs.setVisibility(View.GONE);
                noTask.setVisibility(View.GONE);
                list = new ArrayList<>();
                GenericTypeIndicator<Map<String, Task>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Task>>() {};
                Map<String, Task> map =  dataSnapshot.child(acc.getId()).getValue(genericTypeIndicator);
                if(map != null){
                    for (Map.Entry<String, Task> entry : map.entrySet()){
                        System.out.println(entry.getValue());

                        list.add(entry.getValue());
                        if(entry.getValue().getStatus() == 0){

                            //For Alarm
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            String [] time_spilt = entry.getValue().getTime().split(":");
                            int hour = Integer.parseInt(time_spilt[0]);
                            int minute = Integer.parseInt(time_spilt[1]);
                            if(calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) == minute){

                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);

                                Intent intent = new Intent(getActivity(),
                                        MyReceiver.class);
                                intent.putExtra("id", String.valueOf(entry.getValue().getId()));
                                intent.putExtra("myAction", "mDoNotify");
                                intent.putExtra("Title", entry.getValue().getCategory());
                                intent.putExtra("Description", entry.getValue().getName());

                                //dùng PendingIntent để gọi lớp BroadcastReceiver
                                PendingIntent pendingIntent =
                                        PendingIntent.getBroadcast(getActivity(),
                                                0, intent, 0);//Đặt thông báo
                                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pendingIntent);
                            }
                        }

                    }
                }
                if(list.size() == 0){
                    noTask.setVisibility(View.VISIBLE);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    list.sort(new Comparator<Task>() {
                        @Override
                        public int compare(Task o1, Task o2) {
                            String startTime = o1.getTime();
                            String endTime = o2.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            try {
                                Date d1 = sdf.parse(startTime);
                                Date d2 = sdf.parse(endTime);
                                long elapsed = d1.getTime() - d2.getTime();
                                return (int) elapsed;
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });
                }
                adapter.setListTask(list);
                rev.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get data", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                noRs.setVisibility(View.GONE);
                noTask.setVisibility(View.GONE);

                String search = s.toString().toLowerCase();
                ArrayList<Task> rs = new ArrayList<>();
                for(Task t: list){
                    if(t.getName().contains(search)){
                        rs.add(t);
                    }
                }
                if(rs.size() == 0 && list.size() != 0){
                    noRs.setVisibility(View.VISIBLE);
                }else if(rs.size() == 0 && list.size() == 0){
                    noTask.setVisibility(View.VISIBLE);
                }
                adapter.setListTask(rs);
                rev.setAdapter(adapter);
            }
        });
        return rootView;
    }


    public void init(View v){
        rev = v.findViewById(R.id.revViewHome);
//        floatBtn = v.findViewById(R.id.btnAddHome);
        mAuth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        etSearch = v.findViewById(R.id.etSearchHome);
        noRs = v.findViewById(R.id.noRsHome);
        noTask = v.findViewById(R.id.noTaskHome);
    }

}