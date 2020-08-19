package com.example.d_fir_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.d_fir_login.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference users;
    private EditText txtName, txtEmployeeId, txtPhoneNumber, txtPassword, txtConfirmPassword, txtAge;
    private Button btnSignUp, btnLogin;
    boolean bool ;
    private static final Pattern Password_Pattern =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        txtName = findViewById(R.id.txtName);
        txtEmployeeId = findViewById(R.id.txtEmployeeId);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber3);
        txtAge = findViewById(R.id.txtAge);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        final Spinner mySpinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = new User(txtName.getText().toString(),
                        txtEmployeeId.getText().toString(),
                        txtPhoneNumber.getText().toString(),
                        txtPassword.getText().toString(),
                        mySpinner.getSelectedItem().toString(),
                        txtAge.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getEmployeeId()).exists())
                            Toast.makeText(RegisterActivity.this, "Username Exists", Toast.LENGTH_SHORT).show();
                        else{
                            users.child(user.getEmployeeId()).setValue(user);
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
   /* private boolean validateName(){
        String Name = txtName.getText().toString();
        if(Name.isEmpty()){
            txtName.setError("Field Can't be Empty");
            return false;
        }
        else {
            txtName.setError(null);
            return true;
        }
    }
    private boolean validateEmployeeId(){
        String EmployeeId = txtEmployeeId.getText().toString();

        if(EmployeeId.isEmpty()){
            txtEmployeeId.setError("Field Can't be Empty");
            return false;
        }
        else if(EmployeeId.length() < 6) {
            txtEmployeeId.setError("Enter Valid ID");
            return false;
        }
        else{
            txtEmployeeId.setError(null);
            return true;
        }
    }
    private boolean validatePhoneNumber(){
        String PhoneNumber = txtPhoneNumber.getText().toString();

        if(PhoneNumber.isEmpty()){
            txtPhoneNumber.setError("Field Can't be Empty");
            return false;
        }
        else if(PhoneNumber.length() < 10) {
            txtPhoneNumber.setError("Enter Valid Phone Number");
            return false;
        }
        else {
            txtPhoneNumber.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        String Password = txtPassword.getText().toString();

        if(Password.isEmpty()){
            txtPassword.setError("Field Can't be Empty");
            return false;
        }
        else if(!Password_Pattern.matcher(Password).matches()){
            txtPassword.setError("Password is too week");
            return false;
        }
        else {
            txtPassword.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword(){
        String ConfirmPassword = txtConfirmPassword.getText().toString();
        String Password = txtPassword.getText().toString();

        if(ConfirmPassword.isEmpty()){
            txtConfirmPassword.setError("Field Can't be Empty");
            return false;
        }
        else if(ConfirmPassword.equals(Password)){
            txtConfirmPassword.setError(null);
            return  true;
        }
        else {
            txtConfirmPassword.setError(null);
            return false;
        }
    }

    public boolean EnableSignUp(){
        if(!validateName() | !validateEmployeeId()  | !validatePhoneNumber() | !validatePassword() | !validateConfirmPassword()){
            btnSignUp.setEnabled(true);
            return true;
        }
        return false;
    }*/

}