package com.example.todolist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.model.Account;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SplashActivity extends AppCompatActivity {
//    private Button loginBtn;
    private Button noLoginBtn;
    private SignInButton loginGgl;
    private FirebaseAuth mAuth;
    private LoginButton fbBtn;
    private CallbackManager mCallbackManager;
    private Button loginBtn;
    private EditText etUN;
    private EditText etPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();

        FirebaseUser account = mAuth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(SplashActivity.this, gso);
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SplashActivity.this);

        if(account != null){
            Account acc = new Account();
            acc.setName(account.getDisplayName());
            acc.setEmail(account.getEmail());

            Intent t = new Intent(SplashActivity.this, MainActivity.class);
            t.putExtra("account", acc);
            t.putExtra("msg", "Welcome back " +  acc.getName());
            t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(t);
            finish();
        }
        loginGgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 112);
            }
        });

        noLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(SplashActivity.this, signUpActivity.class);
                startActivityForResult(t, 101);
            }
        });

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        fbBtn.setReadPermissions("email", "public_profile");
        fbBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Fb login", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("fb login", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("fb login", "facebook:onError", error);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = etUN.getText().toString();
                String pw = etPW.getText().toString();

                if(un.isEmpty() || pw.isEmpty()){
                    Toast.makeText(SplashActivity.this, "Please fill the infomartion!",
                            Toast.LENGTH_SHORT).show();
                }else{

                    mAuth.signInWithEmailAndPassword(un, pw)
                            .addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Normal login", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // Signed in successfully, show authenticated UI.
                                        Account acc = new Account();
                                        acc.setName(user.getDisplayName());
                                        acc.setEmail(user.getEmail());

                                        Intent t = new Intent(SplashActivity.this, MainActivity.class);
                                        t.putExtra("account", acc);
                                        if(acc.getName() == null){
                                            t.putExtra("msg", "Welcome User!");
                                        }else{
                                            t.putExtra("msg", "Welcome " +  acc.getName());
                                        }
                                        t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(t);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Normal login", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SplashActivity.this, "Wrong username/password!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

    }
    public void init(){
//        loginBtn = findViewById(R.id.loginBtn);
        noLoginBtn = findViewById(R.id.noLoginBtn);
        loginGgl = findViewById(R.id.loginGgl);
        loginGgl.setSize(SignInButton.SIZE_STANDARD);
        mAuth = FirebaseAuth.getInstance();
        fbBtn = findViewById(R.id.loginFb);
        loginBtn = findViewById(R.id.loginBtn);
        etUN = findViewById(R.id.etUsername);
        etPW = findViewById(R.id.etPassword);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 112) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Firebase Login", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Firebase Login", "Google sign in failed", e);
            }
        }else if(requestCode == 101){
            if(data != null){
                Toast.makeText(SplashActivity.this, "Sign up Successfully", Toast.LENGTH_SHORT).show();
                String email = data.getData().toString();
                etUN.setText(email);
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase Login", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Signed in successfully, show authenticated UI.
                            Account acc = new Account();
                            acc.setName(user.getDisplayName());
                            acc.setEmail(user.getEmail());

                            Intent t = new Intent(SplashActivity.this, MainActivity.class);
                            t.putExtra("account", acc);
                            t.putExtra("msg", "Welcome " +  acc.getName());
                            t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(t);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase Login", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("fb login", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fb login", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Account acc = new Account();
                            acc.setName(user.getDisplayName());
                            acc.setEmail(user.getEmail());

                            Intent t = new Intent(SplashActivity.this, MainActivity.class);
                            t.putExtra("account", acc);
                            t.putExtra("msg", "Welcome " +  acc.getName());
                            t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(t);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fb login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SplashActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}