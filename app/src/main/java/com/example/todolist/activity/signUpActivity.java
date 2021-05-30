package com.example.todolist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signUpActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPw;
    private EditText etPw2;
    private Button suBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();

        suBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPw.getText().toString();
                String password2 = etPw2.getText().toString();
                if(!validate(email)){
                    Toast.makeText(signUpActivity.this, "Not Valid Email!",
                            Toast.LENGTH_SHORT).show();
                }else if(password.length() < 6){
                    Toast.makeText(signUpActivity.this, "Password is at least 6 characters!",
                            Toast.LENGTH_SHORT).show();
                }else if(!password.equals(password2)){
                    Toast.makeText(signUpActivity.this, "Passwords do not match!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("sign up", "createUserWithEmail:success");
                                        Intent t = new Intent(signUpActivity.this, SplashActivity.class);

                                        t.setData(Uri.parse(email));
                                        setResult(101, t);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("sign up", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(signUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });
    }


    public void init(){
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etUsernameSU);
        etPw = findViewById(R.id.etPasswordSU);
        etPw2 = findViewById(R.id.etRePasswordSU);
        suBtn = findViewById(R.id.signUpBtnSU);
    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}