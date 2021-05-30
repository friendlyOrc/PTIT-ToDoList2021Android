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

import com.example.todolist.model.Account;
import com.example.todolist.model.Task;
import com.example.todolist.model.SQLiteHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {
    private TextView tvId;
    private EditText etNameEdit;
    private EditText etTimeEdit;
    private Button btnGetTimeEdit;
    private Button btnUpdtEdit;
    private Button btnRemove;
    private Button btnBackEdit;
    private CheckBox checkBox;
    private Spinner spnCate;
    private SQLiteHelper sqLiteHelper;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        sqLiteHelper = new SQLiteHelper(this);
        init();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();

        Account acc = new Account();
        acc.setId(user.getUid().toString());
        acc.setName(user.getDisplayName());
        acc.setEmail(user.getEmail());

        Intent i = getIntent();
        Task o = (Task) i.getSerializableExtra("Task");
        tvId.setText(o.getId()+"");
        etNameEdit.setText(o.getName());
        etTimeEdit.setText(o.getTime());
        if(o.getStatus() == 1){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        switch (o.getCategory()){
            case "Chores":
                spnCate.setSelection(0);
                break;
            case "Work":
                spnCate.setSelection(1);
                break;
            case "Study":
                spnCate.setSelection(2);
                break;
        }

        btnGetTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int h = c.get(Calendar.HOUR);
                int m = c.get(Calendar.MINUTE);
                TimePickerDialog tpd = new TimePickerDialog(EditTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etTimeEdit.setText(hourOfDay + ":" + minute);
                            }
                        }, h, m, true);
                tpd.show();
            }
        });

        btnBackEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditTaskActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnUpdtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task o = new Task();
                o.setId(Integer.parseInt(tvId.getText().toString()));
                o.setName(etNameEdit.getText().toString());
                o.setCategory(spnCate.getSelectedItem().toString());
                o.setTime(etTimeEdit.getText().toString());
                if(checkBox.isChecked()){
                    o.setStatus(1);
                }else{
                    o.setStatus(0);
                }
                mDatabase.child(acc.getId()).child(String.valueOf(o.getId())).setValue(o);
                Intent i = new Intent(EditTaskActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(acc.getId()).child(String.valueOf(o.getId())).removeValue();
//                int id = Integer.parseInt(tvId.getText().toString());
//                sqLiteHelper.deleteTask(id);
                Intent i = new Intent(EditTaskActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void init() {
        tvId = findViewById(R.id.tvIdEdit);
        etNameEdit = findViewById(R.id.etNameEdit);
        spnCate = findViewById(R.id.spnCateEdit);
        etTimeEdit = findViewById(R.id.etTimeEdit);
        btnGetTimeEdit = findViewById(R.id.btnGetTimeEdit);
        checkBox = findViewById(R.id.checkComEdit);
        btnUpdtEdit = findViewById(R.id.btnUpdateEdit);
        btnRemove = findViewById(R.id.btnRemoveEdit);
        btnBackEdit = findViewById(R.id.btnBackEdit);
        mAuth = FirebaseAuth.getInstance();
    }
}