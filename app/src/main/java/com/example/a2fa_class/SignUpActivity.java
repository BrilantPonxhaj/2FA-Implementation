package com.example.a2fa_class;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

import database.UserDatabase;
import model.User;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailField, firstNameField, lastNameField, passwordField, numberField;
    private Button signUpButton;
    private UserDatabase userDatabase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        numberField = findViewById(R.id.numberField);
        signUpButton = findViewById(R.id.signUpBtn);
        userDatabase = UserDatabase.getInstance(this);
        signUpButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String number = numberField.getText().toString();
            if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || number.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            Executors.newSingleThreadExecutor().execute(() -> {
                User existingUser = userDatabase.userDao().getUserFromEmail(email);
                if (existingUser != null) {
                    runOnUiThread(() -> Toast.makeText(this, "This user already exists", Toast.LENGTH_SHORT).show());
                } else {
                    User newUser = new User(firstName, lastName, email, password, number);
                    userDatabase.userDao().insertUser(newUser);
                    runOnUiThread(() -> Toast.makeText(this, "User is inserted succesfully", Toast.LENGTH_SHORT).show());
                }
            });
        });

    }
}
