package com.example.nyambura.myrecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{

    //from xml
    @Bind(R.id.enterEmailEditText)
    EditText mEnterEmailEditText;
    @Bind(R.id.enterNameEditText) EditText mEnterNameEditText;
    @Bind(R.id.enterPasswordEditText) EditText mPasswordEditText;
    @Bind(R.id.confirmPasswordEditText) EditText mConfirmPasswordEditText;
    @Bind(R.id.createAccountButton)
    Button mCreateAccountButton;
    @Bind(R.id.loginTextView)
    TextView mLoginTextView;

    //to authenticate
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //progress diaglog
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mLoginTextView.setOnClickListener(this);
        mCreateAccountButton.setOnClickListener(this);
        createAuthStateListener();
        createAuthProgressDialog();

    }

    @Override
    public void onClick(View v){
        if(v==mLoginTextView){
            Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
            //new task - make the next activity the start of the history stack
            //clear task clears this activity from the stack
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if(v==mCreateAccountButton){
            createNewAccount();
        }
    }

    private void createAuthProgressDialog(){
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Creating Account in Progress");
        mAuthProgressDialog.cancel();
    }

    private void createNewAccount(){
        final String name = mEnterNameEditText.getText().toString().trim();
        final String email = mEnterEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();


        boolean validEmail = isValidEmail(email);
        boolean validPasswords = isValidPassword(password,confirmPassword);
        boolean validName = isValidName(name);
        if (!validEmail || !validName || !validPasswords) return;

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("Is"," Successfull");
                }else{
                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createAuthStateListener(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }


    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEnterEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    public boolean isValidPassword(String password, String confirmPassword){
        if(password.length() <6 ){
            mPasswordEditText.setError("Password under 6 characters");
            return false;
        }else if(!password.equals(confirmPassword)){
            mPasswordEditText.setError("Passwords Do Not Match");
            return false;
        }
        return true;
    }


    private boolean isValidName(String name) {
        if (name.equals("")) {
            mEnterNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
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
