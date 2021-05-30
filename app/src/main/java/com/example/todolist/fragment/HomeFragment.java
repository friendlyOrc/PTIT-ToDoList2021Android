package com.example.todolist.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import java.util.ArrayList;
import java.util.Calendar;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        init(rootView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();


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

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(t);
            }
        });

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
                List<Task> list = new ArrayList<>();
                GenericTypeIndicator<Map<String, Task>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Task>>() {};
                Map<String, Task> map =  dataSnapshot.child(acc.getId()).getValue(genericTypeIndicator);
                if(map != null){
                    for (Map.Entry<String, Task> entry : map.entrySet()){
                        System.out.println(entry.getValue());

                        list.add(entry.getValue());
                        if(entry.getValue().getStatus() == 0){

                            //For Alarm
                            AlarmManager am =
                                    (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
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


        return rootView;
    }

//    @Override
//    public void onResume() {
//        updateList();
//        super.onResume();
//    }

    public void init(View v){
//        getBtn = v.findViewById(R.id.btnGetAllHome);
        rev = v.findViewById(R.id.revViewHome);
        floatBtn = v.findViewById(R.id.btnAddHome);
        mAuth = FirebaseAuth.getInstance();
    }

//    public void updateList(){
//        List<Task> list = new ArrayList<>();
//        adapter.setListTask(list);
//        rev.setAdapter(adapter);
//
//    }
}