package com.example.a2fa_class;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;

import database.UserDatabase;
import model.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText emailLoginField,passwordLoginField;
    private Button loginButton;
    private UserDatabase userDatabase;
    private static final String SERVER_URL = "https://api.brevo.com/v3/smtp/email";
    private static final String API_KEY = "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"; // Replace with your actual Brevo API key


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailLoginField=findViewById(R.id.emailLoginField);
        passwordLoginField=findViewById(R.id.passwordLoginField);
        loginButton=findViewById(R.id.loginButton);
        userDatabase= UserDatabase.getInstance(this);

        loginButton.setOnClickListener(v->{
            String email=emailLoginField.getText().toString();
            String password=passwordLoginField.getText().toString();
            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
            }
            Executors.newSingleThreadExecutor().execute(()->{
                User user=userDatabase.userDao().getUserFromEmail(email);

                if(user.getPassword().equals(password)){
                    String verificationCode=generateVerificationCode();
                    sendVerificationEmail(email,verificationCode);

                    Intent intent=new Intent(LoginActivity.this,VerificationActivity.class);
                    intent.putExtra("code",verificationCode);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }
            });
        });

    }
public String generateVerificationCode(){
        Random rand=new Random();
        int code=100000+rand.nextInt(900000);
        return String.valueOf(code);
    }
    public void sendVerificationEmail(String email,String code){
        OkHttpClient client=new OkHttpClient();
        JSONObject json=new JSONObject();
        try {
            json.put("sender",new JSONObject().put("email","brilantponxhaj@gmail.com"));
            json.put("to",new JSONArray().put(new JSONObject().put("email",email)));
            json.put("subject","Verification Code");
            json.put("htmlContent","Your verification code is: "+code);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body=RequestBody.create(json.toString(), MediaType.parse("application/json; charset=UTF-8"));
        Request request= new Request.Builder()
                .url(SERVER_URL)
                .post(body)
                .addHeader("api-key",API_KEY)
                .addHeader("Content-Type","application/json").build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(LoginActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Verification code sent to your email", Toast.LENGTH_SHORT).show());
                    } else {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show());
                    }

                }
            });
    }
}
