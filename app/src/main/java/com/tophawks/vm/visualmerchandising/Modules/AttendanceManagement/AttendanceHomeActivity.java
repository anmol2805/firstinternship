package com.tophawks.vm.visualmerchandising.Modules.AttendanceManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.tophawks.vm.visualmerchandising.R;

public class AttendanceHomeActivity extends AppCompatActivity {

    private CardView deskEmpCard;
    private CardView fieldEmpCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_home);

        deskEmpCard = (CardView) findViewById(R.id.deskempcard);
        fieldEmpCard = (CardView) findViewById(R.id.fieldempcard);

        deskEmpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), )
            }
        });

        fieldEmpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
