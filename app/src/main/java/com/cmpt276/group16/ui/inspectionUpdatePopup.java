package com.cmpt276.group16.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.cmpt276.group16.R;

public class inspectionUpdatePopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_update_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.25));

        WindowManager.LayoutParams parameters = getWindow().getAttributes();
        parameters.gravity = Gravity.CENTER;
        parameters.x = 0;
        parameters.y = -20;

        getWindow().setAttributes(parameters);

        Button buttonClose = (Button) findViewById(R.id.inspectionIgnoreButton);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}