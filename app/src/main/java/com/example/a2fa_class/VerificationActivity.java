package com.example.a2fa_class;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationActivity extends AppCompatActivity {
    private EditText codeField;
    private Button verifyButton;
    private String sentCode;
    private LoginActivity loginActivity;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        codeField=findViewById(R.id.codeField);
        verifyButton=findViewById(R.id.verifyBtn);
        sentCode=getIntent().getStringExtra("code");

        verifyButton.setOnClickListener(v->{
            String enteredCode=codeField.getText().toString();
            if(enteredCode.isEmpty()){
                Toast.makeText(this, "Please enter code", Toast.LENGTH_SHORT).show();
            }else {
                if(enteredCode.equals(sentCode)){
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Code deosnt match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
