package com.ashik.justice.developer.studyassistants;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUp1_Activity extends AppCompatActivity implements View.OnClickListener {

    RadioGroup rg;
    int selectedChoice;
    private TextInputEditText username,userEmail,userPassword,confirmPassword;
    private Button button_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up1 );

        rg = (RadioGroup) findViewById( R.id.radioGroup );
        username=(TextInputEditText) findViewById(R.id.user_name);
        userEmail=(TextInputEditText) findViewById(R.id.user_email);
        userPassword=(TextInputEditText) findViewById(R.id.user_password);
        confirmPassword=(TextInputEditText) findViewById(R.id.confirm_password);
        button_next=(Button)findViewById(R.id.button_next);
        button_next.setOnClickListener(this);

    }


    public void checkRadioButton(View view) {
        int radioButtonId = rg.getCheckedRadioButtonId();

        if (radioButtonId == 1 ) {
            selectedChoice = 1;
            Toast.makeText( this,"Admin",Toast.LENGTH_SHORT ).show();
        }
        if (radioButtonId == 2 ) {
            selectedChoice = 2;
            Toast.makeText( this,"Student",Toast.LENGTH_SHORT ).show();
        }
        if (radioButtonId == 3 ) {
            selectedChoice = 3;
            Toast.makeText( this,"Teacher",Toast.LENGTH_SHORT ).show();
        }
    }

    private void signupFirst(){
        String name=username.getText().toString().trim();
        String email=userEmail.getText().toString().trim();
        String password=userPassword.getText().toString().trim();
        String confirmpass=confirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            username.setError("enter user name");
            username.setFocusable(true);
            return;
        }
        if(TextUtils.isEmpty(email)){
        userEmail.setError("enter your email");
        userEmail.setFocusable(true);
        return;
        }
        if(TextUtils.isEmpty(password)){
            userPassword.setError("enter your password");
            userPassword.setFocusable(true);
            return;
        }
        if(TextUtils.isEmpty(confirmpass)){
            confirmPassword.setError("re-enter your password");
            confirmPassword.setFocusable(true);
            return;
        }
        if (password.equals(confirmpass)){
            SharedPrefManager.getIntance(this).saveSignUpFirstData(name,email,password);
            Intent SU1 = new Intent( SignUp1_Activity.this, SignUp2_Activty.class );
            SU1.putExtra( "rbId", selectedChoice);
            startActivity( SU1 );
        }else{
            confirmPassword.setError("password doesn't match");
            confirmPassword.setFocusable(true);
            return;
        }

    }

    @Override
    public void onClick(View view) {
        signupFirst();

    }
}