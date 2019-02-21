package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LogInActivity extends AppCompatActivity {

    private TextView message;

    private Button buttonSignIn;

    private EditText editTextPhone,editTextCode;

    private String codeFromFirebase;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    FirebaseApp.initializeApp(this);

    message=findViewById(R.id.messageLogIn);
    message.setText("enter your phone number");

    buttonSignIn=findViewById(R.id.buttonLogIn);
    editTextCode=findViewById(R.id.codeLogIn);
    editTextPhone=findViewById(R.id.phoneLogIn);



    buttonSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String codeEditText = editTextCode.getText().toString();

            if(codeFromFirebase!=null){ //this is for manual log in, waiting for code

                //message.setText("enter code. ");
                signInWithPassCode(codeFromFirebase,codeEditText);
            }

            gettingCallback();
        }
    });

    //setting callback

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                message.setText("getting credential...");

                signInWithPhoneCredential( phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                message.setText("failed getting credential");
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                codeFromFirebase = s;
                message.setText("enter code..");
                buttonSignIn.setText("click to sign in");

            }
        };


    }

    private void signInWithPassCode(String codeFromFirebase,String enterCode) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeFromFirebase,enterCode);
        signInWithPhoneCredential(credential);
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            
                if(task.isSuccessful()){

                    message.setText("logging in..");

                    userLoggedIn();
                }
                
            }
        });

    }

    private void userLoggedIn() {

        message.setText("user is logged in");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){

            //start Main page intent




        }


    }

    private void gettingCallback() {
        message.setText("getting callback and authorized..");


        PhoneAuthProvider.getInstance().verifyPhoneNumber( editTextPhone.getText().toString(),

                40,
                TimeUnit.SECONDS,
                this,
                mCallBack

                );
    }
}
