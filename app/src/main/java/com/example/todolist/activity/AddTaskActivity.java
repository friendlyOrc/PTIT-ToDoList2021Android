package com.example.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;

import com.example.todolist.R;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todolist.model.Account;
import com.example.todolist.model.SQLiteHelper;
import com.example.todolist.model.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    private EditText etNameAdd;
    private EditText etTimeAdd;
    private Button btnGetDateAdd;
    private Button btnAddAdd;
    private Button btnBackAdd;
    private Spinner spnCate;
    private SQLiteHelper sqlHelper;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        init();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        // Signed in successfully, show authenticated UI.
        Account acc = new Account();
        acc.setId(user.getUid().toString());
        acc.setName(user.getDisplayName());
        acc.setEmail(user.getEmail());
        sqlHelper = new SQLiteHelper(AddTaskActivity.this);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int h = c.get(Calendar.HOUR);
        int m = c.get(Calendar.MINUTE);
        etTimeAdd.setText(h + ":" + m);

        btnAddAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTimeAdd.getText().toString().length() == 0 || etNameAdd.getText().toString().length() == 0 ){
                    Toast.makeText(AddTaskActivity.this, "Please fill the information!", Toast.LENGTH_SHORT).show();
                }else{
                    int id = (int) (new Date().getTime()/1000);
                    Task o = new Task();
                    o.setId(id);
                    o.setName(etNameAdd.getText().toString());
                    o.setCategory(spnCate.getSelectedItem().toString());
                    o.setTime(etTimeAdd.getText().toString());
                    o.setStatus(0);
//                long count = sqlHelper.addTask(o);
                    mDatabase.child(acc.getId()).child(String.valueOf(id)).setValue(o);
                    Intent i = new Intent(AddTaskActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnBackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnGetDateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(AddTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etTimeAdd.setText(hourOfDay + ":" + minute);
                            }
                        }, h, m, true);
                tpd.show();
            }
        });
    }

    private void init() {
        etNameAdd = findViewById(R.id.etNameAdd);
        etTimeAdd = findViewById(R.id.etTimeAdd);
        spnCate = findViewById(R.id.spnCateAdd);
        btnGetDateAdd = findViewById(R.id.btnGetDateAdd);
        btnAddAdd = findViewById(R.id.btnAddAdd);
        btnBackAdd = findViewById(R.id.btnBackAdd);
        mAuth = FirebaseAuth.getInstance();
    }
}