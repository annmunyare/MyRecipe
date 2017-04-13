package com.example.nyambura.myrecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.registerTextView)
    TextView mRegisterTextView;
    @Bind(R.id.loginButton)
    Button mLoginButton;
    @Bind(R.id.loginEmailEditText)
    EditText mLoginEmailEditText;
    @Bind(R.id.passwordLoginEditText) EditText mPasswordLoginEditText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ProgressDialog mAuthProgressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        createAuthProgressDialog();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
        mRegisterTextView.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v == mRegisterTextView){
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
            finish();
        }
        if(v == mLoginButton){
            loginWithPassword();
        }
    }

    private void loginWithPassword(){
        String email = mLoginEmailEditText.getText().toString().trim();
        String password = mPasswordLoginEditText.getText().toString().trim();
        if(email.equals("")){
            mLoginEmailEditText.setError("Please Enter Your Email");
            return;
        }
        if(password.equals("")){
            mPasswordLoginEditText.setError(("Please Enter a Password"));
            return;
        }
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAuthProgressDialog.dismiss();
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Invalid E-Mail And/Or Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void createAuthProgressDialog(){
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setTitle("Signing In!");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}

