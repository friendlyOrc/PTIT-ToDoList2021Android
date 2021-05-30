package com.example.todolist.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BMIActivity extends AppCompatActivity {
    private Spinner spHeight, spWeight;
    private int heightUnit, weightUnit;
    private Button btCalculate;
    private EditText edtCalAge, edtCalWeight, edtCalHeight;
    private RadioButton rbCalMen, rbCalWomen;
    private String nationality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        //getSupportActionBar().setTitle("BMI Caculation");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        setUpSpinner();

        spWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weightUnit = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heightUnit = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCalAge.getText().toString().equals("")) {
                    Toast.makeText(com.example.todolist.activity.BMIActivity.this, "Age field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (edtCalWeight.getText().toString().equals("")) {
                    Toast.makeText(com.example.todolist.activity.BMIActivity.this, "Weight field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (edtCalHeight.getText().toString().equals("")) {
                    Toast.makeText(com.example.todolist.activity.BMIActivity.this, "Height field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!rbCalMen.isChecked() && !rbCalWomen.isChecked()) {
                    Toast.makeText(com.example.todolist.activity.BMIActivity.this, "You must choose gender button", Toast.LENGTH_SHORT).show();
                }
                else {

                }
            }
        });
    }

    public Double caculateBMI () {
        Double age = Double.parseDouble(edtCalAge.getText().toString());
        Double weight = Double.parseDouble(edtCalWeight.getText().toString());
        if (weightUnit == 1) weight *= 0.45359237;
        Double height = Double.parseDouble(edtCalHeight.getText().toString());
        if (heightUnit == 1) height *= 2.54;

        Double bmi = 0.0;
        if (weight == 0 || age == 0 || height == 0)
            Toast.makeText(this, "Field has value equal 0", Toast.LENGTH_SHORT).show();
        else {
            bmi = 10000 * weight / (height * height);
            DecimalFormat df = new DecimalFormat("#.00");
            //Toast.makeText(this, "Bmi " + df.format(bmi), Toast.LENGTH_SHORT).show();
        }
        return bmi;
    }



    public void init () {
        spHeight = findViewById(R.id.spHeight);
        spWeight = findViewById(R.id.spWeight);
        btCalculate = findViewById(R.id.btCalculate);
        edtCalAge = findViewById(R.id.edtCalAge);
        edtCalHeight = findViewById(R.id.edtCalHeight);
        edtCalWeight = findViewById(R.id.edtCalWeight);
        rbCalMen = findViewById(R.id.rbCalMen);
        rbCalWomen = findViewById(R.id.rbCalWomen);
    }


    public void setUpSpinner() {
        ArrayList<String> height = new ArrayList<>();
        height.add("Cm");
        height.add("Inch");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, height);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHeight.setAdapter(dataAdapter);
        ArrayList<String> weight = new ArrayList<>();
        weight.add("Kg");
        weight.add("Ibs");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weight);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(dataAdapter2);
    }
}
