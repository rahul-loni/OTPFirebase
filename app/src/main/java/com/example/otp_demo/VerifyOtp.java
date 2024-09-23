package com.example.otp_demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {
    EditText inputcode1,inputcode2,inputcode3,inputcode4,inputcode5,inputcode6;
    Button btnVerify;
    ProgressBar progressBar;
    private  String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inputcode1=findViewById(R.id.inputCode1);
        inputcode2=findViewById(R.id.inputCode2);
        inputcode3=findViewById(R.id.inputCode3);
        inputcode4=findViewById(R.id.inputCode4);
        inputcode5=findViewById(R.id.inputCode5);
        inputcode6=findViewById(R.id.inputCode6);

        btnVerify=findViewById(R.id.btnVerify);
        progressBar=findViewById(R.id.progress_bar);

        TextView textMobile=findViewById(R.id.txtMobile);
        String mobilenumber=getIntent().getStringExtra("mobile");
        textMobile.setText(String.format("+977%s",mobilenumber));



        verificationId=getIntent().getStringExtra("verificationId");

        senOTPInputs();


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputcode1.getText().toString().trim().isEmpty()
                    || inputcode2.getText().toString().trim().isEmpty()
                        ||inputcode3.getText().toString().trim().isEmpty()
                        ||inputcode4.getText().toString().trim().isEmpty()
                        ||inputcode5.getText().toString().trim().isEmpty()
                        ||inputcode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerifyOtp.this, "Enter Valid OTP number", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code =inputcode1.getText().toString()
                        +inputcode2.getText().toString()
                        +inputcode3.getText().toString()
                        +inputcode4.getText().toString()
                        +inputcode5.getText().toString()
                        +inputcode6.getText().toString();
                if (verificationId !=null){
                    progressBar.setVisibility(View.VISIBLE);
                    btnVerify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(verificationId,code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        btnVerify.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()){
                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else {
                            Toast.makeText(VerifyOtp.this, "The verification code is invalid !!", Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                    findViewById(R.id.txtResendOtp).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber("+97"+ getIntent().getStringExtra("mobile"),
                                    60, TimeUnit.SECONDS,VerifyOtp.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(VerifyOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });
                }
            }
        });
    }

    private void senOTPInputs() {
        EditText[] editText={inputcode1,inputcode2,inputcode3,inputcode4,inputcode5,inputcode6};
        for (int i=0; i<editText.length - 1;i++){
            final  int index=i;
            editText[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    if(!s.toString().trim().isEmpty()){
                       editText[index + 1].requestFocus();


                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }
}