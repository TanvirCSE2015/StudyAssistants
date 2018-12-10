package com.ashik.justice.developer.studyassistants;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignUp2_Activty extends AppCompatActivity {

    private int selectedR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up2 );

        TextInputLayout txtD = findViewById( R.id.txtDes );
        TextInputLayout txtS = findViewById( R.id.txtSession );
        TextInputLayout txtR = findViewById( R.id.txtReg );

        Intent receivedIntent = getIntent();

        selectedR = receivedIntent.getIntExtra("rbId",-1);

        if (selectedR == 1) {
            txtD.setVisibility( View.GONE );
            txtS.setVisibility( View.VISIBLE );
            txtR.setVisibility( View.VISIBLE );
        }
        if (selectedR == 2) {
            txtD.setVisibility( View.GONE );
            txtS.setVisibility( View.VISIBLE );
            txtR.setVisibility( View.VISIBLE );
        }
        if (selectedR == 3) {
            txtD.setVisibility( View.VISIBLE );
            txtS.setVisibility( View.GONE );
            txtR.setVisibility( View.GONE );
        }
    }
}
