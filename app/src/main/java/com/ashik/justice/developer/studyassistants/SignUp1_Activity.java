package com.ashik.justice.developer.studyassistants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUp1_Activity extends AppCompatActivity {

    RadioGroup rg;
    int selectedChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up1 );

        rg = (RadioGroup) findViewById( R.id.radioGroup );
    }

    public void GoTo_Personal_Info(View view) {
        Intent SU1 = new Intent( SignUp1_Activity.this, SignUp2_Activty.class );
        SU1.putExtra( "rbId", selectedChoice);
        startActivity( SU1 );
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
}